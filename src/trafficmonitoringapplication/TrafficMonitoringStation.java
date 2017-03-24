/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmonitoringapplication;

import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author Shane Plater
 */
public class TrafficMonitoringStation extends JFrame implements ActionListener, KeyListener {

    /*
     want to implement the ability that when a station comes online it checks for a list of known stations online then adds itself to it and allocates a unique station id number
     time = current time
     location = unique id
     # lanes - randomly generated on program open but uneditible to simulate a different location for each station
     Avg 3 Vehicles/avg velocity - randomly generated each time submit is pressed so new data can be entered on next submit
     */
    //<editor-fold defaultstate="collapsed" desc="Variables">
    public JLabel lblMainHeading, lblSubHeading, lblTime, lblLocation, lblNumberOfLanes, lblTotalVehicleNum, lblAvVehicleNum, lblAvVelocity;
    public JTextField txtTime, txtLocation, txtNumberOfLanes, txtTotalVehicleNum, txtAvVehichleNum, txtAvVelocity;
    public JButton btnSubmit, btnExit, btnConnect;
    public int locationID;
    public TrafficData currentData;

    SpringLayout springLayout;

    private Socket socket = null;
    private ObjectOutputStream streamOut = null;   
    private String serverName = "localhost";
    private int serverPort = 4444;

    //</editor-fold>
    
    public static void main(String[] args) throws SQLException {
        TrafficMonitoringStation AppWindow = new TrafficMonitoringStation();
        AppWindow.run();
    }

    public void run() throws SQLException {
        setTitle("Monitoring Station");
        setBounds(300, 250, 250, 310);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        readData();
        displayGUI();
        setResizable(false);
        setVisible(true);
    }

    //<editor-fold defaultstate="collapsed" desc="Display GUI">
    private void displayGUI() {
        springLayout = new SpringLayout();
        setLayout(springLayout);
        displayTextFields(springLayout);
        displayButtons(springLayout);
        displayLabels(springLayout);
    }

    private void displayTextFields(SpringLayout layout) {
        txtTime = LibraryComponents.LocateAJTextField(this, this, layout, 8, 120, 50);
        txtLocation = LibraryComponents.LocateAJTextField(this, this, layout, 8, 120, 80);
        txtNumberOfLanes = LibraryComponents.LocateAJTextField(this, this, layout, 8, 120, 110);
        txtTotalVehicleNum = LibraryComponents.LocateAJTextField(this, this, layout, 8, 120, 140);
        txtAvVehichleNum = LibraryComponents.LocateAJTextField(this, this, layout, 8, 120, 170);
        txtAvVelocity = LibraryComponents.LocateAJTextField(this, this, layout, 8, 120, 200);
    }

    private void displayButtons(SpringLayout layout) {
        btnSubmit = LibraryComponents.LocateAJButton(this, this, layout, "Submit", 15, 230, 100, 20);
        btnConnect = LibraryComponents.LocateAJButton(this, this, layout, "Connect", 70, 255, 100, 20);
        btnExit = LibraryComponents.LocateAJButton(this, this, layout, "Exit", 120, 230, 90, 20);
    }

    private void displayLabels(SpringLayout layout) {
        //lblMainHeading = LibraryComponents.LocateAJLabel(this, layout, "Monitoring Station" + locationID, 60, 05);
        lblMainHeading = LibraryComponents.LocateAJLabel(this, layout, "Monitoring Station", 60, 05);
        lblSubHeading = LibraryComponents.LocateAJLabel(this, layout, "Enter Readings And Click Submit", 20, 30);

        lblTime = LibraryComponents.LocateAJLabel(this, layout, "Time:", 20, 50);
        lblLocation = LibraryComponents.LocateAJLabel(this, layout, "Location:", 20, 80);
        lblNumberOfLanes = LibraryComponents.LocateAJLabel(this, layout, "# Lanes:", 20, 110);
        lblTotalVehicleNum = LibraryComponents.LocateAJLabel(this, layout, "Total # Vehicles:", 20, 140);
        lblAvVehicleNum = LibraryComponents.LocateAJLabel(this, layout, "Avg # Vehicles:", 20, 170);
        lblAvVelocity = LibraryComponents.LocateAJLabel(this, layout, "Avg Velocity:", 20, 200);
    }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Action and Key Listeners">
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnExit) {
            close();
            System.exit(0);
        }
        if (e.getSource() == btnSubmit) {
            currentData = new TrafficData();
            currentData.AvVehicleNum = txtTotalVehicleNum.getText();
            currentData.Time = txtTime.getText();
            currentData.LocationID = txtLocation.getText();
            currentData.NumberOfLanes = txtNumberOfLanes.getText();
            currentData.TotalVehicleNum = txtTotalVehicleNum.getText();
            currentData.AvVelocity = txtAvVelocity.getText();

            try {
                send(currentData);
            } catch (IOException ex) {
                System.out.println("Unexpected exception: " + ex.getMessage());
            }

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
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    //</editor-fold>    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="File Management">
    private void readData() {
        // retrieve list of active stations and assign unique id
    }

    private void writeData() {
        //send data to the Monitoring Office
    }

    private void testArrayContents(ArrayList<Object[]> arr) {
        for (int i = 0; i < arr.size(); i++) {
            System.out.println(arr.get(i)[0] + " " + arr.get(i)[1] + " " + arr.get(i)[2] + " " + arr.get(i)[3] + " " + arr.get(i)[4] + " " + arr.get(i)[5]);

        }
        System.out.println();
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
            System.out.println("Unexpected exception: " + ioe.getMessage());
        }
    }


    public void send(TrafficData data) throws IOException {
        try
        {
        streamOut.writeObject(data);
        System.out.println("successfully sent data");
        streamOut.flush();
        }catch(IOException ex){
        System.out.println("Error Sending Data: " + ex);
        }
    }

    public void open() {
        try {
            streamOut = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            streamOut.flush();          
        } catch (IOException ioe) {
            System.out.println("Error opening output stream: " + ioe);
        }
    }

    public void close() {
        try {
            if (streamOut != null) {
                System.out.println("Closing Connection Stream...");
                streamOut.close();
            }
            if (socket != null) {
                System.out.println("Closing Socket...");
                socket.close();
            }
        } catch (IOException ioe) {
          System.out.println("Error closing ..." + ioe);
        }

    }

    //</editor-fold>
}
