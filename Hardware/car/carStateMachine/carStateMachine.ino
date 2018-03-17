//Griffin Saiia, Gjs64
//SocCar: Car State Machine

/*
STATE TABLE
'a' - start/end game, handled inside serialEvent()
'0' - neutral
'1' - Forward, drive()
'2' - Turn Right, right()            
'3' - Reverse, reverse()
'4' - Turn Left, left()             
'5' - Forward+Right, rdrive()
'6' - Forward+Left, ldrive()
'7' - Reverse+Right, rreverse()
'8' - Reverse+Left, lreverse()
'9' - Brake, brake()
'10' - boost, boost()
*/


//*******************Global Variables and Definitions*******************
  
  //*************turn motor (stepper motor)*****************
  #define sIn1  4//pin
  #define sIn2  5//pin
  #define sIn3  6//pin
  #define sIn4  7//pin
  //delay between polarity shifts for stepper
  #define DELAYTIME  1//ms
  //how long steppers should run for 90 degree turn
  #define TURNTIME  10000//ms
  
  //******************drive motor***************************
  //enable allows for in1/in2 to accept input and takes rpm
  #define enA  11//pin
  //foward
  #define in1  9//pin
  //reverse
  #define in2  10//pin
  //normal speed
  #define NSPD  150//rpm
  
  //********************boost state*************************
  //boost speed
  #define BSPD  250//rpm
  //boost duration
  #define BDUR  10000//ms
  //cooldown duration
  #define CDUR  30000//ms
  //flag to indicate if boost is active
  int boostSet;
  //flag to indicate if boost CAN be active
  int bReady;
  //how long boost has been active, global as all states must check this
  unsigned long bTimer;
  //how long since last boosted
  unsigned long cooldown;
  
  //**********************program state*********************
  //timer for whole program
  unsigned long totalTime;
  //current state of program
  char state;
  //needed to check against state in case of new game
  char newState;
  //current wheel position: 0 straight, 1 right, -1 left
  int lastTurn;
  //rpm enA is set to, changes depending on boost
  int driveSpeed;
  
//****************************Utility Functions***************************

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
  bReady = 1;
  boostSet = -1;
  totalTime = millis();
  Serial.begin(9600);//sets baud rate
}

//called at end of loop whenever there is new serial data
void serialEvent(){
  while(Serial.available()){
    //need newState to check for new game
    char newState = (char)(Serial.read());
  }
  if(state != 'a'){
    state = newState;
    if(state == 'a'){
      //straightens wheels, turns off all motors.
      straight();
      brake();
    }
    if(state == '9'){
      state = '0'
      brake();
    }
   }
  else{
    //new game
    if(newState == 'a'){
      //resets all flags
      setup();
      //sets state to neutral so it can start recieving commands
      state = '0';
    }
  }
}

//stepper helper funtion - just sets all stepper pins low
void stepOff(){
  digitalWrite(sIn1, LOW);
  digitalWrite(sIn2, LOW);
  digitalWrite(sIn3, LOW);
  digitalWrite(sIn4, LOW);
}

//right 1, left -1
void stepRoutine(int lOrR, unsigned long time){
  //so that they can be set in loop
  off(1);
  int stepPins[] = {sIn1, sIn2, sIn3, sIn4};
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
  off(1);
}

//makes sure wheels are straight
void straight(){
  if(lastTurn != 0){
    stepRoutine((lastTurn * -1), TURNTIME);
    lastTurn = 0;
  }
}

//method to keep boost in real time
void checkBoost(){
  totalTime = millis();
  if(boostSet){
    if((totalTime - bTimer) > BDUR){
      toggleBoost();
    }
  }
  else if(!bReady){
    if((totalTime - cooldown) > CDUR){
      bReady = 1;
    }
  }
}

//only called at the end of a boost period, or at the start of one
//important to handle boost timers
void toggleBoost(){
  boostSet = !boostSet;
  if(boostSet){
    driveSpeed = BSPD;
    bTimer = millis();
    bReady = -1;
  }
  else{
    driveSpeed = NSPD;
    cooldown = millis();
  }
}

//****************************State Functions*****************************

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
      right();
    }
    else{
      left();
    }
  }
  analogWrite(enA, driveSpeed);
  digitalWrite(in2, LOW);
  digitalWrite(in1, HIGH);
}

//reverse, we're going backwards
//takes direction
//states '3', '7', '8'
void reverse(int turn){
  if(turn == 0){
    //sets wheels straight
    straight();
  }
  else{
    if(turn){
      right();
    }
    else{
      left();
    }
  }
  analogWrite(enA, driveSpeed);
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
}

//right, turning right, '2'
void right(){
  //if we're left, we have to turn twice as far to be right
  if(lastTurn == -1){
    stepRoutine(1, (TURNTIME * 2));
  }
  //base case, we're straight
  else if(lastTurn == 0){
    stepRoutine(1, TURNTIME);
  }
  //if we're right, do nothing
  lastTurn = 1;
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

//boost, things are about to get faster '10'
void boost(){
  //checks if we are allowed to boost
  if(bReady){
    toggleBoost();
  }
}

//*******************************Control Loop******************************

void loop(){
  //called at beginning of each iteration, includes totalTime update
  checkBoost();
  //this is first just because it should have fastest response
  if(state == '9'){
    state = '0';
    brake();
  }
  if(state == '1'){
    state = '0';
    drive(0);
  }
  if(state == '2'){
    state = '0';
    right();
  }
  if(state == '3'){
    state = '0';
    reverse(0);
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
  if(state == '7'){
    state = '0';
    reverse(1);
  }
  if(state == '8'){
    state = '0';
    reverse(-1);
  }
  if(state == '10'){
    state = '0';
    boost();
  }
}
