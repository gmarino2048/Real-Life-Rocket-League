//Griffin Saiia, Gjs64
//control for car
/*commands: -1 - start/end state
             0 - neutral state
             1 - drive state
             2 - position right
             3 - reverse state
             4 - position left
             5 - drive right
             6 - drive left
             7 - reverse right
             8 - reverse left
             9 - break (or when reverse and drive are pressed together)
*/
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <stdint.h>
#include <sched.h>
#include <string.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#define MAX_LINE 80 /* 80 chars per line, per command */

//command state
int state = -1;
char *args[MAX_LINE/2 + 1];	/* command line (of 80) has max of 40 arguments */

//sets up client
int sockfd, portno, n;
struct sockaddr_in serv_addr;
struct hostent *server;
char *buffer[4];
int portno = 2000;


int openSerial(){
  args[0] = "python";
  args[1] = "serialComm.py";
  args[2] = 0;
  execvp(args[0], args);
}

int readRoutine(){
  //initiallizes state to 0 as game is about to start
  state = 0;
  while(1){
    //reads server feedback for terminate command
    n = read( sockfd, buffer, 4);
    //checks error
    if (n < 0){
      fprintf(stderr,"ERROR reading from socket\n");
    }
    //checks start/stop state
    if( atoi((char *)&buffer) == -1 ){
      break;
    }
    else{
      state = atoi((char *)&buffer);
    }
    bzero(buffer, 4);
    sleep(1);
  }
  //closes socket
  close(sockfd);
  return 1;
}


int main(void)
{
  int fd;

  //FIFO file path
  char* myFIFO = "/tmp/myfifo";
  //create FIFO
  mkfifo(myFIFO, 0666);
  pid_t pid;
  sockfd = socket(AF_INET, SOCK_STREAM, 0);
  if (sockfd < 0){
    fprintf(stderr,"ERROR opening socket\n");
  }
  server = gethostbyname("172.20.41.100");  //need to configure static ip for website
  if (server == NULL) {
    fprintf(stderr,"ERROR, no such host\n");
    exit(0);
  }
  bzero((char *) &serv_addr, sizeof(serv_addr));
  serv_addr.sin_family = AF_INET;
  bcopy((char *)server->h_addr, (char *)&serv_addr.sin_addr.s_addr, server->h_length);
  serv_addr.sin_port = htons(portno);
  //connecting to server
  if (connect(sockfd,(struct sockaddr *) &serv_addr,sizeof(serv_addr)) < 0){
    fprintf(stderr,"ERROR connecting\n");
  }
  pid = fork();
  //starts reading socket
  if (pid == 0 ){
    readRoutine();
  }
  else{
    openSerial();
    oldstate = -1;
    while(state != -1){
      if(state != oldstate){
        //starts writing to FIFO to pass state to python
        fd = open(myFIFO, O_WRONLY);
        write(fd, state, 4);
        close(fd);
        oldstate = state;
      }
    }
  }
}
