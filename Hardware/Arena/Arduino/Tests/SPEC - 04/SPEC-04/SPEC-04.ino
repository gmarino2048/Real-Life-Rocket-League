/*  Soccar Project
 *  
 *  v 1.0
 *  
 *  SPEC - 04
 *  
 * The purpose of this test is to ensure that the particle photon
 * can correctly connect to the arduino due and that the due can
 * correctly decode the string and post the component parts to the
 * serial monitor.
 *  
 *  This script was written for the Arduino DUE
 */
String infoBuffer;

String variableKey;
String variableValue;

int score1;
int score2;

String username1;
String username2;

int timeInMinutes;

void setup() {
  // put your setup code here, to run once:
  Serial1.begin(9600);
  SerialUSB.begin(9600);

  infoBuffer = "";

  pinMode(13, OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  /*if (variableKey.equals("score1")){
    score1 = variableValue.toInt();
    infoBuffer = "";
  }

  else if (variableKey.equals("score2")){
    score2 = variableValue.toInt();
    infoBuffer = "";
  }

  else if (variableKey.equals("uname1")){
    username1 = variableValue;
    infoBuffer = "";
  }

  else if (variableKey.equals("uname2")){
    username2 = variableValue;
    infoBuffer = "";
  }

  else if (variableKey.equals("time")) {
    timeInMinutes = variableValue.toInt();
    infoBuffer = "";
  }

  else {
    infoBuffer = "";
  }*/
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
    username1 = variableValue;
    infoBuffer = "";
  }

  else if (variableKey.equals("uname2")){
    username2 = variableValue;
    infoBuffer = "";
  }

  else if (variableKey.equals("time")) {
    timeInMinutes = variableValue.toInt();
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

