// Riley Garrison
// Date: 10/25/2018

#ifndef QUEUE_H
#define QUEUE_H

#include <iostream>
#include <list>

namespace cop4530 {
  
template <typename T, class Container = std::list<T> >
class Queue {
 protected:
 Container c; // Container class used
 
 public:
 Queue(); // Zero parameter constructor
 ~Queue(); // Destructor
 Queue(const Queue &rhs); // Copy Constructor
 Queue(Queue &&rhs); // Move constructor
 Queue& operator=(const Queue &rhs); // Copy assignment operator
 Queue& operator=(Queue &&rhs); // Move assignment

 T& back(); // Reference to last element in queue
 const T& back() const; // same as above

 bool empty() const;  // Checks if the queue is empty
 T& front(); // Returns reference to the first element in the queue
 const T& front() const; // same as previous method
 void pop(); // Remove the first element from queue
 void push(const T& val); // Add new element to end of queue
 void push(T&& val); // Add new element to end of queue
 int size(); // returns the number of elements in the queue
 void clear(); // Clears the queue
};
 
#include "Queue.hpp"
 
}
#endif
