# Griffin Saiia, gjs64
# lil shell script that sends an email to my email when the raspberry pi starts
# with the current IP address of the raspberry pi.

# This allows for a completely headless setup - no display needed.

#writes 'ip addr' command to file
echo $(ip addr) > currentIP.txt;
#python script that edits and rewrites the file, so it's just IPs
python processIP.py;
echo "current address written"
sleep 1;
echo "sending mail.."
sleep 1;
#calls a shell script that prints all smtp commands into the window
#pipes this with an smtp server request
./smtpCommands.sh | telnet smtp.cwru.edu 25
echo "address sent."

# allows quick changes to all smtp request specifiers

#which server
echo "helo eecs.case.edu" < ~/Desktop/car/email_IP/commands/handshake.txt
#address mail is coming from
echo "mail from: <pi@case.edu>" < ~/Desktop/car/email_IP/commands/from.txt
#address mail is going to
echo "rcpt to: <gjbsaiia@gmail.com>" < ~/Desktop/car/email_IP/commands/to.txt
#email content specifier
echo "data" <~/Desktop/car/email_IP/commands/contents.txt

#actual message is stored in currentIP.txt

#end of message command
echo "." < ~/Desktop/car/email_IP/commands/endMsg.txt
#close connection command
echo "quit" < ~/Desktop/car/email_IP/commands/quit.txt
