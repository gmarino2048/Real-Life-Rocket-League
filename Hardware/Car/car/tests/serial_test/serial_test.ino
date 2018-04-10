//Griffin Saiia, Gjs64
//SocCar: Serial Connection Test

//state test sequence: {-1, 1, 9, 4, 5, 9, -1} 

//*************turn motor (stepper motor)*****************
  #define sIn1  4//pin
  #define sIn2  5//pin
  #define sIn3  6//pin
  #define sIn4  7//pin
  //delay between polarity shifts for stepper
  #define DELAYTIME  1//ms
  //how long steppers should run for 90 degree turn
  #define TURNTIME  5000//ms
  
  //******************drive motor***************************
  //enable allows for in1/in2 to accept input and takes rpm
  #define enA  11//pin
  //foward
  #define in1  9//pin
  //reverse
  #define in2  10//pin
  //normal speed
  #define NSPD  150//rpm
  
  //**********************program state*********************
  //timer for whole program
  unsigned long totalTime;
  //current state of program
  char state;
  //current wheel position: 0 straight, 1 right, -1 left
  int lastTurn;
  //rpm enA is set to, changes depending on boost
  int driveSpeed;
  
void setup(){
  //sets drive motor control pins to output
  pinMode(enA, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  //sets stepper control pins
  pinMode(sIn1, OUTPUT);
  pinMode(sIn2, OUTPUT);
  pinMode(sIn3, OUTPUT);
  pinMode(sIn4, OUTPUT);
  //sets default game state values
  driveSpeed = NSPD;
  state = 'a';
  lastTurn = 0;
  totalTime = millis();
  Serial.begin(9600);//sets baud rate
}

//called at end of loop whenever there is new serial data
void serialEvent(){
  while(Serial.available()){
    state = (char)(Serial.read());
  }
  if(state == 'a'){
    straight();
    brake();
    setup();
  }
  if(state == '9'){
    state = '0';
    brake();
  }
}

//-1 turn off drive motor, 1 turn off turn motor
void stepOff(){
  digitalWrite(sIn1, LOW);
  digitalWrite(sIn2, LOW);
  digitalWrite(sIn3, LOW);
  digitalWrite(sIn4, LOW);
}

//right 1, left -1
void stepRoutine(int lOrR, unsigned long time){
  //so that they can be set in loop
  int stepPins[] = {sIn1, sIn2, sIn3, sIn4};
  stepOff();
  int i;
  if(lOrR){
    //start rotating clockwise
    i = 0;
  }
  else{
    //start rotation counter clockwise
    i = 3; 
  }
  //to time our loop
  totalTime = millis();
  unsigned long loopTime = millis();
  while((loopTime - totalTime) < time){
    if(((i-lOrR)%4) < 0){
      digitalWrite(stepPins[3], LOW);
    }
    else{
      digitalWrite(stepPins[(i-lOrR)%4], LOW);
    }
    digitalWrite(stepPins[i%4], HIGH);
    delay(DELAYTIME);
    //using lOrR here because for left (counter clockwise), i needs to decrease
    //                        for right (clockwise), i needs to increase
    if(((i+lOrR)%4) < 0){
      digitalWrite(stepPins[3], HIGH);
      i = 3;
    }
    else{
      digitalWrite(stepPins[(i+lOrR)%4], HIGH);
      i = i + lOrR;
    }
    delay(DELAYTIME);
    loopTime = millis();
  }
  //stepper off
  stepOff();
}

//makes sure wheels are straight
void straight(){
  if(lastTurn != 0){
    stepRoutine((lastTurn * -1), TURNTIME);
    lastTurn = 0;
  }
}

//drive, we're going forward now
//takes direction
//states '1', '5', and '6' 
void drive(int turn){
  if(turn == 0){
    //sets wheels straight
    straight();
  }
  else{
    if(turn){
      ;
    }
    else{
      left();
    }
  }
  analogWrite(enA, driveSpeed);
  digitalWrite(in2, LOW);
  digitalWrite(in1, HIGH);
}

//left, we're turning left, '4'
void left(){
  //will only be true if we're turned right
  if(lastTurn){
    stepRoutine(-1, (TURNTIME * 2));
  }
  //base case, we're straight
  else if(lastTurn == 0){
    stepRoutine(-1, TURNTIME);
  }
  //if already left do nothing
  lastTurn = -1;
}

void brake(){
  digitalWrite(enA, LOW);
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
  digitalWrite(sIn1, LOW);
  digitalWrite(sIn2, LOW);
  digitalWrite(sIn3, LOW);
  digitalWrite(sIn4, LOW);
}

void loop(){
  //called at beginning of each iteration, includes totalTime update
  totalTime = millis();
  //this is first just because it should have fastest response
  if(state == '1'){
    state = '0';
    drive(0);
  }
  if(state == '4'){
    state = '0';
    left();
  }
  if(state == '5'){
    state = '0';
    drive(1);
  }
  if(state == '6'){
    state = '0';
    drive(-1);
  }
}
