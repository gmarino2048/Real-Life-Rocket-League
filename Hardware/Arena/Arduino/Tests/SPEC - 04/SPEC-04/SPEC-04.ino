/*  Soccar Project
 *  
 *  v 1.0
 *  
 *  SPEC - 04
 *  
 * The purpose of this test is to ensure that the particle photon
 * can correctly connect to the arduino due and that the due can
 * correctly decode the string and post the component parts to the
 * serial monitor.
 *  
 *  This script was written for the Arduino DUE
 */
char *infoBuffer;

void setup() {
  // put your setup code here, to run once:
  Serial1.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:

}

void SerialEvent () {
  while (Serial1.available()){
    char c = Serial1.read();
    if (c != '\n'){
      sprintf(infoBuffer, "%s%c", infoBuffer, c);
    }
    else {
      SerialUSB.println
    }
  }
}

void verify (char *regex) {
  
}

void getKey (char *regex){
  
}

void getValue (char *regex) {
  
}

