// Riley Garrison
// Date: 10/25/2018

#include "Queue.h"
#include <iostream>
#include <vector>
#include <fstream>
#include <string>

using namespace std;
using namespace cop4530;

int main (int argc, char *argv[] )
{
  ifstream in1;
  int TotalCities;
  int CityNum;
  int TempInt;
  char TempString[100];
  string Source;
  string Dest;
  int Start = -1;
  int End = -1;
  int TotalDist = 0;
  char Cont = 'Y';
  Queue<int> q1;

  if ( argc != 1 )  // If there is not exactly 1 parameter
    cout << "Usage: " << argv[0] << " airline_file" << endl;

  in1.open(argv[1]);  // Open file named by argv[1]

  if (!in1)  // If the fil can't b opened
  {
    cout << "Usage: " << argv[0] << " airline_file" << endl;
    return 0;
  }

  in1 >> TotalCities;  // Reads in total number of cities
  cout << TotalCities << " cities:" << endl;

  vector<vector<int> > chart;  // Creates vector of vectors that will store the table from the input file
  chart.resize(TotalCities);
  for ( int i = 0; i < TotalCities; i++ )
    chart[i].resize(TotalCities);

  string CityNames[TotalCities];  // Keeps track of names of the cities

  int pred[TotalCities];   // Keeps track of what city preceeds another city
  for ( int i = 0; i < TotalCities; i++ )
    pred[i] = -1;

  in1.ignore();  // Ignores new line

  for ( int i = 0; i < TotalCities; i++ )  // Reads in the city names to CityNames
    {
      in1.getline(TempString,256);
      CityNames[i] = TempString;
      cout << '\t' << CityNames[i] << endl;
    }
  
  for ( int i = 0; i < TotalCities; i++ )  // Reads in the chart
    {
      for ( int j = 0; j < TotalCities; j++ )
	in1 >> chart[i][j];

      if ( in1.peek() == '\n' )
	in1.ignore();

    }
  
  in1.close();  // Closes input file


  cout << endl;

  cout << "direct flights between cities" << endl;
  cout << "-------------------------" << endl;

  for ( int i = 0; i < TotalCities; i++ )  // Prints a city and what cities are directly connected to it with distance
    {
      cout << CityNames[i] << ':' << endl;  // (1) Print city name
      
      for ( int j = 0; j < TotalCities; j++ )  // (2) Prints cities that connect to it with distance
	{
	  if ( chart[i][j] > 0 )
	    {
	      cout << '\t' << CityNames[j] << ", $" << chart[i][j] << endl;
	    }
	}
    }

    cout << "-------------------------" << endl;
    while ( Cont == 'Y' )  {  // Loop continues until user wants to stop
	Cont = 'E';
	
    cout << "Source city : ";  // Requests source and destination city from user
    cin.getline(TempString,256);
    Source = TempString;
    cout << "Destination city : ";
    cin.getline(TempString,256);
    Dest = TempString;
      
    cout << "finding min_hop route...." << endl;
    cout << '\t';
    
    if ( Source.compare(Dest) == 0 )  // If the source is the destination
      {
	cout << Source << ", $0" << endl;
      }
    else {
      for ( int i = 0; i < TotalCities; i++ ) // Finds what number city is the start and which is the end
	{
	  if ( Source.compare(CityNames[i]) == 0 )
	    Start = i;
	  if ( Dest.compare(CityNames[i]) == 0  )
	    End = i;
	  
	  if ( Start != -1 && End != -1 )  // If one city doesn't exist break
	    break;
	}
      
      // If a city doesn't exist print which city does not exist
      if ( Start == -1 ) 
	cout << "path not found, source city, " << Source << ", not on the map" << endl;
      else if ( End == -1 )
	cout << "path not found, destination city, " << Dest << ", not on the map" << endl;
      else {

	q1.push(Start); // Add the starting city to the queue
	
	while ( TempInt != -245 )
	  {
	    CityNum = q1.front(); // Store front city into citynum and pop
	    q1.pop();

	    for ( int i = 0; i < TotalCities; i++ ) // If a city connects to CityNum and hasnt been traversed before add the number to queue
	      {
		if ( chart[CityNum][i] > 0 && pred[i] == -1 && i != Start )
		  {
		    q1.push(i);
		    pred[i] = CityNum;
		    if ( i == End )  // If you hit the end city, stop the for loop
		      {
			TempInt = -245;
			break;
		      }
		  }
	      }

	    if ( q1.empty() && TempInt != -245 )  // If Temp is never -245 ( never hits the end ) there is no route
	      {
		cout << "There is no route from " << Source << " to " << Dest << endl;
		TempInt = -256;
		break;
	      }
	  }

	if ( TempInt != -256 ) // Runs is connected to end 
	  {
	    vector<string> Order;  // Keeps track of Cities from finish to start
	    TempInt = End;
	    Order.push_back(CityNames[TempInt]);
	    while ( pred[TempInt] != -1 )
	      {
		TotalDist += chart[TempInt][pred[TempInt]];
		TempInt = pred[TempInt];
		Order.push_back(CityNames[TempInt]);
	      }
	    
	    for ( int i = Order.size() - 1; i >= 0; i-- ) // Print the order backwards
	      {
		cout << Order[i];
		
		if ( i != 0 )
		  cout << " -> ";
	      }

	    cout <<", $" << TotalDist;
	  }
      }
    }
    while ( Cont != 'Y' && Cont != 'N' ) // Checks if you want to keep searching 
      {
	cout << "\nSearch another route? (Y/N)";
	cin >> Cont;
      }
    // Resets variables so another search wil run properly
    TempInt = -1;
    Start = -1;
    End = -1;
    q1.clear();
    TotalDist = 0;
    for ( int i = 0; i < TotalCities; i++ )
      pred[i] = -1;
    cin.getline(TempString,1);
  }

  return 0;
}

