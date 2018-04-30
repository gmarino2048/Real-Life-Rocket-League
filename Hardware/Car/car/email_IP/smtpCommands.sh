# Griffin Saiia, gjs64
# helper script that relays all commands into smtp server
# uses .txt files so that everything can be changed easily from sendIP.sh

sleep 1;
echo $(more ~/Desktop/car/email_IP/commands/handshake.txt)
sleep 1;
echo $(more ~/Desktop/car/email_IP/commands/from.txt)
sleep 1;
echo $(more ~/Desktop/car/email_IP/commands/to.txt)
sleep 1;
echo $(more ~/Desktop/car/email_IP/commands/contents.txt)
sleep 1;
echo $(more ~/Desktop/car/email_IP/commands/message.txt)
sleep 1;
echo $(more ~/Desktop/car/email_IP/commands/endMsg.txt)
sleep 1;
echo $(more ~/Desktop/car/email_IP/commands/quit.txt)
