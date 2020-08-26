package Server.BusinessLayer.Controllers;

import Server.BusinessLayer.OtherCrudOperations.Account;
import Server.BusinessLayer.OtherCrudOperations.Guest;
import Server.BusinessLayer.RoleCrudOperations.*;

import java.util.ArrayList;
import java.util.List;

public class GuestBusinessController {
    private Guest guest;

    public GuestBusinessController(){
        this.guest=new Guest();
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }



    public List<String> LogIn(String UserName, String Password) {
        List<String> list=new ArrayList<>();
        try {
            Account account=guest.LogIn(UserName,Password);
            return makeControllersByRoles(account);
        } catch (Exception e) { list.add("?"+e.getMessage()) ;}
        return list;
    }


    private List<String> makeControllersByRoles(Account account){

        List<String> controllerList=new ArrayList<>();
        for(Role role:account.getRoles()){
            if(role instanceof Owner)
                controllerList.add("Owner");
            else if(role instanceof TeamManager)
                controllerList.add("TeamManager");
            else if(role instanceof AssociationRepresentative)
                controllerList.add("AssociationRepresentative");
            else if(role instanceof SystemManager)
                controllerList.add("SystemManager");
            else if(role instanceof Player)
                controllerList.add("Player");
            else if(role instanceof Referee)
                controllerList.add("Referee");
            else if(role instanceof Coach)
                controllerList.add("Coach");
            else if(role instanceof Fan)
                controllerList.add("Fan");
        }

        return controllerList;

    }

    public String SignIn(String name, int age, String UserName, String Password){
        try {
            guest.SignIn(name,age,UserName,Password);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "";
    }



}
