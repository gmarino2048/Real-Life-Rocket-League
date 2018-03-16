##   only remaining issue is with timed loops in carStateMachine.ino

####   blank_routine: used while testing, just blank arduino script so I could leave arduino wired up without worrying about it
####   carStateMachine: arduino script car will run on, thoroughly commented - issues with timed loops
####   udp_run.c/run: c script that will run on pi at launch
####   udp_test.c/test: c script I'm running on my computer to test pi response
####   makefile: compilation for both c scripts
####   serialComm.py: python script that goes between pi and arduino
