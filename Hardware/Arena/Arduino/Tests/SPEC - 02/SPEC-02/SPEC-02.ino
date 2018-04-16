/*  Soccar Project
 *  
 *  v 1.0
 *  
 *  SPEC - 02
 *  
 *  This script will be a test of the arduino's ability
 *  to count format the LCD display. The display should
 *  show the first 6 chars of a user's username, the current
 *  time along with a TIME header and scores
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

 // Create the string buffers
 char line1[16];
 char line2[16];

 // Create the variables used in the time display
 long startTime;
 long currentTime;
 long lastReset;


 // Create the char arrays for usernames (Spoofed versions)
 char uname1[] = "SHORT";
 char uname2[] = "THIS ONE IS EVEN LONGER";

 // Create the ints to store scores
 int score1;
 int score2;

 // Temporary value to increment scores
 bool scorebool;
 

void setup() {
  // Begin the LCD Display
  lcd.begin(16, 2);

  // Initialize the countdown in seconds
  startTime = 5*60;
  currentTime = startTime;
  lastReset = millis();

  // Instantiate the scores
  score1 = 0;
  score2 = 0;

  // For some reason it doesn't work if you remove this...
  Serial.begin(9600);
}

void loop() {
  // Decrement the current time and get the min:sec representation
  if (currentTime != 0L){
    currentTime = startTime - ((millis() - lastReset) / 1000);

    // Increment the scores in an alternating fashion
    if (score1 < 99 && score2 < 99){
      score1 = millis() / 2000;
      score2 = (millis()+1000) / 2000;
    }
  }
  
  format();

  /*
   * DEVELOPER'S NOTES:
   * 
   * There was a bug in this test which required the second line
   * to be printed first in order to avoid completely erasing
   * the first line.
   */

  // Print the second line
  lcd.setCursor(0,1);
  lcd.write(line2);

  // Print the first line
  lcd.setCursor(0,0);
  lcd.write(line1);

}

// Gets the number of minutes left in timeInSeconds
int getMinutes (long timeInSeconds) {
  return (int) timeInSeconds/60;
}

// Gets the number of seconds left in timeInSeconds
int getSeconds (long timeInSeconds) {
  return (int) timeInSeconds%60;
}

// Formats both lines of the arena display
void format () {
  // Create two buffers to pad username space
  char temp1[] = "";
  char temp2[] = "";

  // Fill in the padding arrays
  int i = strlen(uname1);
  
  while (i < 6){
    sprintf(temp1, "%s ", temp1);
    i++;
  }

  int j = strlen(uname2);
  while (j < 6) {
    sprintf(temp2, "%s ", temp2);
    j++;
  }

  // Format the first line
  sprintf(line1, "%.6s:%.02d%s  TIME", uname1, score1, temp1);

  // Get the current time in mins and secs
  int minutes = getMinutes(currentTime);
  int seconds = getSeconds(currentTime);

  // Format the second line
  sprintf(line2, "%.6s:%.02d%s  %02d:%02d", uname2, score2, temp2, minutes, seconds);
}

