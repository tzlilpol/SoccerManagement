package Server.BusinessLayer;

import Server.BusinessLayer.OtherCrudOperations.*;
import Server.BusinessLayer.Pages.Page;
import Server.BusinessLayer.RoleCrudOperations.*;
import Server.DataLayer.DBAdapter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

public class DataController {

  private static List<Account> accounts = new ArrayList<Account>();
  private static List<Team> teams= new ArrayList<Team>();
  private static List<League> leagues = new ArrayList<League>();
  private static List<Season> seasons = new ArrayList<Season>();
  private static List<Stadium> stadiums=new ArrayList<>();

  private DBAdapter dbAdapter=new DBAdapter();

  private static DataController dataController=null;

  private DataController(){}

  public static DataController getInstance(){
    if(dataController==null)
      dataController=new DataController();
    return dataController;
  }



  //region Teams Data Methods
  public Team getTeam(int index) {
    Team aTeam = teams.get(index);
    return aTeam;
  }

  public List<Team> getTeams(){return teams;}


  public int numberOfTeams() {
    int number = teams.size();
    return number;
  }

  public boolean hasTeams() {
    boolean has = teams.size() > 0;
    return has;
  }

  public int indexOfTeam(Team aTeam) {
    int index = teams.indexOf(aTeam);
    return index;
  }

  public int minimumNumberOfTeams() {
    return 0;
  }

  public boolean addTeam(Team aTeam) {
    boolean wasAdded = true;
    if (teams.contains(aTeam)) {
      return true;
    }
    teams.add(aTeam);
    wasAdded = true;
    return wasAdded;
  }






  public boolean removeTeam(Team aTeam) {
    boolean wasRemoved = true;
    teams.remove(aTeam);
    wasRemoved = true;
    return wasRemoved;
  }

  //endregion

  //region Accounts Data Methods
  public Account getAccount(int index) {
    Account aAccount = accounts.get(index);
    return aAccount;
  }

  public List<Account> getAccounts() {
    return accounts;
  }

  public int numberOfAccounts() {
    int number = accounts.size();
    return number;
  }

  public boolean hasAccounts() {
    boolean has = accounts.size() > 0;
    return has;
  }

  public int indexOfAccount(Account aAccount) {
    int index = accounts.indexOf(aAccount);
    return index;
  }

  public int minimumNumberOfAccounts() {
    return 0;
  }

  public boolean addAccount(Account aAccount) {


    boolean wasAdded = true;
    if (accounts.contains(aAccount)) {
      return true;
    }
    accounts.add(aAccount);
    wasAdded = true;
    return wasAdded;
  }



  public boolean removeAccount(Account aAccount) {
    boolean wasRemoved = true;
    accounts.remove(aAccount);
    wasRemoved = true;
    return wasRemoved;
  }
  //endregion



  //region League Data Methods
  public League getLeague(int index) {
    League aLeague = leagues.get(index);
    return aLeague;
  }

  public List<League> getLeagues() {
    return leagues;
  }

  public int numberOfLeagues() {
    int number = leagues.size();
    return number;
  }

  public boolean hasLeagues() {
    boolean has = leagues.size() > 0;
    return has;
  }

  public int indexOfLeague(League aLeague) {
    int index = leagues.indexOf(aLeague);
    return index;
  }

  public int minimumNumberOfLeagues() {
    return 0;
  }

  public boolean addLeague(League aLeague) {
    boolean wasAdded = true;
    if (leagues.contains(aLeague)) {
      return true;
    }
    leagues.add(aLeague);
    wasAdded = true;
    return wasAdded;
  }



  public boolean removeLeague(League aLeague) {
    boolean wasRemoved = true;
    if (!leagues.contains(aLeague)) {
      return wasRemoved;
    }
    wasRemoved = true;
    leagues.remove(aLeague);
    return wasRemoved;
  }

  //endregion

  //region Seasons Data Methods
  public Season getSeason(int index) {
    Season aSeason = seasons.get(index);
    return aSeason;
  }

  public List<Season> getSeasons() {
    return seasons;
  }

  public int numberOfSeasons() {
    int number = seasons.size();
    return number;
  }

