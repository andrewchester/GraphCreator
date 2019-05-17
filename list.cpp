#include <string.h>
#include "list.h"

LinkedList::LinkedList(){ //Constructor, initialized a new empty list
  this->length = 0;
  this->first = 0;
  this->last = 0;
}

LinkedList::Node* LinkedList::nodeAt(int index){
	if(index > length){ //If they're  trying to access a value  that doesnt exist
		std::cout << "Out of bounds error during at()" << std::endl;
		return 0; 
	}

  if(index < 0 || index == length) //This is for insert(), using at will return 0s if you're inserting at the beginning or end of list
    return 0;

  Node* current = this->first; //Start at the first node
  for(int i = 0; i < index; i++) //Loop until the index
    current = current->next; //Setting current to the next node each time
  return current; //Return current
}

/// Printing out values from the list ///
std::ostream& operator<<(std::ostream& os, const LinkedList& list){
  LinkedList::Node* current = list.first; //Start at first
  for(int i = 0; i < list.length; i++){ //Looping over the whole list
    if(current == 0) //If current is ever 0, just skip the iteration
      continue;
  
    if(i == 0) //If it's the beginning of the list, print out the value
      os << current->data;
    else //Otherwise, insert a space
      os << " " << current->data;
    
    current = current->next; //Set current to the next node in the array
  }
  return os; //Return the output stream
}

/// Accessing Values ///
auto LinkedList::at(int index){
  if(index > length){ //If they're  trying to access a value  that doesnt exist
    std::cout << "Out of bounds error during at()" << std::endl;
    return 0; 
  }

  if(index < 0 || index == length) //This is for insert(), using at will return 0s if you're inserting at the beginning or end of list
    return 0;

  Node* current = this->first; //Start at the first node
  for(int i = 0; i < index; i++) //Loop until the index
    current = current->next; //Setting current to the next node each time
  return current->data; //Return current
}

//Overloads the [] operator so list[index] is equivilent to list.at(index)
auto LinkedList::operator[](int index){
  return at(index); //Just uses at()
}

/// Adding/Removing values ///
void LinkedList::insert(int index, auto data){
  Node* f = nodeAt(index - 1); //Sets the first value to the node before the index you're inserting to, at() will return 0 if this is the beginning of the list
  Node* s = nodeAt(index); //Sets the node to where you want to insert, at() will return 0 if this is the end of the list

  Node* newNode = new Node(); //Creates a new node with pointers to first and second
  newNode->data = data;
  newNode->previous = f;
  newNode->next = s;

  if(f != 0) //Make the first node point to the new node instead of the second node
    f->next = newNode;
  else
    this->first = newNode; //If it's the beginning of the list, make the newNode first in the list(the previous pointer in newnode is 0)

  if(s != 0) //Make it so the second node's previous points to newNode
    s->previous = newNode;
  else
    this->last = newNode; //If it's the end of the list, set newNode to last

  length++; //Increase the length variable of the list
}

void LinkedList::remove(int index){ //Removes a value from a certain index
  Node* node = nodeAt(index); //Get the node at the index
  Node* next = node->next; //Get the previous node
  Node* previous = node->previous; //Get the next node

  if(previous != 0)
    previous->next = next; //If previous isn't 0, then set it's next to next
  else
    this->first = next; //If there isn't a previous, then we're at the beginning and the next node is going to be the first node
  
  if(next != 0)
    next->previous = previous; //If next isn't 0, then set it's previous to previous
  else
    this->last = previous; //If there isn't a next, then we're at the end, and the previous node is going to be last

  delete node; //Remove the node since now it's not accessible
  length--; //Reduce the length of array
}

void LinkedList::clear(){ //Clears the list using remove()
  while(length > 0) //Keeps deleting the first element while there are still elements
    remove(0);
}

void LinkedList::deleteData(auto data){ //Delete's all students in the list matching the pointer
  for(int i = 0; i < length; i++) //Loop over list
    if(at(i) == data) //If we found a matching node
      remove(i); //Remove them
}
//Append just calls insert() at the end of the list
void LinkedList::append(int num){
  this->insert(length, num);
}

/// Other ///
int LinkedList::size(){ //Returns length
  return this->length;
}

LinkedList::Node* LinkedList::begin(){ //Return first node in list
  return this->first;
}
LinkedList::Node* LinkedList::end(){ //Return last node in list
  return this->last;
}