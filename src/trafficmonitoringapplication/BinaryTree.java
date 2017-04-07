
package trafficmonitoringapplication;

import java.util.ArrayList;

/**
 *
 * @author Shane Plater - 2017
 */
public class BinaryTree {

    Node root;

    public void addNode(int key, String value) {

        Node newNode = new Node(key, value);
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

    public ArrayList inOrderTraverseTree(Node focusNode, ArrayList<String[]> arr) {
        if (focusNode != null) {
            inOrderTraverseTree(focusNode.leftChild, arr);

            String[] temp = new String[2];
            temp[0] = focusNode.key + "";
            temp[1] = focusNode.value;
            arr.add(temp);

            inOrderTraverseTree(focusNode.rightChild, arr);
        }
        return arr;
    }

    public ArrayList preorderTraverseTree(Node focusNode, ArrayList<String[]> arr) {
        if (focusNode != null) {
            String[] temp = new String[2];
            temp[0] = focusNode.key + "";
            temp[1] = focusNode.value;
            arr.add(temp);

            preorderTraverseTree(focusNode.leftChild, arr);
            preorderTraverseTree(focusNode.rightChild, arr);
        }
        return arr;
    }

    public ArrayList postOrderTraverseTree(Node focusNode, ArrayList<String[]> arr) {
        if (focusNode != null) {
            postOrderTraverseTree(focusNode.leftChild, arr);
            postOrderTraverseTree(focusNode.rightChild, arr);

            String[] temp = new String[2];
            temp[0] = focusNode.key + "";
            temp[1] = focusNode.value;
            arr.add(temp);
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
    String value;

    Node leftChild;
    Node rightChild;

    Node(int key, String value) {
        this.key = key;
        this.value = value;

    }

    public String toString() {

        return value + " has the key " + key;
    }
}
