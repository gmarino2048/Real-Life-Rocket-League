/* Soccar Project
 * 
 * v 1.0
 * 
 * Web Connection Module
 * 
 * (Description Here)
 * 
 * This script was developed for the particle Photon
 */

# include <string.h>
# include <ctype.h>

int score1;
int score2;

int minutes;

String username1;
String username2;

String infoBuffer;
String variableKey;
String variableValue;



void setup() {
    score1 = 0;
    score2 = 0;
    
    username1 = "USER1";
    username2 = "USER2";
    
    Particle.variable("Score1", score1);
    Particle.variable("Score2", score2);
    
    Particle.variable("Username1", username1);
    Particle.variable("Username2", username2);
    
    Particle.variable("TimeLeft", minutes);
    
    Particle.function("SetScore1", setScore1);
    Particle.function("SetScore2", setScore2);
    
    Particle.function("SetUName1", setUsername1);
    Particle.function("SetUName2", setUsername2);
    
    Particle.function("SetTime", setTime);
    
    Particle.function("SendRaw", sendRaw);
    
    Particle.function("SendScore1", sendScore1);
    Particle.function("SendScore2", sendScore2);
    
    Particle.function("SendUname1", sendUName1);
    Particle.function("SendUname2", sendUName2);
    
    Particle.function("SendTime", sendTime);
    
    Serial1.begin(9600);
    
    sendScore1("");
    sendScore2("");
    sendUName1("");
    sendUName2("");
}

void loop () {
    while (Serial1.available()){
    char c = Serial1.read();
    if (c != '\n'){
      infoBuffer.concat(String(c));
    }
    else {
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

// Sends raw output from the particle console to the arduino due
// Raw key and raw value are separated by :
int sendRaw (String regex){
    // Separate the string into both parts
    int length = regex.length();
    
    int separate = find(regex, ':');
    
    // Make sure the string has a key and a value;
    if (separate == -1){
        postError("Separate character not found. Use \':\' to separate key and value");
        return -1;
    }
    
    // Separate the key from the value
    String key = regex.substring(0, separate);
    String value;
    
    if (separate >= length){
        value = "";
    }
    else {
        value = regex.substring(separate + 1);
    }
    
    // Generate the printed string
    String retVal = format(key, value);
    
    // Send the result to the cloud
    Particle.publish("Debug", retVal);
    
    // Print the string to the due
    Serial1.println(retVal);
    return 0;
}


int setScore1 (String score_1){
    int length = score_1.length();
    
    for (int i = 0; i < length; i++){
        if (isalpha(score_1.charAt(i))){
            return -1;
        }
    }
    
    score1 = atoi(score_1);
    
    return 0;
}

int setScore2 (String score_2){
    int length = score_2.length();
    
    for (int i = 0; i < length; i++){
        if (isalpha(score_2.charAt(i))){
            return -1;
        }
    }
    
    score2 = atoi(score_2);
    
    return 0;
}

// Sets the first username to the input string
int setUsername1 (String username_1){
    username1 = username_1;
    return 0;
}

// Sets the second username to the input string
int setUsername2 (String username_2){
    username2 = username_2;
    return 0;
}

int setTime (String minutesInput){
    int length = strlen(minutesInput);
    
    for (int i = 0; i < length; i++){
        if (isalpha(minutesInput[i])){
            return -1;
        }
    }
    
    minutes = atoi(minutesInput);
    
    return 0;
}

// Sends both players scores to the due. Scores are controlled by the scores on this device
int sendScore1 (String regex){
    // Format the string to send
    String send1 = format("score1", String(score1));
    
    // Send results to the particle cloud
    Particle.publish("Debug", send1);
    
    // Print the results to the due
    Serial1.println(send1);

    return 0;
}

int sendScore2 (String regex) {
    String send2 = format("score2", String(score2));
    
    Particle.publish("Debug", send2);
    
    Serial1.println(send2);
    
    return 0;
}

// Sends the usernames in rapid succession to the arduino due
int sendUName1 (String regex){
    // Format the usernames
    String send1 = format("uname1", username1);
    
    // Publish results to the cloud
    Particle.publish("Debug", send1);
    
    
    // Send the usernames
    Serial1.println(send1);
    
    
    return 0;
}

int sendUName2 (String regex){
    String send2 = format("uname2", username2);
    
    Particle.publish("Debug", send2);
    
    Serial1.println(send2);
    
    return 0;
}

// Sends the time in minutes to 
int sendTime (String regex){
    String send = format("time", String(minutes));
    
    Particle.publish("Debug", send);
    
    Serial1.println(send);
    
    return 0;
}

// Puts the string in the correct format
String format (String key, String value){
    String retVal = "{";
    retVal.concat(key);
    retVal.concat(":");
    retVal.concat(value);
    retVal.concat("}");
    
    return retVal;
}

// Posts an error to the console in the event that something goes wrong
void postError (String message) {
    Particle.publish("ERROR", message);
}

// Finds char position in a string
int find (String regex, char value) {
    int retVal = -1;
    
    for (int i = 0; i < strlen(regex); i++){
        if (regex[i] == value) {
            retVal = i;
            break;
        }
    }
    
    return retVal;
}

void setVals (){
  if (variableKey.equals("score1")){
    int length = strlen(variableValue);
    bool isValid = true;
    
    for (int i = 0; i < length; i++){
        if (isalpha(variableValue[i])){
            isValid = false;
        }
    }
    
    if (isValid){
        score1 = atoi(variableValue);
    }
    
    infoBuffer = "";
  }

  else if (variableKey.equals("score2")){
    int length = strlen(variableValue);
    bool isValid = true;
    
    for (int i = 0; i < length; i++){
        if (isalpha(variableValue[i])){
            isValid = false;
        }
    }
    
    if (isValid){
        Particle.publish("Converting Score");
        score2 = atoi(variableValue);
    }
    Particle.publish(String(score2));
    
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
    int length = strlen(variableValue);
    bool isValid = true;
    
    for (int i = 0; i < length; i++){
        if (isalpha(variableValue[i])){
            isValid = false;
        }
    }
    
    if (isValid){
        minutes = atoi(variableValue);
    }
    
    infoBuffer = "";
  }

  else {
    infoBuffer = "";
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

