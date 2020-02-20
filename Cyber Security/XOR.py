import os
import binascii

def Main():
    # Opens files and figures out file1 size
    file1 = bytearray(open("meme7.png","rb").read())
    file2 = open("RESULT","wb")
    counter = 0
    size = os.stat('./COPY').st_size

    # Loops through pairs of bytes in file1
    # XOR's first byte with 24 and second byte with 70, and writes to file2
    # The result is a png file containing the flag
    while( counter < size ):
        file2.write((file1[counter]^24).to_bytes(1,'big'))
        counter = counter + 1
        file2.write((file1[counter]^70).to_bytes(1,'big'))
        counter = counter + 1

    file1.close()
    file2.close()


if(__name__=="__main__"):
    Main()
