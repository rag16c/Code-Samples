/*
  Name: Riley Garrison
  Date: 2/12/2019
  Project: 1

  About: Converting a select amount of MIPS code into hexadecimal
*/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void clearString(char str[]);
int idFunction(char function[]);
void parseRType(int addrCounter, int functionID, char mipsParameters[]);
void parseLType(int addrCounter, int functionID, char mipsParameters[], char labels[][20], int labelsAddr[]);
void parseJType(int addrCounter, int functionID, char mipsParameters[], char labels[][20], int labelsAddr[]);
void parseAdd(char regs[], char reg1[], char reg2[], char reg3[] );
void parseNor(char regs[], char reg1[], char reg2[], char reg3[] );
int parseSll(char regs[], char reg1[], char reg3[] );
int parseAddi( char regs[], char reg1[], char reg2[] );
int parseOri( char regs[], char reg1[], char reg2[] );
int parseLui( char regs[], char reg1[]);
int parseSW( char regs[], char reg1[], char reg2[] );
int parseLW( char regs[], char reg1[], char reg2[] );
int parseBne( char regs[], char reg1[], char reg2[], char labels[][20], int labelsAddr[], int addrCounter );
long parseJ( char mipsParameters[], char labels[][20], int labelsAddr[], int addrCounter );
void parseLa(int addrCounter, int functionID, char mipsParameters[], char labels[][20], int labelsAddr[]);
int regToDecimal(char reg[]);

int main() {
  char labels[100][20];  // Holds label names
  int labelsAddr[100];   // Holds addr of labels
  char tempString[100];  // Just a temp string
  int tempInt = 0;       // Just a temp integer
  int spaceAlloc = 4;    // Used for space allocation in directive
  char mipsFunction[10]; // Holds the mips function like la or add
  char mipsParameters[20];  // Hold the parameters associated with a mips function
  int functionID = 0;     // Distinguishes between the different types of instructions
  int addrCounter = 0;  // Keeps track of which address is currently being looked at
  char peekChar = fgetc(stdin);  // Used to peek Chars
  ungetc(peekChar,stdin);

  // First loop to link label with address
  while(peekChar != EOF) {
    if ( peekChar == '\t' ) {
      peekChar = fgetc(stdin);
      peekChar = fgetc(stdin);
 
      // If la is found then increment the memory used by double
      if ( peekChar != '.' ) {
	if ( peekChar == 'l' ) {
	  peekChar = fgetc(stdin);
	  if ( peekChar == 'a' )
	    tempInt = tempInt + 4;
	}
	tempInt = tempInt + 4;
      }
      // Clears rest of the line
      fgets(tempString,100,stdin); 
    }
    else {
      if ( scanf("%s\t%s ",labels[addrCounter], tempString) == 2 ) { 
	// If la is the instruction, allocate double space
	if( strcmp(tempString,"la") == 0 )
	  spaceAlloc = spaceAlloc+4;
      
	// Adds label to array, then flushes the reset of the line
	labelsAddr[addrCounter] = tempInt;
	fgets(tempString,100,stdin);
	addrCounter++;
      }
      // Keeps track of exactly where in memory the instruction is
      tempInt = tempInt + spaceAlloc;
      spaceAlloc = 4;
    }

    peekChar = fgetc(stdin);
    ungetc(peekChar,stdin);
  }

  // Resets the input stream
  fseek(stdin,0,SEEK_SET);
  
  peekChar = fgetc(stdin);
  addrCounter = 0;

  while( peekChar != EOF ) {
    if ( fscanf(stdin, "\t.%s\n",tempString) == 1 ) {
      // If .data is encountered stop reading
      if ( strcmp(tempString,"text") == 0) {}
      else
	break;
    }
    else if ( fscanf(stdin,"%s\t%s ",mipsFunction,mipsParameters) == 2 ) {
      // Assume no label at first, functionID will return 1 if there is a label
      // Then mipsFunction and mipsParameters will have the correct values
      functionID = idFunction(mipsFunction);
      if ( functionID == -1 ) {
	strcpy(mipsFunction,mipsParameters);
	fscanf(stdin,"%s\n",mipsParameters);
	functionID = idFunction(mipsFunction);
      }

      // Deal with function by what type of function it is
      if ( (int)(functionID/10) == 1 )
	parseRType(addrCounter,functionID, mipsParameters);
      else if ( (int)(functionID/10) == 2 )
	parseLType(addrCounter,functionID, mipsParameters, labels, labelsAddr);
      else if ( (int)(functionID/10) == 3 )
	parseJType(addrCounter,functionID, mipsParameters, labels, labelsAddr);
      else if ( (int)(functionID/10) == 4 ) {
	parseLa(addrCounter,functionID,mipsParameters,labels,labelsAddr);
	addrCounter++;
      }
      addrCounter++;
    }
    
    peekChar = fgetc(stdin);
    ungetc(peekChar,stdin);
  }
  
  return 0;
}

