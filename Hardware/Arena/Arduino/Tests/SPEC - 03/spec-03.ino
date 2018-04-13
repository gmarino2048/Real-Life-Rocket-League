/* Soccar Project
 * 
 * v 1.0
 * 
 * The purpose of this test is to ensure that the particle photon
 * can correctly subscribe and post things to the cloud. This code
 * will subscribe to a service and then echo back the received
 * data to the cloud. This demo will also demonstrate retrievable variables
 * and callable functions.
 * 
 * This script was written for the particle Photon
 * 
 */

int counter;

void setup() {
    counter = 0;
    
    // Show the value of counter in the cloud
    Particle.variable("counter", counter);
    
    // Allow reset function to be called from the cloud
    Particle.function("Reset", resetCounter);
}

void loop() {
    // Increment the counter every second
    counter ++;
    delay(1000);
}

int resetCounter(String datatype) {
    counter = 0;
    Particle.publish("Reset", "The counter has been reset.");
    return 0;
}

class Echo {
    public:
    Echo () {
        Particle.subscribe("Echo", &Echo::handler, this);
    }
    void handler (const char *eventName, const char* data) {
        Particle.publish("Echo Received", data);
    }
};

