/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmonitoringapplication;

import java.util.ArrayList;

/**
 *
 * @author UnBayleefable
 */
public class BinaryTree {
//         Different ways to traverse binary trees
//         theTree.inOrderTraverseTree(theTree.root , String[] resultArray);
//         theTree.preorderTraverseTree(theTree.root, String[] resultArray);
//         theTree.postOrderTraverseTree(theTree.root, String[] resultArray);         
//         System.out.println(theTree.findNode(75));

    Node root;

    public void addNode(int key) {
        
        Node newNode = new Node(key);
        if (root == null) {
            root = newNode;
        } else {
            Node focusNode = root;
            Node parent;
            while (true) {
                parent = focusNode;
                if (key < focusNode.key) {
                    focusNode = focusNode.leftChild;
                    if (focusNode == null) {
                        parent.leftChild = newNode;
                        return;
                    }
                } else { 
                    focusNode = focusNode.rightChild;
                    if (focusNode == null) {
                        parent.rightChild = newNode;
                        return;
                    }
                }
            }
        }
    }

    public ArrayList inOrderTraverseTree(Node focusNode, ArrayList<String> arr) {
        if (focusNode != null) {
            inOrderTraverseTree(focusNode.leftChild, arr);
            arr.add(focusNode.key + "");
            inOrderTraverseTree(focusNode.rightChild, arr);
        }
        return arr;
    }

    public ArrayList preorderTraverseTree(Node focusNode, ArrayList<String> arr) {
        if (focusNode != null) {
            arr.add(focusNode.key + "");
            preorderTraverseTree(focusNode.leftChild, arr);
            preorderTraverseTree(focusNode.rightChild, arr);
        }
        return arr;
    }

    public ArrayList postOrderTraverseTree(Node focusNode, ArrayList<String> arr) {
        if (focusNode != null) {
            postOrderTraverseTree(focusNode.leftChild, arr);
            postOrderTraverseTree(focusNode.rightChild, arr);
            arr.add(focusNode.key + "");
        }
        return arr;
    }

    public Node findNode(int key) {
        
        Node focusNode = root;
        while (focusNode.key != key) {
            if (key < focusNode.key) {
                focusNode = focusNode.leftChild;
            } else {
                focusNode = focusNode.rightChild;
            }
            if (focusNode == null) {
                return null;
            }
        }
        return focusNode;
    }
}

class Node {

    int key; 
    String name; // technically i took this out because i am a retard

    Node leftChild;
    Node rightChild;

    Node(int key) {
        this.key = key;

    }
    public String toString() {

		return name + " has the key " + key;

		/*
		 * return name + " has the key " + key + "\nLeft Child: " + leftChild +
		 * "\nRight Child: " + rightChild + "\n";
		 */

	}

   

}