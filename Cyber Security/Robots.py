import telnetlib as tnet
import time as time

# The encoding scheme the robots use
def encode(codestr, xor, shift):
    return "".join([basestr[((basestr.index(s)^xor)+shift)%64] for s in codestr])

# The decoding scheme I created
def decode(base,code,xor,shift):
    return "".join([base[(((base.index(s)-shift)%64)^xor)] for s in code])

# Script should result in an error that contains the flag
def Main():
    base = ""
    msg = ""
    truth = ""
    X = 0  # XOR value
    S = 0  # Shift value
    C = 0  # Correct words found
    list = []
    dict = []
    f = open("dict.txt","r")
    # Add all the dictionary words to the dict list
    for word in f:
        word = word[0:(len(word)-1)]
        dict.append(word)
    f.close()
    # Connects to the server and retrieves the base string
    t = tnet.Telnet("fsuctf20.com",2203)
    time.sleep(3)
    base = t.read_until(b"=")
    base = t.read_until(b"\n")
    base = base[1:(len(base)-1)].decode("utf-8")
    print(base)

    # Although this doesn't work perfectly, I learned the first two words of the decoded string
    # are always "A robot", so that makes automating this process a lot more accurate
    while(1):
        X = 0
        S = 0
        time.sleep(0.1)
        # Reads extra text in, also prints msg so that the flag is eventually printed
        msg = t.read_until(b"\n")
        msg = t.read_until(b"\n")
        print(msg)
        # Finds the encoded string and store it into msg
        msg = t.read_until(b"\n")
        msg = t.read_until(b"\n")
        msg = msg[0:(len(msg)-1)].decode("utf-8")
        print(msg)
        # Brute force loop to figure out decoded string since the XOR and shift values are random
        for i in range(0,4096):
            C = 0
            # Stores the supposed decoded string in truth and splits based on words
            truth = decode(base,msg,X,S)
            list = truth.split()
            # Finds the number of words in the decoded string that are also in the dictionary list
            for str in list:
                str = str.lower()
                str = str.capitalize()
                if (str in dict):
                    C+=1
            # If there is more than one word found, and the decoded string
            # contains "A robot" as the first words, write the decoded string to the server
            if ( C >= 1 ):
                print(truth)
                if ( list[1] == "robot" and list[0] == "A" ):
                    t.write(truth.encode("utf-8"))
                    t.write(b"\n")
                    X = 0
                    S = 0
                    break                
            X += 1
            # Since X can only be 8 bits, once it becomes larger I reset it and loop through
            # it all again but with a new shift value until the decoded string is correct
            if ( X == 64 ):
                X = 0
                S += 1
        
if(__name__=="__main__"):
    Main()
