char rw[16];

void setup() {
  // put your setup code here, to run once:
  Serial1.begin(9600);
  SerialUSB.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  while (SerialUSB.available()){
    char c = SerialUSB.read();
    sprintf(rw, "%s%c", rw, c);

    if (c == '\n'){
      SerialUSB.print(rw);
      Serial1.print(rw);
      memset(rw, 0, sizeof(rw));
    }
  }
}
