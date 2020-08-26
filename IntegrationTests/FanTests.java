import Server.BusinessLayer.DataController;
import Server.BusinessLayer.OtherCrudOperations.Alert;
import Server.BusinessLayer.OtherCrudOperations.League;
import Server.BusinessLayer.OtherCrudOperations.Match;
import Server.BusinessLayer.OtherCrudOperations.Stadium;
import Server.BusinessLayer.RoleCrudOperations.Fan;
import Server.BusinessLayer.RoleCrudOperations.Owner;
import Server.DataLayer.DBAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class FanTests {
    Fan fan;
    Connection con;
    DBAdapter dbAdapter;

    @Before
    public void setUp() throws Exception {
        fan=new Fan("myNewFan");

        dbAdapter=new DBAdapter();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost;databaseName=DB2020;integratedSecurity=true";
            con = DriverManager.getConnection(connectionUrl);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }

        addAccountToDatabase(fan.getUsername());
        addFanToDatabase(fan.getUsername());
    }
    @After
    public void clearDB(){
        try {
            PreparedStatement ps=con.prepareStatement("DELETE FROM Fan WHERE UserName=(?)");
            ps.setString(1,fan.getUsername());
            ps.executeUpdate();
            ps=con.prepareStatement("DELETE FROM Account WHERE UserName=(?)");
            ps.setString(1,fan.getUsername());
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    @Test
    public void subscribeGetMatchNotifications()
    {
        fan.SubscribeGetMatchNotifications();
        assertTrue(dbAdapter.getNotifiedFans().contains(fan.getUsername()));
    }
    @Test
    public void unSubscribeGetMatchNotifications()
    {
        fan.unSubscribeGetMatchNotifications();
        assertFalse(dbAdapter.getNotifiedFans().contains(fan.getUsername()));
    }

    private void addAccountToDatabase(String username) {
        try {
            PreparedStatement ps=con.prepareStatement("insert into Account values(?,?,?,?,?) ");
            ps.setString(1,username);
            ps.setString(2,null);
            ps.setString(3,null);
            ps.setString(4,null);
            ps.setString(5,null);
            ps.executeUpdate();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }


    private void addFanToDatabase(String username) {
        try {
            PreparedStatement ps=con.prepareStatement("insert into Fan values(?,?,?,?) ");
            ps.setString(1,username);
            ps.setString(2,null);
            ps.setString(3,null);
            ps.setString(4,null);
            ps.executeUpdate();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }


}