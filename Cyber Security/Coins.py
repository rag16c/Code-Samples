import telnetlib as tnet
import time as time

def Main():
    # Setup variables and connect to server with Telnet
    Total = 0
    currMoney = 0
    change = 0
    t = tnet.Telnet("fsuctf20.com",2201) 
    time.sleep(3)
    
    # Loop parsing and making change until not prompted to
    # After loop ends Flag should print
    while (1):
        time.sleep(0.1)
        resp = t.read_until(b"\n") # Should read the first sentence
        # If the first character is 'F', stop I have the flag
        # Else read teh money value into resp
        if ( resp[0] == 70 ):
            break
        if ( resp[0] == 32 ):
            print(resp)
            time.sleep(0.1)
            resp = t.read_until(b"\n")
        # Format the money to be actual numbers and remove ','
        resp = resp[1:(len(resp)-1)].decode("utf-8")
        Total = float(resp.replace("," , ""))
        print(Total)
        
        #Loops through change if >= $1
        for i in range(10):
            change = 0
            # Store change value into resp and format to make it a number
            resp = t.read_until(b"$")
            resp = t.read_until(b":")
            resp = resp[0:(len(resp)-7)].decode("utf-8")
            currMoney = float(resp.replace("," , ""))
            # Print statements to make sure it is working
            print(currMoney, end=" ")
            # Figure out how much of the specified change value I can take out of the 
            # Total money left
            if ( currMoney != 0 ):
                change = str(int(Total/currMoney))
            else:
                change = 0
            print(change)
            # Change the Total money left, and send the amount of change to the server
            change = change.encode("utf-8")
            Total -= int(Total/currMoney)*currMoney
            Total = round(Total,2)
            t.write(change) 
            t.write(b"\n")
            change = 0 

        # Loops through change < $1 since it has a different format
        for i in range(5):
            change = 0
            resp = t.read_until(b"(")
            resp = t.read_until(b":")
            resp = resp[0:(len(resp)-3)].decode("utf-8")
            currMoney = float(resp.replace("," , ""))*0.01
            print(currMoney, end=" ")
            print(": ", end=" ")
            if ( currMoney != 0 ):
                change = str(int(Total/currMoney))
            else:
                change = 0
            print(change)
            change = change.encode("utf-8")
            Total -= int(Total/currMoney)*currMoney
            Total = round(Total,2)
            t.write(change) 
            t.write(b"\n")
            change = 0 
    print(resp)

            
if (__name__=="__main__"):
    Main()
