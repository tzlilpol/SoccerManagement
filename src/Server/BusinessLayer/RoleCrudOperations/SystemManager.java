package Server.BusinessLayer.RoleCrudOperations;
import Server.BusinessLayer.DataController;
import Server.BusinessLayer.Logger.Logger;
import Server.BusinessLayer.OtherCrudOperations.Account;
import Server.BusinessLayer.Pages.Page;
import Server.BusinessLayer.OtherCrudOperations.Team;
import Client.ServiceLayer.OurSystemClient;

import java.io.Serializable;
import java.util.*;


public class SystemManager extends Role implements Serializable
{

  HashMap<String,String> complaintAndComments;

  public SystemManager(String aName)
  {
    setUsername(aName);
    complaintAndComments= new HashMap<>();
  }

  public HashMap<String, String> getComplaintAndComments() {
    return complaintAndComments;
  }

  public void setComplaintAndComments(HashMap<String, String> complaintAndComments) {
    this.complaintAndComments = complaintAndComments;
  }

  /**
   * delete team for Permanently, saving all her actions, notify owner and team manger and delete team's
   * page from all fans.
   * @param team
   * @return
   */
  public String DeleteTeamPermanently(Team team){
    if(team == null){
      Logger.getInstanceError().writeNewLineError(this.getUsername()+" delete "+(team.getName() +"Permanently failed"));
      return "Delete team failed";
    }
    removeTeamFromDm(team);
    //saveAction(team);
    notifyOnDelete(team);
    deleteFromAllFollowers(team);
    team.delete();
    Logger.getInstance().writeNewLine(this.getUsername()+" delete "+(team.getName() +"Permanently" ));
    return "Team successfully deleted";
  }

  private void removeTeamFromDm(Team aTeam) {
    DataController.getInstance().removeTeam(aTeam);
  }

  private void notifyOnDelete(Team team) {
    for (Owner owner : team.getOwners()){
      owner.notifyAccount(owner.getUsername(),"Delete Team Permanently:" + team.getName());
    }
    for(TeamManager teamManager : team.getTeamManagers()){
      teamManager.notifyAccount(teamManager.getUsername(),"Delete Team Permanently:" + team.getName());
    }
  }

  /**
   * delete team's page from all fans of the team (follower)
   * @param team
   */
  private void deleteFromAllFollowers(Team team) {
    Page teamPageToDelete = team.getPage();
    List<Fan> fans = teamPageToDelete.getFans();
    for (Fan fan : fans) {
      for (Page page : fan.getPages()) {
        if (page.equals(teamPageToDelete)) {
          page.delete();
          if(fans.size()==0)
            return;
          break;
        }
      }
    }

  }

//  private void saveAction(BusinessLayer.OtherCrudOperations.Team team) { }

  /**
   * remove account from system
   * @param account
   * @return
   */
  public String deleteAccount(Account account){
    if (account == null){
      Logger.getInstanceError().writeNewLineError(this.getUsername()+" delete "+(account.getName() +" failed"));
      return  "Delete account failed";
    }
    List <Role> roles = account.getRoles();
    boolean isOwner= false;
    boolean isSystemManger = false;
    Owner owner=null;
    for(Role role : roles){
      if(role instanceof Owner){
        owner= (Owner)role;
        isOwner = true;
      }
      if(role instanceof SystemManager){
        isSystemManger = true;
      }
    }
    if(isOwner){
      if(owner.getTeam().getOwners().size()>1){
        owner=null;
        DataController.getInstance().removeAccount(account);
        Logger.getInstance().writeNewLine(this.getUsername()+" delete "+(account.getName() +"successfully"));
        return  "Delete account successfully";
      }else {
        Logger.getInstanceError().writeNewLineError(this.getUsername() + " delete " + (account.getName() + "failed"));
        return "Delete account failed";// to find someone else who will be next owner before remove this one
      }
    }
    if(isSystemManger){
      List<Account> accounts = DataController.getInstance().getAccounts();
      for(Account account1 : accounts){
        for(Role role : account1.getRoles()){
          if(role instanceof SystemManager && !account.equals(account1)){
            DataController.getInstance().removeAccount(account);
            Logger.getInstance().writeNewLine(this.getUsername()+" delete "+(account.getName() +"successfully"));
            return  "Delete account successfully";
          }
        }
      }
      Logger.getInstanceError().writeNewLineError(this.getUsername() + " delete " + (account.getName() + "failed"));
      return  "Delete account failed";
    }
    DataController.getInstance().removeAccount(account);
    Logger.getInstance().writeNewLine(this.getUsername()+" delete "+(account.getName() +"successfully"));
    return "Delete account successfully";
  }

  /**
   * show all the complaints of the accounts in the system
   */
  public String showComplaints(){
    String complaints=null;
    for(Map.Entry <String,String> complain : complaintAndComments.entrySet()) {
      //System.out.println(complain.getKey().toString());
      complaints= complaints + "\n" + complain.getKey().toString();
    }
    return complaints;
  }

  /**
   * add new complain
   */
  public void addComplain(String complain){
    complaintAndComments.put(complain,null);

  }

  /**
   * add comment to the complaint
   */
  public String addComment(String comment,String acomplain){
    for(Map.Entry <String,String> complain : complaintAndComments.entrySet()){
      if(complain.getKey().equals(acomplain)){
        complaintAndComments.put(acomplain,comment);
      }
    }
    Logger.getInstance().writeNewLine(this.getUsername()+" add "+(comment +" successfully"));
    return "Add comment successfully";
  }

  /**
   * show system logger
   */
  public String showSystemLog(){
    String s= Logger.getInstance().readLoggerFile();
    //System.out.println(s);
    return s;
  }

  /**
   * build recommendation system
   */
  public void buildRecommendationSystem(){ }

  public static boolean createAccount(Account accountToAdd){
    DataController.getInstance().addAccount(accountToAdd);
    return true;
  }


  /**
   * creates a new BusinessLayer.RoleCrudOperations.Owner
   */
  public Account createOwner(String aName,int age,String userName, String password)
  {
    Owner owner=new Owner(aName,null,null);
    Account account=new Account(aName,age,userName,password);
    account.addRole(owner);
    createAccount(account);
    Logger.getInstance().writeNewLine(getName()+" just created a new owner: "+aName);
    return account;
  }

  /**
   * creates a new System Manager
   */
  public Account createSystemManager(String aName,int age,String userName, String password)
  {
    SystemManager systemManager=new SystemManager(aName);
    Account account=new Account(aName,age,userName,password);
    account.addRole(systemManager);
    createAccount(account);
    Logger.getInstance().writeNewLine(getName()+" just created a new System Manager: "+aName);
    return account;
  }

  /**
   * creates a new Association Representative
   */
  public Account createAssociationRepresentative(String aName,int age,String userName, String password)
  {
    AssociationRepresentative associationRepresentative=new AssociationRepresentative(aName);
    Account account=new Account(aName,age,userName,password);
    account.addRole(associationRepresentative);
    createAccount(account);
    Logger.getInstance().writeNewLine(getName()+" just created a new Association Representative: "+aName);
    return account;
  }

}