  public boolean hasSeasons() {
    boolean has = seasons.size() > 0;
    return has;
  }

  public int indexOfSeason(Season aSeason) {
    int index = seasons.indexOf(aSeason);
    return index;
  }


  public int minimumNumberOfSeasons() {
    return 0;
  }

  public boolean addSeason(Season aSeason) {
    boolean wasAdded = true;
    if (seasons.contains(aSeason)) {
      return true;
    }
    seasons.add(aSeason);
    wasAdded = true;
    return wasAdded;
  }



  public boolean removeSeason(Season aSeason) {
    boolean wasRemoved = true;
    seasons.remove(aSeason);
    wasRemoved = true;
    return wasRemoved;
  }

  //endregion

  //region Stadiums Data Methods
  public Stadium getStadium(int index) {
    return stadiums.get(index);
  }

  public List<Stadium> getStadiums() {
    return stadiums;
  }

  public int numberOfStadiums() {
    return stadiums.size();
  }

  public boolean hasStadiums() {
    return stadiums.isEmpty();
  }

  public int indexOfStadium(Team aTeam) {
    return stadiums.indexOf(aTeam);
  }

  public int minimumNumberOfStadiums() {
    return 0;
  }

  public boolean addStadium(Stadium aStadium) {
    boolean wasAdded = true;
    if (stadiums.contains(aStadium)) {
      return true;
    }
    stadiums.add(aStadium);
    wasAdded = true;
    return wasAdded;
  }



  public boolean removeStadium(Stadium aStadium) {
    boolean wasRemoved = true;
    stadiums.remove(aStadium);
    wasRemoved = true;
    return wasRemoved;
    //endregion
  }
  //endregion

  //region Get Roles From Accounts
  public List<Player> getPlayersFromAccounts(){
    List<Player> players=new LinkedList<>();
    for(Account account: DataController.getInstance().getInstance().getAccounts()){
      for(Role role:account.getRoles()){
        if(role instanceof Player){
          players.add((Player)role);
          break;
        }
      }
    }
    return players;
  }

  public List<Coach> getCoachesFromAccounts(){
    List<Coach> coaches=new LinkedList<>();
    for(Account account: DataController.getInstance().getAccounts()){
      for(Role role:account.getRoles()){
        if(role instanceof Coach){
          coaches.add((Coach)role);
          break;
        }
      }
    }
    return coaches;
  }

  public List<TeamManager> getTeamManagersFromAccounts(){
    List<TeamManager> tms=new LinkedList<>();
    for(Account account: DataController.getInstance().getAccounts()){
      for(Role role:account.getRoles()){
        if(role instanceof TeamManager){
          tms.add((TeamManager) role);
          break;
        }
      }
    }
    return tms;
  }

  public List<Owner> getOwnersFromAccounts(){
    List<Owner> owners=new LinkedList<>();
    for(Account account: DataController.getInstance().getAccounts()){
      for(Role role:account.getRoles()){
        if(role instanceof Owner){
          owners.add((Owner) role);
          break;
        }
      }
    }
    return owners;
  }

  public List<Referee> getRefereesFromAccounts(){
    List<Referee> refs=new LinkedList<>();
    for(Account account: DataController.getInstance().getAccounts()){
      for(Role role:account.getRoles()){
        if(role instanceof Referee){
          refs.add((Referee) role);
          break;
        }
      }
    }
    return refs;
  }

  public List<Fan> getFansFromAccounts(){
    List<Fan> fans=new LinkedList<>();
    List<Account> accounts= DataController.getInstance().getAccounts();
    if(accounts.size()==0) return null;
    for(Account account: DataController.getInstance().getAccounts()){
      for(Role role:account.getRoles()){
        if(role instanceof Fan){
          fans.add((Fan) role);
          break;
        }
      }
    }
    return fans;
  }

  public List<AssociationRepresentative> getAssiciationRepresentivesFromAccounts(){
    List<AssociationRepresentative> assiciationRepresentives=new LinkedList<>();
    List<Account> accounts= DataController.getInstance().getAccounts();
    if(accounts.size()==0) return null;
    for(Account account: DataController.getInstance().getAccounts()){
      for(Role role:account.getRoles()){
        if(role instanceof AssociationRepresentative){
          assiciationRepresentives.add((AssociationRepresentative) role);
          break;
        }
      }
    }
    return assiciationRepresentives;
  }

