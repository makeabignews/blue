#!/usr/bin/python
# -*- coding:UTF-8 -*-
#author wenbo 2017-11-15
import os
import time
from socket import socket, AF_INET, SOCK_DGRAM

s = socket(AF_INET, SOCK_DGRAM)

def send(msg,addr,port):
    s.sendto(msg, (addr, port))
    #re_msg, addr = s.recvfrom(size)
    #print re_msg
def init():
    os.system("sudo hciconfig hci0 down")
    os.system("sudo hciconfig hci0 up")
    send("请打开温度计",'192.168.2.22',33333)
    time.sleep(5)
def get_value():
    process = os.popen("sudo gatttool -b C6:05:04:03:F1:BF --char-read -a 0x001b")
    value = process.read()
    return value
def out_txt(str):
    #str="Characteristic value/descriptor: fe fd 40 01 6b 0a"
    msg=""
    state=""
    temp_str=""
    str=str[33:50].replace(" ","").replace("fefd","").replace("0a","")
    if len(str)==6:
        state=str[0:2] #状态：00表示测量中温度正常，40表示测量结束，温度正常。41表示测量中，温度低，42表示测量结束，温度高。A0表示错误，A1表示电压不足。
        temp_hex=str[2:6] #16进制温度
        temp_int=int(temp_hex,16)*0.1 #转化为10进制的温度
        temp_str="%s℃" % (temp_int) #字符串的温度
        if state=="00":
            msg="测量中 %s" % (temp_str)
        if state=="40":
            msg="测量成功，您的温度是：%s" % (temp_str)
        if state=="42":
            msg="体温异常！您的温度是：%s" % (temp_str)
        if state=="A0":
            msg="系统错误！"
        if state=="A1":
            msg="电池不足，请更换！"
    return msg,state,temp_str
if __name__ == '__main__':
    init()
    while True:
        time.sleep(1)
        text,state,temp_str=out_txt(get_value())
        if(state=="00"):
            print(text)
            send(text,'192.168.2.22',33333)
        if(state=="40" or state=="42"):
            post="unknowUser_%s" % temp_str
            print ("ok")
            send(text,'192.168.2.22',33333)
            send(post,'semantic.semioe.com',33333)
            os.sys.exit()
