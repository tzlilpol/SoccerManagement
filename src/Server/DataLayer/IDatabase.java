package Server.DataLayer;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public interface IDatabase {

    public Connection connectToDB();

    public void addAccount(String userName, String password, String name, int age);

    public List<String> getNames(String Object);

    public List<String> getUserNames(String Object) ;


    public void addOwnerRole(String userName, String name, String teamName, String appointedUserName) ;

    public void addPlayerRole(String userName, String name, Date birthday, String position, String teamName, int pageID) ;

    public void addFanRole(String userName, String name, boolean trackPersonalPages, boolean getMatchNotifications, List<Integer> pagesIDs) ;

    public void addRefereeRole(String userName, String name, String training, HashMap<String, String> refLeaguesAndSeasons) ;

    public void addTeamManagerRole(String userName, String name, String teamName, String appointerUserName, List<String> permissions) ;

    public void addSystemManagerRole(String userName, String name, HashMap<String, String> complaintAndComments) ;

    public void addAssociationRepresentativeRole(String userName, String name) ;

    public void addCoachRole(String userName, String name, String training,String teamRole, int pageID, String teamName) ;

    public void addLeague(String name, List<String> seasonList, List<String[]> policyList) ;

    public void addSeason(String name, List<String> leagueList, List<String[]> policyList) ;

    public void addStadium(String name) ;

    public void addTeam(String name, int pageID, String leagueName, String stadiumName,int points) ;

    public void addMatch(String date, Time time, int awayScore, int homeScore, String awayTeamName, String homeTeamName,
                         String mainRefUN, String lineRefUN1, String lineRefUN2, String stadiumName,String seasonName) ;

    public void addAlert(String userName, List<String> alerts) ;

    public void addPage(int pageID);

    public void addRefsToMatch(String date, Time time, String awayTeamName, String homeTeamName, String username1,String username2, String username3) ;

    public void addGameEvent(String eventType, Time hour, String description, int gameMinute, String date, String awayTeamName, String homeTeamName) ;

    public  void increaseMatchScore(String teamScored, String date, String awayTeamName, String homeTeamName1) ;

    public String[] getUserNamePasswordDC(String userName) ;

    public List<String> getAccountRoles(String userName) ;

    public void setTeamToOwner(String username, String teamname) ;

    public void approveTeam(String ownerUsername, String ARusername, String teamName) ;

    public void addOpenTeamRequest(String username, String teamName) ;

    public void removeOpenTeamRequest(String username, String teamName) ;

    public String checkIfRequestExists(String username, String teamName) ;

    public void setAccountLogIn(String username,String isLoggedIn) ;

    public List<String> getAlertsForAccount(String username) ;

    public void clearAlertsForAccount(String username) ;

    public void addAlertToAccount(String userName, String notification) ;

    public String isAccountloggedIn(String userName) ;

    public void addRefereeToLeague(String referee, String league, String season) ;

    public int getNewPageCounter() ;

    public void addGameSchedualPolicy(String name) ;

    public void addPointCalcPolicy(String name) ;

    public void updateLeaguePolicy(String season,String league, String policyType,String policyValue) ;

    public int getPolicyCounter() ;

    public List<String> getGameSchedualPolicies(String policy) ;
    public void removeAlertsFromAccount(String username) ;
    public Time getMatchTime(String awayTeam,String homeTeam,String date);
    public List<String> getMatch(String awayTeam,String homeTeam,String date);
    public String getMainRefereeInMatch(String awayTeam,String homeTeam,String date);

    public void updateGameEvent(String event,String description,String awayTeam,String homeTeam,String date) ;
    public List<String> getGameEvents(String event,String minute,String description);
    public List<String> getAllMatches();

    public List<String> getGameEventsByMatch(String homeTeam,String awayTeam,String date);

    public List<String> getNotifiedFans();

    public void setGameNotificationSubscribtion(String username, String assignment) ;

    public void addMatches(List<List<String>> matches) ;

    public String getGameSchedulePolicy(String leagueName, String seasonName) ;

    public List<List<String>> getTeamsInLeague(String leagueName) ;

    public List<String> getRefereesInLeague(String leagueName, String seasonName) ;

    public void deleteTestTeam() ;

    public void addTestMatch(String date, String time, String awayScore, String homeScore, String awayTeamName, String homeTeamName,
                             String mainRefUN, String lineRefUN1, String lineRefUN2, String stadiumName,String seasonName);
    public void deleteTestMatch(String date,String awayTeamName, String homeTeamName);
    public void deleteTestEvent(String date,String awayTeamName, String homeTeamName,String Description);


}
