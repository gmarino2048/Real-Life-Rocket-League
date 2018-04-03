//Griffin Saiia, Gjs64
char states[] = {'a', '1', '9', '4', '5', '9', 'a'};
char state;
int i = 0;


void setup() {
  // initialize digital pin LED_BUILTIN as an output.
  pinMode(LED_BUILTIN, OUTPUT);  
}

//called at end of loop whenever there is new serial data
void serialEvent(){
  while(Serial.available()){
    state = (char)(Serial.read());
  }
  if(state == states[i]){
    digitalWrite(LED_BUILTIN, HIGH);
  }
  else{
    digitalWrite(LED_BUILTIN, LOW);
  }
  i++;
}

void loop() {
}