// Sets a string complete with null pointers
void clearString(char str[]) {
  int i = 0;
  while(i < strlen(str)) {
    str[i] = '\0';
  }
}    

// To identify the mips function. Starting value determines what type of function
// 1 = R-Type
// 2 = L-Type
// 3 = J-Type
// 4 = No type
int idFunction(char function[]) {
  if ( strcmp(function,"add") == 0 )
    return 11;
  else if ( strcmp(function,"nor") == 0 )
    return 12;
  else if ( strcmp(function,"sll") == 0 )
    return 13;
  else if ( strcmp(function,"addi") == 0 )
    return 21;
  else if ( strcmp(function,"ori") == 0 )
    return 22;
  else if ( strcmp(function,"lui") == 0 )
    return 23;
  else if ( strcmp(function,"sw") == 0 )
    return 24;
  else if ( strcmp(function,"lw") == 0 )
    return 25;
  else if ( strcmp(function,"bne") == 0 )
    return 26;
  else if ( strcmp(function,"j") == 0 )
    return 31;
  else if ( strcmp(function,"la") == 0 )
    return 41;
  else 
    return -1;
  

}

// Converts add function into hexadecimal and prints
void parseRType(int addrCounter, int functionID, char mipsParameters[]) {
  long mipCode;
  int opcode = 0;
  int rs = 0;
  int rt = 0;
  int rd = 0;                 
  int shift = 0;
  int function = 0;
  char reg1[3];
  char reg2[3];
  char reg3[3];

  if ( functionID == 11 ) {
    // ADD
    parseAdd(mipsParameters,reg1,reg2,reg3);
    opcode = 0;
    function = 32;
    
    // Convert registers to decimal numbers
    rd = regToDecimal(reg1);
    rs = regToDecimal(reg2);
    rt = regToDecimal(reg3);       
    shift = 0;
  }
  else if ( functionID == 12 ) {
    // NOR
    parseNor(mipsParameters,reg1,reg2,reg3);
    opcode = 0;
    function = 39;
    
    rd = regToDecimal(reg1);
    rs = regToDecimal(reg2);
    rt = regToDecimal(reg3);
    shift = 0;
  }
  else if ( functionID == 13 ) {
    // SLL
    shift = parseSll(mipsParameters,reg1,reg3);
    opcode = 0;
    function = 0;
    
    rd = regToDecimal(reg1);
    rs = 0;
    rt = regToDecimal(reg3);
    
  }

  // Prints address
  printf("0x%08X: ",addrCounter*4);

  // Left shift to tack rs onto number
  mipCode = opcode << 5;
  mipCode = mipCode | rs;
  // Tack rt onto number
  mipCode = mipCode << 5;
  mipCode = mipCode | rt;
  // Tack rd on
  mipCode = mipCode << 5;
  mipCode = mipCode | rd;
  // Tack on shift
  mipCode = mipCode << 5;
  mipCode = mipCode | shift;
  // Tack function number on
  mipCode = mipCode << 6;
  mipCode = mipCode | function; 

  // Prints mips function in hex
  printf("0x%08X\n",mipCode);
}

// Parses L Types and prints the code in hex
void parseLType(int addrCounter, int functionID, char mipsParameters[], char labels[][20], int labelsAddr[]) {
  long mipCode = 0;
  int opcode = 0;
  int rs = 0;
  int rt = 0;
  long immed = 0;
  char reg1[3];
  char reg2[3];

  
  if ( functionID == 21 ) {
    // ADDI
    immed = parseAddi(mipsParameters,reg1,reg2); 
    opcode = 8;
    rt = regToDecimal(reg1);
    rs = regToDecimal(reg2);
  }
  else if ( functionID == 22 ) {
    // ORI
    immed = parseOri(mipsParameters,reg1,reg2);
    opcode = 13;
    rt = regToDecimal(reg1);
    rs = regToDecimal(reg2);
  }
  else if ( functionID == 23 ) {
    //LUI
    immed = parseLui(mipsParameters,reg1);
    opcode = 15;
    rt = regToDecimal(reg1);
    rs = 0;
  }
  else if ( functionID == 24 ) {
    //SW
    immed = parseSW(mipsParameters,reg1,reg2);
    opcode = 43;
    rt = regToDecimal(reg1);
    rs = regToDecimal(reg2);
  }
  else if ( functionID == 25 ) {
    // LW
    immed = parseLW(mipsParameters,reg1,reg2);
    opcode = 35;
    rt = regToDecimal(reg1);
    rs = regToDecimal(reg2);
  }
  else if ( functionID == 26 ) {
    // BNE
    immed = parseBne(mipsParameters,reg1,reg2,labels,labelsAddr,addrCounter);
    opcode = 5;
    rt = regToDecimal(reg2);
    rs = regToDecimal(reg1);
  }
  
  // Prints address
  printf("0x%08X: ",addrCounter*4);
  
  mipCode = opcode;
  mipCode = mipCode << 5;
  mipCode = mipCode | rs;
  mipCode = mipCode << 5;
  mipCode = mipCode | rt;
  mipCode = mipCode << 16;
  mipCode = mipCode | immed;

  printf("0x%08X\n",mipCode);
}

