/*
 * udpserver.c - A simple UDP echo server
 * usage: udpserver <port>
 */

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>

#define BUFSIZE 1024

//perror wrapper
void error(char *msg) {
  perror(msg);
  exit(1);
}

int main(int argc, char **argv) {
  //********test states
  int states[] = {1, 2, 3, 4, 5, -1};

  //********server shit
  int sockfd; //socket
  int portno; //port
  int clientlen; //byte size of client address
  struct sockaddr_in serveraddr; //server address
  struct sockaddr_in clientaddr; //client address
  struct hostent *hostp; //client host info
  char buf[BUFSIZE]; //buffer we'll be using
  char *hostaddrp; //host address string
  int optval; //flag for setsockopt
  int n; //message byte size

  //port we'll be operating on
  portno = 1234;
  //creates socket
  sockfd = socket(AF_INET, SOCK_DGRAM, 0);
  if (sockfd < 0){
    error("ERROR opening socket");
  }
  else{
    fprintf(stderr, "socket open\n");
  }
  //setsockopt enables the reuse of this port immediately after
  //use. disables port timeout.
  optval = 1;
  setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR,
	     (const void *)&optval , sizeof(int));
  //build the server address
  bzero((char *) &serveraddr, sizeof(serveraddr));
  serveraddr.sin_family = AF_INET;
  serveraddr.sin_addr.s_addr = htonl(INADDR_ANY);
  serveraddr.sin_port = htons((unsigned short)portno);
  //binds socket to port
  if (bind(sockfd, (struct sockaddr *) &serveraddr,
	   sizeof(serveraddr)) < 0){
    error("ERROR on binding");
  }
  else{
    fprintf(stderr, "bound.\n");
  }
  //builds client address
  memset(&clientaddr, 0, sizeof(clientaddr));
  clientlen = sizeof(clientaddr);
  clientaddr.sin_family = AF_INET;
  clientaddr.sin_addr.s_addr = inet_addr("192.168.1.7"); //ip address of pi
  clientaddr.sin_port = htons((unsigned short)portno);
  hostp = gethostbyaddr((const char *)&clientaddr,
			  clientlen, AF_INET);
  if (hostp == NULL){
    error("ERROR on gethostbyaddr");
  }
  else{
    fprintf(stderr, "host found.\n");
  }
  hostaddrp = inet_ntoa(clientaddr.sin_addr);
  if (hostaddrp == NULL){
      error("ERROR on inet_ntoa\n");
  }
  else{
    fprintf(stderr, "ready to send.\n");
  }

  //indexing variable
  int i = 0;
  fprintf(stderr, "Starting test sequence: \n");
  //starts sending data
  while (1) {
    sprintf((char *)&buf, "%d", states[i]);
    fprintf(stderr, "Sending state %d\n", states[i]);
    //sending state
    n = sendto(sockfd, buf, strlen(buf), 0,
	       (struct sockaddr *) &clientaddr, clientlen);
    if (n < 0)
      error("ERROR in sendto");
    if(states[i] == -1){
      break;
    }
    i++;
    sleep(3);
  }
  return 1;
}
