/*
 * Soccar project
 * 
 * SPEC - 06
 * 
 * This code was written for the arduino due
 */

String commBuffer;

void setup() {
  // put your setup code here, to run once:
  SerialUSB.begin(9600);
  Serial1.begin(9600);

  commBuffer = "";
}

void loop() {
  // put your main code here, to run repeatedly:
  while (SerialUSB.available()){
    char c = SerialUSB.read();

    if (c == '\n'){
      Serial1.println (commBuffer);
      SerialUSB.println(commBuffer);
      commBuffer = "";
    }
    else{
      commBuffer.concat(String(c));
    }
  }
}
