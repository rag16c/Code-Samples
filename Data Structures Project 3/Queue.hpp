// Name: Riley Garrison
// Project 3

#ifndef QUEUE_HPP
#define QUEUE_HPP

#include <iostream>

template <typename T, class Container> // Empty constructor
Queue<T,Container>::Queue()
{}

template <typename T, class Container> // Destructor
Queue<T,Container>::~Queue()
{
  c.clear();
}

template<typename T, class Container>  // Copy Constructor
Queue<T,Container>::Queue(const Queue<T,Container> &rhs)
{
  c = rhs.c;
}

template<typename T, class Container>  // Move Constructor
Queue<T,Container>::Queue(Queue<T,Container> &&rhs)
{
  c = rhs.c;
  rhs.c.clear();
}

template <typename T, class Container>  // Copy Assignment 
Queue<T,Container>& Queue<T,Container>::operator=(const Queue<T,Container> &rhs)
{
  Queue<T,Container> temp(rhs);
  std::swap(*this, temp);
  return *this;
}

template <typename T, class Container>  // Move Assignment
Queue<T,Container>& Queue<T,Container>::operator=(Queue<T,Container> &&rhs)
{
  if ( this != &rhs )
    {
      c.clear();
      c = rhs.c;
      rhs.c.clear();
    }
  
  return *this;
}

template <typename T, class Container> // Reference to last element of Queue
T& Queue<T,Container>::back()
{
  return c.back();
}

template <typename T, class Container>  // Const reference to last element of Queue
const T& Queue<T,Container>::back() const
{
  return c.back();
}

template <typename T, class Container>  // Checks if the queue is empty
bool Queue<T,Container>::empty() const
{
  return c.empty();
}

template <typename T, class Container>  // Reference to first element of Queue
T& Queue<T,Container>::front()
{
  return c.front();
}

template <typename T, class Container>  // Reference to first element of queue
const T& Queue<T,Container>::front() const
{
  return c.front();
}

template <typename T, class Container>  // Removes first element of queue
void Queue<T,Container>::pop()
{
  if ( c.empty() )
    return; 

  c.pop_front();
}

template <typename T, class Container>  // Adds new element to end of queue
void Queue<T,Container>::push(const T& val)
{
  c.push_back(val);
}

template <typename T, class Container>  // Adds new element to end of queue
void Queue<T,Container>::push(T&& val)
{
  T temp = std::move(val);
  c.push_back(temp);
}

template <typename T, class Container>  // Returns size of queue
int Queue<T,Container>::size()
{
  return c.size();
}

template <typename T, class Container>  // Clears all data in the queue
void Queue<T,Container>::clear()
{
  c.clear();
}

#endif
