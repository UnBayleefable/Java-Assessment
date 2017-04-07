/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmonitoringapplication;


import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shane Plater - 2017
 */
public class OfficeThread extends Thread
{
    private Socket socket = null;
    private TrafficMonitoringApplication client = null;  
    private ObjectInputStream streamIn = null;
    
    
    public OfficeThread (TrafficMonitoringApplication _client, Socket _socket)
    {
        client = _client;
        socket = _socket;
        
        open();
        start();
    }
    public void open()
    {
        try
        {
            streamIn = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));         
        }
        catch (IOException ioe)
        {
            System.out.println("Error getting input stream: " + ioe);            
            client.close();
        }
    }

    public void close()
    {
        try
        {
            if (streamIn != null)
            {
                streamIn.close();
            }
        }
        catch (IOException ioe)
        {
            System.out.println("Error closing input stream: " + ioe);
            
        }
    }

    public void run()
    {
        while (true)
        {
            try
            {
                TrafficData ob = (TrafficData)streamIn.readObject();
                client.handle(ob);
            }
            catch (IOException ioe)
            {
                System.out.println("Listening error: " + ioe.getMessage());                
                client.close();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(OfficeThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}


