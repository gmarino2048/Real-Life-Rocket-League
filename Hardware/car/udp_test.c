#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/socket.h>
#include <netdb.h>
#include <arpa/inet.h>

#define PORT 1234 //port we're communicating on

//perror wrapper
void error(char *msg) {
  perror(msg);
  exit(1);
}

int main(int argc, char **argv) {
  //**********************test states*********************************
  int states[] = {1, 2, 3, 4, 5, -1};

  //***********************server*****************************
  int sockfd; //socket
  struct sockaddr_in server; //server address
  struct sockaddr_in client; //client address
  socklen_t serverlen = sizeof(server); // byte size of server address
  socklen_t clientlen = sizeof(client); //byte size of client address
  int msglen; //message byte size
  char msg[80]; //buffer we'll be using
  int optval; //flag for setsockopt
  //build the server address
  memset((char *)&server, 0, serverlen);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = inet_addr("192.168.1.12"); //self address
  server.sin_port = htons(PORT);
  //creates socket
  if((sockfd = socket(AF_INET, SOCK_DGRAM, 0)) < 0){
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
  if (bind(sockfd, (struct sockaddr *)&server, serverlen) < 0){
    error("ERROR on binding");
  }
  else{
    fprintf(stderr, "socket bound.\n");
  }
  //waits to recieve connection
  fprintf(stderr, "waiting for handshake...");
  if(
    (msglen = recvfrom(sockfd, msg, sizeof(msg), 0,
    (struct sockaddr *)&client, &clientlen)) > 0){
    fprintf(stderr, "%s\n", msg);
  }
  else{
    error("recvfrom failed.");
  }
  //ensures msg buffer is clear
  bzero(msg, sizeof(msg));
  msglen = -1;
  //indexing variable
  int i = 0;
  fprintf(stderr, "Starting test sequence: \n");
  //starts sending data
  while (1) {
    sprintf((char *)&msg, "%d", states[i]);
    fprintf(stderr, "Sending state %d\n", states[i]);
    //sending state
    if ((msglen = sendto(sockfd, msg, sizeof(msg), 0,
	      (struct sockaddr *)&client, clientlen)) < 0){
      error("ERROR in sendto");
    }
    //checks for end game state
    if(states[i] == -1){
      break;
    }
    //waits between sending states
    sleep(2);
    i++;
  }
  close(sockfd);
  return 1;
}
