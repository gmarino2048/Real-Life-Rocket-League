/*  Soccar Project
 *  
 *  v 1.0
 *  
 *  Arena-Controller
 *  
 *  This script was written for the Arduino DUE
 */

// Data transfer variables
String infoBuffer;

String variableKey;
String variableValue;

int score1;
int score2;

String username1;
String username2;

int timeInMinutes;

// Interrupt Variables
long lastInterrupt;


void setup() {
  // put your setup code here, to run once:
  Serial1.begin(9600);
  Serial2.begin(9600);
  SerialUSB.begin(9600);

  infoBuffer = "";
  lastInterrupt = 0;

  attachInterrupt(digitalPinToInterrupt(52), isr1, FALLING);
  attachInterrupt(digitalPinToInterrupt(53), isr2, FALLING);
}

void loop() {
  // put your main code here, to run repeatedly:
}

void serialEvent1(){
  while (Serial1.available()){
    char c = Serial1.read();
    if (c != '\n'){
      infoBuffer.concat(String(c));
    }
    else {
      if (verify(infoBuffer)){
        variableKey = getKey(infoBuffer);
        variableValue = getValue(infoBuffer);
        SerialUSB.println(variableKey);
        SerialUSB.println(variableValue);
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
    Serial2.println(format(variableKey, variableValue));
    SerialUSB.println(format(variableKey, variableValue));
    infoBuffer = "";
  }

  else if (variableKey.equals("score2")){
    score2 = variableValue.toInt();
    Serial2.println(format(variableKey, variableValue ));
    infoBuffer = "";
  }

  else if (variableKey.equals("uname1")){
    username1 = variableValue;
    Serial2.println(format(variableKey, variableValue));
    infoBuffer = "";
  }

  else if (variableKey.equals("uname2")){
    username2 = variableValue;
    Serial2.println(format(variableKey, variableValue));
    infoBuffer = "";
  }

  else if (variableKey.equals("time")) {
    timeInMinutes = variableValue.toInt();
    Serial2.println(format(variableKey, variableValue));
    infoBuffer = "";
  }

  else {
    infoBuffer = "";
  }

  SerialUSB.println(score1);
  SerialUSB.println(score2);
  SerialUSB.println(username1);
  SerialUSB.println(username2);
  SerialUSB.println(timeInMinutes);
}

void isr1 () {
  if (millis() - lastInterrupt > 2500){
    score1 ++;
    //Send Score Here
    String temp = format("score1", String(score1));
    Serial1.println(temp);
    Serial2.println(temp);
    
    SerialUSB.println(temp);
    lastInterrupt = millis();
  }
}

void isr2 () {
  if (millis() - lastInterrupt > 2500){
    score2 ++;
    //Send Score Here
    String temp = format("score2", String(score2));
    Serial1.println(temp);
    Serial2.println(temp);
    
    SerialUSB.println(temp);
    lastInterrupt = millis();
  }
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

String format (String key, String value){
    String retVal = "{";
    retVal.concat(key);
    retVal.concat(":");
    retVal.concat(value);
    retVal.concat("}");
    
    return retVal;
}