  public List<SystemManager> getSystemManagersFromAccounts() {
    List<SystemManager> systemManagers=new LinkedList<>();
    List<Account> accounts= DataController.getInstance().getAccounts();
    if(accounts.size()==0) return null;
    for(Account account: DataController.getInstance().getAccounts()){
      for(Role role:account.getRoles()){
        if(role instanceof SystemManager){
          systemManagers.add((SystemManager) role);
          break;
        }
      }
    }
    return systemManagers;
  }
  //endregion


  public void setAccounts(List<Account> accounts) {
    DataController.getInstance().accounts = accounts;
  }

  public void setTeams(List<Team> teams) {
    DataController.getInstance().teams = teams;
  }

  public void setLeagues(List<League> leagues) {
    DataController.getInstance().leagues = leagues;
  }

  public void setSeasons(List<Season> seasons) {
    DataController.getInstance().seasons = seasons;
  }

  public void setStadiums(List<Stadium> stadiums) {
    DataController.getInstance().stadiums = stadiums;
  }

  public void saveAction(Team team) {
  }

  public void notifyOnDelete(Team team) {

  }

  /**
   *
   */

  /**
   * create new referee
   *
   * @param training
   * @param name
   */
  public Referee CreateNewReferee(String training, String name) {
    Referee referee = new Referee(training, name);
    sendInvitation(referee);
    return referee;

  }

  /**
   * send invitation to the referee
   *
   * @param referee
   */
  public void sendInvitation(Referee referee) {

  }


  /**
   * delete team's page from all fans of the team (follower)
   *
   * @param team
   */
  public void deleteFromAllFollowers(Team team) {
    Page teamPageToDelete = team.getPage();
    List<Fan> fans = teamPageToDelete.getFans();
    for (Fan fan : fans) {
      for (Page page : fan.getPages()) {
        if (page.equals(teamPageToDelete)) {
          page.delete();
          break;
        }
      }
    }
  }

  public void clearDataBase(){
    teams= new ArrayList<Team>();
    accounts = new ArrayList<Account>();
    leagues = new ArrayList<League>();
    seasons = new ArrayList<Season>();
    stadiums=new ArrayList<>();
  }

  public Account getAccountByRole(Role role){
    for(Account account:accounts){
      for(Role currRole:account.getRoles()){
        if(role.equals(currRole))
          return account;
      }
    }
    return null;
  }




  /*--------------------------------------------actual database--------------------------------------------------------*/

  public Account getAccountRolesDC(Account account) {
    List<String> roles=dbAdapter.getAccountRoles(account.getUserName());
    for(String role:roles){
      if(role.equals("Owner"))
        account.addRole(new Owner(account.getName()));
      else if(role.equals("TeamManager"))
        account.addRole(new TeamManager(account.getName()));
      else if(role.equals("AssociationRepresentative"))
        account.addRole(new AssociationRepresentative(account.getName()));
      else if(role.equals("SystemManager"))
        account.addRole(new SystemManager(account.getName()));
      else if(role.equals("Player"))
        account.addRole(new Player(account.getName()));
      else if(role.equals("Referee"))
        account.addRole(new Referee(account.getName()));
      else if(role.equals("Coach"))
        account.addRole(new Coach(account.getName()));
      else if(role.equals("Fan"))
        account.addRole(new Fan(account.getName()));
    }
    return account;
  }

  public String[] getUserNamePasswordDC(String userName) {
    return dbAdapter.getUserNamePasswordDC(userName);
  }

  public void addSeasonDC(Season season){
    List<String> leagueList=new ArrayList<>();
    List<String[]> policyList=new ArrayList<>();
    for(Map.Entry<League,SLsettings> entry:season.getsLsettings().entrySet()){
      leagueList.add(entry.getKey().getName());
      Policy policy=entry.getValue().getPolicy();
      String[] arr={policy.getPointCalc(),policy.getGameSchedual(),policy.getId()};
      policyList.add(arr);
    }
    dbAdapter.addSeason(season.getName(),leagueList,policyList);
  }

