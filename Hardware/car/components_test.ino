//connections test

//turn motor (stepper motor)
#define StepIn1  4
#define StepIn2  5
#define StepIn3  6
#define StepIn4  7
//delay between polarity shifts for stepper
#define DELAYTIME  1//ms
//timer for stepper turn loop
unsigned long loopTime;
#define TURNTIME  500//ms

//drive motor
//enable allows for in1/in2 to accept input and takes rpm
#define enA  2
//foward
#define in1  0
//reverse
#define in2  1
//rpm enA is set to, changes depending on boost
int driveSpeed;
//normal speed
#define RESTINGSPEED  100//rpm

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
  //sets driveSpeed to default (normal speed)
  driveSpeed = RESTINGSPEED;
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

//drive, we're going forward now, '1'
void drive(){
  //sets wheels straight
  straight();
  analogWrite(enA, driveSpeed);
  //makes sure we don't burn out motor 
  digitalWrite(in2, LOW);
  digitalWrite(in1, HIGH);
}

void straight(){
  stepRoutine(-1 , TURNTIME);
}

void loop(){
  drive();
  delay(40);
}
