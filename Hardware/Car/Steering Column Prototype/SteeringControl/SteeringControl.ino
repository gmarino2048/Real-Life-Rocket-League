//Guy Marino
//gxm262

//Simple steering control to run the motor which powers the
//steering column
/*
#include<AccelStepper.h>

#define INPUT_1 3
#define INPUT_2 4
#define INPUT_3 5
#define INPUT_4 6

#define HALFSTEP 8

AccelStepper stepper1(HALFSTEP, INPUT_1, INPUT_2, INPUT_3, INPUT_4);

void setup () {
  pinMode(13, OUTPUT);
  pinMode(INPUT_1, OUTPUT);
  pinMode(INPUT_2, OUTPUT);
  pinMode(INPUT_3, OUTPUT);
  pinMode(INPUT_4, OUTPUT);

  stepper1.setMaxSpeed(2000.0);
  stepper1.move(1);
  stepper1.setAcceleration(100.0);
  stepper1.setSpeed(1000);
  stepper1.moveTo(0);
  stepper1.run();
}

void loop () {
  digitalWrite(13, HIGH);
  stepper1.moveTo(2000);
  stepper1.run();
  delay(1000);
  digitalWrite(13, LOW);
  stepper1.moveTo(-2000);
  delay(1000);
}
*/

#include <AccelStepper.h>
#define HALFSTEP 8

// Motor pin definitions
#define motorPin1  3     // IN1 on the ULN2003 driver 1
#define motorPin2  4     // IN2 on the ULN2003 driver 1
#define motorPin3  5     // IN3 on the ULN2003 driver 1
#define motorPin4  6     // IN4 on the ULN2003 driver 1

// Initialize with pin sequence IN1-IN3-IN2-IN4 for using the AccelStepper with 28BYJ-48
AccelStepper stepper1(HALFSTEP, motorPin1, motorPin3, motorPin2, motorPin4);

void setup() {
  stepper1.setMaxSpeed(1000.0);
  stepper1.setAcceleration(100.0);
  stepper1.setSpeed(200);
  stepper1.moveTo(20000);

}//--(end setup )---

void loop() {

  //Change direction when the stepper reaches the target position
  if (stepper1.distanceToGo() == 0) {
    stepper1.moveTo(-stepper1.currentPosition());
  }
  stepper1.run();
}
