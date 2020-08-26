
import Client.Client;
import Client.ServiceLayer.GuestController.GuestController;
import Client.ServiceLayer.OurSystemClient;
import Client.ServiceLayer.RoleController.AssociationRepresentativeController;
import Client.ServiceLayer.RoleController.OwnerController;
import Client.ServiceLayer.RoleController.RefereeController;
import javafx.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class AcceptanceTest {
    private final ByteArrayOutputStream OS = new ByteArrayOutputStream();
    private final PrintStream PS = System.out;
    OurSystemClient OSC;
    long acceptableTime=2000;
    Date currDate;
    Time currTime;
    String strDate;
    String strTime;

    @Before
    public void setUp() {
        System.setOut(new PrintStream(OS));
        OSC=new OurSystemClient();
        OSC.Initialize();
        currDate=new Date();
        currTime=new Time(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        strDate = dateFormat.format(currDate);
        strTime=currTime.getHours()+":"+currTime.getMinutes()+":"+currTime.getSeconds();
    }

    @After
    public void restore() {
        System.setOut(PS);
    }

    @Test
    public void guestControllerMaking(){
        long before=System.currentTimeMillis();
        assertTrue(OurSystemClient.makeGuestController() instanceof GuestController);
        long after=System.currentTimeMillis();
        assertTrue((after-before)<acceptableTime);
    }

    @Test
    public void dropListsTest(){

        String[] teamsArr={"Team1","Team2","Team3","Team4"};
        long before=System.currentTimeMillis();
        assertTrue(checkIfListHasStrings(OurSystemClient.getDropList("Team",null,null),teamsArr));
        long after=System.currentTimeMillis();
        assertTrue((after-before)<acceptableTime);

        String[] seasonArr={"Season"};
        before=System.currentTimeMillis();
        assertTrue(checkIfListHasStrings(OurSystemClient.getDropList("Season",null,null),seasonArr));
        after=System.currentTimeMillis();
        assertTrue((after-before)<acceptableTime);

        String[] leagueArr={"League1","League2"};
        before=System.currentTimeMillis();
        assertTrue(checkIfListHasStrings(OurSystemClient.getDropList("League",null,null),leagueArr));
        after=System.currentTimeMillis();
        assertTrue((after-before)<acceptableTime);

        String[] stadiumsArr={"Stadium1","Stadium2","Stadium3","Stadium4"};
        before=System.currentTimeMillis();
        assertTrue(checkIfListHasStrings(OurSystemClient.getDropList("Stadium",null,null),stadiumsArr));
        after=System.currentTimeMillis();
        assertTrue((after-before)<acceptableTime);

        String[] evantsArr={"foul","yellowCard","redCard","goal","offside","substitutionPlayer","injury"};
        before=System.currentTimeMillis();
        assertTrue(checkIfListHasStrings(OurSystemClient.getDropList("EventEnum",null,null),evantsArr));
        after=System.currentTimeMillis();
        assertTrue((after-before)<acceptableTime);


        RefereeController RefController=(RefereeController) OurSystemClient.makeGuestController().LogIn("Referee1X","Referee1X").get(0);
        List<Object> list=new ArrayList<>();
        list.add(RefController);
        String[] matchesArr={"Teams: Team1 against Team2, Date: 12/08/2009","Teams: Team4 against Team3, Date: 28/03/2008"};
        before=System.currentTimeMillis();
        assertTrue(checkIfListHasStrings(OurSystemClient.getDropList("Match",list,null),matchesArr));
        after=System.currentTimeMillis();
        assertTrue((after-before)<acceptableTime);



        String[] gameEventsArr={"EventType: goal, Description: Team2 scored, Minute: 80"};
        List<String> arguments=new ArrayList<>();
        arguments.add("Teams: Team1 against Team2, Date: 12/08/2009");
        before=System.currentTimeMillis();
        assertTrue(checkIfListHasStrings(OurSystemClient.getDropList("GameEvent",list,arguments),gameEventsArr));
        after=System.currentTimeMillis();
        assertTrue((after-before)<acceptableTime);

        String[] refArr={"Referee1X","Referee2X","Referee3X","Referee4X","Referee5X","Referee6X"};
        before=System.currentTimeMillis();
        assertTrue(checkIfListHasStrings(OurSystemClient.getDropList("Referee",null,null),refArr));
        after=System.currentTimeMillis();
        assertTrue((after-before)<acceptableTime);

        String accounts="AR1X,AR2X,Coach1X,Coach2X,Coach3X,Coach4X,Fan1X,Fan2X,Fan3X,Fan4X,Fan5X,Fan6X,Fan7X,Fan8X,Owner1X,Owner2X,Owner3X,Owner4X,Player1X,Player2X,Player3X,Player4X,Player5X,Player6X,Player7X,Player8X,Referee1X,Referee2X,Referee3X,Referee4X,Referee5X,Referee6X,SM1X,TM1X,TM2X,TM3X,TM4X";
        String[] accountArr=accounts.split(",");
        before=System.currentTimeMillis();
        assertTrue(checkIfListHasStrings(OurSystemClient.getDropList("Account",null,null),accountArr));
        after=System.currentTimeMillis();
        assertTrue((after-before)<acceptableTime);


        String[] policyArr={"Once in a season","Twice in a season"};
        before=System.currentTimeMillis();
        assertTrue(checkIfListHasStrings(OurSystemClient.getDropList("GameSchedual",null,null),policyArr));
        after=System.currentTimeMillis();
        assertTrue((after-before)<acceptableTime);

        String[] pointArr={"Win=2 loose=0 draw=1"};
        before=System.currentTimeMillis();
        assertTrue(checkIfListHasStrings(OurSystemClient.getDropList("PointCalc",null,null),pointArr));
        after=System.currentTimeMillis();
        assertTrue((after-before)<acceptableTime);



    }

    private boolean checkIfListHasStrings(List<String> list,String[] arr){
        for(String s:arr){
            if(!list.contains(s))
                return false;
        }
        return true;
    }


    //region Guest tests
    @Test
    public void LogInTest(){
        GuestController GC=OSC.makeGuestController();
        List<Object> list=null;
        //Test for empty username field
        long before=System.currentTimeMillis();
        list=GC.LogIn("","Password");
        assertEquals("Username length is 0",list.get(0));
        long after=System.currentTimeMillis();
        assertTrue((after-before)<acceptableTime);
        //endtest

        //Test for empty password field
        before=System.currentTimeMillis();
        list=GC.LogIn("Owner1X","");
        assertEquals("Password length is 0",list.get(0));
        after=System.currentTimeMillis();
        assertTrue((after-before)<acceptableTime);
        //endtest

        //Test for both username and password empty
        before=System.currentTimeMillis();
        list=GC.LogIn("","");
        assertEquals("Username and password length is 0",list.get(0));
        after=System.currentTimeMillis();
        assertTrue((after-before)<acceptableTime);
        //endtest

        //Test for not existing username
        before=System.currentTimeMillis();
        list=GC.LogIn("Owner1XX","Pasword");
        assertEquals("Username does not exist",list.get(0));
        after=System.currentTimeMillis();
        assertTrue((after-before)<acceptableTime);
        //endtest

        //Test for wrong password
        before=System.currentTimeMillis();
        list=GC.LogIn("Owner1X","Paswordd");
        assertEquals("Wrong password",list.get(0));
        after=System.currentTimeMillis();
        assertTrue((after-before)<acceptableTime);
        //endtest

        //Test for successful logging in
        before=System.currentTimeMillis();
        list=GC.LogIn("Owner1X","Owner1X");
        assertTrue(list.get(0) instanceof OwnerController);
        after=System.currentTimeMillis();
        assertTrue((after-before)<acceptableTime);
        //endtest

        logOffAfterAcceptanceTests("Owner1X");
    }

    @Test
    public void createTeamTest() {
        GuestController GC=OSC.makeGuestController();
        List<Object> controllerList = GC.LogIn("Owner1X", "Owner1X");
        OwnerController ownerController = (OwnerController) controllerList.get(0);
        //send for approval
        long beforeTime = System.currentTimeMillis();
        assertEquals("Request sent, waiting for approval",ownerController.createTeam("Test team", "League1","Stadium1"));
        long afterTime = System.currentTimeMillis();
        long runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);

        //sent, not approved
        beforeTime = System.currentTimeMillis();
        assertEquals("waiting for approval",ownerController.createTeam("Test team", "League1","Stadium1") );
        afterTime = System.currentTimeMillis();
        runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);

        //create team

        logOffAfterAcceptanceTests("Owner1X");

        GuestController GC1=OSC.makeGuestController();
        List<Object> controllerList1 = GC1.LogIn("AR1X", "AR1X");
        AssociationRepresentativeController associationRepresentativeController = (AssociationRepresentativeController) controllerList1.get(0);
        associationRepresentativeController.approveTeam("Test team", "Owner1X");
        logOffAfterAcceptanceTests("AR1X");
        controllerList = GC.LogIn("Owner1X", "Owner1X");
        ownerController = (OwnerController) controllerList.get(0);

        beforeTime = System.currentTimeMillis();
        assertEquals("Team successfully created",ownerController.createTeam("Test team", "League1","Stadium1") );
        afterTime = System.currentTimeMillis();
        runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);


        //already exists
        beforeTime = System.currentTimeMillis();
        assertEquals("Wrong input, team already exists",ownerController.createTeam("Test team", "League1","Stadium1") );
        afterTime = System.currentTimeMillis();
        runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);

        deleteTestTeam();

        logOffAfterAcceptanceTests("Owner1X");

    }


    @Test
    public void approveTeamTest() {
        GuestController GC=OSC.makeGuestController();
        List<Object> controllerList = GC.LogIn("AR1X", "AR1X");
        AssociationRepresentativeController associationRepresentativeController = (AssociationRepresentativeController) controllerList.get(0);
        long beforeTime = System.currentTimeMillis();
        //true                                        ///////To send request for Owner Test//////////
        assertEquals("Request doesn't exist",associationRepresentativeController.approveTeam("Test", "Owner1X"));
        long afterTime = System.currentTimeMillis();
        long runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);
        //false
        assertEquals("Request doesn't exist",associationRepresentativeController.approveTeam("team1", "Owner1X") );

        logOffAfterAcceptanceTests("AR1X");
    }

    @Test
    public void addRefereeToLeagueTest() {
        GuestController GC=OSC.makeGuestController();
        List<Object> controllerList = GC.LogIn("AR1X", "AR1X");
        AssociationRepresentativeController associationRepresentativeController = (AssociationRepresentativeController) controllerList.get(0);
        long beforeTime = System.currentTimeMillis();
        //true
        assertEquals("Added referee to league successfully",associationRepresentativeController.addRefereeToLeague("referee1", "league2", "season"));
        long afterTime = System.currentTimeMillis();
        long runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);
        //false
        assertEquals("",associationRepresentativeController.addRefereeToLeague(null, "league2", "season") );
        logOffAfterAcceptanceTests("AR1X");
    }

    @Test
    public void setLeaguePointCalcPolicyTest() {
        GuestController GC=OSC.makeGuestController();
        List<Object> controllerList = GC.LogIn("AR1X", "AR1X");
        AssociationRepresentativeController associationRepresentativeController = (AssociationRepresentativeController) controllerList.get(0);
        long beforeTime = System.currentTimeMillis();
        //true
        assertEquals("Set league point calculation policy successfully", associationRepresentativeController.setLeaguePointCalcPolicy("league1", "season", "Test"));
        long afterTime = System.currentTimeMillis();
        long runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);
        //false
        assertEquals("", associationRepresentativeController.setLeaguePointCalcPolicy(null, "season", "Test"));

        logOffAfterAcceptanceTests("AR1X");
    }


    @Test
    public void setLeagueGameSchedulePolicyTest() {
        GuestController GC=OSC.makeGuestController();
        List<Object> controllerList = GC.LogIn("AR1X", "AR1X");
        AssociationRepresentativeController associationRepresentativeController = (AssociationRepresentativeController) controllerList.get(0);
        long beforeTime = System.currentTimeMillis();
        //true
        assertEquals("Set league game schedule policy successfully", associationRepresentativeController.setLeagueGameSchedualPolicy("league1", "season", "Test"));
        long afterTime = System.currentTimeMillis();
        long runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);
        //false
        assertEquals("", associationRepresentativeController.setLeagueGameSchedualPolicy(null, "season", "Test"));
        logOffAfterAcceptanceTests("AR1X");
    }


    @Test
    public void scheduleGamesInSeasonTest() {
        GuestController GC=OSC.makeGuestController();
        List<Object> controllerList = GC.LogIn("AR1X", "AR1X");
        AssociationRepresentativeController associationRepresentativeController = (AssociationRepresentativeController) controllerList.get(0);
        long beforeTime = System.currentTimeMillis();
        //true
        assertEquals("Set league game schedule policy successfully",associationRepresentativeController.setLeagueGameSchedualPolicy("league1", "season","Once in a season"));
        long afterTime = System.currentTimeMillis();
        long runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);
        //false
        assertEquals("",associationRepresentativeController.setLeagueGameSchedualPolicy(null, "season","Once in a season") );

        logOffAfterAcceptanceTests("AR1X");
    }


    private void deleteTestTeam(){
        String sendToServer="Data@"+ Client.getUserName();
        List<String> parameters=new ArrayList<>();
        Client.connectToServer(new Pair<>(sendToServer,new Pair<>("deleteTestTeam",parameters)));
    }

    //region Referee tests
    @Test
    public void refereeLogInTest(){
        GuestController GC=OSC.makeGuestController();
        List<Object> list=null;
        //Test for empty username field
        list=GC.LogIn("","Password");
        assertEquals("Username length is 0",list.get(0));
        //endtest

        //Test for empty password field
        list=GC.LogIn("Referee1X","");
        assertEquals("Password length is 0",list.get(0));
        //endtest

        //Test for both username and password empty
        list=GC.LogIn("","");
        assertEquals("Username and password length is 0",list.get(0));
        //endtest

        //Test for not existing username
        list=GC.LogIn("Referee1XX","Pasword");
        assertEquals("Username does not exist",list.get(0));
        //endtest

        //Test for wrong password
        list=GC.LogIn("Referee1X","Paswordd");
        assertEquals("Wrong password",list.get(0));
        //endtest

        //Test for successful logging in
        list=GC.LogIn("Referee1X","Referee1X");
        assertTrue(list.get(0) instanceof RefereeController);
        //endtest

        logOffAfterAcceptanceTests("Referee1X");
    }

    @Test
    public void reportGameTest() {
        GuestController GC=OSC.makeGuestController();
        List<Object> controllerList = GC.LogIn("Referee1X", "Referee1X");
        RefereeController refereeController= (RefereeController) controllerList.get(0);
        //check empty string
        long beforeTime = System.currentTimeMillis();
        assertEquals("Please select match",refereeController.gameReport(""));
        long afterTime = System.currentTimeMillis();
        long runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);

        //check string report
        beforeTime = System.currentTimeMillis();
        assertEquals("\tMatch Location:  Stadium1           Date:  12/08/2009           Start Time:  22:00:00\n"+
                "\tHome Team : Team1                     Score:  0\n"+
                "\tAway  Team : Team2                     Score:  1\n"+
                "\tMain Referee :  Referee1X\n"+"" +
                "\tLine Referee 1:  Referee2X         Line Referee 2:  Referee3X\n"+
                "\tGame Events:\n"+
                "\tEventType: goal, Description: Team2 scored, Minute: 80\n",refereeController.gameReport("Teams: Team1 against Team2, Date: 12/08/2009") );
        afterTime = System.currentTimeMillis();
        runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);

        logOffAfterAcceptanceTests("Referee1X");
    }


    @Test
    public void updateEventDuringMatchTest() {
        GuestController GC=OSC.makeGuestController();
        List<Object> controllerList = GC.LogIn("Referee1X", "Referee1X");
        RefereeController refereeController= (RefereeController) controllerList.get(0);
        //check empty match string
        long beforeTime = System.currentTimeMillis();
        try {
            assertEquals("Please select match",refereeController.updateEventDuringMatch("","redCard","Lionel Messi"));
        } catch (Exception e) {
        }
        long afterTime = System.currentTimeMillis();
        long runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);
        //check empty event string
        beforeTime = System.currentTimeMillis();
        try {
            assertEquals("Please select game event you wont to add",refereeController.updateEventDuringMatch("Teams: Team1 against Team2, Date: 12/08/2009","","Lionel Messi"));
        } catch (Exception e) {
        }
        afterTime = System.currentTimeMillis();
        runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);
        //check containsDigit event string
        beforeTime = System.currentTimeMillis();
        try {
            assertEquals("The event type contains invalid characters",refereeController.updateEventDuringMatch("Teams: Team1 against Team2, Date: 12/08/2009","dsfs45","Lionel Messi"));
        } catch (Exception e) {
        }
        afterTime = System.currentTimeMillis();
        runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);
        //check  description string length<5
        beforeTime = System.currentTimeMillis();
        try {
            assertEquals("Description must contain at least 5 characters",refereeController.updateEventDuringMatch("Teams: Team1 against Team2, Date: 12/08/2009","redCard","Lion"));
        } catch (Exception e) {
        }
        afterTime = System.currentTimeMillis();
        runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);
        //tried to add event not during the match
        beforeTime = System.currentTimeMillis();
        try {
            assertEquals("Referee tried to add event not during the match",refereeController.updateEventDuringMatch("Teams: Team1 against Team2, Date: 12/08/2009","redCard","Lionel Messi"));
        } catch (Exception e) {
        }
        afterTime = System.currentTimeMillis();
        runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);

        //add test game that occur now
        addTestMatch(strDate,strTime,"0","0","Team2","Team1","Referee1X","Referee1X","Referee1X","Stadium1","Season");
        beforeTime = System.currentTimeMillis();
        try {
            assertEquals("Add successful",refereeController.updateEventDuringMatch("Teams: Team1 against Team2, Date: "+strDate,"redCard","Lionel Messi"));
        } catch (Exception e) {
        }
        afterTime = System.currentTimeMillis();
        runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);
        deleteTestEvent(strDate,"Team2","Team1","Lionel Messi");
        deleteTestMatch(strDate,"Team2","Team1");

        logOffAfterAcceptanceTests("Referee1X");

    }
    @Test
    public void editEventAfterGameTest() {
        GuestController GC = OSC.makeGuestController();
        List<Object> controllerList = GC.LogIn("Referee1X", "Referee1X");
        RefereeController refereeController = (RefereeController) controllerList.get(0);
        //check empty match string
        long beforeTime = System.currentTimeMillis();
        try {
            assertEquals("Please select match", refereeController.editEventAfterGame("", "goal", "redCard","test"));
        } catch (Exception e) {
        }
        long afterTime = System.currentTimeMillis();
        long runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);
        //check empty event string
        beforeTime = System.currentTimeMillis();
        try {
            assertEquals("Please select game event you want to change", refereeController.editEventAfterGame("Teams: Team1 against Team2, Date: 12/08/2009", "", "redCard","test"));
        } catch (Exception e) {
        }
        afterTime = System.currentTimeMillis();
        runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);
        //check empty update event string
        beforeTime = System.currentTimeMillis();
        try {
            assertEquals("Please select updated game event", refereeController.editEventAfterGame("Teams: Team1 against Team2, Date: 12/08/2009", "EventType: goal, Description: Team2 scored, Minute: 80", "","test"));
        } catch (Exception e) {
        }
        afterTime = System.currentTimeMillis();
        runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);
        //check contains invalid characters update event string
        beforeTime = System.currentTimeMillis();
        try {
            assertEquals("The event type contains invalid characters", refereeController.editEventAfterGame("Teams: Team1 against Team2, Date: 12/08/2009", "EventType: goal, Description: Team2 scored, Minute: 80", "34werwrw","test"));
        } catch (Exception e) {
        }
        afterTime = System.currentTimeMillis();
        runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);
        //check Description > 5
        beforeTime = System.currentTimeMillis();
        try {
            assertEquals("Description must contain at least 5 characters", refereeController.editEventAfterGame("Teams: Team1 against Team2, Date: 12/08/2009", "EventType: goal, Description: Team2 scored, Minute: 80", "dsf","test"));
        } catch (Exception e) {
        }
        afterTime = System.currentTimeMillis();
        runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);
        //check update success

        beforeTime = System.currentTimeMillis();
        try {
            assertEquals("Update successful", refereeController.editEventAfterGame("Teams: Team1 against Team2, Date: 12/08/2009", "EventType: goal, Description: Team2 scored, Minute: 80", "redCard","testEditEvent"));
            refereeController.editEventAfterGame("Teams: Team1 against Team2, Date: 12/08/2009", "EventType: redCard, Description: testEditEvent, Minute: 80", "goal","Team2 scored");
        } catch (Exception e) {
        }
        afterTime = System.currentTimeMillis();
        runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);

        logOffAfterAcceptanceTests("Referee1X");


        GC = OSC.makeGuestController();
        List<Object> controllerList2 = GC.LogIn("Referee2X", "Referee2X");
        RefereeController refereeController2 = (RefereeController) controllerList.get(0);

        //check not main referee try to update

        beforeTime = System.currentTimeMillis();
        try {
            assertEquals("Referee is not a main referee", refereeController2.editEventAfterGame("Teams: Team1 against Team2, Date: 12/08/2009", "EventType: goal, Description: Team2 scored, Minute: 80", "redCard","testEditEvent"));
        } catch (Exception e) {
        }
        afterTime = System.currentTimeMillis();
        runTime = afterTime - beforeTime;
        assertTrue(runTime < acceptableTime);


        logOffAfterAcceptanceTests("Referee2X");

    }



    private void addTestMatch(String date, String time, String awayScore, String homeScore, String awayTeamName, String homeTeamName,
                              String mainRefUN, String lineRefUN1, String lineRefUN2, String stadiumName,String seasonName)
    {
        String sendToServer="Data@"+ Client.getUserName();
        List<String> parameters=new ArrayList<>();
        parameters.add(date);
        parameters.add(time);
        parameters.add(awayScore);
        parameters.add(homeScore);
        parameters.add(awayTeamName);
        parameters.add(homeTeamName);
        parameters.add(mainRefUN);
        parameters.add(lineRefUN1);
        parameters.add(lineRefUN2);
        parameters.add(stadiumName);
        parameters.add(seasonName);
        Client.connectToServer(new Pair<>(sendToServer,new Pair<>("addTestMatch",parameters)));
    }
    private void deleteTestMatch(String date,String awayTeamName, String homeTeamName)
    {
        String sendToServer="Data@"+ Client.getUserName();
        List<String> parameters=new ArrayList<>();
        parameters.add(date);
        parameters.add(awayTeamName);
        parameters.add(homeTeamName);
        Client.connectToServer(new Pair<>(sendToServer,new Pair<>("deleteTestMatch",parameters)));
    }
    private void deleteTestEvent(String date,String awayTeamName, String homeTeamName,String Description)
    {
        String sendToServer="Data@"+ Client.getUserName();
        List<String> parameters=new ArrayList<>();
        parameters.add(date);
        parameters.add(awayTeamName);
        parameters.add(homeTeamName);
        parameters.add(Description);
        Client.connectToServer(new Pair<>(sendToServer,new Pair<>("deleteTestEvent",parameters)));
    }

    private void logOffAfterAcceptanceTests(String username){
        String sendToServer="Data@"+ Client.getUserName();
        List<String> parameters=new ArrayList<>();
        parameters.add(username);
        Client.connectToServer(new Pair<>(sendToServer,new Pair<>("logOffAfterTests",parameters)));
    }





}