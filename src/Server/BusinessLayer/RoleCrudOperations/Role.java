package Server.BusinessLayer.RoleCrudOperations;
import Server.BusinessLayer.DataController;
import Server.BusinessLayer.OtherCrudOperations.Account;
import Server.BusinessLayer.OtherCrudOperations.Alert;
import Server.Server;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.*;


public abstract class Role implements Serializable
{

  private String name;
  private String username;
  private List<Alert> alertList;

  public Role(){
  }

  public Role(String aName)
  {
    name = aName;
    alertList=new ArrayList<>();
  }





  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public String toString()
  {
    return "name: "+this.name;
  }

  public List<Alert> getAlertList() {
//    return alertList;
    return DataController.getInstance().getAlertsForAccount(username);
  }

  public void setAlertList(List<Alert> alertList) {
    this.alertList = alertList;
  }

  public void addAlert(Alert alert){
    alertList.add(alert);
  }
  public void clearAlerts(){
    DataController.getInstance().clearAlertsForAccount(username);
  }


  public void removeAlert(Alert alert){
    for(int i=0;i<alertList.size();i++){
      if(alertList.get(i).equals(alert))
        alertList.remove(i);
    }
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public void notifyAccount(String userName, String notification)
  {
    if(DataController.getInstance().isAccountloggedIn(userName))
    {
      Pair<String,String> message=new Pair<>(userName,notification);
      Server.getInstance().sendMessageToClient(userName,message);
    }
    else
    {
      DataController.getInstance().addAlertToAccount(userName,notification);
    }
  }

    public void logOff()
    {
      DataController.getInstance().removeAlertsFromAccount(new Account(name,0,getUsername(),""));
      DataController.getInstance().setAccountLogIn(new Account(name,0,getUsername(),""),false);
    }
}