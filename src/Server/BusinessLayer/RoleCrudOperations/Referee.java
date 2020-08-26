package Server.BusinessLayer.RoleCrudOperations;
import Server.BusinessLayer.DataController;
import Server.BusinessLayer.Logger.Logger;
import Server.BusinessLayer.OtherCrudOperations.*;
import Server.Server;

import java.io.Serializable;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Referee extends Role implements Serializable
{

  private String training;
  private HashMap<League, Season> leagues;
  private List<Match> matchs;
  private SLsettings sLsettings;

  public Referee(String username){setUsername(username);}



  public Referee(String aTraining, String aName)
  {
    super(aName);
    training = aTraining;
    //leagues = new ArrayList<BusinessLayer.OtherCrudOperations.League>();
    leagues = new HashMap<>();
    matchs = new ArrayList<Match>();
  }

  public SLsettings getsLsettings() {
    return sLsettings;
  }

  public void setsLsettings(SLsettings sLsettings) {
    this.sLsettings = sLsettings;
  }

  public boolean setTraining(String aTraining)
  {
    boolean wasSet = false;
    training = aTraining;
    wasSet = true;
    return wasSet;
  }

  public String getTraining()
  {
    return training;
  }

  public void setMatchs(List<Match> matchs) {
    this.matchs = matchs;
  }

  public List<Match> getMatchs() {
    return matchs;
  }

  public HashMap<League, Season> getLeagues() {
    return leagues;
  }

  public void setLeagues(HashMap<League, Season> leagues) {
    this.leagues = leagues;
  }

  public static int minimumNumberOfLeagues()
  {
    return 0;
  }

  public boolean addLeague(League aLeague,Season aSeason)
  {
    leagues.put(aLeague,aSeason);
    if(!aSeason.hasLeague(aLeague)){
      aSeason.addSLsettingsToLeague(aLeague, sLsettings);
    }
    return true;
  }

  /**
   * remove the policy of the season
   */
  public boolean removeLeague(League league, Season aSeason)
  {
    if (!leagues.containsKey(aSeason)) {
      return true;
    }
    leagues.remove(aSeason);
    return true;
  }

  public static int minimumNumberOfMatchs()
  {
    return 0;
  }

  /**
   * add match to referee, if the match is full return false
   * @param aMatch
   * @param mainORline
   * @return
   */
  public boolean addMatch(Match aMatch, String mainORline)
  {
    boolean wasAdded = false;
    if (matchs.contains(aMatch)) { return false; }

    if(mainORline.equalsIgnoreCase("main"))
    {
      if(aMatch.getMainReferee()==null) {
        aMatch.setMainReferee(this);
        wasAdded=true;
      }
      else
        return false;
    }
    else if(mainORline.equalsIgnoreCase("line"))
    {

      if(aMatch.getLineRefereeOne()==null) {
        aMatch.setLineRefereeOne(this);
        wasAdded=true;
      }
      else if(aMatch.getLineRefereeTwo()==null) {
        aMatch.setLineRefereeTwo(this);
        wasAdded=true;
      }
      else
        return false;
    }
    matchs.add(aMatch);
    return wasAdded;
  }

  /**
   * remove match from referee, remove referee from match
   * @param aMatch
   * @return
   */
  public boolean removeMatch(Match aMatch)
  {
    boolean wasRemoved = true;
    if (!matchs.contains(aMatch))
    {
      return wasRemoved;
    }

    int oldIndex = matchs.indexOf(aMatch);
    matchs.remove(oldIndex);

    if(aMatch.getMainReferee().equals(this))
      aMatch.setMainReferee(null);
    else if(aMatch.getLineRefereeOne().equals(this))
      aMatch.setLineRefereeOne(null);
    else if(aMatch.getLineRefereeTwo().equals(this))
      aMatch.setLineRefereeTwo(null);

    return wasRemoved;
  }

  public void delete()
  {
    ArrayList<Match> copyOfMatchs = new ArrayList<Match>(matchs);
    matchs.clear();
    for(Match aMatch : copyOfMatchs)
    {
      if(aMatch.getMainReferee().equals(this))
        aMatch.setMainReferee(null);
      else if(aMatch.getLineRefereeOne().equals(this))
        aMatch.setLineRefereeOne(null);
      else if(aMatch.getLineRefereeTwo().equals(this))
        aMatch.setLineRefereeTwo(null);
    }

  }
  public String toString()
  {
    return super.toString() + "["+
            "training" + ":" + getTraining()+ "]";
  }
  /*
  `UC-10.1 update details
   */
  public boolean updateDetails(String name){
    String before=super.getName();
    super.setName(name);
    Logger.getInstance().writeNewLine("Referee "+before+" update name to: "+super.getUsername());
    return true;
  }
  /*
  UC-10.2 display all matches()
   */
  public void displayAllMatches() throws Exception {
    if (!matchs.isEmpty()) {
      for (Match m:matchs
      ) {
        m.ShowMatch();
        System.out.println();
      }
      Logger.getInstance().writeNewLine("Referee "+super.getUsername()+" watch all his Matches");
    }
    else{
      (Logger.getInstanceError()).writeNewLineError("This referee don't taking part at any match");
      throw new Exception("This referee don't taking part at any match");
    }
  }
  /*
  UC-10.3 update event during match
   */
  public boolean updateEventDuringMatch(Match match, EventEnum aType, String aDescription) throws Exception {
    boolean wasUpdate=false;
    if(!DataController.getInstance().getMatch(match.getAwayTeam().getName(),match.getHomeTeam().getName(),match.getDate()).isEmpty()){
      Date currDate=new Date(System.currentTimeMillis());
      Date gameDate=new SimpleDateFormat("dd/MM/yyyy").parse(match.getDate());
      Time time =DataController.getInstance().getMatchTime(match.getAwayTeam().getName(),match.getHomeTeam().getName(),match.getDate());
      gameDate.setHours(time.getHours());
      gameDate.setMinutes(time.getMinutes());
      gameDate.setSeconds(time.getSeconds());
      if (getDateDiff(gameDate,currDate,TimeUnit.MINUTES)<90)
      {
        Time currTime=new Time(Calendar.getInstance().getTimeInMillis());
        GameEvent event=new GameEvent(aType,currDate,currTime,aDescription,(int)(getDateDiff(gameDate,currDate,TimeUnit.MINUTES)),match.getEventCalender());
        DataController.getInstance().addGameEvent(aType.toString(),currTime,aDescription,(int)(getDateDiff(gameDate,currDate,TimeUnit.MINUTES)),convertDateToString(gameDate),match.getAwayTeam().getName(),match.getHomeTeam().getName());
        List<String> fans=DataController.getInstance().getNotifiedFans();
        for (String fan:fans
        ) {
          if (DataController.getInstance().isAccountloggedIn(fan))
            notifyAccount(fan,"During Match between "
                    +match.getAwayTeam().getName()+" and "+match.getHomeTeam().getName()+" occur event : "+aType.toString()+" at minute : "+event.getGameMinute()+"\nDescription : "+aDescription);
        }
        wasUpdate=true;
        Logger.getInstance().writeNewLine("Referee "+super.getName()+" update event during the match between: "+match.getHomeTeam().getName()+","+match.getAwayTeam().getName()+" to "+event.getType());
      }
      else {
        (Logger.getInstanceError()).writeNewLineError("Referee tried to add event not during the match");
        throw new Exception("Referee tried to add event not during the match");
      }
    }
    else{
      (Logger.getInstanceError()).writeNewLineError("Referee didnt take part in this match");
      throw new Exception("Referee didnt take part in this match");
    }
    return wasUpdate;
  }
  /*
  UC - 10.4 edit game after the game end
   */
  public boolean editEventAfterGame(Match match, GameEvent gameEvent, EventEnum aType, String aDescription) throws Exception {
    boolean wasEdit = false;
    if(!DataController.getInstance().getMatch(match.getAwayTeam().getName(),match.getHomeTeam().getName(),match.getDate()).isEmpty())
    {
      if(DataController.getInstance().getMainRefereeInMatch(match.getAwayTeam().getName(),match.getHomeTeam().getName(),match.getDate()).equals(this.getUsername()))
      {
        Date currDate=new Date(System.currentTimeMillis());
        Date gameDate=new SimpleDateFormat("dd/MM/yyyy").parse(match.getDate());
        Time time =DataController.getInstance().getMatchTime(match.getAwayTeam().getName(),match.getHomeTeam().getName(),match.getDate());
        gameDate.setHours(time.getHours());
        gameDate.setMinutes(time.getMinutes());
        gameDate.setSeconds(time.getSeconds());
        if (getDateDiff(gameDate, currDate, TimeUnit.MINUTES) > 390) {
          if (!DataController.getInstance().getGameEvents(gameEvent.getType().toString(),gameEvent.getGameMinute()+"",gameEvent.getDescription()).isEmpty()) {
            DataController.getInstance().updateGameEvent(aType.toString(),aDescription,match.getAwayTeam().getName(),match.getHomeTeam().getName(),match.getDate());
            wasEdit = true;
            Logger.getInstance().writeNewLine("Referee " + super.getUsername() + " edit event after the match between: " + match.getHomeTeam().getName() + "," + match.getAwayTeam().getName() + " to " + aType);

          }
          else{
            (Logger.getInstanceError()).writeNewLineError("This match don't contain given game event");
            throw new Exception("This match don't contain given game event");
          }
        }
        else{
          (Logger.getInstanceError()).writeNewLineError("Referee can edit event only after 5 hours");
          throw new Exception("Referee can edit event only after 5 hours");
        }
      }
      else{
        (Logger.getInstanceError()).writeNewLineError("Referee is not a main referee");
        throw new Exception("Referee is not a main referee");
      }
    }
    else{
      (Logger.getInstanceError()).writeNewLineError("Referee didnt take part in this match");
      throw new Exception("Referee didnt take part in this match");
    }
    return wasEdit;
  }

  public void ShowReferee() {
    System.out.println("Name:");
    System.out.println(this.getName());
    System.out.println();
    System.out.println("Training:");
    System.out.println(this.getTraining());
    System.out.println();
    System.out.println("Matches judged:");
    for(Match match:this.getMatchs())
      match.ShowMatch();
  }


  public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
    long diffInMillies = date2.getTime() - date1.getTime();
    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
  }


  public static Date convertStringToDate( String day,String month,String year) {

    int iyear=Integer.parseInt(year);
    int imonth=Integer.parseInt(month);
    int iday=Integer.parseInt(day);
    Date date=new Date(iyear,imonth,iday);
    return date;
  }
  public static String convertDateToString(Date date)
  {
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String strDate = dateFormat.format(date);
    return strDate;
  }

  public String gameReport(Match match){
    List<String> matchList=DataController.getInstance().getMatch(match.getAwayTeam().getName(),match.getHomeTeam().getName(),match.getDate());
    String report= "\tMatch Location:  "+matchList.get(9)+"           Date:  "+matchList.get(0)+"           Start Time:  "+matchList.get(1)+"\n"+
            "\tHome Team : "+matchList.get(5)+"                     Score:  "+matchList.get(3)+"\n"+
            "\tAway  Team : "+matchList.get(4)+"                     Score:  "+matchList.get(2)+"\n"+
            "\tMain Referee :  "+matchList.get(6)+"\n"+"" +
            "\tLine Referee 1:  "+matchList.get(7)+"         Line Referee 2:  "+matchList.get(8)+"\n"+
            "\tGame Events:\n";
    List<String> events=DataController.getInstance().getGameEventsByMatch(matchList.get(5),matchList.get(4),matchList.get(0));
    for (String event:events) {
      report+= "\t"+event+"\n";
    }
    return report;
  }
}