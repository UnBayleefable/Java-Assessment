/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmonitoringapplication;

import java.util.ArrayList;

/**
 *
 * @author student
 */
public class NewDoublyLinkedList {
    
    DLLNode head;
    DLLNode tail;
    
    NewDoublyLinkedList()
    {
        head  = null;
        tail = null;
    }
    
    public void InsertAfter(NewDoublyLinkedList list, DLLNode currentNode, DLLNode newNode)
    {
        newNode.next = currentNode.next;
        newNode.prev = currentNode;
        currentNode.next.prev = newNode;
        currentNode.next = newNode;
    }
    public void InsertBefore (NewDoublyLinkedList list, DLLNode currentNode, DLLNode newNode)
    {
        InsertAfter(list, currentNode.prev, newNode);
    }
    public void InsertHeadNode(NewDoublyLinkedList list, DLLNode newNode)
    {                
        if (list.head == null) // if list is empty
        {
            list.head = newNode;
            list.tail = newNode;
            newNode.prev = newNode;
            newNode.next = newNode;            
        }
        else // list is not empty
        {
            InsertBefore(list, list.head, newNode);
            head.prev = newNode; // current head points to new head
            newNode.next = head; // new head points to old head
            head = newNode; // make new head current head
        }
        
    }
    public void InsertTailNode(NewDoublyLinkedList list, DLLNode newNode)
    {
        if(list.tail == null)
        {
            InsertHeadNode(list, newNode);
        }
        else
        {
            InsertAfter(list, list.tail, newNode);
        }
        list.tail = newNode;
    }
    public void DeleteNode(NewDoublyLinkedList list, DLLNode node)
    {
        if(node.prev == null)
        {
            list.head = node.next;
        }
        else
        {
            node.prev.next = node.next;
        }
        if(node.next == null)
        {
            list.tail = node.prev;
        }
        else
        {
            node.next.prev = node.prev;
        }
    }
    
    public ArrayList ReturnListArray(ArrayList<String> arr) // this wont work in a circular list as it will continue to cycle through as the end links to the front
    {       
       DLLNode tempDisplay = head;
       arr.add(tempDisplay.DLLNodeData.Time + "-" + tempDisplay.DLLNodeData.AvVehicleNum + "-" + tempDisplay.DLLNodeData.AvVelocity);
       tempDisplay = tempDisplay.next;
       while (tempDisplay != head)
       {
           arr.add(tempDisplay.DLLNodeData.Time + "-" + tempDisplay.DLLNodeData.AvVehicleNum + "-" + tempDisplay.DLLNodeData.AvVelocity);
           tempDisplay = tempDisplay.next;
       }       
       return arr;
    }
    
        public ArrayList ReturnReverseListArray(ArrayList<String> arr) // this wont work in a circular list as it will continue to cycle through as the end links to the front
    {       
       DLLNode tempDisplay = tail;
       arr.add(tempDisplay.DLLNodeData.Time + "-" + tempDisplay.DLLNodeData.AvVehicleNum + "-" + tempDisplay.DLLNodeData.AvVelocity);
       tempDisplay = tempDisplay.prev;
       while (tempDisplay != tail)
       {
           arr.add(tempDisplay.DLLNodeData.Time + "-" + tempDisplay.DLLNodeData.AvVehicleNum + "-" + tempDisplay.DLLNodeData.AvVelocity);
           tempDisplay = tempDisplay.prev;
       }
       
       return arr;
    }
    
     public void DisplayBackwards()
    {
       System.out.print("Displaying in forward direction [first--->last] : ");
       DLLNode tempDisplay = tail;
       tempDisplay.DisplayNode();
       tempDisplay = tempDisplay.prev;
       while (tempDisplay != tail)
       {
           tempDisplay.DisplayNode();
           tempDisplay = tempDisplay.prev;
       }
       System.out.println();
    }
    
}
class DLLNode
{
    DLLNode prev;
    DLLNode next;
    TrafficData DLLNodeData;
    
    DLLNode()
    {
        prev = this;
        next = this;
        DLLNodeData = new TrafficData();
    }
    DLLNode(TrafficData data)
    {
        prev = null;
        next = null;
        DLLNodeData = data;
    }
    
    public void DisplayNode() 
    {
           System.out.print( DLLNodeData.Time);
    }
}
