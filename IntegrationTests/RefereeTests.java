import Server.BusinessLayer.OtherCrudOperations.EventEnum;
import Server.BusinessLayer.OtherCrudOperations.GameEvent;
import Server.BusinessLayer.OtherCrudOperations.Match;
import Server.BusinessLayer.RoleCrudOperations.Referee;
import Server.DataLayer.DBAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RefereeTests {
    Referee referee1;
    Referee referee2;
    Connection con;
    DBAdapter dbAdapter;
    Date currDate;
    Time currTime;
    String strDate;
    String strTime;

    @Before
    public void setUp() throws Exception {
        referee1 =new Referee("RefereeTest1");
        referee2 =new Referee("RefereeTest2");
        dbAdapter = new DBAdapter();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost;databaseName=DB2020;integratedSecurity=true";
            con = DriverManager.getConnection(connectionUrl);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            if (!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
        addAccountToDatabase("RefereeTest1");
        addAccountToDatabase("RefereeTest2");
        addRefereeToDatabase("RefereeTest1");
        addRefereeToDatabase("RefereeTest2");

        //---add match that end after 5 hours
        List<String> match1=new LinkedList<>();
        match1.add("15/05/2020");
        match1.add("12:00:00");
        match1.add("0");
        match1.add("0");
        match1.add("Team2");
        match1.add("Team1");
        match1.add("RefereeTest1");
        match1.add("RefereeTest2");
        match1.add("RefereeTest2");
        match1.add("Stadium1");
        match1.add("Season");
        addMatchToDatabase(match1);
        //---add match that occur now
        List<String> match2=new LinkedList<>();
        currDate=new Date();
        currTime=new Time(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        strDate = dateFormat.format(currDate);
        strTime=currTime.getHours()+":"+currTime.getMinutes()+":"+currTime.getSeconds();
        match2.add(strDate);
        match2.add(strTime);
        match2.add("0");
        match2.add("0");
        match2.add("Team3");
        match2.add("Team2");
        match2.add("RefereeTest1");
        match2.add("RefereeTest2");
        match2.add("RefereeTest2");
        match2.add("Stadium1");
        match2.add("Season");
        addMatchToDatabase(match2);
        List<String> event1=new LinkedList<>();
        event1.add("redCard");
        event1.add("12:00:00");
        event1.add("Messei got");
        event1.add("15");
        event1.add("15/05/2020");
        event1.add("Team2");
        event1.add("Team1");
        addEventToDatabase(event1);
        List<String> event2=new LinkedList<>();
        event2.add("redCard");
        event2.add(strTime);
        event2.add("just now");
        event2.add("15");
        event2.add(strDate);
        event2.add("Team3");
        event2.add("Team2");
        addEventToDatabase(event2);
    }
    @Test
    public void gameReportTest()
    {
        Match match=Match.convertStringToMatch("Teams: Team1 against Team2, Date: 15/05/2020");
        assertTrue(referee1.gameReport(match).equals("\tMatch Location:  Stadium1           Date:  15/05/2020           Start Time:  12:00:00\n"+
                "\tHome Team : Team1                     Score:  0\n"+
                "\tAway  Team : Team2                     Score:  0\n"+
                "\tMain Referee :  RefereeTest1\n"+"" +
                "\tLine Referee 1:  RefereeTest2         Line Referee 2:  RefereeTest2\n"+
                "\tGame Events:\n"+
                "\tEventType: redCard, Description: Messei got, Minute: 15\n"));
    }
    @Test
    public void updateEventDuringMatchTest()
    {
        //1.success
        Match match1=Match.convertStringToMatch("Teams: Team2 against Team3, Date: "+strDate);
        try {
          assertTrue(referee1.updateEventDuringMatch(match1, EventEnum.foul,"Test1"));
        } catch (Exception e) {
        }
        //2.try to update not during the game
        Match match2=Match.convertStringToMatch("Teams: Team1 against Team2, Date: 15/05/2020");
        try {
            assertFalse(referee1.updateEventDuringMatch(match2, EventEnum.foul,"Test2"));
        } catch (Exception e) {
          assertTrue(e.getMessage().equals("Referee tried to add event not during the match"));
        }

    }
    @Test
    public void editEventAfterGameTest()
    {
        //1.pass 5 hours success
        Match match1=Match.convertStringToMatch("Teams: Team1 against Team2, Date: 15/05/2020");
        GameEvent gameEvent=convertStringToGameEvent("EventType: redCard, Description: Messei got, Minute: 15");
        try {
            assertTrue(referee1.editEventAfterGame(match1,gameEvent,EventEnum.injury,"Test1"));
        } catch (Exception e) {
        }
        //2.occur now
        Match match2=Match.convertStringToMatch("Teams: Team2 against Team3, Date: "+strDate);
        GameEvent gameEvent2=convertStringToGameEvent("EventType: redCard, Description: just now, Minute: 15");
        try {
            assertFalse(referee1.editEventAfterGame(match2,gameEvent2,EventEnum.injury,"Test2"));
        } catch (Exception e) {
            assertTrue(e.getMessage().equals("Referee can edit event only after 5 hours"));
        }
        //3.not main referee
        try {
            assertFalse(referee2.editEventAfterGame(match1,gameEvent,EventEnum.injury,"Test1"));
        } catch (Exception e) {
            assertTrue(e.getMessage().equals("Referee is not a main referee"));
        }
    }

    @After
    public void clearDatabase()
    {
        try {
            PreparedStatement ps=con.prepareStatement("DELETE FROM GameEvent WHERE Date=(?) AND AwayTeam=(?) AND homeTeam=(?) AND Hour=(?)");
            ps.setString(1,"15/05/2020");
            ps.setString(2,"Team2");
            ps.setString(3,"Team1");
            ps.setString(4,"12:00:00");
            ps.executeUpdate();

            ps=con.prepareStatement("DELETE FROM GameEvent WHERE Date=(?) AND AwayTeam=(?) AND homeTeam=(?) AND Description=(?)");
            ps.setString(1,strDate);
            ps.setString(2,"Team3");
            ps.setString(3,"Team2");
            ps.setString(4,"just now");
            ps.executeUpdate();

            ps=con.prepareStatement("DELETE FROM GameEvent WHERE Date=(?) AND AwayTeam=(?) AND homeTeam=(?) AND Description=(?)");
            ps.setString(1,strDate);
            ps.setString(2,"Team3");
            ps.setString(3,"Team2");
            ps.setString(4,"test1");
            ps.executeUpdate();

            ps=con.prepareStatement("DELETE FROM Match WHERE Date=(?) AND AwayTeam=(?) AND homeTeam=(?)");
            ps.setString(1,"15/05/2020");
            ps.setString(2,"Team2");
            ps.setString(3,"Team1");
            ps.executeUpdate();


            ps=con.prepareStatement("DELETE FROM Match WHERE Date=(?) AND AwayTeam=(?) AND homeTeam=(?)");
            ps.setString(1,strDate);
            ps.setString(2,"Team3");
            ps.setString(3,"Team2");
            ps.executeUpdate();

            ps=con.prepareStatement("DELETE FROM Referee WHERE UserName=(?)");
            ps.setString(1,"RefereeTest1");
            ps.executeUpdate();
            ps=con.prepareStatement("DELETE FROM Referee WHERE UserName=(?)");
            ps.setString(1,"RefereeTest2");
            ps.executeUpdate();
            ps=con.prepareStatement("DELETE FROM Account WHERE UserName=(?)");
            ps.setString(1,"RefereeTest1");
            ps.executeUpdate();
            ps=con.prepareStatement("DELETE FROM Account WHERE UserName=(?)");
            ps.setString(1,"RefereeTest2");
            ps.executeUpdate();


            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
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
    public void addEventToDatabase(List<String> event) {
        try {
            PreparedStatement ps=con.prepareStatement("insert into GameEvent values(?,?,?,?,?,?,?) ");
            ps.setString(1,event.get(0));
            ps.setString(2,event.get(1));
            ps.setString(3,event.get(2));
            ps.setString(4,event.get(3));
            ps.setString(5,event.get(4));
            ps.setString(6,event.get(5));
            ps.setString(7,event.get(6));
            ps.executeUpdate();

        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }
    public void addMatchToDatabase(List<String> match) {
        try {
            PreparedStatement ps=con.prepareStatement("insert into Match values(?,?,?,?,?,?,?,?,?,?,?) ");
            ps.setString(1,match.get(0));
            ps.setString(2,match.get(1));
            ps.setString(3,match.get(2));
            ps.setString(4,match.get(3));
            ps.setString(5,match.get(4));
            ps.setString(6,match.get(5));
            ps.setString(7,match.get(6));
            ps.setString(8,match.get(7));
            ps.setString(9,match.get(8));
            ps.setString(10,match.get(9));
            ps.setString(11,match.get(10));
            ps.executeUpdate();

        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public GameEvent convertStringToGameEvent(String gameEvent){
        String [] splitArr=gameEvent.split(",");
        String eventType=splitArr[0].substring(new String("EventType: ").length());
        String description=splitArr[1].substring(new String(" Description: ").length());
        String minute=splitArr[2].substring(new String(" Minute: ").length());
        GameEvent event=new GameEvent(EventEnum.redCard,new Date(),new Time(System.currentTimeMillis()),description,Integer.parseInt(minute),null);
        return event;
    }


}