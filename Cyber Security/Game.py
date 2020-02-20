import telnetlib as tnet
import time as time

def Main():
    Total = 0
    number1 = 0
    sign = 0
    number2 = 0
    counter = 0
    start = 0
    i = 0
    tempStr = ""
    t = tnet.Telnet("fsuctf20.com",2202)
    time.sleep(5)
    
    while (1):
        time.sleep(0.1)
        resp = t.read_until(b":")
        if ( resp[14] == 102 ):
            print(resp)
        resp = t.read_until(b"=")
        resp = resp[1:(len(resp)-2)].decode("utf-8")
        print(resp)
        i = 0
        start = 0
        while(int(resp[i]) != 32 ):
            i+=1
            if(resp[i] == " "):
                break
        tempStr = str(resp[start:i])
        number1 = int(tempStr.replace("," , ""))
#       print(number1, end = " ")
        i+=1
        start = i
        sign = resp[i]
#       print(sign, end = " ")
        i+=1
        i+=1
        tempStr = str(resp[i:len(resp)])
        number2 = int(tempStr.replace("," , ""))
#       print(number2, end = " ")

        if ( sign == "+" ):
            Total = str(number1 + number2)
        elif ( sign == "-" ):
            Total = str(number1 - number2)
        elif ( sign == "*" ):
            Total = str(number1 * number2)
#       print(Total)
        
        Total = Total.encode("utf-8")
        t.write(Total)
        t.write(b"\n")
    
if (__name__=="__main__"):
    Main()
        
