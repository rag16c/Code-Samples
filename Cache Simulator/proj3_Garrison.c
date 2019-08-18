#include <stdio.h>
#include <stdlib.h>

int getBinary(int num, int start, int end);

int main() {
  int reference[100];
  char character[100];
  int *cacheV;
  int *cacheT;
  int *cacheD;
  int *cacheLRU;
  int numRef = 0;
  int numBlocks;
  int blockSize;
  int numSet;
  int setAssoc;
  int numIndex;
  int numOffset;
  int numTag;
  int hits = 0;
  int misses = 0;
  int memRef = 0;
  int tag = 0;
  int index = 0;
  int currentBlock = 0;
  char request = ' ';
  int i = 0;
  int j = 0;
  int tempInt = 0;

  scanf("%i\n",&blockSize);
  i = 0;
  tempInt = blockSize;
  while (tempInt != 1 ) {
    tempInt = tempInt/2;
    i++;
  }
  numOffset = i;
  
  scanf("%i\n",&numSet);
  i = 0;
  tempInt = numSet;
  while (tempInt != 1) {
    tempInt = tempInt/2;
    i++;
  }
  numIndex = i;
  numTag = 32-numIndex-numOffset;

  scanf("%i\n",&setAssoc);
  
  numBlocks = numSet*setAssoc;

  while ( !feof(stdin) ) {
    scanf("%c\t%i\n",&character[numRef],&reference[numRef]);
    numRef++;
  }

  cacheT = (int *) malloc (numBlocks * sizeof(int));
  cacheD = (int *) malloc (numBlocks * sizeof(int));
  cacheV = (int *) malloc (numBlocks * sizeof(int));
  cacheLRU = (int *) malloc (numBlocks * sizeof(int));

  i = 0;
  while ( i < numBlocks ) {
    cacheT[i] = -1;
    cacheD[i] = 0;
    cacheV[i] = 0;
    cacheLRU[i] = 0;
    i++;
  }

  //Write Through, no Write Allocate
  i = 0;
  while ( i < numRef ) {
    tag = getBinary(reference[i],32,(32-numTag));
    index = getBinary(reference[i],(numOffset+numIndex),numOffset);
    request = character[i];
  
    currentBlock = index*setAssoc;
    if ( request == 'R' ) {
      j = 0;
      while ( j < setAssoc ) {
	if ( cacheT[currentBlock] == tag ) { // Read hit
	  if ( cacheLRU[currentBlock] != setAssoc ) {
	    cacheLRU[currentBlock] = setAssoc;
	    j = index*setAssoc;
	    while ( j < (index+1)*(setAssoc) ) {
	      if ( j != currentBlock && cacheLRU[j] != 0 )
		cacheLRU[j]--;
	      j++;
	    } 
	  }
	  hits++;
	  break;
	}
	else
	  currentBlock++;
	
	j++;
      }
      // Read Miss
      if ( currentBlock == (index+1)*setAssoc ) {
	misses++;
	j = 0;
	currentBlock = index*setAssoc;
	while ( j < setAssoc ) {  // Read room 
	  if ( cacheT[currentBlock] == -1 ) {
	    cacheT[currentBlock] = tag;
	    cacheD[currentBlock] = reference[i];
	    memRef++;
	    if ( cacheLRU[currentBlock] != setAssoc ) {
		cacheLRU[currentBlock] = setAssoc;
		j = index*setAssoc;
		while ( j < (index+1)*(setAssoc)) {
		  if ( j != currentBlock && cacheLRU[j] != 0 )
		    cacheLRU[j]--;
		  j++;
		}
	      }
	    break;
	  }
	  else 
	    currentBlock++;
	  
	  j++;
	}
	if ( currentBlock == (index+1)*(setAssoc) )  {
	  // Read no room 
	  j = 0;
	  currentBlock = index*setAssoc;
	  while ( j < setAssoc ) {
	    if ( cacheLRU[currentBlock] == 1 ) {
	      cacheT[currentBlock] = tag;
	      cacheD[currentBlock] = reference[i];
	      memRef++;
	      if ( cacheLRU[currentBlock] != setAssoc ) { 
		cacheLRU[currentBlock] = setAssoc;
		j = index*setAssoc;
		while ( j < (index+1)*(setAssoc) ) {
		  if ( j != currentBlock )
		    cacheLRU[j]--;
		  j++;
		}
	      }
	      break;
	    }
	    else 
	      currentBlock++;
	    
	    j++;
	  }
	}
      }	
    }
    else if ( request == 'W' ) {
      j = 0;
      currentBlock = index*setAssoc;
      while ( j < setAssoc ) { // write Hit
	if ( cacheT[currentBlock] == tag ) {
	  hits++;
	  memRef++;
	  if ( cacheLRU[currentBlock] != setAssoc ) {
	    cacheLRU[currentBlock] = setAssoc;
	    j = index*setAssoc;
	    while ( j < (index+1)*(setAssoc)) {
	      if ( j != currentBlock && cacheLRU[j] != 0 )
		cacheLRU[j]--;
	      j++;
	    }
	  }
	  break;
	}
	else
	  currentBlock++;
	
	j++;
      }
      if ( currentBlock == (index+1)*(setAssoc) ) { // Write miss
	misses++;
	memRef++;
      }
    }
    
    i++;
  }

  printf("Block size: %i",blockSize);
  printf("\nNumber of sets: %i",numSet);
  printf("\nAssociativity: %i",setAssoc);
  printf("\nNumber of offset bits: %i ",numOffset);
  printf("\nNumber of index bits: %i",numIndex);
  printf("\nNumber of tag bits: %i",numTag);
  printf("\n****************************************");
  printf("\nWrite-through with No Write Allocate\n");
  printf("****************************************\n");
  printf("Total number of references: %i\n",numRef);
  printf("Hits: %i\n",hits);
  printf("Misses: %i\n",misses);
  printf("Memory References: %i\n",memRef);

  i = 0;

  while ( i < numBlocks ) {
    cacheT[i] =	-1;
    cacheD[i] = 0;
    cacheV[i] = 0;
    cacheLRU[i] = 0;
    i++;
  }

  i = 0;
  hits = 0;
  misses = 0;
  memRef = 0;

  while ( i < numRef ) {
    tag = getBinary(reference[i],32,(32-numTag));
    index = getBinary(reference[i],(numOffset+numIndex),numOffset);
    request = character[i];
    
    currentBlock = index*setAssoc;

    if ( request == 'R' ) {
      j = 0;
      while ( j < setAssoc ) {
	if ( cacheT[currentBlock] == tag ) {
	  hits++;
	  if ( cacheLRU[currentBlock] != setAssoc ) {
	    cacheLRU[currentBlock] = setAssoc;
	    j = index*setAssoc;
	    while ( j < (index+1)*setAssoc ) {
	      if ( j != currentBlock && cacheLRU[j] != 0 )
		cacheLRU[j]--;
	      j++;
	    }
	  }
	  break;
	}
	else 
	  currentBlock++;
	
	j++;
      }
      
      if ( currentBlock == (index+1)*setAssoc ) { // read miss
	misses++; 
	j = 0;
	currentBlock = index*setAssoc;
	while ( j < setAssoc ) { // read room
	  if ( cacheT[currentBlock] == -1 ) {
	    cacheT[currentBlock] = tag;
	    cacheD[currentBlock] = reference[i];
	    memRef++;
	    if ( cacheLRU[currentBlock] != setAssoc ) {
	      cacheLRU[currentBlock] = setAssoc;
	      j = index*setAssoc;
	      while ( j < (index+1)*setAssoc ) {
		if ( j != currentBlock && cacheLRU[j] != 0 ) 
		  cacheLRU[j]--;
		j++;
	      }
	    }
	    break;
	  }
	  else
	    currentBlock++;
	  
	  j++;
	}

	if ( currentBlock == (index+1)*setAssoc ) {
	  j = 0;
	  currentBlock = index*setAssoc;
	  while ( j < setAssoc ) {
	    if ( cacheLRU[currentBlock] == 1 ) {
	      if ( cacheV[currentBlock] == 1 ) {
		memRef++;
		cacheT[currentBlock] = tag;
		cacheV[currentBlock] = 0;
		memRef++;
		if ( cacheLRU[currentBlock] != setAssoc ) {
		  cacheLRU[currentBlock] = setAssoc;
		  j = index*setAssoc;
		  while ( j < (index+1)*setAssoc ) {
		    if ( j != currentBlock && cacheLRU[j] != 0 )
		      cacheLRU[j]--;
		    j++;
		  }
		}
	      }
	      else {
		cacheT[currentBlock] = tag;
		memRef++;
		if ( cacheLRU[currentBlock] != setAssoc ) {
		  cacheLRU[currentBlock] = setAssoc;
		  j = index*setAssoc;
		  while ( j < (index+1)*setAssoc ) {
		    if ( j != currentBlock && cacheLRU[j] != 0 )
		      cacheLRU[j]--;
		    j++;
		  }
		}
	      }
	      break;
	    }
	    else
	      currentBlock++;
	    
	    j++;
	  }
	}
      }
    }
    else if ( request == 'W' ) {
      j = 0;
      currentBlock = index*setAssoc;
      while ( j < setAssoc ) {
	if ( cacheT[currentBlock] == tag ) {
	  hits++;
	  cacheT[currentBlock] = tag;
	  cacheV[currentBlock] = 1;
	  if ( cacheLRU[currentBlock] != setAssoc ) {
	    cacheLRU[currentBlock] = setAssoc;
	    j = index*setAssoc;
	    while ( j < (index+1)*setAssoc ) {
	      if ( j != currentBlock && cacheLRU[j] != 0 )
		cacheLRU[j]--;
	      j++;
	    }
	  }
	  break;
	}
	else
	  currentBlock++;
	
	j++;
      }
      
      if ( currentBlock == (index+1)*setAssoc ) {
	misses++;
	j = 0;
	currentBlock = index*setAssoc;
	while ( j < setAssoc ) {
	  if ( cacheT[currentBlock] == -1 ) {
	    cacheT[currentBlock] = tag;
	    cacheV[currentBlock] = 1;
	    memRef++;
	    if ( cacheLRU[currentBlock] != setAssoc ) {
	      cacheLRU[currentBlock] = setAssoc; 
	      j = index*setAssoc;
	      while ( j < (index+1)*setAssoc ) {
		if ( j != currentBlock && cacheLRU[j] != 0 )
		  cacheLRU[j]--;
		j++;
	      }
	    }
	    break;
	  }
	  else
	    currentBlock++;

	  j++;
	}
	
	if ( currentBlock == (index+1)*setAssoc ) {
	  j = 0;
	  currentBlock = index*setAssoc;
	  while ( j < setAssoc ) {
	    if ( cacheLRU[currentBlock] == 1 ) {
	      if ( cacheV[currentBlock] == 1 ) { // prev write
		memRef++;
		cacheT[currentBlock] = tag;
		cacheV[currentBlock] = 1;
	     	memRef++;
		if ( cacheLRU[currentBlock] != setAssoc ) {
		  cacheLRU[currentBlock] = setAssoc;
		  j = index*setAssoc;
		  while ( j < (index+1)*setAssoc ) {
		    if ( j != currentBlock )
		      cacheLRU[j]--;
		    j++;
		  }
		}
	      }
	      else {
		cacheT[currentBlock] = tag;
		cacheV[currentBlock] = 1;
		memRef++;
		if ( cacheLRU[currentBlock] != setAssoc ) {
		  cacheLRU[currentBlock] = setAssoc;
		  j = index*setAssoc;
		  while ( j < (index+1)*setAssoc ) {
		    if ( j != currentBlock )
		      cacheLRU[j]--;
		    j++;
		  }
		}
	      }
	      break;
	    }
	    else
	      currentBlock++;
	    
	    j++;
	  }
	}
	
      }
    }
  
    i++;
  }

  printf("****************************************\n");
  printf("Write-back with Write Allocate\n");
  printf("****************************************\n");
  printf("Total number of references: %i\n",numRef);
  printf("Hits: %i\n",hits);
  printf("Misses: %i\n",misses);
  printf("Memory References: %i\n",memRef);

  
  free(cacheT);
  free(cacheD);
  free(cacheV);
  free(cacheLRU);

  return 0;
}

int getBinary(int num,int start,int end) {
  int result = 0;

  int i = 0;
  int j = start-1;
  int temp = start-1;
  int k = num;

  while ( temp >= end ) {
    while ( j > 0 ) {
      k = (int)k/2;
      j--;
    }
    if ( k%2 == 0 && num > 0  ) {
      result = result << 1;
    }
    else if ( k%2 == 1 && num > 0 ) {
      result = result << 1;
      result++;
    }
    else if ( k%2 == 0 && num < 0 ) {
      result = result << 1;
      result++;
    }
    else {
      result = result << 1;
    }
    temp--;
    j = temp;
    k = num;
  }

  return result;
}
