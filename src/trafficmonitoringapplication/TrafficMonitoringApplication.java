package trafficmonitoringapplication;

import java.awt.Color;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

public class TrafficMonitoringApplication extends JFrame implements ActionListener, KeyListener {

    //currently the linked list doesnt update with the sorting of the table, im thinking i have to implement the table sorting with the main array instead of a seperate array so it can do both
    //monitor table currently uses same data as normal table so i need to seperate the two
//<editor-fold defaultstate="collapsed" desc="Variables">
    public JButton btnExit, btnBTDisplay, btnPODisplay, btnPOSave, btnIODisplay, btnIOSave, btnPostDisplay, btnPostSave, btnLocSort, btnVehicleSort, btnVelocitySort, btnConnect;
    public JLabel lblAppHeading, lblDataRecievedHeading, lblLinkedList, lblBinaryTree, lblPreOrder, lblInOrder, lblPostOrder, lblTableHeading, lblSort;
    public JTextArea txtLinkedList, txtBinaryTree, txtDataRecieved;
    public JTable tblTrafficData, tblMonitorData;
    public BinaryTree trafficTree;
    public TableModel myModel;

    public NewDoublyLinkedList myDLList;
    public DLLNode node;

    String[] tblTrafficDataHeadings = new String[]{"Time", "Location", "Av.Vehicle#", "Av.Velocity"};
    String[] tblMonitorHeadings = new String[]{"Station ID:", "Report Status:"};

    ArrayList<TrafficData> trafficData;
    ArrayList<Object[]> trafficTableData;
    ArrayList<Object[]> monitorData;
    ArrayList<String> binaryData;
    ArrayList<String> linkedListData;

    SpringLayout springLayout;

    private Socket socket = null;
    private OfficeThread clientThread = null;
    private ObjectOutputStream streamOut = null;
    private String serverName = "127.0.0.1";
    private int serverPort = 4444;

    //</editor-fold>
    public static void main(String[] args) throws SQLException {
        TrafficMonitoringApplication AppWindow = new TrafficMonitoringApplication();
        AppWindow.run();
    }

