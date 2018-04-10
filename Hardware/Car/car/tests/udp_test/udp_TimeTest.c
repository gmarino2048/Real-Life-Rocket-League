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

int main(int argc, char **argv) {

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
  //variable used to set two cases
  double i = 0;
  int entries = 0;
  double n = -1;
  time_t start;
  time_t stop;
  long int diff;
  long int totalTime = 0;
  //starts connection monitoring, checks for end game state
  while(1){
    //waits for message from server
    if ((msglen = recvfrom(sockfd, msg, sizeof(msg), 0,
        (struct sockaddr *) &server, &serverlen)) < 0){
      error("ERROR in recvfrom");
    }
    else{
      if(pow(n, i) > 0){
        start = time(NULL);
      }
      else{
        stop = time(NULL);
        diff = stop - start;
        fprintf(stderr, "time = %ld\n", diff);
        totalTime += diff;
        entries++;
      }
      bzero(msg, sizeof(msg));
    }
    i++;
    if( i == 100 ){
      break;
    }
  }
  totalTime = totalTime/entries;
  fprintf(stderr,"average delay: %ld\n", totalTime);
  //closes socket
  close(sockfd);
  return 1;
}
