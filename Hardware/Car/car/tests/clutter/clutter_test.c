#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>

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
  //states to send
  int states[] = {-1, 1, 9, 4, 5, 9, -1};
  int delay = 1000;
  double multiplier = 2;

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
  int i = 0;
  //************************start test*************************************
  while(i < 7){
    fd = open(myFIFO, O_WRONLY);
    if(states[i] == -1){
      sprintf(arr1, "a");
      write(fd, arr1, sizeof(arr1));
    }
    else{
      sprintf(arr1, "%d", states[i]);
      write(fd, arr1, sizeof(arr1));
    }
    close(fd);
    //used for testing
    fprintf(stderr, "sent state: %s\n", arr1);
    sleep((int)(delay * multiplier));
    i++
    if( (i == 7) && (multiplier != 0.03125) ){
      i = 0;
      multiplier = multipler / 2;
    }
  }
  return 1;
}
