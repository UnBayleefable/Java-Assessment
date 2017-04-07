/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmonitoringapplication;

import java.net.*;
import java.io.*;

/**
 *
 * @author Shane Plater - 2017
 */
public class ServerThread extends Thread {

    private TrafficData trafficData;
    private Server server = null;
    private Socket socket = null;
    private int ID = -1;
    private ObjectInputStream streamIn = null;
    private ObjectOutputStream streamOut = null;
   

    public ServerThread(Server _server, Socket _socket) {
        super();
        server = _server;
        socket = _socket;
        ID = socket.getPort();
    }

    public void send(TrafficData data) {
        try {
            streamOut.writeObject(data);            
            streamOut.flush();
        } catch (IOException ioe) {
            System.out.println(ID + " ERROR sending: " + ioe.getMessage());
            server.remove(ID);
            stop();

        }
    }

    public int getID() {
        return ID;
    }

    public void run() {
        System.out.println("Server Thread " + ID + " running.");
        while (true) {
            try {
                trafficData = (TrafficData) streamIn.readObject();               
                server.handle(trafficData);
            } catch (IOException ex) {
                System.out.println(ID + " ERROR reading: " + ex.getMessage());
                server.remove(ID);
            } catch (ClassNotFoundException ex) {
                System.out.println("Class not Found: " + ex);
            }
        }
    }

    public void open() throws IOException {
        // output stream is required to be instantiated before the input stream or the input will hang
        streamOut = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        streamOut.flush();
        streamIn = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (streamIn != null) {
            streamIn.close();
        }
        if (streamOut != null) {
            streamOut.close();
        }
    }
}
