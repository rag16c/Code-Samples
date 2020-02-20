/*
  Name: Riley Garrison
  Date 9/19/2018
  Project 1: Collect and report specified data from the BGP routing table
  The goal of the project is to determine the set of neighboring autonomous systems(AS) for each AS
  The BGP table is redirected to input
*/

#include "Group.h"
#include <iostream>
#include <vector>
#include <string>
#include <sstream>

using namespace std;

Group::Group()  // Initializes variables
{
  PreviousNode = 0;
  PreviousPlace = -1;
  NodesUsed = 0;
  CurrentNode = 0;
  CurrentPlace = -1;
}

void Group::ReadNodes()  // Reads and processes the Nodes, finishes with sorting by Neighbors
{
  string Read = "";

  while( cin.eof() == false )    // Until the end of the file, this loop will continue
    {
      for( int i = 0; i < 6; i++ )  // Read the line up until the AS Nodes
	getline (cin, Read, '|');

      Read.clear();

      while ( cin.eof() == false ) // Loops until it reaches the last node, then broken
      {
	cin >> Read;

	stringstream(Read) >> CurrentNode;

	if (Read[0] == '[') // Checks to see if it read in ASPaths, if so break
	  break;
       	
        AddNode(CurrentNode); // Adds the node to the nested vector if needed
        AddNeighbor();        // Adds the node as a neighbor if possible
       
	PreviousNode = CurrentNode;  // Stores the current node properties into previous
	PreviousPlace = CurrentPlace;
      
	if (StringCheck(Read) )  // Checks to see if the last Node was read for the line
	  break;
      }

      getline(cin,Read);  // Discards the rest of the line
      Read.clear();

      PreviousNode = 0;   // Resets previous node properties
      PreviousPlace = -1;
    }

  NodeSort();  // After all nodes processed, sort by neighbors

  
}

void Group::AddNode(int Node) // Adds Node to the nested vector if possible
{
  if ( NodesUsed == List.size() )  // If no more space in vector, add space
    Resize();
  
  int Check = CheckNode(Node); // Checks to see if Node has been added before

  if ( Check != -1 )  // If it has mark its place and stop
    {
      CurrentPlace = Check;
      return;
    }
  
  List[NodesUsed].push_back(Node);  // If not add the node to the next free vector
  CurrentPlace = NodesUsed; 

  NodesUsed++;                      // Update How many nodes are being used

}

int Group::CheckNode(int Node) // Checks to see if the Node has been added before
{
  if ( List.empty() == true )  // If outer vector empty it hasnt been added
    return -1;

  for ( int i = 0; i < List.size(); i++ )  // Checks to see if it has been added to any vectors
    {
      if ( List[i].empty() == true ) // Once it reaches a nonused vector, the node hasnt been added
	return -1;
      else if ( List[i][0] == Node ) // If the node is found, return its location
	return i;
    }

  return -1;    // Not found return -1
}

void Group::AddNeighbor()  // Adds Previous and Current Nodes as neighbors if possible
{
  if ( PreviousPlace == -1 ) // If no previous node it will not run
    return;

  if ( CheckNeighbor() == false )  // If it doesnt have the specified neighbor, add neighbors
    {
      List[CurrentPlace].push_back(PreviousNode);
      List[PreviousPlace].push_back(CurrentNode);
    }

}

bool Group::CheckNeighbor() // Checks to see if the vector for the current node 
                            // contains the previous node as a neighbor
{
  if ( List[CurrentPlace].empty() == true ) // no neighbors means false
    return false;

  for ( int i = 0; i < List[CurrentPlace].size(); i++ )
  {
    if ( PreviousNode == List[CurrentPlace][i] ) // if it contains the neighbor true
      return true;
  }

  return false;  // doesnt contain the neighbor means false
 

}

