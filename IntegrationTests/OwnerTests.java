
import Server.BusinessLayer.DataController;
import Server.BusinessLayer.Logger.Logger;
import Server.BusinessLayer.OtherCrudOperations.*;
import Server.BusinessLayer.OurSystemServer;
import Server.BusinessLayer.RoleCrudOperations.*;
import Server.DataLayer.DBAdapter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OwnerTests {
    private final ByteArrayOutputStream OS=new ByteArrayOutputStream();
    private final PrintStream PS=System.out;

    Owner owner;
    League league;
    Stadium stadium;
    Connection con;
    DBAdapter dbAdapter;

    @Before
    public void setUp() throws Exception {
        owner=new Owner("myNewOwner");
        league=new League("someLeague");
        stadium=new Stadium("someStadium");

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

        addAccountToDatabase(owner.getUsername());
        addOwnerToDatabase(owner.getUsername());
        addLeagueToDatabase(league.getName());
        addStadiumToDatabase(stadium.getName());
    }

    @Test
    public void createTeam()
    {

        //positive
        //1.
        assertEquals("Request sent, waiting for approval",owner.createTeam("myNewTeam",league,stadium));
        assertTrue(dbAdapter.checkIfRequestExists(owner.getUsername(),"myNewTeam").length()!=0);
        //2.
        assertEquals("waiting for approval",owner.createTeam("myNewTeam",league,stadium));
        //3.
        dbAdapter.approveTeam(owner.getUsername(),null,"myNewTeam");
        assertEquals("Team successfully created",owner.createTeam("myNewTeam",league,stadium));
        assertEquals(getOwnerTeam(owner.getUsername()),"myNewTeam");
        assertTrue(teamExistsInDatabase("myNewTeam"));

        //negative
        assertEquals("Wrong input, team already exists",owner.createTeam("myNewTeam",league,stadium));;
    }

    @After
    public void clearDatabase()
    {
        try {
            PreparedStatement ps=con.prepareStatement("DELETE FROM Owner WHERE UserName=(?)");
            ps.setString(1,owner.getUsername());
            ps.executeUpdate();
            ps=con.prepareStatement("DELETE FROM Team WHERE Name=(?)");
            ps.setString(1,"myNewTeam");
            ps.executeUpdate();
            ps=con.prepareStatement("DELETE FROM Account WHERE UserName=(?)");
            ps.setString(1,owner.getUsername());
            ps.executeUpdate();
//            ps=con.prepareStatement("DELETE FROM ApprovedTeams WHERE TeamName=(?)");
//            ps.setString(1,"myNewTeam");
//            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    private String getOwnerTeam(String username) {
        String ans="";
        try {
            PreparedStatement ps=con.prepareStatement("select Team from Owner WHERE UserName=(?)");
            ps.setString(1,username);
            ResultSet RS=ps.executeQuery();
            while(RS.next()){
                ans=RS.getString("Team");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    private boolean teamExistsInDatabase(String myNewTeam) {
        String ans="";
        try {
            PreparedStatement ps=con.prepareStatement("select * from Team WHERE Name=(?)");
            ps.setString(1,myNewTeam);
            ResultSet RS=ps.executeQuery();
            while(RS.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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


    private void addOwnerToDatabase(String username) {
        try {
            PreparedStatement ps=con.prepareStatement("insert into Owner values(?,?,?,?) ");
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

    public void addLeagueToDatabase(String name) {
        try {
            PreparedStatement ps1=con.prepareStatement("insert into League values(?) ");
            ps1.setString(1,name);
            ps1.executeUpdate();

        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void addStadiumToDatabase(String name) {
        try {
            PreparedStatement ps1=con.prepareStatement("insert into Stadium values(?) ");
            ps1.setString(1,name);
            ps1.executeUpdate();

        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }


}