    public void run() throws SQLException {
        setTitle("Traffic Monitoring Application");
        setBounds(500, 250, 700, 610);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                System.exit(0);
            }
        });

        readData();
        displayGUI();
        drawData();

        setResizable(true);
        setVisible(true);
    }

    //<editor-fold defaultstate="collapsed" desc="Display GUI"> 
    private void displayGUI() {
        springLayout = new SpringLayout(); // instantiating a new springlayout for my fields and text boxes to use
        setLayout(springLayout);
        displayTextAreas(springLayout);
        displayButtons(springLayout);
        displayLabels(springLayout);
        displayTable(springLayout);
        setupTables();
    }

    private void displayTable(SpringLayout layout) {
        tblTrafficData = LibraryComponents.LocateAJTable(this, myModel, layout, tblTrafficData, trafficTableData, tblTrafficDataHeadings, 40, 110, 340, 200);
        tblMonitorData = LibraryComponents.LocateAJTable(this, myModel, layout, tblMonitorData, monitorData, tblMonitorHeadings, 400, 110, 260, 200);
    }

    private void displayTextAreas(SpringLayout layout) {
        txtLinkedList = LibraryComponents.LocateAJTextArea(this, layout, txtLinkedList, 38, 370, 4, 56);
        txtBinaryTree = LibraryComponents.LocateAJTextArea(this, layout, txtBinaryTree, 38, 470, 3, 56);
        txtLinkedList.setLineWrap(true);
    }

    private void displayLabels(SpringLayout layout) {
        //need to figure out how to adjust size of text
        lblAppHeading = LibraryComponents.LocateAJLabel(this, layout, "Monitoring Office", 300, 20);
        lblDataRecievedHeading = LibraryComponents.LocateAJLabel(this, layout, "Active Monitoring Stations:", 450, 80);
        lblLinkedList = LibraryComponents.LocateAJLabel(this, layout, "Linked List: ", 40, 350);
        lblBinaryTree = LibraryComponents.LocateAJLabel(this, layout, "Binary Tree: ", 40, 450);
        lblPreOrder = LibraryComponents.LocateAJLabel(this, layout, "Pre - Order ", 78, 520);
        lblInOrder = LibraryComponents.LocateAJLabel(this, layout, "In - Order ", 308, 520);
        lblPostOrder = LibraryComponents.LocateAJLabel(this, layout, "Post - Order ", 508, 520);
        lblTableHeading = LibraryComponents.LocateAJLabel(this, layout, "Traffic Monitoring Data", 150, 80);
        lblSort = LibraryComponents.LocateAJLabel(this, layout, "Sort By: ", 50, 310);
    }

    private void displayButtons(SpringLayout layout) {
        btnExit = LibraryComponents.LocateAJButton(this, this, layout, "Exit", 400, 310, 260, 20);
        btnBTDisplay = LibraryComponents.LocateAJButton(this, this, layout, "Display", 568, 442, 80, 20);
        btnPODisplay = LibraryComponents.LocateAJButton(this, this, layout, "Display", 20, 540, 75, 20);
        btnPOSave = LibraryComponents.LocateAJButton(this, this, layout, "Save", 110, 540, 75, 20);
        btnIODisplay = LibraryComponents.LocateAJButton(this, this, layout, "Display", 240, 540, 75, 20);
        btnIOSave = LibraryComponents.LocateAJButton(this, this, layout, "Save", 330, 540, 75, 20);
        btnPostDisplay = LibraryComponents.LocateAJButton(this, this, layout, "Display", 460, 540, 75, 20);
        btnPostSave = LibraryComponents.LocateAJButton(this, this, layout, "Save", 550, 540, 75, 20);
        btnLocSort = LibraryComponents.LocateAJButton(this, this, layout, "Location", 124, 308, 85, 20);
        btnVehicleSort = LibraryComponents.LocateAJButton(this, this, layout, "Vehicle#", 209, 308, 85, 20);
        btnVelocitySort = LibraryComponents.LocateAJButton(this, this, layout, "Velocity", 294, 308, 85, 20);
        btnConnect = LibraryComponents.LocateAJButton(this, this, layout, "Connect", 10, 10, 85, 20);
    }

    private void setupTables() {
        tblTrafficData.isForegroundSet();
        tblTrafficData.setShowHorizontalLines(true);
        tblTrafficData.setRowSelectionAllowed(true);
        tblTrafficData.setColumnSelectionAllowed(false);
        tblTrafficData.setSelectionForeground(Color.white);
        tblTrafficData.setSelectionBackground(Color.blue);

    }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Action and Key Listeners">
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnExit) {
            System.exit(0);
        }

        if (e.getSource() == btnBTDisplay) {
            // button for displaying the binary tree extention task
        }

        if (e.getSource() == btnPODisplay) {
            binaryData = new ArrayList<>();
            trafficTree.preorderTraverseTree(trafficTree.root, binaryData);
            txtBinaryTree.setText("Pre-Order: ");
            DisplayBinaryTree();
        }
        if (e.getSource() == btnPOSave) {

        }

        if (e.getSource() == btnIODisplay) {
            binaryData = new ArrayList<>();
            txtBinaryTree.setText("In-Order: ");
            trafficTree.inOrderTraverseTree(trafficTree.root, binaryData);
            DisplayBinaryTree();
        }

        if (e.getSource() == btnIOSave) {

        }

        if (e.getSource() == btnPostDisplay) {
            binaryData = new ArrayList<>();
            trafficTree.postOrderTraverseTree(trafficTree.root, binaryData);
            txtBinaryTree.setText("Post-Order: ");
            DisplayBinaryTree();
        }
        if (e.getSource() == btnPostSave) {

        }

        if (e.getSource() == btnLocSort) {
            trafficTableData = SortingLibrary.BubbleSort(trafficTableData);
            tblTrafficData.repaint();
           
            DisplayLinkedList();
        }

        if (e.getSource() == btnVehicleSort) {
            trafficTableData = SortingLibrary.SelectionSort(trafficTableData);
            tblTrafficData.repaint();
            
            DisplayLinkedList();
        }

        if (e.getSource() == btnVelocitySort) {
            trafficTableData = SortingLibrary.InsertionSort(trafficTableData);
            tblTrafficData.repaint();
            
            DisplayLinkedList();
        }
        if (e.getSource() == btnConnect) {
            try {
                connect();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TrafficMonitoringApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    //<editor-fold defaultstate="collapsed" desc="Unused">
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e
    ) {

    }

    @Override
    public void keyReleased(KeyEvent e
    ) {

    }
    //</editor-fold>

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="File Management">
    private void readData() throws SQLException {

        trafficData = new ArrayList<>();
        TrafficData data = new TrafficData();
        trafficData = data.GetAllData();
        disperseData(trafficData);
    }

    public void disperseData(ArrayList<TrafficData> arr) {
        trafficTableData = new ArrayList<>();
        myModel = new TableModel(trafficTableData, tblTrafficDataHeadings);
        
        binaryData = new ArrayList<>();
        trafficTree = new BinaryTree();
        myDLList = new NewDoublyLinkedList();
        for (TrafficData temp : arr) {
            
            myDLList.InsertTailNode(myDLList, new DLLNode(temp));
            trafficTree.addNode(Integer.parseInt(temp.TotalVehicleNum));
            addTableData(temp);
        }
        
    }

    public void addTableData(TrafficData arr) {

        myModel.add(arr.Time, arr.LocationID, arr.AvVehicleNum, arr.AvVelocity);
        //drawData();

    }

    public void writeData(String fileName) throws IOException {
        BufferedWriter outFile = new BufferedWriter(new FileWriter(fileName + ".txt"));

        for (int i = 0; i < binaryData.size(); i++) {
            //process for writing out binary data with hashing algorithm
        }
    }

    private void drawData() {

        DisplayLinkedList();
        DisplayBinaryTree();
tblTrafficData.repaint();
    }

    private void DisplayBinaryTree() {

        if (binaryData.isEmpty()) { // setting up first entry into txtBinaryTree

            txtBinaryTree.setText("In-Order: ");
            trafficTree.inOrderTraverseTree(trafficTree.root, binaryData);
        }

        for (int i = 0; i < binaryData.size(); i++) {
            if (i == binaryData.size() - 1) {
                txtBinaryTree.append(binaryData.get(i));
            } else {
                txtBinaryTree.append(binaryData.get(i) + ", ");
            }
        }
    }

    private void DisplayLinkedList() {
        txtLinkedList.setText("");
         linkedListData = new ArrayList<>();
         //disperseData(trafficData);
        myDLList.ReturnListArray(linkedListData);
        for (int i = 0; i < linkedListData.size(); i++) {
            if (i == 0) {
                txtLinkedList.append("Head <-> " + linkedListData.get(i) + " <--> ");
            } else {
                if (i == linkedListData.size() - 1) {
                    txtLinkedList.append(linkedListData.get(i) + " <-> Tail");
                } else {
                    txtLinkedList.append(linkedListData.get(i) + " <--> ");
                }
            }
        }

    }

    private void testArrayContents(ArrayList<Object[]> arr) {
        for (int i = 0; i < arr.size(); i++) {
            System.out.println(arr.get(i)[0] + " " + arr.get(i)[1] + " " + arr.get(i)[2] + " " + arr.get(i)[3]);

        }
        System.out.println();
    }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Server Connection">
    public void connect() throws ClassNotFoundException {
        System.out.println("Establishing connection. Please wait ...");
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            open();
        } catch (UnknownHostException uhe) {
            System.out.println("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            System.out.println("Unexpected exception: " + ioe.getMessage());
        }
    }

    public void handle(TrafficData data) throws IOException, ClassNotFoundException {

        trafficData.add(data);
        System.out.println("data read in: " + data.Time);
        addTableData(data);

        binaryData = new ArrayList<>(); // fixing a doubling issue within the binary data after initial startup        
        myDLList.InsertTailNode(myDLList, new DLLNode(data));
        trafficTree.addNode(Integer.parseInt(data.TotalVehicleNum));

        
        drawData();
    }

    public void open() throws ClassNotFoundException {
        try {
            streamOut = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            streamOut.flush();
            clientThread = new OfficeThread(this, socket);
            // streamIn = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));            

        } catch (IOException ioe) {
            System.out.println("Error opening output stream: " + ioe);
        }
    }

    public void close() {
        try {
            if (streamOut != null) {
                streamOut.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
        }

    }

    public void getParameters() {
        serverName = "localhost";
        serverPort = 4444;
    }
    //</editor-fold>
}

class TableModel extends AbstractTableModel {

    ArrayList<Object[]> al;

    // the headers
    String[] header;

    // constructor 
    TableModel(ArrayList<Object[]> obj, String[] header) {
        // save the header
        this.header = header;
        // and the data
        al = obj;
    }

    // method that needs to be overload. The row count is the size of the ArrayList
    @Override
    public int getRowCount() {
        return al.size();
    }

    // method that needs to be overload. The column count is the size of our header
    @Override
    public int getColumnCount() {
        return header.length;
    }

    // method that needs to be overload. The object is in the arrayList at rowIndex
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return al.get(rowIndex)[columnIndex];
    }

    // a method to return the column name 
    @Override
    public String getColumnName(int index) {
        return header[index];
    }

    // a method to add a new line to the table
    public void add(String time, String location, String avVehicle, String avVelocity) {
        // make it an array[2] as this is the way it is stored in the ArrayList
        // (not best design but we want simplicity)
        al.add(new Object[]{time, location, avVehicle, avVelocity});
        // inform the GUI that I have change
        fireTableDataChanged();
    }

    public void tableDataChange() {
        // inform the GUI that I have change
        fireTableDataChanged();
    }

}
