/*  Soccar Project
 *  
 *  v 1.0
 *  
 *  LCD Display
 *  
 *  This script was written for the Arduino DUE
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

 // Create variables for string Analysis
 String infoBuffer;
 String variableKey;
 String variableValue;

 // Create the char arrays for usernames (Spoofed versions)
 String uname1;
 String uname2;

 // Create the ints to store scores
 int score1;
 int score2;

 // Create the int to store the time
 int minutes;

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
  
  formatLCD();
  printToLCD();
}

void setVals (){
  if (variableKey.equals("score1")){
    score1 = variableValue.toInt();
    infoBuffer = "";
  }

  else if (variableKey.equals("score2")){
    score2 = variableValue.toInt();
    infoBuffer = "";
  }

  else if (variableKey.equals("uname1")){
    uname1 = variableValue;
    infoBuffer = "";
  }

  else if (variableKey.equals("uname2")){
    uname2 = variableValue;
    infoBuffer = "";
  }

  else if (variableKey.equals("time")) {
    minutes = variableValue.toInt();
    reset();
    infoBuffer = "";
  }

  else {
    infoBuffer = "";
  }
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
void formatLCD () {
  // Create two buffers to pad username space
  char temp1[] = "";
  char temp2[] = "";

  char *username1;
  char *username2;

  // Fill in the padding arrays
  int i = uname1.length();
  while (i < 6) {
    sprintf(temp1, "%s ", temp1);
    i++;
  }

  int j = uname2.length();
  while (j < 6) {
    sprintf(temp2, "%s ", temp2);
    j++;
  }

  i = uname1.length();
  j = uname2.length();

  if (i > 6){
    uname1.substring(0, 6).toCharArray(username1, 6);
  }
  else {
    uname1.toCharArray(username1, uname1.length());
  }

  if (i > 6){
    uname2.substring(0, 6).toCharArray(username2, 6);
  }
  else {
    uname2.toCharArray(username2, uname2.length());
  }
  
  // Format the first line
  sprintf(line1, "%s:%.02d%s  TIME", username1, score1, temp1);

  // Get the current time in mins and secs
  int minutes = getMinutes(currentTime);
  int seconds = getSeconds(currentTime);

  // Format the second line
  sprintf(line2, "%.6s:%.02d%s  %02d:%02d", username2, score2, temp2, minutes, seconds);
}

void printToLCD () {
  // Print the second line
  lcd.setCursor(0,1);
  lcd.write(line2);

  // Print the first line
  lcd.setCursor(0,0);
  lcd.write(line1);
}

// Verify the syntax of the incoming string
bool verify (String regex) {
  bool starts = regex.startsWith("{");
  bool ends = regex.charAt(regex.length() - 2) == '}';
  bool isSplit = regex.indexOf(':') != -1;

  return starts && ends && isSplit;
}

String getKey (String regex){
  int spacer = regex.indexOf(":");
  if (spacer != 1 && spacer != -1){
    String key = regex.substring(1, spacer);
    return key;
  }
  else {
    return "";
  }
}

String getValue (String regex) {
  int spacer = regex.indexOf(":");
  if (spacer < regex.length() && spacer != -1){
    String value = regex.substring(spacer + 1, regex.length() - 2);
    return value;
  }
  else {
    return "";
  }
}

void reset () {
  startTime = minutes * 60;
  lastReset = millis();
}

