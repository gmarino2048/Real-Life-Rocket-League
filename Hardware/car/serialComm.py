#Griffin Saiia, Gjs64
#routine to send all states through to arduino
import serial
import time
import os
import sys

def main():
    #path for FIFO
    FIFO = "/tmp/myfifo"
    #control loop
    time.sleep(1)
    while True:
        print("Opening pipe...")
        with open(FIFO) as fifo:
            print("pipe open")
            while True:
                #checks state
                state = fifo.read()
                if len(state) == 0:
                    print("no state in pipe.")
                    break
                print('{0}'.format(state))
                #sets the port of the arduino + the baud rate
                ser = serial.Serial('/dev/ttyACM0',9600)
                time.sleep(1)
                ser.write(b"{0}".format(state))
                ser.close()
                if state == '-1':
                    KeyboardInterrupt
                break
    os.remove("/tmp/myfifo")

if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print 'Interrupted'
        try:
            os.remove("/tmp/myfifo")
            sys.exit(0)
        except SystemExit:
            os._exit(0)
