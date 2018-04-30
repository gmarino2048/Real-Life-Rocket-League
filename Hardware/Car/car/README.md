##   Everything works like a charm - minimal delay.

####   email_IP/sendIP.sh: shell script that sends IP addr to person running car
				---> calls processIP.py, and smtpCommands.sh to help
####   udp_run.c/run: c script that will run on pi at launch
				---> calls serialComm.py, a script that goes between
						 Pi and Arduino.
####   makefile: compilation for c script
####   carStateMachine: arduino script car will run on, thoroughly commented
