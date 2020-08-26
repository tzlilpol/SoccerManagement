package Server.BusinessLayer.RoleCrudOperations;
import Server.BusinessLayer.DataController;
import Server.BusinessLayer.Logger.Logger;
import Server.BusinessLayer.OtherCrudOperations.*;

import Server.BusinessLayer.OutsideSystems.ProxyAssociationAccountingSystem;
import Server.BusinessLayer.OutsideSystems.ProxyTaxSystem;
import javafx.util.Pair;

import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class AssociationRepresentative extends Role {

  public static HashMap<Pair<Owner,String>,Boolean> approvedTeams=new HashMap<>();


  public AssociationRepresentative(String username){setUsername(username);}

  /**
   * create new league and put teams inside
   * @param name
   * @param teams
   * @return
   */
  public String createNewLeague(String name, List<Team> teams){
    League league = new League(name);
    DataController.getInstance().addLeague(league);
    for(Team team : teams){
      league.addTeam(team);
    }
    Logger.getInstance().writeNewLine(this.getUsername()+" created new leauge: "+(league.getName() ));
    return "New leauge created successfully";
  }

  /**
   * set season to league by year
   * @param league
   * @param year
   * @return
   */
  public String setYearToLeague(League league, String year){
    Season season=new Season(year);
    DataController.getInstance().addSeason(season);
    DataController.getInstance().addLeague(league);
    SLsettings sLsettings = league.getSLsettingsBySeason(season);
    if (sLsettings==null)
    {
      sLsettings = new SLsettings(new Policy(null,null));
    }
    league.addSLsettingsToSeason(season,sLsettings);
    Logger.getInstance().writeNewLine(this.getUsername()+" set new year to league: "+(league.getName() ));
    return "Set year to league successfully";
  }

  /**
   * remove specific referee
   * @return
   */
  public String deleteReferee(Referee referee){
    SLsettings refSLsettings = referee.getsLsettings();
    for(Referee referee1 : refSLsettings.getReferees()){
      if(referee.equals(referee1)) {
        referee1.delete();
        Logger.getInstance().writeNewLine(this.getUsername()+" remove referee: "+(referee.getName() ));
        return "Remove referee successfully";
      }
    }
    Logger.getInstanceError().writeNewLineError(this.getUsername()+" delete "+(referee.getName() +" failed"));
    return "Remove referee failed";
  }

  /**
   * creates an account and adds it as a referee to the system
   * @param name the name of the user
   * @param userName the desire user name to the account
   * @param password the desire password to the account
   * @param age the age of the user
   * @param training the training of the user, as a referee
   */
  public String addNewReferee(String training, String name, int age, String userName, String password){
    Account account = new Account(name,age,userName,password);
    DataController.getInstance().addAccount(account);
    Referee referee = createNewReferee(account,training,name);
    Logger.getInstance().writeNewLine(this.getUsername()+" add referee: "+(referee.getName()));
    return "Add new referee successfully";
  }

  /**
   * create new referee and links it to a specific account
   * @param training
   * @param name
   * @param account
   */
  public Referee createNewReferee(Account account, String training, String name) {
    Referee referee = new Referee(training, name);
    account.addRole(referee);
    sendInvitation(referee);
    return referee;
  }

  /**
   * send invitation to the referee
   * @param referee
   */
  private void sendInvitation(Referee referee) {
    referee.addAlert(new Alert("Invitation to be a referee"));
  }

  /**
   * add referee to a league in specific season , add referee to SLsetting referee list
   * @param league
   * @param referee
   * @param season
   * @return
   */
  public String addRefereeToLeague(Referee referee, League league, Season season){
//    SLsettings sLsettings = league.getSLsettingsBySeason(season);
//    if (sLsettings==null)
//    {
//      sLsettings = new SLsettings(new Policy(null,null));
//    }
//    referee.setsLsettings(sLsettings);
//    sLsettings.addReferee(referee);
//    referee.addLeague(league,season);
    DataController.getInstance().addRefereeToLeague(referee,league,season);
    Logger.getInstance().writeNewLine(this.getUsername()+" add referee: " +referee.getName()+  " to league: "+(league.getName()));
    return  "Added referee to league successfully";
  }

  /**
   * set league pointCalc policy in specific season
   * @param league
   * @param season
   * @return
   */
  public  String setLeaguePointCalcPolicy(League league, Season season, String pointCalc){
//    policy.setPointCalc(pointCalc);
//    SLsettings sLsettings = league.getSLsettingsBySeason(season);
//    if(sLsettings!=null)
//      sLsettings.setPolicy(policy);
//    else{
//      sLsettings = new SLsettings(policy);
//      season.addSLsettingsToLeague(league,sLsettings);
//    }
    DataController.getInstance().updateLeaguePolicy(season,league,"PointCalc",pointCalc);
    Logger.getInstance().writeNewLine(this.getUsername()+" set League Point Calculation Policy: " +(pointCalc));
    return "Set league point calculation policy successfully";
  }

  /**
   * set league game Schedule policy in specific season
   * @param league
   * @param season
   * @return
   */
  public  String setLeagueGameSchedualPolicy(League league, Season season, String gameSchedule){
//    policy.setGameSchedual(gameSchedule);
//    SLsettings sLsettings = league.getSLsettingsBySeason(season);
//    if(sLsettings!=null)
//      sLsettings.setPolicy(policy);
//    else{
//      sLsettings = new SLsettings(policy);
//      season.addSLsettingsToLeague(league,sLsettings);
//    }
    DataController.getInstance().updateLeaguePolicy(season,league,"GameSchedual",gameSchedule);
    Logger.getInstance().writeNewLine(this.getUsername()+" set League Game schedule Policy: " +(gameSchedule)+ " to: "+ league.getName()+ " in: "+ season.getName());
    return "Set league game schedule policy successfully";
  }

  public String setNewSeason(String year){
    Season season = new Season(year);
    DataController.getInstance().addSeason(season);
    Logger.getInstance().writeNewLine(this.getUsername()+" set new season: " +(season.getName()));
    return "Set new season successfully";
  }

  public double getTaxRate(double amount) {
    return ProxyTaxSystem.getInstance().getTaxRate(amount);
  }

  public void addAmountToAssociationBudget(String teamName, String date, double amount){
    ProxyAssociationAccountingSystem.getInstance().addPayment(teamName,date,amount);
  }

  public void subtractAmountFromAssociationBudget(String teamName, String date,double amount){
    ProxyAssociationAccountingSystem.getInstance().addPayment(teamName,date,-1*amount);
  }

  /**
   * approve the opening of a team
   */
  public String approveTeam(String teamName, Owner owner)
  {
    Pair request=new Pair(owner,teamName);
    if(!checkIfRequestExists(owner,teamName)){
      Logger.getInstanceError().writeNewLineError(this.getUsername()+" approve team "+(teamName) +" failed");
      return "Request doesn't exist";
    }
    else
    {
      DataController.getInstance().approveTeam(owner,this,teamName);
      notifyAccount(owner.getUsername(),"You are approved to open team: "+teamName+" by "+getUsername());
      Logger.getInstance().writeNewLine(getUsername()+" approved "+owner.getUsername()+" to open the team: "+teamName);

      return "Team approved successfully";
    }
  }

  /**
   * add a request for opening a team
   */
  public static boolean addOpenTeamRequest(Owner owner, String teamName)
  {
    if(teamName==null || owner==null)
      return false;

    Pair request=new Pair(owner,teamName);
    if(checkIfRequestExists(owner,teamName))
      return false;
    else
      DataController.getInstance().addOpenTeamRequest(owner,teamName);
    return true;
  }

  /**
   * remove a request for opening a team
   */
  public static boolean removeOpenTeamRequest(Owner owner, String teamName)
  {
    if(teamName==null || owner==null)
      return false;

    Pair request=new Pair(owner,teamName);
    if(!checkIfRequestExists(owner,teamName))
      return false;
    else
      DataController.getInstance().removeOpenTeamRequest(owner,teamName);
    return true;
  }

  /**
   * check if a certain request for opening a team exists
   */
  public static boolean checkIfRequestExists(Owner owner, String teamName)
  {
    if(teamName==null || owner==null)
      return false;

    Pair request=new Pair(owner,teamName);
    return DataController.getInstance().checkIfRequestExists(owner,teamName);
  }

  /**
   * get the status of a pending request
   */
  public static boolean getRequestStatus(Owner owner, String teamName)
  {
    if(teamName==null || owner==null)
      return false;

    Pair request=new Pair(owner,teamName);
    return DataController.getInstance().getRequestStatus(owner,teamName);
  }

  //new!!
  public String scheduleGamesInSeason(League league, Season season){
    String gameSchedulePolicy = DataController.getInstance().getGameSchedulePolicy(league.getName(),season.getName());
    List<Team> teams = stringToTeams(DataController.getInstance().getTeamsInLeague(league.getName()));
    List<Referee> referees = stringToReferees(DataController.getInstance().getRefereesInLeague(league.getName(),season.getName()));
    List<Match> matches = new LinkedList<>();
    Random random = new Random();
    for (int i = 0; i <teams.size() ; i++) {
      Team team1 = teams.get(i);
      for (int j = i+1; j <teams.size() ; j++){
        Team team2 = teams.get(j);
        String date = getRandomDate();
        Time time = getRandomTime();
        Match match = new Match(date, time, 0, 0, team1.getStadium(), season, team2, team1,
                referees.get(random.nextInt(referees.size())), referees.get(random.nextInt(referees.size())), referees.get(random.nextInt(referees.size())));
        matches.add(match);
        if (gameSchedulePolicy.equals("Twice in a season")) {
          Match match2 = new Match(date, time, 0, 0, team2.getStadium(), season, team1, team2,
                  referees.get(random.nextInt(referees.size())), referees.get(random.nextInt(referees.size())), referees.get(random.nextInt(referees.size())));
          matches.add(match2);
        }
      }
    }
    //season.setMatchs(matches);
    List<List<String>> stringMatches = matchesToString(matches);
    DataController.getInstance().addMatches(stringMatches);
    notifyFansForNewGames(league,season);
    Logger.getInstance().writeNewLine(getUsername()+" created game schedule to season "+season.getName());
    return "Game schedule succeed";
  }

  private void notifyFansForNewGames(League league, Season season){
    List<String> usersToNotify = DataController.getInstance().getNotifiedFans();
    for(String user : usersToNotify) {
      notifyAccount(user,"New games were scheduled to league "+league.getName()+" in season "+season.getName());
    }
  }

  private List<List<String>> matchesToString(List<Match> matches) {
    List<List<String>> stringMatches = new LinkedList<>();
    for(Match match : matches){
      List<String> stringMatch = new LinkedList<>();
      stringMatch.add(match.getDate());
      stringMatch.add(String.valueOf(match.getTime()));
      stringMatch.add(String.valueOf(match.getAwayScore()));
      stringMatch.add(String.valueOf(match.getHomeScore()));
      stringMatch.add(match.getAwayTeam().getName());
      stringMatch.add(match.getHomeTeam().getName());
      stringMatch.add(match.getMainReferee().getUsername());
      stringMatch.add(match.getLineRefereeOne().getUsername());
      stringMatch.add(match.getLineRefereeTwo().getUsername());
      stringMatch.add(match.getStadium().getName());
      stringMatch.add(match.getSeason().getName());
      stringMatches.add(stringMatch);
    }
    return stringMatches;
  }

  private List<Team> stringToTeams(List<List<String>> stringTeams){
    List<Team> teams = new LinkedList<>();
    for(List<String> stringTeam : stringTeams){
      Team team = new Team(stringTeam.get(0),new League(stringTeam.get(1)),new Stadium(stringTeam.get(2)));
      teams.add(team);
    }
    return teams;
  }

  private List<Referee> stringToReferees(List<String> stringReferees){
    List<Referee> referees = new LinkedList<>();
    for(String refereeUserName : stringReferees){
      Referee referee = new Referee(refereeUserName);
      referees.add(referee);
    }
    return referees;
  }

  private Time getRandomTime() {
    Random random = new Random();
    return new Time(random.nextLong());

  }

  public static String getRandomDate() {
    LocalDate currentDate= LocalDate.now();
    LocalDate nextYear = LocalDate.of(currentDate.getYear()+1, currentDate.getMonth(),currentDate.getDayOfMonth());

    long startEpochDay = currentDate.toEpochDay();
    long endEpochDay = nextYear.toEpochDay();
    long randomDay = ThreadLocalRandom
            .current()
            .nextLong(startEpochDay, endEpochDay);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
    return (formatter.format(LocalDate.ofEpochDay(randomDay)));

  }
}