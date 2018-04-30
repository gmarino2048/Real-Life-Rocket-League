##   Car code --> all code in each car.


####   carStateMachine: script running on arduino
####   /email_IP/sendIP.sh: shell script runs on start up
        ---> sends IP address of car to specified email
		---> calls processIP.py, and smtpCommands.sh to help
####   run: c script that runs after shell script
        ---> udp_run.c compiles to run, this is done prior to startup.
		---> calls serialComm.py, a script that goes between Pi and Arduino.
####   makefile: compiles udp_run.c

#####  remoteControlMod:
        ---> a lil bit of code I wrote for fun, that isn't really part of the project 
        ---> - but I didn't know where else to put it. 
        ---> if you have a linux machine it allows you to control the car without backend/frontend
