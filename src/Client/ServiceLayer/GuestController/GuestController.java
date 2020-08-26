package Client.ServiceLayer.GuestController;
import Client.Client;
import Client.ServiceLayer.RoleController.*;
import Server.BusinessLayer.Controllers.GuestBusinessController;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GuestController {


    private GuestBusinessController guestBusinessController;


    public GuestController()
    {
        //guestBusinessController=new GuestBusinessController();
    }


    public List<Object> LogIn(String UserName, String Password) {
        try {
            List<Object> list=new ArrayList<>();
            if(UserName.length()==0||Password.length()==0){
                if(UserName.length()==0&&Password.length()!=0)
                    list.add("Username length is 0");
                else if(UserName.length()!=0&&Password.length()==0)
                    list.add("Password length is 0");
                else if(UserName.length()==0&&Password.length()==0)
                    list.add("Username and password length is 0");
                return list;
            }
            List<String> parameters = new LinkedList<>();
            parameters.add(UserName);
            parameters.add(Password);
            Client.getInstance().setUserName(UserName);
            String sendToServer = "Guest@"+Client.getUserName();
            List<String> stringControllers = (List<String>) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("LogIn",parameters)));
            if(stringControllers.get(0).contains("?")){
                list.add(stringControllers.get(0).substring(1));
                return list;
            }
            else{
                Client.getInstance().startListen(UserName);
                return getControllersList(stringControllers);
            }
        } catch (Exception e) {
            List<Object> ret=new ArrayList<>();
            ret.add("Please Check your connection to the server!");
            return ret;
        }

//        return guestBusinessController.LogIn(UserName,Password);

    }

    private List<Object> getControllersList(List<String> roles){
        List<Object> serviceControllersList = new LinkedList<>();
        for(String role:roles){
            if(role.equals("Owner"))
                serviceControllersList.add(new OwnerController());
            else if(role.equals("TeamManager"))
                serviceControllersList.add(new TeamManagerController());
            else if(role.equals("AssociationRepresentative"))
                serviceControllersList.add(new AssociationRepresentativeController());
            else if(role.equals("SystemManager"))
                serviceControllersList.add(new SystemManagerController());
            else if(role.equals("Player"))
                serviceControllersList.add(new PlayerController());
            else if(role.equals("Referee"))
                serviceControllersList.add(new RefereeController());
            else if(role.equals("Coach"))
                serviceControllersList.add(new OwnerController());
            else if(role.equals("Fan"))
                serviceControllersList.add(new FanController());
        }
        return serviceControllersList;
    }

    public String SignIn(String name, int age, String UserName, String Password){
        if(name.length()==0) return "Name field is empty";
        if(UserName.length()==0) return "Username field is empty";
        if(Password.length()==0) return "Password field is empty";
        for(int i=0;i<name.length();i++){
            if(!Character.isAlphabetic(name.charAt(i)))
                return "Name field can not contain symbols other than letters";
        }
        if(age<=0)
            return "Age has to be postive";
        return guestBusinessController.SignIn(name,age,UserName,Password);
    }




}