// Parses LA instruction and creates a LUI and ORI instruction 
void parseLa(int addrCounter, int functionID, char mipsParameters[], char labels[][20], int labelsAddr[]) {
  char ParametersLUI[25];
  char ParametersORI[25];
  char reg1[3];
  char label[20];
  char tempString[33];
  long address = 0;
  int temp = 0;

  int i = 1;
  reg1[0] = mipsParameters[i];
  reg1[1] = mipsParameters[i+1];
  reg1[2] = '\0';
  while ( mipsParameters[i] != ',' )
    i++;
  i++;

  int j = 0;
  
  // Stores label into the variable label
  while ( (mipsParameters[i] >= 65 && mipsParameters[i] <=90) || (mipsParameters[i] >= 97 && mipsParameters[i] <= 122) || (mipsParameters[i] >= 48 && mipsParameters[i] <= 57) ) {
    label[j] = mipsParameters[i];
    j++;
    i++;
  }
  label[j] = ':';
  label[j+1] = '\0';

  // Finds where in labels the label name is to be able to find its address
  i = 0;
  j = 15;
  while ( strcmp(label,labels[i]) != 0 )
    i++;


  strcpy(ParametersLUI,"$1,");
  strcpy(ParametersORI,"$");
  strcat(ParametersORI,reg1);
  strcat(ParametersORI,",$1,");

  // Store the first 16 bits into the LUI Params
  address = labelsAddr[i];
  address = address >> 16;
  sprintf(tempString, "%d",address);
  clearString(tempString);
  strcat(ParametersLUI,tempString);
  
  // Store the last 16 bits into the ORI Params
  temp = labelsAddr[i];
  address = address << 16;
  temp = temp^address;
  sprintf(tempString, "%d",temp);
  strcat(ParametersORI,tempString);
  
  // Create a LUI and ORI instruction from the la instruction
  parseLType(addrCounter,23,ParametersLUI,labels,labelsAddr);
  parseLType(addrCounter+1,22,ParametersORI,labels,labelsAddr);
} 

// Parses j type and creates the hex code
void parseJType(int addrCounter, int functionID, char mipsParameters[], char labels[][20], int labelsAddr[]) {
  long mipCode = 0;
  int opcode = 0;
  long address = 0;

  if ( functionID == 31 ) {
    // Creates the address for the jump instruction
    address = parseJ(mipsParameters,labels,labelsAddr,addrCounter);
    opcode = 2;
  }

  // Prints address
  printf("0x%08X: ",addrCounter*4);

  mipCode = opcode;
  mipCode = mipCode << 26;
  mipCode = mipCode | address;
  
  printf("0x%08X\n",mipCode);
}

// Creates the address for jump
long parseJ( char mipsParameters[], char labels[][20], int labelsAddr[], int addrCounter ) {
  long address = 0;
  char label[20];
  
  strcpy(label,mipsParameters);
  strcat(label,":");
  
  int i = 0;
  while ( strcmp(label,labels[i]) != 0 )
    i++;

  address = labelsAddr[i];
  address = address & (16*256*256*256-1);
  address = address >> 2;
  return address;
}