  public void addLeagueDC(League league){
    List<String> seasonList=new ArrayList<>();
    List<String[]> policyList=new ArrayList<>();
    for(Map.Entry<Season,SLsettings> entry:league.getsLsetting().entrySet()){
      seasonList.add(entry.getKey().getName());
      Policy policy=entry.getValue().getPolicy();
      String[] arr={policy.getPointCalc(),policy.getGameSchedual(),policy.getId()};
      policyList.add(arr);
    }
    dbAdapter.addLeague(league.getName(),seasonList,policyList);
  }

  public void addStadiumDC(Stadium stadium){
    dbAdapter.addStadium(stadium.getName());
  }

  public List<String> getNames(String Object) {
    if(Object.equals("EventEnum")){
      List<String> list=new ArrayList<>();
      for(EventEnum EE:EventEnum.values())
        list.add(EE.toString());
      return list;
    }
    return dbAdapter.getNames(Object);
  }

  public void addTeamDC(Team team){
    if(team.getPage()!=null)
    {
      dbAdapter.addPage(team.getPage().getPageID());
      dbAdapter.addTeam(team.getName(),team.getPage().getPageID(),team.getLeague().getName(),team.getStadium().getName(),team.getPoints());
    }
    else
      dbAdapter.addTeam(team.getName(),-1,team.getLeague().getName(),team.getStadium().getName(),team.getPoints());
  }

  public void addMatchDC(Match match){
    dbAdapter.addMatch(match.getDate(),match.getTime(),
            match.getAwayScore(),match.getHomeScore(),match.getAwayTeam().getName(),match.getHomeTeam().getName(),
            match.getMainReferee().getUsername(),match.getLineRefereeOne().getUsername(),match.getLineRefereeTwo().getUsername(),
            match.getStadium().getName(),match.getSeason().getName());
  }

  public void addMatchWithoutRefsDC(Match match){
    dbAdapter.addMatch(match.getDate(),match.getTime(),
            match.getAwayScore(),match.getHomeScore(),match.getAwayTeam().getName(),match.getHomeTeam().getName(),
            null,null,null,
            match.getStadium().getName(),match.getSeason().getName());

    List<GameEvent> eventList=match.getEventCalender().getGameEvents();
    for(GameEvent GE:eventList)
      dbAdapter.addGameEvent(GE.getType().toString(),GE.getHour(),GE.getDescription(),GE.getGameMinute(),match.getDate(),
              match.getAwayTeam().getName(),match.getHomeTeam().getName());
  }

  public void addRefToMatchDC(Match match,Referee ref1,Referee ref2,Referee ref3){
    dbAdapter.addRefsToMatch(match.getDate(),match.getTime(),match.getAwayTeam().getName(),
            match.getHomeTeam().getName(),ref1.getUsername(),ref2.getUsername(),ref3.getUsername());
  }

