#Griffin Saiia, gjs64
# lil shell script that allows you to control the car without any of the other components.
# to run replace the ip in the nc command, with the current IP command

echo "Control your networked car using 'WASD' for movement,
'q' for break, 'e' for boost, and 'p' to quit."

# calls helper script to listen to keystrokes and then write the corresponding
# states into the window.
# pipes this with a netcat (nc) udp packet command--> sends states as udp packets
./keystrokeListener.sh | nc -u 172.20.42.174 1234
