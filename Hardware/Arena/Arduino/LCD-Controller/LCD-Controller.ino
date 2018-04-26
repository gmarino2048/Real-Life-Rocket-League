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
 String line1;
 String line2;

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
 

void setup() {
  // Begin the LCD Display
  lcd.begin(16, 2);
  Serial.begin(9600);

  // Initialize the values
  minutes = 1;
  uname1 = "USER1";
  uname2 = "USER2";
  score1 = 0;
  score2 = 0;

  variableKey = "";
  variableValue = "";
  
  // Initialize the countdown in seconds
  startTime = minutes*60;
  currentTime = startTime;
  lastReset = millis();

  // For some reason it doesn't work if you remove this...
  Serial.begin(9600);
}

void loop() {
  // Decrement the current time and get the min:sec representation
  if (currentTime != 0L){
    currentTime = startTime - ((millis() - lastReset) / 1000);
  }
  
  formatLCD();
  printToLCD();

  while (Serial.available()){
    char c = Serial.read();
    if (c != '\n'){
      infoBuffer.concat(String(c));
    }
    else {
      Serial.println(infoBuffer);
      if (verify(infoBuffer)){
        variableKey = getKey(infoBuffer);
        variableValue = getValue(infoBuffer);
        setVals();
      }
      else {
        infoBuffer = "";
      }
    }
  }
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
    Serial.println(uname1);
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
  line1 = uname1.substring(0, 6);
  line2 = uname2.substring(0, 6);

  line1.concat(":");
  line2.concat(":");

  line1.concat(String(score1));
  line2.concat(String(score2));

  while(line1.length() < 11){
    line1.concat(" ");
  }

  while(line2.length() <= 10){
    line2.concat(" ");
  }

  line1.concat(" TIME");

  char timeBuffer[5];
  sprintf(timeBuffer, "%.02d:%.02d", getMinutes(currentTime), getSeconds(currentTime));
  line2.concat(String(timeBuffer));
}

void printToLCD () {
  // Print the second line
  lcd.setCursor(0,1);
  lcd.print(line2);

  // Print the first line
  lcd.setCursor(0,0);
  lcd.print(line1);
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
  currentTime = startTime;
  lastReset = millis();
}

