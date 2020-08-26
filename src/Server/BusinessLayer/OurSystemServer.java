package Server.BusinessLayer;

import Server.BusinessLayer.DataController;
import Server.BusinessLayer.Logger.Logger;
import Server.BusinessLayer.OtherCrudOperations.*;
import Server.BusinessLayer.Pages.Page;
import Server.BusinessLayer.RoleCrudOperations.*;
import Server.Server;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OurSystemServer {

    public void Initialize() {
        Server.getInstance().start();
        System.out.println("Established connection to Accounty System");
        System.out.println("Established connection to Federal Tax System");



        File checkFile=new File("firstInitCheck");
        if(!checkFile.exists()){
            File[] loggers={new File("event log"), new File("error log")};
            for(File file:loggers){
                if(file.exists())
                    file.delete();
            }
            try { checkFile.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
            Account SMaccount=new Account("SM1",99,"SM1X","Password");
            SystemManager SM=new SystemManager(SMaccount.getName());
            SMaccount.addRole(SM);
            DataController.getInstance().addAccountDC(SMaccount);
            (Logger.getInstance()).writeNewLine("System has been initialized");
            try { InitDatabase(); } catch (Exception e) { e.printStackTrace(); }
        }

    }

    private void InitDatabase() throws Exception {
        System.out.println("Initializing database");
        Account arAccount1, arAccount2;
        AssociationRepresentative ar1, ar2;
        League league1, league2;
        Season season;
        Policy policy1, policy2;
        Account refAccount1, refAccount2, refAccount3, refAccount4, refAccount5, refAccount6;
        Referee referee1, referee2, referee3, referee4, referee5, referee6;
        SLsettings sLsettings1, sLsettings2;
        Account ownerAccount1, ownerAccount2, ownerAccount3, ownerAccount4;
        Owner owner1, owner2, owner3, owner4;
        Account tmAccount1, tmAccount2, tmAccount3, tmAccount4;
        TeamManager tm1, tm2, tm3, tm4;
        Account playerAccount1, playerAccount2, playerAccount3, playerAccount4, playerAccount5, playerAccount6, playerAccount7, playerAccount8;
        Player player1, player2, player3, player4, player5, player6, player7, player8;
        Match match1, match2;
        Team team1, team2, team3, team4;
        Stadium stadium1, stadium2, stadium3, stadium4;
        Account coachAccount1, coachAccount2, coachAccount3, coachAccount4;
        Coach coach1, coach2, coach3, coach4;
        Page playerPage1, playerPage2, playerPage3, playerPage4, playerPage5, playerPage6, playerPage7, playerPage8,
                coachPage1, coachPage2, coachPage3, coachPage4, teamPage1, teamPage2, teamPage3, teamPage4;
        Account fanAccount1, fanAccount2, fanAccount3, fanAccount4, fanAccount5, fanAccount6, fanAccount7, fanAccount8;
        Fan fan1, fan2, fan3, fan4, fan5, fan6, fan7, fan8;

        //region AssociationRepresantives creations
        arAccount1 = new Account("AR1", 99, "AR1X", "AR1X");
        ar1 = new AssociationRepresentative("AR1");
        arAccount1.addRole(ar1);

        arAccount2 = new Account("AR2", 99, "AR2X", "AR2X");
        ar2 = new AssociationRepresentative("AR2");
        arAccount2.addRole(ar2);
        //endregion

        //region Referees creation
        refAccount1 = new Account("Referee1", 99, "Referee1X", "Referee1X");
        referee1 = ar1.createNewReferee(refAccount1, "Complete", "Referee1");

        refAccount2 = new Account("Referee2", 99, "Referee2X", "Referee2X");
        referee2 = ar1.createNewReferee(refAccount2, "Complete", "Referee2");

        refAccount3 = new Account("Referee3", 99, "Referee3X", "Referee3X");
        referee3 = ar1.createNewReferee(refAccount3, "Complete", "Referee3");

        refAccount4 = new Account("Referee4", 99, "Referee4X", "Referee4X");
        referee4 = ar1.createNewReferee(refAccount4, "Complete", "Referee4");

        refAccount5 = new Account("Referee5", 99, "Referee5X", "Referee5X");
        referee5 = ar1.createNewReferee(refAccount5, "Complete", "Referee5");

        refAccount6 = new Account("Referee6", 99, "Referee6X", "Referee6X");
        referee6 = ar1.createNewReferee(refAccount6, "Complete", "Referee6");

        //endregion

        //region Leagues and seasons creation
        league1 = new League("League1");
        league2 = new League("League2");
        season = new Season("Season");
        policy1 = new Policy("", "");
        policy2 = new Policy("", "");
        sLsettings1 = new SLsettings(policy1);
        sLsettings1.addReferee(referee1);
        sLsettings1.addReferee(referee2);
        sLsettings1.addReferee(referee3);
        sLsettings2 = new SLsettings(policy2);
        sLsettings2.addReferee(referee4);
        sLsettings2.addReferee(referee5);
        sLsettings2.addReferee(referee6);
        league1.addSLsettingsToSeason(season, sLsettings1);
        league2.addSLsettingsToSeason(season, sLsettings2);
        season.addSLsettingsToLeague(league1, sLsettings1);
        season.addSLsettingsToLeague(league2, sLsettings2);
        referee1.setsLsettings(sLsettings1);
        referee2.setsLsettings(sLsettings1);
        referee3.setsLsettings(sLsettings1);
        referee4.setsLsettings(sLsettings2);
        referee5.setsLsettings(sLsettings2);
        referee6.setsLsettings(sLsettings2);
        referee1.addLeague(league1, season);
        referee2.addLeague(league1, season);
        referee3.addLeague(league1, season);
        referee4.addLeague(league2, season);
        referee5.addLeague(league2, season);
        referee6.addLeague(league2, season);
        policy1.setsLsettings(sLsettings1);
        policy2.setsLsettings(sLsettings2);

        //endregion  //

        //region Stadium creation
        stadium1 = new Stadium("Stadium1");
        stadium2 = new Stadium("Stadium2");
        stadium3 = new Stadium("Stadium3");
        stadium4 = new Stadium("Stadium4");
        //endregion

        //region Teams creation
        team1 = new Team("Team1", league1, stadium1);
        team2 = new Team("Team2", league1, stadium2);
        team3 = new Team("Team3", league2, stadium3);
        team4 = new Team("Team4", league2, stadium4);
        //endregion

        //region Owner creation
        ownerAccount1 = new Account("Owner1", 99, "Owner1X", "Owner1X");
        owner1 = new Owner("Owner1", team1, null);
        ownerAccount1.addRole(owner1);

        ownerAccount2 = new Account("Owner2", 99, "Owner2X", "Owner2X");
        owner2 = new Owner("Owner2", team2, null);
        ownerAccount2.addRole(owner2);

        ownerAccount3 = new Account("Owner3", 99, "Owner3X", "Owner3X");
        owner3 = new Owner("Owner3", team3, null);
        ownerAccount3.addRole(owner3);

        ownerAccount4 = new Account("Owner4", 99, "Owner4X", "Owner4X");
        owner4 = new Owner("Owner4", team4, null);
        ownerAccount4.addRole(owner4);
        //endregion

        //region TeamManagers creation
        tmAccount1 = new Account("TM1", 99, "TM1X", "TM1X");
        List<TeamManager.PermissionEnum> tm1Permissions = new ArrayList<>();
        tm1Permissions.add(TeamManager.PermissionEnum.managePlayers);
        tm1Permissions.add(TeamManager.PermissionEnum.manageMatches);
        owner1.appointTeamManagerToTeam(tmAccount1, tm1Permissions);
        tm1 = tmAccount1.checkIfTeamManagr();

        tmAccount2 = new Account("TM2", 99, "TM2X", "TM2X");
        List<TeamManager.PermissionEnum> tm2Permissions = new ArrayList<>();
        tm2Permissions.add(TeamManager.PermissionEnum.managePlayers);
        tm2Permissions.add(TeamManager.PermissionEnum.manageMatches);
        owner2.appointTeamManagerToTeam(tmAccount2, tm2Permissions);
        tm2 = tmAccount2.checkIfTeamManagr();

        tmAccount3 = new Account("TM3", 99, "TM3X", "TM3X");
        List<TeamManager.PermissionEnum> tm3Permissions = new ArrayList<>();
        tm3Permissions.add(TeamManager.PermissionEnum.managePlayers);
        tm3Permissions.add(TeamManager.PermissionEnum.manageMatches);
        owner3.appointTeamManagerToTeam(tmAccount3, tm3Permissions);
        tm3 = tmAccount3.checkIfTeamManagr();

        tmAccount4 = new Account("TM4", 99, "TM4X", "TM4X");
        List<TeamManager.PermissionEnum> tm4Permissions = new ArrayList<>();
        tm4Permissions.add(TeamManager.PermissionEnum.managePlayers);
        tm4Permissions.add(TeamManager.PermissionEnum.manageMatches);
        owner4.appointTeamManagerToTeam(tmAccount4, tm4Permissions);
        tm4 = tmAccount4.checkIfTeamManagr();

        //endregion

        //region Players creation
        playerAccount1 = new Account("Player1", 99, "Player1X", "Player1X");
        player1 = new Player("Player1", new Date(), PositionEnum.Striker, team1, null);
        playerAccount1.addRole(player1);

        playerAccount2 = new Account("Player2", 99, "Player2X", "Player2X");
        player2 = new Player("Player2", new Date(), PositionEnum.Striker, team1, null);
        playerAccount2.addRole(player2);

        playerAccount3 = new Account("Player3", 99, "Player3X", "Player3X");
        player3 = new Player("Player3", new Date(), PositionEnum.Striker, team2, null);
        playerAccount3.addRole(player3);

        playerAccount4 = new Account("Player4", 99, "Player4X", "Player4X");
        player4 = new Player("Player4", new Date(), PositionEnum.Striker, team2, null);
        playerAccount4.addRole(player4);

        playerAccount5 = new Account("Player5", 99, "Player5X", "Player5X");
        player5 = new Player("Player5", new Date(), PositionEnum.Striker, team3, null);
        playerAccount5.addRole(player5);

        playerAccount6 = new Account("Player6", 99, "Player6X", "Player6X");
        player6 = new Player("Player6", new Date(), PositionEnum.Striker, team3, null);
        playerAccount6.addRole(player6);

        playerAccount7 = new Account("Player7", 99, "Player7X", "Player7X");
        player7 = new Player("Player7", new Date(), PositionEnum.Striker, team4, null);
        playerAccount7.addRole(player7);

        playerAccount8 = new Account("Player8", 99, "Player8X", "Player8X");
        player8 = new Player("Player8", new Date(), PositionEnum.Striker, team4, null);
        playerAccount8.addRole(player8);
        //endregion

        //region Coach creation
        coachAccount1 = new Account("Coach1", 99, "Coach1X", "Coach1X");
        coach1 = new Coach("Coach1", "Full", "Main", null);
        coachAccount1.addRole(coach1);

        coachAccount2 = new Account("Coach2", 99, "Coach2X", "Coach2X");
        coach2 = new Coach("Coach2", "Full", "Main", null);
        coachAccount2.addRole(coach2);

        coachAccount3 = new Account("Coach3", 99, "Coach3X", "Coach3X");
        coach3 = new Coach("Coach3", "Full", "Main", null);
        coachAccount3.addRole(coach3);

        coachAccount4 = new Account("Coach4", 99, "Coach4X", "Coach4X");
        coach4 = new Coach("Coach4", "Full", "Main", null);
        coachAccount4.addRole(coach4);
        //endregion

        //region Owners, TeamManagers and Coaches teams setting
        owner1.setTeam(team1);
        owner2.setTeam(team2);
        owner3.setTeam(team3);
        owner4.setTeam(team4);
        tm1.setTeam(team1);
        tm2.setTeam(team2);
        tm3.setTeam(team3);
        tm4.setTeam(team4);
        coach1.addTeam(team1);
        coach2.addTeam(team2);
        coach3.addTeam(team3);
        coach4.addTeam(team4);
        //endregion

        //region Match creation
        match1 = new Match("12/08/2009", new Time(22, 0, 0), 0, 0, stadium1, season
                , team2, team1, null, null, null);

        match2 = new Match("28/03/2008", new Time(19, 0, 0), 0, 0, stadium4, season
                , team3, team4, null, null, null);
        //endregion

        //region Referess match setting
        referee1.addMatch(match1, "main");
        referee2.addMatch(match1, "line");
        referee3.addMatch(match1, "line");
        referee4.addMatch(match2, "main");
        referee5.addMatch(match2, "line");
        referee6.addMatch(match2, "line");
        //endregion

        //region Seasons matches setting
        season.addMatch(match1);
        season.addMatch(match2);
        //endregion

        //region Players teams setting
        player1.setTeam(team1);
        player2.setTeam(team1);
        player3.setTeam(team2);
        player4.setTeam(team2);
        player5.setTeam(team3);
        player6.setTeam(team3);
        player7.setTeam(team4);
        player8.setTeam(team4);
        //endregion

        //region BusinessLayer.Pages.Page creation
        playerPage1 = new Page(player1);
        playerPage2 = new Page(player2);
        playerPage3 = new Page(player3);
        playerPage4 = new Page(player4);
        playerPage5 = new Page(player5);
        playerPage6 = new Page(player6);
        playerPage7 = new Page(player7);
        playerPage8 = new Page(player8);
        coachPage1 = new Page(coach1);
        coachPage2 = new Page(coach2);
        coachPage3 = new Page(coach3);
        coachPage4 = new Page(coach4);
        teamPage1 = new Page(team1);
        teamPage2 = new Page(team2);
        teamPage3 = new Page(team3);
        teamPage4 = new Page(team4);
        //endregion

        //region Fan creation
        fanAccount1 = new Account("Fan1", 99, "Fan1X", "Fan1X");
        fan1 = new Fan("Fan1");
        fanAccount1.addRole(fan1);

        fanAccount2 = new Account("Fan2", 99, "Fan2X", "Fan2X");
        fan2 = new Fan("Fan2");
        fanAccount2.addRole(fan2);

        fanAccount3 = new Account("Fan3", 99, "Fan3X", "Fan3X");
        fan3 = new Fan("Fan3");
        fanAccount3.addRole(fan3);

        fanAccount4 = new Account("Fan4", 99, "Fan4X", "Fan4X");
        fan4 = new Fan("Fan4");
        fanAccount4.addRole(fan4);

        fanAccount5 = new Account("Fan5", 99, "Fan5X", "Fan5X");
        fan5 = new Fan("Fan5");
        fanAccount5.addRole(fan5);

        fanAccount6 = new Account("Fan6", 99, "Fan6X", "Fan6X");
        fan6 = new Fan("Fan6");
        fanAccount6.addRole(fan6);

        fanAccount7 = new Account("Fan7", 99, "Fan7X", "Fan7X");
        fan7 = new Fan("Fan7");
        fanAccount7.addRole(fan7);

        fanAccount8 = new Account("Fan8", 99, "Fan8X", "Fan8X");
        fan8 = new Fan("Fan8");
        fanAccount8.addRole(fan8);
        //endregion

        //region Fan page setting
        fan1.addPage(playerPage1);
        fan1.addPage(playerPage2);
        fan2.addPage(coachPage3);
        fan3.addPage(coachPage4);
        fan3.addPage(teamPage3);
        fan2.addPage(playerPage3);
        fan5.addPage(teamPage4);
        fan4.addPage(coachPage1);
        fan5.addPage(playerPage8);
        fan7.addPage(playerPage2);
        fan8.addPage(playerPage2);
        fan1.addPage(teamPage4);
        //endregion

        owner1.addAlert(new Alert("bla bla bla bla"));
        owner1.addAlert(new Alert("tralalalala"));

        match1.getEventCalender().addGameEvent(new GameEvent(EventEnum.goal,new Date(),new Time(1,1,1),"Team2 scored",80,match1.getEventCalender()));


        //region Datamanager adding
        DataController.getInstance().addLeagueDC(league1);
        DataController.getInstance().addLeagueDC(league2);
        DataController.getInstance().addSeasonDC(season);
        DataController.getInstance().addStadiumDC(stadium1);
        DataController.getInstance().addStadiumDC(stadium2);
        DataController.getInstance().addStadiumDC(stadium3);
        DataController.getInstance().addStadiumDC(stadium4);
        DataController.getInstance().addTeamDC(team1);
        DataController.getInstance().addTeamDC(team2);
        DataController.getInstance().addTeamDC(team3);
        DataController.getInstance().addTeamDC(team4);
        for(Match match:team1.getMatchs()) DataController.getInstance().addMatchWithoutRefsDC(match);

        for(Match match:team2.getMatchs()) DataController.getInstance().addMatchWithoutRefsDC(match);

        for(Match match:team3.getMatchs()) DataController.getInstance().addMatchWithoutRefsDC(match);

        for(Match match:team4.getMatchs()) DataController.getInstance().addMatchWithoutRefsDC(match);




        DataController.getInstance().addAccountDC(arAccount1);
        DataController.getInstance().addAccountDC(arAccount2);
        DataController.getInstance().addAccountDC(refAccount1);
        DataController.getInstance().addAccountDC(refAccount2);
        DataController.getInstance().addAccountDC(refAccount3);
        DataController.getInstance().addRefToMatchDC(match1,referee1,referee2,referee3);
        DataController.getInstance().addAccountDC(refAccount4);
        DataController.getInstance().addAccountDC(refAccount5);
        DataController.getInstance().addAccountDC(refAccount6);
        DataController.getInstance().addRefToMatchDC(match2,referee4,referee5,referee6);
        DataController.getInstance().addAccountDC(ownerAccount1);
        DataController.getInstance().addAccountDC(ownerAccount2);
        DataController.getInstance().addAccountDC(ownerAccount3);
        DataController.getInstance().addAccountDC(ownerAccount4);
        DataController.getInstance().addAccountDC(tmAccount1);
        DataController.getInstance().addAccountDC(tmAccount2);
        DataController.getInstance().addAccountDC(tmAccount3);
        DataController.getInstance().addAccountDC(tmAccount4);
        DataController.getInstance().addAccountDC(playerAccount1);
        DataController.getInstance().addAccountDC(playerAccount2);
        DataController.getInstance().addAccountDC(playerAccount3);
        DataController.getInstance().addAccountDC(playerAccount4);
        DataController.getInstance().addAccountDC(playerAccount5);
        DataController.getInstance().addAccountDC(playerAccount6);
        DataController.getInstance().addAccountDC(playerAccount7);
        DataController.getInstance().addAccountDC(playerAccount8);
        DataController.getInstance().addAccountDC(coachAccount1);
        DataController.getInstance().addAccountDC(coachAccount2);
        DataController.getInstance().addAccountDC(coachAccount3);
        DataController.getInstance().addAccountDC(coachAccount4);
        DataController.getInstance().addAccountDC(fanAccount1);
        DataController.getInstance().addAccountDC(fanAccount2);
        DataController.getInstance().addAccountDC(fanAccount3);
        DataController.getInstance().addAccountDC(fanAccount4);
        DataController.getInstance().addAccountDC(fanAccount5);
        DataController.getInstance().addAccountDC(fanAccount6);
        DataController.getInstance().addAccountDC(fanAccount7);
        DataController.getInstance().addAccountDC(fanAccount8);

        DataController.getInstance().addGameSchedualPolicy("Once in a season");
        DataController.getInstance().addGameSchedualPolicy("Twice in a season");
        DataController.getInstance().addPointCalcPolicy("Win=2 loose=0 draw=1");

        System.out.println("Database initialized");





        //end
    }
}
