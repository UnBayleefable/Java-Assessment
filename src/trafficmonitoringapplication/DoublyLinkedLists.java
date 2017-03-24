/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmonitoringapplication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author student
 */
/**
 * **************************************************************************
 */
/*                                                                           */
/*                    Doubly-Linked List Manipulation                        */
/*                                                                           */
/*                     January 1998, Toshimi Minoura                         */
/*                                                                           */
/**
 * **************************************************************************
 */
// Filename: Doubly-LinkedList_ToshimiMinoura
// Source:   TBA
//package WordAssoc;
// A LinkedListNode is a node in a doubly-linked list.
class LinkedListNode
{              // class for nodes in a doubly-linked list

    LinkedListNode prev;              // previous LinkedListNode in a doubly-linked list
    LinkedListNode next;              // next LinkedListNode in a doubly-linked list
    TrafficData classInstance;
    //public char data;       // data stored in this LinkedListNode

    LinkedListNode()
    {                // constructor for head LinkedListNode 
        prev = this;           // of an empty doubly-linked list
        next = this;
        classInstance = new TrafficData();
//    classInstance.Word1 = "Yellow";
//    classInstance.Word2 = "No";
        // data = 'H';           // not used except for printing data in list head
    }

    LinkedListNode(TrafficData data) throws SQLException
    {       // constructor for a LinkedListNode with data
        prev = null;
        next = null;
        classInstance = data;
        //this.data = data;     // set argument data to instance variable data
    }

    public void append(LinkedListNode newLinkedListNode)
    {  // attach newLinkedListNode after this LinkedListNode
        
        newLinkedListNode.prev = this;
        newLinkedListNode.next = next;
        if (next != null)
        {
            next.prev = newLinkedListNode;
        }
        next = newLinkedListNode;
        System.out.println("LinkedListNode with data " + newLinkedListNode.classInstance.Time + "_" + classInstance.LocationID
                + " appended after LinkedListNode with data " + classInstance.Time + "_" + classInstance.LocationID);
    }

    public void insert(LinkedListNode newLinkedListNode)
    {  // attach newLinkedListNode before this LinkedListNode
        newLinkedListNode.prev = prev;
        newLinkedListNode.next = this;
        prev.next = newLinkedListNode;;
        prev = newLinkedListNode;
        System.out.println("LinkedListNode with data " + newLinkedListNode.classInstance.Time + "_" + classInstance.LocationID
                + " inserted before LinkedListNode with data " + classInstance.Time + "_" + classInstance.LocationID);
    }

    public void remove()
    {              // remove this LinkedListNode
        next.prev = prev;                 // bypass this LinkedListNode
        prev.next = next;
        System.out.println("LinkedListNode with data " + classInstance.Time + "_" + classInstance.LocationID + " removed");
    }

    public String toString()
    {
        return this.classInstance.Time + " - " + this.classInstance.LocationID;
    }
}

class DList
{

    LinkedListNode head;

    public DList(TrafficData data) throws SQLException
    {
        head = new LinkedListNode(data);
    }

    public LinkedListNode find(String wrd1) // change this to search for a location and then within that the time of day and return the amount of traffic
    {          // find LinkedListNode containing x
        for (LinkedListNode current = head.next; current != head; current = current.next)
        {
            if (current.classInstance.Time.compareToIgnoreCase(wrd1) == 0)
            {        // is x contained in current LinkedListNode?
                System.out.println("Data " + wrd1 + " found");
                return current;               // return LinkedListNode containing x
            }
        }
        System.out.println("Data " + wrd1 + " not found");
        return null;
    }

    //This Get method Added by Matt C
    public LinkedListNode get(int i)
    {
        LinkedListNode current = this.head;
        if (i < 0 || current == null)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        while (i > 0)
        {
            i--;
            current = current.next;
            if (current == null)
            {
                throw new ArrayIndexOutOfBoundsException();
            }
        }
        return current;
    }

    @Override
    public String toString()
    {
        String str = "";
        if (head.next == head)
        {             // list is empty, only header LinkedListNode
            return "List Empty";
        }
        str = "list content = ";
        for (LinkedListNode current = head.next; current != head && current != null; current = current.next)
        {
            str = str + current.classInstance.Time;
        }
        return str;
    }

    public void print()
    {                  // print content of list
        if (head.next == head)
        {             // list is empty, only header LinkedListNode
            System.out.println("list empty");
            return;
        }
        System.out.print("list content = ");
        for (LinkedListNode current = head.next; current != head; current = current.next)
        {
            System.out.print(" " + current.classInstance.Time);
        }
        System.out.println("");
    }

//  public static void main(String[] args) {
//    DList dList = new DList();              // create an empty dList
//    dList.print();
//
//    dList.head.append(new LinkedListNode("1","2"));       // add LinkedListNode with data '1'
//    dList.print();
//    dList.head.append(new LinkedListNode("3", "4"));       // add LinkedListNode with data '2'
//    dList.print();
//    dList.head.append(new LinkedListNode("5","6"));       // add LinkedListNode with data '3'
//    dList.print();
//    dList.head.insert(new LinkedListNode("A","B"));       // add LinkedListNode with data 'A'
//    dList.print();
//    dList.head.insert(new LinkedListNode("C","D"));       // add LinkedListNode with data 'B'
//    dList.print();
//    dList.head.insert(new LinkedListNode("E","F"));       // add LinkedListNode with data 'C'
//    dList.print();
//
//    LinkedListNode nodeA = dList.find("A");           // find LinkedListNode containing 'A'
//    nodeA.remove();                         // remove that LinkedListNode
//    dList.print();
//
//    LinkedListNode node2 = dList.find("3");           // find LinkedListNode containing '2'
//    node2.remove();                           // remove that LinkedListNode
//    dList.print();
//
//    LinkedListNode nodeB = dList.find("5");            // find LinkedListNode containing 'B'
//    nodeB.append(new LinkedListNode("Linked","List"));   // add LinkedListNode with data X
//    dList.print();
//  }
}