// Parses BNE instruction by loading all the registers and returning the immediate
int parseBne( char regs[], char reg1[], char reg2[], char labels[][20], int labelsAddr[], int addrCounter ) {
  long immed = 0;
  char currentLabel[20];
  int i = 1;
  int j = 0;

  // Parses the first register and stores it into reg1
  if ( regs[i] == '0' )
    reg1[0] = '0';
  else {
    reg1[0] = regs[i];
    reg1[1] = regs[i+1];
  }

  // Searches for the point right before the next register in regs
  while ( regs[i] != '$' )
    i++;
  i++;

  // Parses the second register and stores it into reg2
  if ( regs[i] == '0' )
    reg2[0] = '0';
  else {
    reg2[0] = regs[i];
    reg2[1] = regs[i+1];
  }

  // Searches for the point right before the label name
  while( regs[i] != ',' )
    i++;
  i++;

  // Reads in the label name into currentLabel
  while ( (regs[i] >= 65 && regs[i] <=90) || (regs[i] >= 97 && regs[i] <= 122) || (regs[i] >= 48 && regs[i] <= 57) ) {
    currentLabel[j] = regs[i];
    j++;
    i++;
  }
  currentLabel[j] = ':';
  currentLabel[j+1] = '\0';

  // Finds where in labels is the label name to find the address of the label
  i = 0;
  while ( strcmp(currentLabel,labels[i]) != 0 )
    i++;

  // Create the immediate value
  immed = labelsAddr[i];
  immed = immed - (addrCounter*4 + 4);
  immed = immed/4;

  // Use two's compliment if immediate value is negative
  if ( immed < 0 ) {
    immed = immed*-1;
    immed = immed^(256*256-1);
    immed = immed + 1;
  }

  return immed;
}

// Parses LW instruction by loading registers and returning immediate
int parseLW( char regs[], char reg1[], char reg2[] ) {
  int Negative = 0;
  long immed = 0;
  int i = 1;
  reg1[0] = regs[i];
  reg1[1] = regs[i+1];
  
  while( regs[i] != ',' )
    i++;
  i++;

  if ( regs[i] == '-' ) {
    Negative = 1;
    i++;
  }
  
  while( regs[i] >= 48 && regs[i] <= 57 ) {
    immed = immed*10;
    immed = immed + (int)regs[i] -48;
    i++;
  }

  if ( Negative == 1 ) {
    immed = immed^(256*256-1);
    immed = immed + 1;
  }

  while ( regs[i] != '$' )
    i++;
  i++;
  
  reg2[0] = regs[i];
  reg2[1] = regs[i+1];
  
  return immed;
}

// Parses SW instruction by storing reg1 and reg2 and returning immediate
int parseSW( char regs[], char reg1[], char reg2[] ) {
  int Negative = 0;
  long immed = 0;
  int i = 1;
  
  if ( regs[i] == '0' )
    reg1[0] = '0';
  else {
    reg1[0] = regs[i];
    reg1[1] = regs[i+1];
  }
  
  while ( regs[i] != ',' )
    i++;
  i++;

  if ( regs[i] == '-' ) {
    Negative = 1;
    i++;
  }
  
  while( regs[i] >= 48 && regs[i] <= 57 ) {
    immed = immed*10;
    immed = immed + (int)regs[i] -48;
    i++;
  }

  if ( Negative == 1 ) {
    immed = immed^(256*256-1);
    immed = immed + 1;
  }
  
  while( regs[i] != '$' )
    i++;
  i++;
  
  reg2[0] = regs[i];
  reg2[1] = regs[i+1];

  return immed;
}

// Parses Lui by storing registers and returning immediate
int parseLui( char regs[], char reg1[] ) {
  int Negative = 0;
  long immed = 0;
  int i = 1;
  if ( regs[i] == '1' )
    reg1[0] = '1';
  else {
    reg1[0] = regs[i];
    reg1[1] = regs[i+1];
  }

  while ( regs[i] != ',' )
    i++;
  i++;
  
  if ( regs[i] == '-' ) {
    Negative = 1;
    i++;
  }
  
  while( regs[i] >= 48 && regs[i] <= 57 ) {
    immed = immed*10;
    immed = immed + (int)regs[i] -48;
    i++;
  }

  if ( Negative == 1 ) {
    immed = immed^(256*256-1);
    immed = immed + 1;
  }

  return immed;
}

// Parses ori instruction by storing registers and returning immediate
int parseOri( char regs[], char reg1[], char reg2[] ) {
  int Negative = 0;
  long immed = 0;
  int i = 1;
  reg1[0] = regs[i];
  reg1[1] = regs[i+1];

  while( regs[i] != '$' )
    i++;
  i++;

  if( regs[i] == '0' )
    reg2[0] = '0';
  else if ( regs[i] == '1' )
    reg2[0] = '1';
  else {
    reg2[0] = regs[i];
    reg2[1] = regs[i+1];
  }

  while( regs[i] != ',' )
    i++;
  i++;

  if ( regs[i] == '-' ) {
    Negative = 1;
    i++;
  }
  
  while( regs[i] >= 48 && regs[i] <= 57 ) {
    immed = immed*10;
    immed = immed + (int)regs[i] -48;
    i++;
  }

  if ( Negative == 1 ) {
    immed = immed^(256*256-1);
    immed = immed + 1;
  }

  return immed;
}

