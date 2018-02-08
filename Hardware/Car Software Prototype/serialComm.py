#Griffin Saiia, Gjs64
#routine to put arduino into reverse state
import serial
import time
import os
import sys

#path for FIFO
path = "/tmp/myfifo"
#sets the port of the arduino + the baud rate
ser = serial.Serial('/dev/ttyACM0',9600)
#waits on connection
time.sleep(2)

#control loop
while True:
    #checks state
    fifo = open(path, "r")
    state = int(fifo.readline(0))
    fifo.close
    #checks end game state
    if(data == -1):
        break
    ser.write(data)
    #time delay
    time.sleep(1)
#close serial connection
ser.close()
