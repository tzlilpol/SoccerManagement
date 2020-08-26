import Server.BusinessLayer.DataController;
import Server.BusinessLayer.Logger.Logger;
import Server.BusinessLayer.OtherCrudOperations.Account;
import Server.BusinessLayer.OtherCrudOperations.Guest;
import Server.BusinessLayer.RoleCrudOperations.Fan;
import Server.BusinessLayer.RoleCrudOperations.Owner;
import Server.BusinessLayer.RoleCrudOperations.Role;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GuestTests {

    Guest guest;
    private final ByteArrayOutputStream OS=new ByteArrayOutputStream();
    private final PrintStream PS=System.out;


    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(OS));
        Guest.resetGuestIDCounter();
        guest=new Guest();
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


    //region UC and Technical Tests
    @Test
    public void logInNotExistingAccount(){
        try {
            guest.LogIn("A1XX","Password");
        }
        catch (Exception e){
            assertEquals(e.getMessage(),"Username does not exist");
        }
        assertTrue(checkLoggerLine("error log","Guest #1 tried to log in with wrong username"));
    }

    @Test
    public void logInWrongPassword(){
        try {
            guest.LogIn("Owner1X","Passwordd");
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
            account=guest.LogIn("Owner1X","Owner1X");
        } catch (Exception e) { e.printStackTrace(); }

        assertEquals("Owner1X",account.getUserName());

        assertTrue(account.getRole(0) instanceof Owner);

        assertTrue(checkLoggerLine("event log","Account Owner1X logged in"));

        DataController.getInstance().logOffAfterTests("Owner1X");
    }


    //endregion

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