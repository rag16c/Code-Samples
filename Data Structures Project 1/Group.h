/*
  Name: Riley Garrison
  Date: 9/19/2018
  Project 1: Collect and report specified data from the BGP routing table
*/

#include <vector>
#include <string>

class Group
{
 public:
  Group();
  void ReadNodes();
  void AddNode(int Node);  // Adds First Element to Vector
  int CheckNode(int Node); // Checks to see if Node is Entered
  int NodeComparison( const int& N1, const int& N2); // Compares two nodes/ neighbors
  void AddNeighbor();      // Adds Neighbor to Node
  bool CheckNeighbor();    // Checks to see if Neighbor is Entered 
  void Print();            // Prints the vectors 
  void Resize();           // Resizes the outer vector to hold more vectors
  bool StringCheck(std::string NumStr);  // Checks if the last AS has been read on a line
  void NodeSort();                       // Sorts the vectors by number of neighbors
  void NeighborSort(int Place);          // Sorts the neighbors in numerical order
  
 private:
  std::vector<std::vector<int> > List;   // Holds the AS and respective neighbors
  int PreviousNode;                      // Keeps the previous node 
  int PreviousPlace;                     // Keeps the place of the previous node in the vector
  int CurrentNode;                       // Keeps the current Node here
  int CurrentPlace;                      // Keeps track of the place of the current node in the vector
  int NodesUsed;                         // Keeps track of the used size of the vector
};
