#define StepIn1  4
#define StepIn2  5
#define StepIn3  6
#define StepIn4  7
#define DELAYTIME  1//ms

void setup() {
  pinMode(StepIn1, OUTPUT);
  pinMode(StepIn2, OUTPUT);
  pinMode(StepIn3, OUTPUT);
  pinMode(StepIn4, OUTPUT);
}


void stepRoutine(int Direction){
  for(int i = 0; i < 10; i++){
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
     off(); 
  }
}
void off(){
  digitalWrite(StepIn1, LOW);
  digitalWrite(StepIn2, LOW);
  digitalWrite(StepIn3, LOW);
  digitalWrite(StepIn4, LOW);
}

void loop() {
  stepRoutine(1);
  delay(5000);
  stepRoutine(-1);
}
