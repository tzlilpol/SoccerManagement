package Server.BusinessLayer.OtherCrudOperations;
import Server.BusinessLayer.DataController;
import Server.BusinessLayer.Logger.Logger;
import Server.BusinessLayer.RoleCrudOperations.*;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public class Guest implements Serializable {


  private int id;
  private static int guestIDCounter=0;

  public Guest()
  {
    id=(++guestIDCounter);
  }



  public static int getGuestIDCounter() {
    return guestIDCounter;
  }

  public static void resetGuestIDCounter(){
    guestIDCounter=0;
  }

  public int getId()
  {
    return id;
  }

  public String toString()
  {
    return "Guest ID: "+id;
  }



  public Account LogIn(String UserName, String Password) throws Exception {
    String[] accountInfo=DataController.getInstance().getUserNamePasswordDC(UserName);
    if(accountInfo==null){
      (Logger.getInstanceError()).writeNewLineError("Guest #"+this.id+" tried to log in with wrong username");
      throw new Exception("Username does not exist");
    }
    else if(accountInfo[0].equals(UserName)&&!accountInfo[1].equals(hashPassword(Password))){
      (Logger.getInstanceError()).writeNewLineError("Guest #"+this.id+" tried to log in with wrong password");
      throw new Exception("Wrong password");
    }
    else if(accountInfo[0].equals(UserName)&&accountInfo[1].equals(hashPassword(Password))){
      Account account=new Account(accountInfo[2],Integer.parseInt(accountInfo[3]),accountInfo[0],accountInfo[1]);
      account.setLoggedIn(true);
      account=DataController.getInstance().getAccountRolesDC(account);
      (Logger.getInstance()).writeNewLine("Account "+UserName+" logged in");
      return account;
    }
    return null;
  }

  public void SignIn(String Name, int Age, String UserName, String Password) throws Exception {
    for(Account account: DataController.getInstance().getAccounts()){
      if(account.getUserName().equals(UserName)){
        (Logger.getInstanceError()).writeNewLineError("Guest #"+this.id+"tries to sign in with existing username");
        throw new Exception("Username already exists");
      }
    }
    Account newAccount=new Account(Name,Age,UserName,Password);
    newAccount.addRole(new Fan(newAccount.getName()));
    DataController.getInstance().addAccount(newAccount);
    (Logger.getInstance()).writeNewLine("New account "+UserName+" created");
  }




  private String hashPassword(String password){
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] buffer = password.getBytes("UTF-8");
      md.update(buffer);
      byte[] digest = md.digest();
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < digest.length; i++) {
        sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return "";
  }

}