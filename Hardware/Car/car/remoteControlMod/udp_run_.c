//Griffin Saiia, gjs64
//Car server for remoteControlMod

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

#define PORT 1234 //port we're communicating on

/*
STATE TABLE
'12' - start/end game, handled inside serialEvent()
'0' - neutral
'1' - Forward, drive()
'2' - Turn Right, right()
'3' - Reverse, reverse()
'4' - Turn Left, left()
'5' - Forward+Right, rdrive()
'6' - Forward+Left, ldrive()
'7' - Reverse+Right, rreverse()
'8' - Reverse+Left, lreverse()
'9' - Brake, brake()
'13' - boost, boost()
*/

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
  int state = -2; //-2, just because it's not a state value.
  //starts connection monitoring - checks exit state, '-1'.
  while(state != -1){
    //waits for message from server
    if ((msglen = recvfrom(sockfd, msg, sizeof(msg), 0,
        (struct sockaddr *) &server, &serverlen)) < 0){
      error("ERROR in recvfrom");
    }
    else{
      char* input = msg;
      state = atoi(input);
      //passes state into pipe
      fd = open(myFIFO, O_WRONLY);
      //byte conversion doesn't like -1, 10, or 11.
      if(state == 12){
				//arduino takes "a" for end game
        sprintf(arr1, "a");
        write(fd, arr1, sizeof(arr1));
				//sets state to break state
				state = -1;
      }
      else if(state == 13){
				//arduino takes "b" for end game
        sprintf(arr1, "b");
        write(fd, arr1,sizeof(arr1));
      }
      else{
        sprintf(arr1, "%d", state);
        write(fd, arr1, sizeof(arr1));
      }
      close(fd);
      //used for testing
      fprintf(stderr, "server sent: %s\n", arr1);
    }
		//clears buffers
		bzero(input, sizeof(input));
		bzero(arr1, sizeof(arr1));
		bzero(msg, sizeof(msg));
  }
  //closes socket
  close(sockfd);
  return 1;
}
