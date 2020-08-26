package Client;

import Client.PresentationLayer.FXcontroller;
import Server.Server;
import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Observable;

public class Client extends Observable {

    private static String userName;
    private static InetAddress serverIP;

    private static volatile boolean stop=false;

    //----------
    private static Client client;
    private Client(){}
    public static Client getInstance(){
        if(client==null)
            client=new Client();
        return client;
    }

    //----------

    static {
        try {
//            serverIP =  InetAddress.getLocalHost();
            serverIP =  InetAddress.getByName("132.72.65.41");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static int serverPort=5400;
    private static Socket theServer;
    private static Socket theServerListener;
    private static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;

//    public Client(InetAddress serverIP, int serverPort) {
//        this.serverIP = serverIP;
//        this.serverPort = serverPort;
//    }


    public static void setUserName(String userName) {
        Client.userName = userName;
    }

    public static String getUserName() {
        return userName;
    }

    public static Object connectToServer(Pair<String, Pair<String, List<String>>> objectToServer){
        return start(objectToServer);
    }

    public static Object start(Pair<String, Pair<String, List<String>>> objectToServer) {
        try {
            //if (theServer==null) {
            theServer = new Socket(serverIP, serverPort);
            theServer.setKeepAlive(true);
            objectOutputStream = new ObjectOutputStream(theServer.getOutputStream());//sends to server
            objectInputStream = new ObjectInputStream(theServer.getInputStream());//receive from server
            //}
            objectOutputStream.flush();
            objectOutputStream.writeObject(objectToServer);
            return objectInputStream.readObject();

            //LOG.info(String.format("Client is connected to server (IP: %s, port: %s)", serverIP, serverPort));
            //theServer.close();!!!

        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }

    public void startListen(String UserName){
        new Thread(() -> {
            runListening(UserName);
        }).start();
    }

    private void runListening(String UserName)
    {
        try {
            theServerListener = new Socket(serverIP, serverPort);
            theServerListener.setKeepAlive(true);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(theServerListener.getOutputStream());//sends to server
            ObjectInputStream objectInputStream = new ObjectInputStream(theServerListener.getInputStream());
            //}
            objectOutputStream.flush();
            objectOutputStream.writeObject(UserName);
            while(true){
                Object o= null;
                try {
                    o = objectInputStream.readObject();
//                    if(o !=null && o instanceof Pair &&((Pair<String,String>)o).getKey().equals(userName))
//                    {
//                        FXcontroller.realTime=o;
                    setChanged();
                    notifyObservers(o);
//                    }
//                    System.out.println((String)o);
                } catch (IOException e) {
                } catch (ClassNotFoundException e) {
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            ServerSocket serverSocket = new ServerSocket(5401);
//            while(!stop){
//                Socket clientSocket = serverSocket.accept(); // waiting for new request and blocking other requests
//                handleServerNotifications(clientSocket);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private static void handleServerNotifications(Socket clientSocket) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            objectOutputStream.flush();
            Object objectFromClient = objectInputStream.readObject();
            if(objectFromClient!=null) {
                System.out.println((String)objectFromClient);
            }

            //clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeSockets(){
        try {
            theServer.close();
            theServerListener.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
