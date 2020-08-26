package Server;

import Server.BusinessLayer.Controllers.*;
import Server.BusinessLayer.DataController;
import Server.BusinessLayer.OurSystemServer;
import Server.BusinessLayer.RoleCrudOperations.*;
import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    private int port=5400;
    private int listeningInterval=1000;
    private volatile boolean stop=false;

    private static HashMap<String, ObjectOutputStream> clientsMap;

    private static Server server;

    private static OurSystemServer ourSystemServer;




    private Server() {
    }

    public static Server getInstance(){
        if(server==null)
            server=new Server();
        return server;
    }

    public void start() {
        clientsMap=new HashMap<>();
        new Thread(() -> {
            runServer();
        }).start();
    }

    private void runServer() {
        System.out.println("Server started");
        try {
            int threadPoolSize = 5; //Configurations
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadPoolSize);//newCachedThreadPool();
            threadPoolExecutor.setCorePoolSize(threadPoolSize);

            ServerSocket serverSocket = new ServerSocket(port);
            //serverSocket.setSoTimeout(listeningInterval);

            while (!stop) {
                try { //opens new thread per request!!!!
                    Socket clientSocket = serverSocket.accept(); // waiting for new request and blocking other requests
                    threadPoolExecutor.execute(() -> {
                        handleClient(clientSocket);
                    });
                } catch (SocketTimeoutException e) {
                }
            }
            serverSocket.close();
            threadPoolExecutor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

            objectOutputStream.flush();
            Object objectFromClient = objectInputStream.readObject();
            if(objectFromClient!=null) {
                if(objectFromClient instanceof String){
                    System.out.println("Listen request accepted");
                    clientsMap.put((String)objectFromClient,objectOutputStream);
                }
                else{
                    System.out.println("Operation request accepted");
                    objectOutputStream.writeObject(receiveFromClient((Pair<String, Pair<String, List<String>>>) objectFromClient));
                }
            }

            //clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Object receiveFromClient(Pair<String, Pair<String, List<String>>> fromClient) {
        String controllerName = fromClient.getKey().split("@")[0];
        String userName = fromClient.getKey().split("@")[1];
        Object controller=null;
        Object answer = null;
        if (controllerName.equals("Guest"))
            controller = new GuestBusinessController();
        else if(controllerName.equals("Owner"))
            controller = new OwnerBusinessController(new Owner(userName));
        else if(controllerName.equals("TeamManager"))
            controller = new TeamManagerBusinessController(new TeamManager(userName));
        else if(controllerName.equals("AssociationRepresentative"))
            controller = new AssociationRepresentativeBusinessController(new AssociationRepresentative(userName));
        else if(controllerName.equals("SystemManager"))
            controller= new SystemManagerBusinessController(new SystemManager(userName));
        else if(controllerName.equals("Player"))
            controller = new PlayerBusinessController(new Player(userName));
        else if(controllerName.equals("Referee"))
            controller =new RefereeBusinessController(new Referee(userName));
        else if(controllerName.equals("Coach"))
            controller = new CoachBusinessController(new Coach(userName));
        else if(controllerName.equals("Fan"))
            controller = new FanBusinessController(new Fan(userName));
        else if(controllerName.equals("Data"))
            controller= DataController.getInstance();

        try {
            if(controller!=null) {
                String declareMethod = fromClient.getValue().getKey();
                List<String> parameters = fromClient.getValue().getValue();
                Method method=getRightMethod(controller.getClass().getDeclaredMethods(),declareMethod);
                answer = method.invoke(controller, parameters.toArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answer;

    }


    public Method getRightMethod(Method[] methods,String name){
        for(Method method:methods){
            if(method.getName().equals(name))return method;
        }
        return null;
    }

    public void stop() {
        System.out.println("Stopping server..");
        stop = true;
    }

    public void sendMessageToClient(String userName, Object Message) {
        try {
            clientsMap.get(userName).writeObject(Message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void sendMessageToClient(String string){
//        try {
//            System.out.println(clientsMap.get("Owner1X").getHostAddress());
//            Socket socket=new Socket(clientsMap.get("Owner1X"),5401);
//            socket.setKeepAlive(true);
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());//sends to server
//            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());//receive from server
//            objectOutputStream.flush();
//            objectOutputStream.writeObject(string);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
