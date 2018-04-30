/*
Soccar Project

SPEC - 06

This code was written for the particle photon

This is just a quick read script to check the Due's capability

*/


String buffer;

void setup() {
    Serial1.begin(9600);
    buffer = "";
}

void loop() {
    while (Serial1.available()){
        char c = Serial1.read();
        
        if (c == '/n'){
            Particle.publish("SPEC-06", buffer);
            buffer = "";
        }
        else {
            buffer.concat(String(c));
        }
    }
}