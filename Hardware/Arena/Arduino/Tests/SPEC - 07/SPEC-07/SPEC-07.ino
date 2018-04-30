/*
 * Soccar Project
 * 
 * SPEC - 07
 * 
 * This script was written to run on the arduino DUE
 */

String infoBuffer;

void setup() {
  // put your setup code here, to run once:
  SerialUSB.begin(9600);

  infoBuffer = "";
}

void serialEvent () {
  while (SerialUSB.available()){
    char c = SerialUSB.read();

    if (c == '\n'){
      SerialUSB.println(infoBuffer);
      infoBuffer = "";
    }
    else {
      infoBuffer.concat(String(c));
    }
  }
}

