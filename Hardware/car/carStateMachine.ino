//Griffin Saiia, Gjs64
//SocCar, Car Motor Control

/*
STATE TABLE
'-1' - end game, caught in serial exception reverts state to '0' --> idle()
'0' - neutral
'1' - Forward, drive()
'2' - Turn Right, right()            
'3' - Reverse, reverse()
'4' - Turn Left, left()             
'5' - Forward+Right, rdrive()
'6' - Forward+Left, ldrive()
'7' - Reverse+Right, rreverse()
'8' - Reverse+Left, lreverse()
'9' - Brake, caught in serial exception reverts state to '0' --> idle()
'10' - boost, setBoost()-->boost = 1
*/

#include <AccelStepper.h>
#include <MultiStepper.h>

//****************global variables and pin definitions***************

//turn motor (stepper motor)
#define HALFSTEP 8
#define StepIn1  4
#define StepIn2  5
#define StepIn3  6
#define StepIn4  7
AccelStepper stepper(HALFSTEP, StepIn1, StepIn3, StepIn2, StepIn4);
//delay between polarity shifts for stepper
#define DELAYTIME  1//ms
//timer for stepper turn loop
unsigned long loopTime;
#define TURNTIME  500//ms
//flag for last turn - needed for straighten 
//(-1 - left, 1 - right, 0 - straight)
int lastTurn;

//drive motor
//enable allows for in1/in2 to accept input and takes rpm
#define enA  2
//foward
#define in1  0
//reverse
#define in2  1
//rpm enA is set to, changes depending on boost
int driveSpeed;

//flag to indicate if boost is active or not
boolean boost;
//boost speed
#define BOOSTSPEED   250//rpm
//normal speed
#define RESTINGSPEED  100//rpm

//holds how long boost has been active for
unsigned long boostTimer;
//holds how long boost should be active for
#define BOOSTDURATION  10000//ms

//holds whether boost is ready to be called again
boolean boostReady;
//holds how long since user last boosted
unsigned long boostCooldown;
//holds how long user should wait before boosting again
#define COOLDOWNDURATION  30000//ms

//two state variables, state holds current state
//new state holds newest state. 
//At the end of a state execution state becomes new state, 
//new state becomes 0. 
//This ensures all states are executed.
int state;
int newState;


//*********utility functions***********

//alternative setup function if we decide we want to use library
/*
void setup(){
  //sets drive motor control pins to output
  pinMode(enA, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  //sets default values for stepper library
  stepper.setMaxSpeed(1000.0);
  stepper.setAcceleration(100.0);
  stepper.setSpeed(200);
  Serial.begin(9600);//sets baud rate
}
*/

