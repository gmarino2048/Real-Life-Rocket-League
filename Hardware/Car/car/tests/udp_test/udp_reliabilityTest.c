#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <time.h>
#include <math.h>

#define PORT 1234 //port we're communicating on

//****************************perror wrapper****************************
void error(char *msg) {
  perror(msg);
  //closes program
  exit(0);
}

//**************process to launch serial python script******************
void openSerial(){
  pid_t pid = fork();
  char *args[3];
  if( pid == 0){
  }
  else{
    args[0] = "python";
    args[1] = "serialComm.py";
    args[2] = 0;
    if(execvp(args[0], args) == -1){
      error("serial connection failed\n");
    }
    else{
      fprintf(stderr, "serial connection open\n");
      fflush(stderr);
    }
  }
}

int main(int argc, char **argv) {
  int states[] = {1, 2, 3, 4, 5, -1};
  //launches python script
  openSerial();

  //**************************named pipe************************************
  //holds fifo object
  int fd;
  //FIFO file path
  char* myFIFO = "/tmp/myfifo";
  //create FIFO
  if(mkfifo(myFIFO, 0666) == -1){
    error("Named pipe failed.\n");
  }
  else{
    fprintf(stderr, "Pipe is open.\n");
  }
  char arr1[80];
  bzero(arr1, sizeof(arr1));

  //*****************************client*************************************
  int sockfd; //socket
  struct sockaddr_in server; ////server address
  socklen_t serverlen = sizeof(server); //byte size of client address
  char msg[80]; //buffer we'll be using
  int msglen;  //message byte size
  int optval; //flag for setsockopt
  //ensures buffer is clear
  bzero(msg, sizeof(msg));
  //writes connection message
  sprintf(msg, "connected");
  //builds server address
  memset(&server, 0, serverlen);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = htonl(INADDR_ANY); //ip address of server
  server.sin_port = htons(PORT);
  //creates socket
  sockfd = socket(AF_INET, SOCK_DGRAM, 0);
  if (sockfd < 0){
      error("ERROR opening socket");
  }
  else{
    fprintf(stderr, "socket open\n");
  }
  //setsockopt enables the reuse of this port immediately after use.
  //disables port timeout.
  optval = 1;
  setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR,
	     (const void *)&optval , sizeof(int));
  //binds socket to port
  if(bind(sockfd, (struct sockaddr *)&server, serverlen) < 0){
    error("bind failed");
  }
  else{
    fprintf(stderr, "socket bound.\n");
  }
  //names socket
  if(getsockname(sockfd, (struct sockaddr *)&server, &serverlen) < 0){
    error("getsockname failed");
  }
  else{
    fprintf(stderr, "socket named.\n");
  }
  //clears communication buffer
  bzero(msg, strlen(msg));
  msglen = -1;
  //variable to be passed around
  int state = -4; //-4, just because it's not a state value.
  //to iterate through array
  int i = 0;
  int count = 0;
  //starts connection monitoring, checks for end game state
  while(1){
    //waits for message from server
    if ((msglen = recvfrom(sockfd, msg, sizeof(msg), 0,
        (struct sockaddr *) &server, &serverlen)) < 0){
      error("ERROR in recvfrom");
    }
    else{
      //updates state
      uint32_t myInt1 = msg[0] + (msg[1] << 8) + (msg[2] << 16) + (msg[3] << 24);
      state = (int)myInt1;
      if(state != state[i]){
        fprintf(stderr, "false\n");
      }
      bzero(msg, sizeof(msg));
    }
    i++;
    //checks for end game state
    if(states[i] == -1){
      i = 0;
    }
    if(count == 100){
      fprintf(stderr, "true!\n");
      break;
    }
  }
  //closes socket
  close(sockfd);
  return 1;
}
