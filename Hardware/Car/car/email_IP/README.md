##  Code for Pi to send Current IP on start-up

####	 Fixed the issue we were having with the car getting different IP addresses.
####   Took Savi's idea, and implented it with two shell scripts and a python script.
####	 Store all commands in .txt files (in /commands/) so that the code can be quickly customized.

####   I've configured the Pi to connect to the internet before it starts up.
####   It then runs sendIP.sh at the end of its bootscript.