void Group::Print() // Prints the Nodes and neigbbors in the specified format
{
  // Format: Node Number_of_Neighbors Neighbors
  for ( int i = 0; i < NodesUsed; i++ )
  {
    NeighborSort(i);
    for ( int  j = 0; j < List[i].size(); j++ )
    {
      if ( j == 0 )
	  cout << List[i][j] << ' ' << List[i].size()-1 << ' ';
      else if ( j != List[i].size()-1 )
	cout << List[i][j] << '|';
      else 
	cout << List[i][j] << endl;
    }
  }
}

void Group::Resize() // Resizes the outer array by adding 5 vectors
{
  vector<int> Temp;

  for ( int i = NodesUsed; i < NodesUsed+5; i++ )
    List.push_back(Temp);
}

bool Group::StringCheck(string NumStr) // Checks to see if the last Node has been read for the line
{
  // The string Read takes in the | for the last Node, so I check if Read has |
  if ( NumStr.find('|') == NumStr.npos )
    return false;
  else 
    return true;
}

int Group::NodeComparison(const int& N1, const int& N2 ) // Compares two nodes to help sorting
{
  if ( N1 > N2 ) 
    return 1;
  else if ( N2 > N1 ) 
    return 0;
  else 
    return -1;
}

void Group::NodeSort() // Sorts by number of neighbors
{
  int High = 0;          // Stores number with highest neighbors
  int HighPlace = 0;     // Stores place with highest neighbors
  int HighNeighbors = 0; // Stores neighbros of the node
  int i = 0;             // Used in for loop
  int Compare = 0;       // Stores comparison of two nodes

  while( i < NodesUsed)  // Runs until the vector is sorted by neighbors
    {
      for ( int j = i; j < NodesUsed; j++ )  // Organizes all neigbors  
	{
	  if ( j == i ) // If this is the first node checked, set as highest
	    {
	      High = List[j][0];
	      HighPlace = j;
	      HighNeighbors = List[j].size();
	    }
	  else  
	    {
	      Compare = NodeComparison(HighNeighbors, List[j].size()); // Check to see which is higher
	      
	      if( Compare == 0 ) // If List[j].size() is higher, it is the new high
		{
		  High = List[j][0];
		  HighPlace = j;
		  HighNeighbors = List[j].size();
		}
	      else if ( Compare == -1 ) // if they have equal neighbors, organize by Node
		{
		  if ( NodeComparison(High, List[j][0]) == 1 ) 
		    {
		      High = List[j][0];
		      HighPlace = j;
		      HighNeighbors = List[j].size();
		    }
		}
	    }
	}
      
      List[i].swap(List[HighPlace]); // Swaps the higher neighbors up in the vector
      i++;
    }
}

void Group::NeighborSort(int Place) // Sorts the neighbors in numerical order
{
  int i = List[Place].size()-1;  // Starts with the last added neighbor
  int High = 0;                  // Stores the highest neighbor
  int HighPlace = 0;             // Stores location of highest neighbor
  int LastHigh = 0;              // Stores the last highest neighbor
  int Temp = 0;                  // Used to swap ints in the vector

  while ( i > 1 )  // Sorts all but the Main Node
    {
      for ( int j = i; j >= 1 ; j-- ) // Goes through all but main Node to find the biggest
	{
	  if ( j == i )  // If this is the first checked, set as highest
	    {
	      High = List[Place][j];
	      HighPlace = j;
	    }
	  else if ( NodeComparison(High, List[Place][j]) == 0 ) // If new neighbor is greater than high
	    {
	      if ( i == List[Place].size()-1 || List[Place][j] < LastHigh ) 
		{
	        // if this is the first loop through or it is less than the last highest  
		  High = List[Place][j];
		  HighPlace = j;
		}
	    }
	}
      
      // Switches the highest to whereever it should be 
      Temp = List[Place][i];
      List[Place][i] = High;
      List[Place][HighPlace] = Temp;
      
      LastHigh = High;
      i--;
    }

}
