/*
 * udpclient.c - A simple UDP client
 * usage: udpclient <host> <port>
 */
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

#define BUFSIZE 1024

//perror wrapper
void error(char *msg) {
    perror(msg);
    exit(0);
}

//process to launch serial python script
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
    int i = 0;
    //launches python script
    openSerial();
    //********named pipe shit
    //holds fifo object
    int fd;
    //holds past state
    int oldstate;
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


    //*********client shit
    int sockfd, portno, n;
    int serverlen;
    struct sockaddr_in serveraddr;
    struct hostent *server;
    char *hostname;
    char buf[BUFSIZE];
    memset(&serveraddr, 0, sizeof(serveraddr));
    serveraddr.sin_family = AF_INET;
    serveraddr.sin_addr.s_addr = inet_addr("192.168.1.12"); //ip address of server
    serveraddr.sin_port = htons(portno);
    hostname = "192.168.1.12"; //ip for server
    portno = 1234; //port we're using
    //creates socket
    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockfd < 0){
        error("ERROR opening socket");
    }
    else{
      fprintf(stderr, "socket open\n");
    }
    //gets the server's dns entry
    server = gethostbyname(hostname);
    if (server == NULL) {
        fprintf(stderr,"ERROR, no such host as %s\n", hostname);
        exit(0);
    }
    else{
      fprintf(stderr, "hostname acquired.\n");
    }
    bcopy((char *)server->h_addr,
	  (char *)&serveraddr.sin_addr.s_addr, server->h_length);
    /*//builds servers address
    bzero((char *) &serveraddr, sizeof(serveraddr));
    serveraddr.sin_family = AF_INET;
    bcopy((char *)server->h_addr,
	  (char *)&serveraddr.sin_addr.s_addr, server->h_length);
    serveraddr.sin_port = htons(portno);*/

    sleep(1);
    //starts connection monitoring
    while(1){
      sleep(8);
      /*n = recvfrom(sockfd, buf, strlen(buf), 0, (struct sockaddr *) &serveraddr, &serverlen);
      if (n < 0){
        error("ERROR in recvfrom");
      }
      else if(atoi((char *)&buf) == -1){*/
        //updates state
        //state = atoi((char*)&buf);
        //passes state into pipe
        bzero(arr1, sizeof(arr1));
        fd = open(myFIFO, O_WRONLY);
        sprintf(arr1, "%d", states[i]);
        write(fd, arr1, sizeof(arr1));
        close(fd);
        printf("server: %s\n", arr1);
        bzero(arr1, sizeof(arr1));
        //checks start/stop state
        if( states[i] == -1 ){
          sleep(4);
          break;
        }
      //}
      bzero(buf, BUFSIZE);
      i++;
    }
    //closes socket
    close(sockfd);
    return 1;
}
