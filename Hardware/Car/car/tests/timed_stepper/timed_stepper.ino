//Griffin Saiia, Gjs64
//SocCar: Timed Stepper Test

//*************turn motor (stepper motor)*****************
  #define sIn1  4//pin
  #define sIn2  5//pin
  #define sIn3  6//pin
  #define sIn4  7//pin
  //delay between polarity shifts for stepper
  #define DELAYTIME  1//ms
  //how long steppers should run for 90 degree turn
  #define TURNTIME  10000//ms
  
  //timer for whole program
  unsigned long totalTime;

void setup(){
  //sets stepper control pins
  pinMode(sIn1, OUTPUT);
  pinMode(sIn2, OUTPUT);
  pinMode(sIn3, OUTPUT);
  pinMode(sIn4, OUTPUT);
  totalTime = millis();
}

void stepRoutine(int lOrR, unsigned long time){
  off();
  //so that they can be set in loop
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
  off();
}

void off(){
  digitalWrite(sIn1, LOW);
  digitalWrite(sIn2, LOW);
  digitalWrite(sIn3, LOW);
  digitalWrite(sIn4, LOW);
}

void loop(){
  stepRoutine(1, TURNTIME);
  delay(5000);
  stepRoutine(-1, TURNTIME);
  delay(5000);
}
