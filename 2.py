from bluetooth import *
foundDevs = discover_devices(lookup_names=True)
for(addr,name) in foundDevs:    
    print "[*] Found Bluetooth Device :  " +str(name)
    print "[+] MAC address :  " +str(addr)