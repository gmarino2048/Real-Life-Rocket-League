#include <LiquidCrystal.h>

// initialize the library by associating any needed LCD interface pin
// with the arduino pin number it is connected to
const int rs = 12, en = 11, d4 = 5, d5 = 4, d6 = 3, d7 = 2;
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);

char rw[16];
char legacy[16];
bool line;

void setup() {
  // set up the LCD's number of columns and rows:
  lcd.begin(16, 2);
  // Print a message to the LCD

  pinMode(9, INPUT);
  pinMode(13, OUTPUT);

  Serial.begin(9600);
  line = 0;
}

void loop() {
  // set the cursor to column 0, line 1
  // (note: line 1 is the second row, since counting begins with 0):
  digitalWrite(13, digitalRead(9));

  while (Serial.available()){
    char c = Serial.read();

    if (c == '\n'){
      lcd.clear();
      lcd.setCursor(0,line);
      lcd.print(rw);
      lcd.setCursor(0,!line);
      lcd.print(legacy);
      sprintf(legacy, "%s", rw);
      line = !line;
      memset(rw, 0, sizeof(rw));
    }
    else {
      sprintf(rw, "%s%c", rw, c);
    }
  }
} 
