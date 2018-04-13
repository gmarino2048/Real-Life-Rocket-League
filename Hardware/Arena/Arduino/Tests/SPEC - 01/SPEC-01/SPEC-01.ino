/*  Soccar Project
 *  
 *  v 1.0
 *  
 *  SPEC - 01
 *  
 *  This script will be a test of the arduino's ability
 *  to count down the time in minutes and seconds using
 *  the built in millis library. The script will, by 
 *  default, start at 5 minutes and count down to zero.
 *  
 *  This script was written for the Arduino NANO
 */

 // Define the Liquid Crystal Pinouts
 #define RS 2
 #define EN 3
 #define D0 4
 #define D1 5
 #define D2 6
 #define D3 7

 // Include the LiquidCrystal library
 #include <LiquidCrystal.h>

 // Set up the lcd object to use
 LiquidCrystal lcd(RS, EN, D0, D1, D2, D3);

 // Create the string buffer
 char timeBuffer[16];

 long startTime;
 long currentTime;
 long lastReset;

void setup() {
  // Begin the LCD Display
  lcd.begin(16, 2);

  // Initialize the countdown in seconds
  startTime = 5*60;
  currentTime = startTime;
  lastReset = millis();
}

void loop() {
  // Decrement the current time and get the min:sec representation
  if (currentTime != 0L){
    currentTime = startTime - ((millis() - lastReset) / 1000);
  }

  int minutes = getMinutes(currentTime);
  int seconds = getSeconds(currentTime);

  char toDisplay[16];
  sprintf(toDisplay, "Time: %02d:%02d", minutes, seconds);

  lcd.setCursor(0,0);
  lcd.print(toDisplay);
}

//Gets the number of minutes left in timeInSeconds
int getMinutes (long timeInSeconds) {
  return (int) timeInSeconds/60;
}

//Gets the number of seconds left in timeInSeconds
int getSeconds (long timeInSeconds) {
  return (int) timeInSeconds%60;
}
