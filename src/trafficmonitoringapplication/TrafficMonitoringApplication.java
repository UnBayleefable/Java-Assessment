package trafficmonitoringapplication;

import java.awt.Color;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Shane Plater - 2017
 */
public class TrafficMonitoringApplication extends JFrame implements ActionListener, KeyListener {
    
    //<editor-fold defaultstate="collapsed" desc="Variables">
    public JButton btnExit, btnBTDisplay, btnPODisplay, btnPOSave, btnIODisplay, btnIOSave, btnPostDisplay, btnPostSave, btnLocSort, btnVehicleSort, btnVelocitySort, btnConnect;
    public JLabel lblAppHeading, lblDataRecievedHeading, lblLinkedList, lblBinaryTree, lblPreOrder, lblInOrder, lblPostOrder, lblTableHeading, lblSort;
    public JTextArea txtLinkedList, txtBinaryTree, txtDataRecieved, txtMonitorData;
    public JTable tblTrafficData;
    public BinaryTree trafficTree;
    public TableModel myModel;

    public NewDoublyLinkedList myDLList;
    public DLLNode node;
    public HashMap<Integer, String> hm;

    String[] tblTrafficDataHeadings = new String[]{"Time", "Location", "Av.Vehicle#", "Av.Velocity"};
    String[] tblMonitorHeadings = new String[]{"Station ID:", "Report Status:"};

    ArrayList<TrafficData> trafficData;
    ArrayList<Object[]> trafficTableData;
    ArrayList<Object[]> monitorData;
    ArrayList<String[]> binaryData;
    ArrayList<String> linkedListData;
    SpringLayout springLayout;

    private Socket socket = null;
    private OfficeThread clientThread = null;
    private ObjectOutputStream streamOut = null;
    private String serverName = "127.0.0.1";
    private int serverPort = 4444;

    //</editor-fold>
    
    public static void main(String[] args) throws SQLException, IOException, FileNotFoundException, ClassNotFoundException {
        TrafficMonitoringApplication AppWindow = new TrafficMonitoringApplication();
        AppWindow.run();
    }

    public void run(){
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

    }

    private void displayTextAreas(SpringLayout layout) {
        txtLinkedList = LibraryComponents.LocateAJTextArea(this, layout, txtLinkedList, 38, 370, 4, 56);
        txtBinaryTree = LibraryComponents.LocateAJTextArea(this, layout, txtBinaryTree, 38, 470, 3, 56);
        txtMonitorData = LibraryComponents.LocateAJTextArea(this, layout, txtMonitorData, 400, 110, 12, 23);
        txtBinaryTree.setLineWrap(true);
        txtMonitorData.setLineWrap(true);
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
            close();
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
            writeData();
        }

        if (e.getSource() == btnIODisplay) {
            binaryData = new ArrayList<>();
            txtBinaryTree.setText("In-Order: ");
            trafficTree.inOrderTraverseTree(trafficTree.root, binaryData);
            DisplayBinaryTree();
        }

        if (e.getSource() == btnIOSave) {
            writeData();

        }

        if (e.getSource() == btnPostDisplay) {
            binaryData = new ArrayList<>();
            trafficTree.postOrderTraverseTree(trafficTree.root, binaryData);
            txtBinaryTree.setText("Post-Order: ");
            DisplayBinaryTree();
        }
        if (e.getSource() == btnPostSave) {
            writeData();
        }

        if (e.getSource() == btnLocSort) {
            trafficTableData = SortingLibrary.BubbleSort(trafficTableData);
            tblTrafficData.repaint();
        }

        if (e.getSource() == btnVehicleSort) {
            trafficTableData = SortingLibrary.SelectionSort(trafficTableData);
            tblTrafficData.repaint();
        }

        if (e.getSource() == btnVelocitySort) {
            trafficTableData = SortingLibrary.InsertionSort(trafficTableData);
            tblTrafficData.repaint();
        }

        if (e.getSource() == btnConnect) {
            connect();
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
    private void readData() {
        trafficData = new ArrayList<>();
        trafficTableData = new ArrayList<>();
        binaryData = new ArrayList<>();
        trafficTree = new BinaryTree();
        myDLList = new NewDoublyLinkedList();
        TrafficData data = new TrafficData();
        try {

            trafficData = data.GetAllData(); // call for data using UCanAccess
            myModel = new TableModel(trafficTableData, tblTrafficDataHeadings); // create table model using returned data
            
            // iterate through records and disperse data
            for (TrafficData temp : trafficData) {
                myDLList.InsertTailNode(myDLList, new DLLNode(temp));
                trafficTree.addNode(Integer.parseInt(temp.TotalVehicleNum), temp.Time + temp.LocationID);
                addTableData(temp);
            }
            try {
                // attempt to read in hash map from serialized file within directory
                ObjectInputStream hmIn = new ObjectInputStream(new FileInputStream("HashMap.ser"));
                hm = (HashMap) hmIn.readObject();
                hmIn.close();
            } catch (IOException ex) {
                // dont do anything if the file doesnt exist
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("error: " + ex);
        }
    }

    public void addTableData(TrafficData arr) {
        myModel.add(arr.Time, arr.LocationID, arr.AvVehicleNum, arr.AvVelocity);
    }

    public void writeData() {
        // saving hashmap as serialized file        
        hm = new HashMap<>();

        for (String[] key : binaryData) {
            hm.put(Integer.parseInt(key[0]), key[1]);
        }
        try {
            ObjectOutputStream hashOut = new ObjectOutputStream(new FileOutputStream("HashMap.ser"));
            hashOut.writeObject(hm);
            hashOut.close();
        } catch (IOException ex) {
            System.out.println("error: " + ex);
        }

    }

    private void drawData() {

        DisplayLinkedList();
        DisplayBinaryTree();
        tblTrafficData.repaint();
    }

    private void DisplayBinaryTree() {

        if (binaryData.isEmpty()) { // if no entries are in the binary list
            txtBinaryTree.setText("In-Order: ");
            trafficTree.inOrderTraverseTree(trafficTree.root, binaryData);
        }

        for (int i = 0; i < binaryData.size(); i++) {
            if (i == binaryData.size() - 1) {
                txtBinaryTree.append(binaryData.get(i)[0]);
            } else {
                txtBinaryTree.append(binaryData.get(i)[0] + ", ");
            }
        }
    }

    private void DisplayLinkedList() {
        txtLinkedList.setText(""); // reseting linked list display
        linkedListData = new ArrayList<>();
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

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Server Connection">
    public void connect() {
        System.out.println("Establishing connection. Please wait ...");
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            open();
        } catch (UnknownHostException uhe) {
            System.out.println("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            System.out.println("Cannot connect to server: " + ioe.getMessage());
        }
    }

    public void handle(TrafficData data) {
    // add recieved data to the table and array
        trafficData.add(data); 
        addTableData(data); 
        txtMonitorData.append("Data Recieved From Station: " + data.LocationID + " at " + data.Time + "\n");
        binaryData = new ArrayList<>(); // clear binary array to insure no doubleups occur
        //disperse new entry to binary tree and linked list
        myDLList.InsertTailNode(myDLList, new DLLNode(data));
        trafficTree.addNode(Integer.parseInt(data.TotalVehicleNum), data.Time + data.LocationID);
        drawData();
    }

    public void open() {
        try { 
            // output stream needs to be flushed to insure the pipe is clear
            streamOut = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            streamOut.flush();
            clientThread = new OfficeThread(this, socket);
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
