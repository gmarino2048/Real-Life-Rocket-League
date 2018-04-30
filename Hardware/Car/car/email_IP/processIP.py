# Griffin Saiia, gjs64\
# helper for shell script that cleans 'ip addr' output to just ip addresses


import os
import sys

#cleans up the output of the terminal "ip addr" command
def main():
	filename = "currentIP.txt"
	ip = []
	with open(filename) as f:
		lines = f.readlines()
		f.close()
		flag = False
		for line in lines:
			line = line.split(" ")
			for word in  line:
				if(flag):
					flag = False
					if(word != "127.0.0.1/8"):
						ip.append(word)
				else:
					if word == "inet":
						flag = True
	#clears file
	f = open(filename, 'w')
	for addr in ip:
		f.write(addr+"\n")
	f.close()

if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print 'Interrupted'
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)
