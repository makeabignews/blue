kill $(ps aux | grep -m 1 'hc05.py' | awk '{ print $2 }')
