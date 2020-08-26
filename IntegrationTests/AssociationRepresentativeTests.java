import Server.BusinessLayer.OtherCrudOperations.*;
import Server.BusinessLayer.RoleCrudOperations.AssociationRepresentative;
import Server.BusinessLayer.RoleCrudOperations.Owner;
import Server.BusinessLayer.RoleCrudOperations.Referee;
import Server.DataLayer.DBAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import static org.junit.Assert.*;

public class AssociationRepresentativeTests {

    AssociationRepresentative testAssociationRepresentative;
    Owner testOwner;
    Referee testReferee;
    League testLeague;
    Season testSeason;
    Policy testPolicy;
    Connection con;


    @Before
    public void setUp() throws Exception {
        testAssociationRepresentative =new AssociationRepresentative("testAssociationRepresentative");
        testOwner=new Owner("testOwner");
        testReferee=new Referee("testReferee");
        testLeague=new League("testLeague");
        testSeason = new Season("0000");
        testPolicy = new Policy("testPoint","testSchedule");
        SLsettings testSLsettings=new SLsettings(testPolicy);
        HashMap<Season,SLsettings> slMap = new HashMap();
        slMap.put(testSeason,testSLsettings);
        testLeague.setsLsetting(slMap);

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

        addAccountToDatabase(testAssociationRepresentative.getUsername());
        addAssociationRepresentativeToDatabase(testAssociationRepresentative.getUsername());
        addAccountToDatabase(testOwner.getUsername());
        addOwnerToDatabase(testOwner.getUsername());
        addLeagueToDatabase(testLeague.getName());
        addSeasonToDatabase(testSeason.getName());
        addAccountToDatabase(testReferee.getUsername());
        addRefereeToDatabase(testReferee.getUsername());
    }



    @Test
    public void addRefereeToLeague() {
        String actual = (testAssociationRepresentative.addRefereeToLeague(testReferee,testLeague,testSeason));
        assertEquals("Added referee to league successfully",actual);


    }

    @Test
    public void setLeaguePointCalcPolicy() {
        assertEquals("Set league point calculation policy successfully", testAssociationRepresentative.setLeaguePointCalcPolicy(testLeague,testSeason,testPolicy.getPointCalc()));
    }

    @Test
    public void setLeagueGameSchedualPolicy() {
        assertEquals("Set league game schedule policy successfully", testAssociationRepresentative.setLeagueGameSchedualPolicy(testLeague,testSeason,testPolicy.getGameSchedual()));

    }

    @Test
    public void approveTeam() {
        testOwner.createTeam("testTeam",testLeague,null);
        assertEquals("Team approved successfully", testAssociationRepresentative.approveTeam("testTeam",testOwner));

    }



    ///////DB insertions

    private void addAccountToDatabase(String username) {
        try {
            PreparedStatement ps=con.prepareStatement("insert into Account values(?,?,?,?,?) ");
            ps.setString(1,username);
            ps.setString(2,null);
            ps.setString(3,null);
            ps.setString(4,null);
            ps.setString(5,"0");
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

    private void addAssociationRepresentativeToDatabase(String username) {
        try {
            PreparedStatement ps=con.prepareStatement("insert into AssociationRepresentative values(?,?) ");
            ps.setString(1,username);
            ps.setString(2,null);
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

    private void addSeasonToDatabase(String name) {
        try {
            PreparedStatement ps1=con.prepareStatement("insert into Season values(?) ");
            ps1.setString(1,name);
            ps1.executeUpdate();

        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    private void addRefereeToDatabase(String username) {
        try {
            PreparedStatement ps=con.prepareStatement("insert into Referee values(?,?,?) ");
            ps.setString(1,username);
            ps.setString(2,null);
            ps.setString(3,null);
            ps.executeUpdate();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    ////DB delete
    @After
    public void clearDatabase()
    {
        try {
            PreparedStatement ps;

            ps=con.prepareStatement("DELETE FROM ApprovedTeams WHERE Owner=(?) and TeamName=(?)");
            ps.setString(1, testOwner.getUsername());
            ps.setString(2 ,"testTeam");
            ps.executeUpdate();

            ps=con.prepareStatement("DELETE FROM Alert WHERE UserName=(?)");
            ps.setString(1, testOwner.getUsername());
            ps.executeUpdate();

            ps=con.prepareStatement("DELETE FROM Alert WHERE UserName=(?)");
            ps.setString(1, testAssociationRepresentative.getUsername());
            ps.executeUpdate();

            ps=con.prepareStatement("DELETE FROM LeaguesForReferee WHERE Referee=(?) and League=(?) and Season=(?)");
            ps.setString(1, testReferee.getUsername());
            ps.setString(2 ,testLeague.getName());
            ps.setString(3,testSeason.getName());
            ps.executeUpdate();

            ps=con.prepareStatement("DELETE FROM SLsettings WHERE League=(?) and Season=(?)");
            ps.setString(1 ,testLeague.getName());
            ps.setString(2,testSeason.getName());
            ps.executeUpdate();

            ps=con.prepareStatement("DELETE FROM Policy WHERE League=(?) and Season=(?)");
            ps.setString(1 ,testLeague.getName());
            ps.setString(2,testSeason.getName());
            ps.executeUpdate();

            ps=con.prepareStatement("DELETE FROM League WHERE Name=(?)");
            ps.setString(1,testLeague.getName());
            ps.executeUpdate();

            ps=con.prepareStatement("DELETE FROM Owner WHERE UserName=(?)");
            ps.setString(1,testOwner.getUsername());
            ps.executeUpdate();

            ps=con.prepareStatement("DELETE FROM Season WHERE Name=(?)");
            ps.setString(1,testSeason.getName());
            ps.executeUpdate();

            ps=con.prepareStatement("DELETE FROM Referee WHERE UserName=(?)");
            ps.setString(1,testReferee.getUsername());
            ps.executeUpdate();

            ps=con.prepareStatement("DELETE FROM AssociationRepresentative WHERE UserName=(?)");
            ps.setString(1,testAssociationRepresentative.getUsername());
            ps.executeUpdate();

            ps=con.prepareStatement("DELETE FROM Account WHERE UserName=(?)");
            ps.setString(1,testOwner.getUsername());
            ps.executeUpdate();

            ps=con.prepareStatement("DELETE FROM Account WHERE UserName=(?)");
            ps.setString(1,testReferee.getUsername());
            ps.executeUpdate();

            ps=con.prepareStatement("DELETE FROM Account WHERE UserName=(?)");
            ps.setString(1,testAssociationRepresentative.getUsername());
            ps.executeUpdate();

            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }
}
