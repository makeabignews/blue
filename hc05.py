import serial
import time
import RPi.GPIO as GPIO
#GPIO.setmode(GPIO.BCM) #cpu bcm2837
GPIO.setmode(GPIO.BOARD) #raspberry pi
outpin=12
GPIO.setup(outpin,GPIO.OUT)
s=serial.Serial("/dev/serial0",9600)
print s.isOpen()
#s.write("ok\n")
while True:
    count=s.inWaiting()
    t=s.read(count)
    if(t=="on"):
        GPIO.output(outpin,GPIO.HIGH)
        print "on"
    if(t=="off"):
        print "off"
        GPIO.output(outpin,GPIO.LOW)
    s.flushInput()
    time.sleep(0.1)
GPIO.clearup()