// Parses addi instruction by storing registers and returning immediate
int parseAddi( char regs[], char reg1[], char reg2[] ) {
  int Negative = 0;
  long immed = 0;
  int i = 1;
  reg1[0] = regs[i];
  reg1[1] = regs[i+1];

  while( regs[i] != '$' )
    i++;
  i++;
  
  if( regs[i] == '0' )
    reg2[0] = '0';
  else {
    reg2[0] = regs[i];
    reg2[1] = regs[i+1];
  }
  
  while( regs[i] != ',' )
    i++;
  i++;
  
  if ( regs[i] == '-' ) {
    Negative = 1;
    i++;
  }
  
  while( regs[i] >= 48 && regs[i] <= 57 ) {
    immed = immed*10;
    immed = immed + (int)regs[i] -48;
    i++;
  }

  if ( Negative == 1 ) {
    immed = immed^(256*256-1);
    immed = immed + 1;
  }

  return immed;
}

// Parses add by storing registers
void parseAdd( char regs[], char reg1[], char reg2[], char reg3[]) {
  // Set reg1 to first register stated
  int i = 1;
  if ( regs[i] == '0' )
    reg1[0] = '0';
  else { 
    reg1[0] = regs[i];
    reg1[1] = regs[i+1];
  }
  
  // Sets i to the position of next reg in regs[]
  while ( regs[i] != '$' )
    i++;
  i++;
  if ( regs[i] == '0' )
    reg2[0] = '0';
  else {
    reg2[0] = regs[i];
    reg2[1] = regs[i+1];
  }
  
  // moves i to the next register position
  while ( regs[i] != '$' )
    i++;
  i++;
  if ( regs[i] == '0' )
    reg3[0] = '0';
  else {
    reg3[0] = regs[i];
    reg3[1] = regs[i+1];
  }

}

// Parses nor by storing registers
void parseNor(char regs[], char reg1[], char reg2[], char reg3[] ) {
  // Set reg1 to first register stated
  int i = 1;
  if ( regs[i] == '0' )
    reg1[0] = '0';
  else { 
    reg1[0] = regs[i];
    reg1[1] = regs[i+1];
  }
  
  // Sets i to the position of next reg in regs[]
  while ( regs[i] != '$' )
    i++;
  i++;
  if ( regs[i] == '0' )
    reg2[0] = '0';
  else {
    reg2[0] = regs[i];
    reg2[1] = regs[i+1];
  }
  
  // moves i to the next register position
  while ( regs[i] != '$' )
    i++;
  i++;
  if ( regs[i] == '0' )
    reg3[0] = '0';
  else {
    reg3[0] = regs[i];
    reg3[1] = regs[i+1];
  }
  
}

// Parses Sll by storing registers and returning the value of the shift
int parseSll(char regs[], char reg1[], char reg3[]) {  
  // Set reg1 to first register stated
  int shift = 0;
  int i = 1;
  if ( regs[i] == '0' )
    reg1[0] = '0';
  else { 
    reg1[0] = regs[i];
    reg1[1] = regs[i+1];
  }
  
  // Sets i to the position of next reg in regs[]
  while ( regs[i] != '$' )
    i++;
  i++;
  if ( regs[i] == '0' )
    reg3[0] = '0';
  else {
    reg3[0] = regs[i];
    reg3[1] = regs[i+1];
  }
  
  while( regs[i] != ',' )
    i++;
  i++;

  while( regs[i] >= 48 && regs[i] <= 57 ) {
    shift = shift*10;
    shift = shift + (int)regs[i] -48;
    i++;
  }

  return shift;
}

// Converts the register to a number in decimal
int regToDecimal(char reg[]) {
  int regType = 0;
 
  if ( reg[0] == '0' )
    return 0;
  else if ( reg[0] == '1' )
    return 1;
  else if ( reg[0] == 't' )
    regType = 8;
  else 
    regType = 16;

  if ( reg[1] == '0' )
    return regType + 0;
  else if ( reg[1] == '1' )
    return regType + 1;
  else if ( reg[1] == '2' )
    return regType + 2;
  else if ( reg[1] == '3' )
    return regType + 3;
  else if ( reg[1] == '4' )
    return regType + 4;
  else if ( reg[1] == '5' )
    return regType + 5;
  else if ( reg[1] == '6' )
    return regType + 6;
  else
    return regType + 7;

}