  public void addAccountDC(Account account){
    List<Role> roles=account.getRoles();
    List<String> alerts=new ArrayList<>();
    dbAdapter.addAccount(account.getUserName(),hashPassword(account.getPassword()),account.getName(),account.getAge());

    for(Role role: roles){
      for(Alert alert:role.getAlertList())
        alerts.add(alert.getDescription());
      dbAdapter.addAlert(account.getUserName(),alerts);
      if(role instanceof Owner)
        dbAdapter.addOwnerRole(account.getUserName(),role.getName(),((Owner) role).getTeam().getName(),((Owner) role).getAppointer().getUsername());
      else if(role instanceof Player){
        dbAdapter.addPage(((Player) role).getPage().getPageID());
        dbAdapter.addPlayerRole(account.getUserName(),role.getName(),new Date(((Player) role).getBirthday().getYear(),((Player) role).getBirthday().getMonth(),((Player) role).getBirthday().getMonth()),
                ((Player) role).getPosition().toString(), ((Player) role).getTeam().getName(),((Player) role).getPage().getPageID());
      }
      else if(role instanceof Fan){
        List<Integer> pagesIDs=new ArrayList<>();
        for(Page page:((Fan) role).getPages())
          pagesIDs.add(new Integer(page.getPageID()));
        dbAdapter.addFanRole(account.getUserName(),role.getName(),((Fan) role).isTrackPersonalPages(),((Fan) role).isGetMatchNotifications(),pagesIDs);
      }
      else if(role instanceof Referee){
        HashMap<String,String> refLeaguesAndSeasons=new HashMap<>();
        for(Map.Entry<League,Season> entry: ((Referee) role).getLeagues().entrySet())
          refLeaguesAndSeasons.put(entry.getKey().getName(),entry.getValue().getName());
        dbAdapter.addRefereeRole(account.getUserName(),role.getName(),((Referee) role).getTraining(),refLeaguesAndSeasons);
      }
      else if(role instanceof TeamManager){
        List<String> permissions=new ArrayList<>();
        for(TeamManager.PermissionEnum permissionEnum:((TeamManager) role).getAppointer().getAllPermitions((TeamManager) role))
          permissions.add(permissionEnum.toString());
        dbAdapter.addTeamManagerRole(account.getUserName(),role.getName(),((TeamManager) role).getTeam().getName(),
                ((TeamManager) role).getAppointer().getUsername(),permissions);
      }
      else if(role instanceof SystemManager)
        dbAdapter.addSystemManagerRole(account.getUserName(),account.getName(),((SystemManager) role).getComplaintAndComments());
      else if(role instanceof AssociationRepresentative)
        dbAdapter.addAssociationRepresentativeRole(account.getUserName(),role.getName());
      else if(role instanceof Coach){
        dbAdapter.addPage(((Coach) role).getPage().getPageID());
        dbAdapter.addCoachRole(account.getUserName(),role.getName(),((Coach) role).getTraining(),((Coach) role).getTeamRole(),
                ((Coach) role).getPage().getPageID(),((Coach) role).getTeams().get(0).getName());
      }

    }
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

  public void setTeamToOwner(Owner owner, Team team) {
    dbAdapter.setTeamToOwner(owner.getUsername(),team.getName());
  }

  public void approveTeam(Owner owner, AssociationRepresentative associationRepresentative, String teamName) {
    dbAdapter.approveTeam(owner.getUsername(),associationRepresentative.getUsername(),teamName);
  }

  public void addOpenTeamRequest(Owner owner, String teamName) {
    dbAdapter.addOpenTeamRequest(owner.getUsername(),teamName);
  }

  public void removeOpenTeamRequest(Owner owner, String teamName) {
    dbAdapter.removeOpenTeamRequest(owner.getUsername(),teamName);
  }

  public boolean checkIfRequestExists(Owner owner, String teamName) {
    String ans=dbAdapter.checkIfRequestExists(owner.getUsername(),teamName);
    if(ans.length()==0)
      return false;
    return true;
  }

  public boolean getRequestStatus(Owner owner, String teamName) {

    String ans=dbAdapter.checkIfRequestExists(owner.getUsername(),teamName);
    return ans.equals("1");
  }

  public void setAccountLogIn(Account account, boolean loggedIn) {
    dbAdapter.setAccountLogIn(account.getUserName(),String.valueOf(loggedIn));
  }

  public List<Alert> getAlertsForAccount(String username) {
    List<Alert> alerts=new ArrayList<>();
    List<String> strings=dbAdapter.getAlertsForAccount(username);
    for(String s:strings)
      alerts.add(new Alert(s));
    return alerts;
  }

  public void clearAlertsForAccount(String username) {
    dbAdapter.clearAlertsForAccount(username);
  }

  public void addAlertToAccount(String userName, String notification) {
    dbAdapter.addAlertToAccount(userName,notification);
  }

  public boolean isAccountloggedIn(String userName) {
    String ans=dbAdapter.isAccountloggedIn(userName);
    return ans.equals("1");
  }

  public List<String> getUserNames(String object) {
    return dbAdapter.getUserNames(object);
  }

  public void addRefereeToLeague(Referee referee, League league, Season season) {
    dbAdapter.addRefereeToLeague(referee.getUsername(),league.getName(),season.getName());
  }

  public int getNewPageCounter() {
    return dbAdapter.getNewPageCounter();
  }

  public void addGameSchedualPolicy(String name) {
    dbAdapter.addGameSchedualPolicy(name);
  }

  public void addPointCalcPolicy(String name) {
    dbAdapter.addPointCalcPolicy(name);
  }

  public void updateLeaguePolicy(Season season,League league, String policyType,String policyValue) {
    dbAdapter.updateLeaguePolicy(season.getName(),league.getName(),policyType,policyValue);
  }

  public int getPolicyCounter() {
    return dbAdapter.getPolicyCounter();
  }

  public List<String> getPolicies(String policy) {
    return dbAdapter.getGameSchedualPolicies(policy);
  }

  public void removeAlertsFromAccount(Account account) {
    dbAdapter.removeAlertsFromAccount(account.getUserName());
  }
  public Time getMatchTime(String awayTeam, String homeTeam, String date){
    return dbAdapter.getMatchTime(awayTeam,homeTeam,date);
  }
  public void addGameEvent(String eventType, Time hour, String description, int gameMinute, String date, String awayTeamName, String homeTeamName) {
    dbAdapter.addGameEvent(eventType,hour,description,gameMinute,date,awayTeamName,homeTeamName);
  }
  public List<String> getMatch(String awayTeam,String homeTeam,String date)
  {
    return dbAdapter.getMatch(awayTeam,homeTeam,date);
  }
  public String getMainRefereeInMatch(String awayTeam,String homeTeam,String date){
    return dbAdapter.getMainRefereeInMatch(awayTeam,homeTeam,date);
  }
  public void updateGameEvent(String event,String description,String awayTeam,String homeTeam,String date) {
    dbAdapter.updateGameEvent(event,description,awayTeam,homeTeam,date);
  }
  public List<String> getGameEvents(String event,String minute,String description)
  {
    return dbAdapter.getGameEvents(event,minute,description);
  }
  public List<String> getAllMatches()
  {
    return dbAdapter.getAllMatches();
  }
  public List<String> getGameEventsByMatch(String homeTeam,String awayTeam,String date)
  {
    return dbAdapter.getGameEventsByMatch(homeTeam,awayTeam,date);
  }
  public List<String> getNotifiedFans()
  {
    return dbAdapter.getNotifiedFans();
  }

  public void addMatches(List<List<String>> matches) {
    dbAdapter.addMatches(matches);
  }
  public String getGameSchedulePolicy(String leagueName, String seasonName) {
    return dbAdapter.getGameSchedulePolicy(leagueName,seasonName);
  }
  public List<List<String>> getTeamsInLeague(String leagueName) {
    return dbAdapter.getTeamsInLeague(leagueName);
  }

  public List<String> getRefereesInLeague(String leagueName, String seasonName) {
    return dbAdapter.getRefereesInLeague(leagueName,seasonName);
  }

  public void setGameNotificationSubscribtion(Fan fan, boolean b) {
    dbAdapter.setGameNotificationSubscribtion(fan.getUsername(),String.valueOf(b));
  }

  public void deleteTestTeam(){
    dbAdapter.deleteTestTeam();
  }

  public void addTestMatch(String date, String time, String awayScore, String homeScore, String awayTeamName, String homeTeamName,
                           String mainRefUN, String lineRefUN1, String lineRefUN2, String stadiumName, String seasonName) {
    dbAdapter.addTestMatch( date,  time,  awayScore,  homeScore, awayTeamName, homeTeamName,
            mainRefUN, lineRefUN1, lineRefUN2, stadiumName, seasonName);
  }
  public void deleteTestMatch(String date,String awayTeamName, String homeTeamName)
  {
    dbAdapter.deleteTestMatch(date,awayTeamName,homeTeamName);
  }
  public void deleteTestEvent(String date,String awayTeamName, String homeTeamName,String Description)
  {
    dbAdapter.deleteTestEvent(date,awayTeamName,homeTeamName,Description);
  }

  public void logOffAfterTests(String username){
    Account account=new Account("account",99,username,"password");
    setAccountLogIn(account,false);
    clearAlertsForAccount(username);
  }
}

