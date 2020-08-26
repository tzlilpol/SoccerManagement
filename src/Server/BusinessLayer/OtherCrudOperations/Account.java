package Server.BusinessLayer.OtherCrudOperations;
import Server.BusinessLayer.RoleCrudOperations.*;
import Server.BusinessLayer.DataController;

import java.io.Serializable;
import java.util.*;


public class Account implements Serializable {

  private String userName;
  private String password;
  private List<Role> roles;
  private String name;
  private int age;
  private boolean isLoggedIn;




  public Account(String aName, int aAge,String aUserName, String aPassword)
  {
    userName = aUserName;
    password = aPassword;
    name=aName;
    age=aAge;
    roles = new ArrayList<Role>();
    isLoggedIn=DataController.getInstance().isAccountloggedIn(userName);
  }

  public boolean isLoggedIn() {
      return isLoggedIn;
  }

  public void setLoggedIn(boolean loggedIn) {
    isLoggedIn = loggedIn;
    DataController.getInstance().setAccountLogIn(this,loggedIn);
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public boolean setUserName(String aUserName)
  {
    boolean wasSet = false;
    userName = aUserName;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public String getUserName()
  {
    return userName;
  }

  public String getPassword()
  {
    return password;
  }

  public Role getRole(int index)
  {
    Role aRole = roles.get(index);
    return aRole;
  }

  public List<Role> getRoles()
  {
    List<Role> newRoles = Collections.unmodifiableList(roles);
    return newRoles;
  }

  /**
   * returns the number of roles the account has
   */
  public int numberOfRoles()
  {
    int number = roles.size();
    return number;
  }

  /**
   * checks if the account has any roles
   */
  public boolean hasRoles()
  {
    boolean has = roles.size() > 0;
    return has;
  }

  /**
   * returns the index of the BusinessLayer.RoleCrudOperations.Role in the list
   */
  public int indexOfRole(Role aRole)
  {
    int index = roles.indexOf(aRole);
    return index;
  }

  /**
   * returns the minimus number of roles that an account can have
   */
  public static int minimumNumberOfRoles()
  {
    return 1;
  }

  /**
   * adds a role to the account
   * @return
   */
  public boolean addRole(Role aRole)
  {
    //instanceOf check

    //instanceOf...
    aRole.setUsername(userName);
    boolean wasAdded = true;
    if (roles.contains(aRole)) { return true; }
    roles.add(aRole);
    return wasAdded;
  }

  /**
   * remove a role from an acount
   */
  public boolean removeRole(Role aRole)
  {
    if(aRole==null)
      return false;
    boolean wasRemoved = true;
    if (!roles.contains(aRole))
    {
      return wasRemoved;
    }
    roles.remove(aRole);
    return wasRemoved;
  }

  /**
   * removes all the roles from the account
   */
  public void emptyRoles()
  {
    roles.clear();
  }

  public String toString()
  {
    return super.toString() + "["+
            "userName" + ":" + getUserName()+ "," +
            "password" + ":" + getPassword()+ "]";
  }

  /**
   * checks is the account is a coach
   */
  public Coach checkIfCoach()
  {
    for(Role role: roles)
    {
      if(role instanceof Coach) return (Coach) role;
    }
    return null;
  }

  /**
   * checks is the account is a team manager
   */
  public TeamManager checkIfTeamManagr()
  {
    for(Role role: roles)
    {
      if(role instanceof TeamManager) return (TeamManager) role;
    }
    return null;
  }

  /**
   * checks is the account is a owner
   */
  public Owner checkIfOwner()
  {
    for(Role role: roles)
    {
      if(role instanceof Owner) return (Owner) role;
    }
    return null;
  }

  /**
   * checks is the account is a player
   */
  public Player checkIfPlayer()
  {
    for(Role role: roles)
    {
      if(role instanceof Player) return (Player) role;
    }
    return null;
  }

  /**
   * checks is the account is a fan
   */
  public Fan checkIfFan()
  {
    for(Role role: roles)
    {
      if(role instanceof Fan) return (Fan) role;
    }
    return null;
  }

  /**
   * checks is the account is a system manager
   */
  public SystemManager checkIfSystemManager()
  {
    for(Role role: roles)
    {
      if(role instanceof SystemManager) return (SystemManager) role;
    }
    return null;
  }

  /**
   * checks is the account is a Assiciation Representive
   */
  public AssociationRepresentative checkIfAssiciationRepresentive()
  {
    for(Role role: roles)
    {
      if(role instanceof AssociationRepresentative) return (AssociationRepresentative) role;
    }
    return null;
  }

  /**
   * checks is the account is a referee
   */
  public Referee checkIfReferee()
  {
    for(Role role: roles)
    {
      if(role instanceof Referee) return (Referee) role;
    }
    return null;
  }

  public void ShowAccount(){
    System.out.println("Name:");
    System.out.println(this.getName());
    System.out.println();
    System.out.println("Age:");
    System.out.println(this.getAge());
    System.out.println();
    System.out.println("Roles:");
    for(Role role:this.getRoles())
      System.out.println(role.getClass().getName());
    System.out.println();
  }

  public static Account convertStringToAccount(String userName){
    for (Account account : DataController.getInstance().getAccounts()){
      if(account.getUserName().equals(userName)){
        return account;
      }
    }
    return null;
  }

}