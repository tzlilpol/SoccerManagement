
import Client.PresentationLayer.Main;
import Server.BusinessLayer.DataController;
import Server.BusinessLayer.Logger.Logger;
import Server.BusinessLayer.OtherCrudOperations.Account;
import Server.BusinessLayer.OtherCrudOperations.Guest;
import Server.BusinessLayer.RoleCrudOperations.Fan;
import Server.BusinessLayer.RoleCrudOperations.Owner;
import Server.BusinessLayer.RoleCrudOperations.Role;
import Server.BusinessLayer.RoleCrudOperations.TeamManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class GuestTest {
    Guest guest;
    List<Account> accountList=new ArrayList<>();
    private final ByteArrayOutputStream OS=new ByteArrayOutputStream();
    private final PrintStream PS=System.out;


    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(OS));
        Guest.resetGuestIDCounter();
        guest=new Guest();
        accountList.add(new Account("A1",99,"A1X",hashPassword("Password")));
        accountList.add(new Account("A2",99,"A2X",hashPassword("Password")));
        accountList.add(new Account("A3",99,"A3X",hashPassword("Password")));
        accountList.add(new Account("A4",99,"A4X",hashPassword("Password")));

        File[] loggers={new File("event log"), new File("error log")};
        for(File file:loggers){
            if(file.exists())
                file.delete();
        }
    }
    @After
    public void restore(){
        System.setOut(PS);
    }

    //region Getters&Setters Tests
    @Test
    public void getGuestIDCounter() {
        assertEquals(1, Guest.getGuestIDCounter());
    }
    @Test
    public void resetGuestIDCounter(){
        Guest.resetGuestIDCounter();
        assertEquals(0,Guest.getGuestIDCounter());
    }
    @Test
    public void getId() {
        Guest.resetGuestIDCounter();
        guest=new Guest();
        assertEquals(1,guest.getId());
    }
    @Test
    public void testToString() {

        assertEquals("Guest ID: 1",guest.toString());
    }
    //endregion

    //region UC and Technical Tests
    @Test
    public void logInNotExistingAccount(){
        try {
            LogIn("A1XX","Password");
        }
        catch (Exception e){
            assertEquals(e.getMessage(),"Username does not exist");
        }
        assertTrue(checkLoggerLine("error log","Guest #1 tried to log in with wrong username"));
    }

    @Test
    public void logInWrongPassword(){
        try {
            LogIn("A1X","Passwordd");
        }
        catch (Exception e){
            assertEquals(e.getMessage(),"Wrong password");
        }
        assertTrue(checkLoggerLine("error log","Guest #1 tried to log in with wrong password"));
    }

    @Test
    public void logIn(){
        Account account=null;
        try {
            account=LogIn("A1X","Password");
        } catch (Exception e) { e.printStackTrace(); }

        assertEquals("A1X",account.getUserName());

        assertTrue(account.getRole(0) instanceof Role);

        assertTrue(checkLoggerLine("event log","Account A1X logged in"));
    }


    //endregion





    public Account LogIn(String UserName, String Password) throws Exception {
        String[] accountInfo = checkUserExistenceStub(UserName);
        if(accountInfo==null){
            (Logger.getInstanceError()).writeNewLineError("Guest #1 tried to log in with wrong username");
            throw new Exception("Username does not exist");
        }
        else if(accountInfo[0].equals(UserName)&&!accountInfo[1].equals(hashPassword(Password))){
            (Logger.getInstanceError()).writeNewLineError("Guest #1 tried to log in with wrong password");
            throw new Exception("Wrong password");
        }
        else if(accountInfo[0].equals(UserName)&&accountInfo[1].equals(hashPassword(Password))){
            Account account=new Account(accountInfo[2],Integer.parseInt(accountInfo[3]),accountInfo[0],accountInfo[1]);
            account.setLoggedIn(true);
            account=getUserRolesStub(account);
            (Logger.getInstance()).writeNewLine("Account "+UserName+" logged in");
            return account;
        }
        return null;
    }
    private String[] checkUserExistenceStub(String userName){
        Account foundAccount=null;
        for(Account account:accountList){
            if(account.getUserName().equals(userName))
                foundAccount=account;
        }
        if(foundAccount==null) return null;

        String[] arr={foundAccount.getUserName(),foundAccount.getPassword(),foundAccount.getName(),foundAccount.getAge()+""};
        return arr;
    }

    private Account getUserRolesStub(Account account){
       account.addRole(new Fan("Fan1"));
       return account;
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

    private boolean checkLoggerLine(String loggerName, String line){
        try {
            File loggerFile=new File(loggerName);
            BufferedReader BR=new BufferedReader(new FileReader(loggerFile));
            String readLine="";
            while((readLine=BR.readLine())!=null){
                readLine=readLine.substring(readLine.indexOf("-")+2);
                if(!readLine.equals(line))
                    return false;
            }

            BR.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }




}
