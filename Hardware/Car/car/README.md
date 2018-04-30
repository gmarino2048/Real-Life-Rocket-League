##   Car code --> all code in each car.


####   carStateMachine: arduino script running
####   email_IP/sendIP.sh: shell script that sends IP addr to person running car
		---> calls processIP.py, and smtpCommands.sh to help
####   udp_run.c/run: c script that will run on pi at launch
		---> calls serialComm.py, a script that goes between Pi and Arduino.
####   makefile: compiles for c script
        ---> calls serialComm.py, a script that goes between Pi and Arduino.

#####  remoteControlMod:
        ---> a lil bit of code I wrote for fun, that isn't really part of the project 
        ---> - but I didn't know where else to put it. 
        ---> if you have a linux machine it allows you to control the car without backend/frontend
