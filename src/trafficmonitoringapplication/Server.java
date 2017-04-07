
package trafficmonitoringapplication;

//Reference:
//  Creating a simple Chat Client/Server Solution 
//  http://pirate.shu.edu/~wachsmut/Teaching/CSAS2214/Virtual/Lectures/chat-client-server.html
import java.net.*;
import java.io.*;

/**
 *
 * @author Shane Plater - 2017
 */
public class Server implements Runnable {

    private ServerThread clients[] = new ServerThread[50];
    private ServerSocket server = null;
    private Thread thread = null;    
    private int clientCount = 0;

    public Server(int port) {
        try {
            System.out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            System.out.println("Server started: " + server);
            start();
        } catch (IOException ioe) {
            System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
        }
    }

    public void run() {
        while (thread != null) {
            try {
                System.out.println("Waiting for a client ...");
                addThread(server.accept()); // add new thread to list of clients

            } catch (IOException ioe) {
                System.out.println("Server accept error: " + ioe);
                stop();
            }
        }
    }

    public void start() {
        if (thread == null) { // if no thread is available when start is called
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() { // stop has been depreciated from the java.lang library
        if (thread != null) {
            thread.stop();
            thread = null;
        }
    }

    private int findClient(int ID) {
        for (int i = 0; i < clientCount; i++) {
            if (clients[i].getID() == ID) {
                return i;
            }
        }
        return -1;
    }

    public synchronized void handle(TrafficData data) {

        System.out.println("sending Data");
  for (int i = 0; i < clientCount; i++)
            {
                //if(clients[i].getID() != ID)
                clients[i].send(data); // send recieved data to all clients available
                System.out.println("Sent data to: " + clients[i].toString());
            }
        
    }

    public synchronized void remove(int ID) {
        int pos = findClient(ID);
        if (pos >= 0) {
            ServerThread toTerminate = clients[pos];
            System.out.println("Removing client thread " + ID + " at " + pos);
            if (pos < clientCount - 1) {
                for (int i = pos + 1; i < clientCount; i++) {
                    clients[i - 1] = clients[i];
                }
            }
            clientCount--;
            try {
                toTerminate.close();
            } catch (IOException ioe) {
                System.out.println("Error closing thread: " + ioe);
            }
            toTerminate.stop();
        }
    }

    private void addThread(Socket socket) {
        if (clientCount < clients.length) {
            System.out.println("Client accepted: " + socket);
            clients[clientCount] = new ServerThread(this, socket);
            try {
                clients[clientCount].open();
                clients[clientCount].start();
                clientCount++;
            } catch (IOException ioe) {
                System.out.println("Error opening thread: " + ioe);
            }
        } else {
            System.out.println("Client refused: maximum " + clients.length + " reached.");
        }
    }

    public static void main(String args[]) {
        Server server;
        if (args.length != 1) {            
            server = new Server(4444); // no argument (port) defined run stock port 4444
        } else {
            server = new Server(Integer.parseInt(args[0])); // port defined so use it.
        }
    }



}