void setup()
{
  //sets drive motor control pins to output
  pinMode(enA, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  //sets (stepper) turn motor pins to output
  pinMode(StepIn1, OUTPUT);
  pinMode(StepIn2, OUTPUT);
  pinMode(StepIn3, OUTPUT);
  pinMode(StepIn4, OUTPUT);
  //sets baud rate of connection
  Serial.begin(9600);
  //sets states
  state = 0;
  newState = 0;
  //sets boost flags
  boost = false;
  boostReady = true;
  boostCooldown = 0;
  //sets driveSpeed to default (normal speed)
  driveSpeed = RESTINGSPEED;
  //sets lastTurn to straight
  lastTurn = 0;
  
}

//runs stepper motor through one rotation
//1 right, -1, left
//implement timer for degree
void stepRoutine(int Direction, int time){
 loopTime = millis();
 while(loopTime <= time){
   if(1 == Direction){
      digitalWrite(StepIn2, LOW);
      digitalWrite(StepIn3, LOW);
      digitalWrite(StepIn4, LOW);
      digitalWrite(StepIn1, HIGH);
      delay(DELAYTIME);
      digitalWrite(StepIn3, LOW);
      digitalWrite(StepIn4, LOW);
      digitalWrite(StepIn2, HIGH);
      digitalWrite(StepIn1, HIGH);
      delay(DELAYTIME);
      digitalWrite(StepIn1, LOW);
      digitalWrite(StepIn3, LOW);
      digitalWrite(StepIn4, LOW);
      digitalWrite(StepIn2, HIGH);
      delay(DELAYTIME);
      digitalWrite(StepIn1, LOW);
      digitalWrite(StepIn4, LOW);
      digitalWrite(StepIn3, HIGH);
      digitalWrite(StepIn2, HIGH);
      delay(DELAYTIME);
      digitalWrite(StepIn1, LOW);
      digitalWrite(StepIn2, LOW);
      digitalWrite(StepIn4, LOW);
      digitalWrite(StepIn3, HIGH);
      delay(DELAYTIME);
      digitalWrite(StepIn1, LOW);
      digitalWrite(StepIn2, LOW);
      digitalWrite(StepIn3, HIGH);
      digitalWrite(StepIn4, HIGH);
      delay(DELAYTIME);
      digitalWrite(StepIn1, LOW);
      digitalWrite(StepIn2, LOW);
      digitalWrite(StepIn3, LOW);
      digitalWrite(StepIn4, HIGH);
      delay(DELAYTIME);
      digitalWrite(StepIn2, LOW);
      digitalWrite(StepIn3, LOW);
      digitalWrite(StepIn1, HIGH);
      digitalWrite(StepIn4, HIGH);
      delay(DELAYTIME);
   }
   if(Direction == -1){
      digitalWrite(StepIn1, LOW);
      digitalWrite(StepIn2, LOW);
      digitalWrite(StepIn3, LOW);
      digitalWrite(StepIn4, HIGH);
      delay(DELAYTIME);
      digitalWrite(StepIn1, LOW);
      digitalWrite(StepIn2, LOW);
      digitalWrite(StepIn3, HIGH);
      digitalWrite(StepIn4, HIGH);
      delay(DELAYTIME);
      digitalWrite(StepIn1, LOW);
      digitalWrite(StepIn2, LOW);
      digitalWrite(StepIn4, LOW);
      digitalWrite(StepIn3, HIGH);
      delay(DELAYTIME);
      digitalWrite(StepIn1, LOW);
      digitalWrite(StepIn4, LOW);
      digitalWrite(StepIn3, HIGH);
      digitalWrite(StepIn2, HIGH);
      delay(DELAYTIME);
      digitalWrite(StepIn1, LOW);
      digitalWrite(StepIn3, LOW);
      digitalWrite(StepIn4, LOW);
      digitalWrite(StepIn2, HIGH);
      delay(DELAYTIME);
      digitalWrite(StepIn3, LOW);
      digitalWrite(StepIn4, LOW);
      digitalWrite(StepIn2, HIGH);
      digitalWrite(StepIn1, HIGH);
      delay(DELAYTIME);
      digitalWrite(StepIn2, LOW);
      digitalWrite(StepIn3, LOW);
      digitalWrite(StepIn4, LOW);
      digitalWrite(StepIn1, HIGH);
      delay(DELAYTIME);
      digitalWrite(StepIn2, LOW);
      digitalWrite(StepIn3, LOW);
      digitalWrite(StepIn1, HIGH);
      digitalWrite(StepIn4, HIGH);
      delay(DELAYTIME);
   }
 } 

}

//ensures wheels are forward
void straight(){
  if(lastTurn == 0){
    stepRoutine((lastTurn * -1), TURNTIME);
    lastTurn = 0;
  }
}

//library stepper function
void libraryRoutine(){
  stepper.run();
}

//ensures all states get executed
void stateChange(){
  state = newState;
  newState = '0';
}

//updates boost flags, called at end of every state function
void updateBoostFlags(){
  //checks if boost is active
  if(boost){
    //checks if boost has been active too long
    if(boostTimer >= BOOSTDURATION){
      //boost is now unavailable for calls
      boostReady = false;
      //handles speed and other flag switching
      setBoost();
    }
  }
  //checks if boost should be available or not
  else if(boostCooldown >= COOLDOWNDURATION){
    //boost is now available for calls
    boostReady = true;
    //ensures that boostReady is not set unnecessarily
    boostCooldown = 0;
  }
}

//catches interrupt thrown at Serial Connection
void serialEvent(){
  while(Serial.available()){
    newState = (char)(Serial.read());
    if(newState == '-1' || newState == '0'){
      idle();
      state = '0';
      newState = '0';
    }
  }
}


//************state functions**********

//boost, '10'
void setBoost(){
  //checks if boost is not already active, and is available
  if(!boost && boostReady){
    //sets boost speed
    driveSpeed = BOOSTSPEED;
    //makes further boost calls unavailable
    boostReady= false;
    //starts boost timer
    boostTimer = millis();
  }
  //boost is either active, or not available
  else{
    //sets normal speed
    driveSpeed = RESTINGSPEED;
    //if boost was set
    if(boost){
      //starts cooldown timer
      boostCooldown = millis();
      //ensures that boost timer is zeroed for future boosts
      boostTimer = 0;
    }
  }
  //toggles boost flag
  boost = !boost;
}

//idle sets all motors inactive, '9' - handled in serialEvent()
void idle(){
  digitalWrite(StepIn1, LOW);
  digitalWrite(StepIn2, LOW);
  digitalWrite(StepIn3, LOW);
  digitalWrite(StepIn4, LOW);
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
  //straightens wheels
  straight();
  updateBoostFlags();
}

//drive, we're going forward now, '1'
void drive(){
  //sets wheels straight
  straight();
  analogWrite(enA, driveSpeed);
  //makes sure we don't burn out motor 
  digitalWrite(in2, LOW);
  digitalWrite(in1, HIGH);
  updateBoostFlags();
}

//turn right, '2'
void right(){
  //if wheels were oriented other way first
  if(lastTurn == -1)
    stepRoutine(1, (TURNTIME * 2));
  //if we're straight
  else if(lastTurn == 0){
    stepRoutine(1, TURNTIME);
  }
  //do nothing if we're already right
  lastTurn = 1;
  updateBoostFlags();
}

//reverse, '3'
void reverse(){
  //sets wheels straight
  straight();
  analogWrite(enA, driveSpeed);
  //makes sure we don't burn out motor 
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
  updateBoostFlags();
}

//left, '4'
void left(){
  //if wheels were oriented other way first
  if(lastTurn == 1)
    stepRoutine(-1, (TURNTIME * 2));
  //if we're straight
  else if(lastTurn == 0){
    stepRoutine(-1, TURNTIME);
  }
  //do nothing if we're already right
  lastTurn = -1;
  updateBoostFlags();
}

//forward+right, '5'
void rdrive(){
  right();
  analogWrite(enA, driveSpeed);
  //makes sure we don't burn out motor 
  digitalWrite(in2, LOW);
  digitalWrite(in1, HIGH);
  updateBoostFlags();
}

//forward+left, '6'
void ldrive(){
  left();
  analogWrite(enA, driveSpeed);
  //makes sure we don't burn out motor 
  digitalWrite(in2, LOW);
  digitalWrite(in1, HIGH);
  updateBoostFlags();
}

//reverse+right, '7'
void rreverse(){
  right();
  analogWrite(enA, driveSpeed);
  //makes sure we don't burn out motor 
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
  updateBoostFlags();
}

//reverse+left, '8'
void lreverse(){
  left();
  analogWrite(enA, driveSpeed);
  //makes sure we don't burn out motor 
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
  updateBoostFlags();
}

//********control loop*********

void loop(){
  //if no new state is queued nothing happens
  if(state != newState){
    if(state == '10')
      setBoost();
    else if(state == '1')
      drive();
    else if(state == '2')
      right();
    else if(state == '3')
      reverse();
    else if(state == '4')
      left();
    else if(state == '5')
      rdrive();
    else if(state == '6')
      ldrive();
    else if(state == '7')
      rreverse();
    else if(state == '8')
      lreverse();
  }
  stateChange();
}









