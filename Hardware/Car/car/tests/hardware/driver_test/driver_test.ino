//Griffin Saiia, Gjs64
//SocCar: Car State Machine


//*******************Global Variables and Definitions*******************
  
  //******************drive motor***************************
  //enable allows for in1/in2 to accept input and takes rpm
  #define enA  11//pin
  //foward
  #define in1  9//pin
  //reverse
  #define in2  10//pin
  //normal speed
  #define NSPD  150//rpm
  
//****************************Utility Functions***************************

void setup(){
  //sets drive motor control pins to output
  pinMode(enA, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
}

void off(){
  digitalWrite(enA, LOW);
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
}

void forward(){
  analogWrite(enA, 250);
  digitalWrite(in1, HIGH);
}

void reverse(){
  analogWrite(enA, 250);
  digitalWrite(in2, HIGH);
}

//*******************************Control Loop******************************

void loop(){
  off();
  forward();
  off();
  delay(1000);
  reverse();
}
