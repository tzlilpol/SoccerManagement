package Server.BusinessLayer.OtherCrudOperations;
import Server.BusinessLayer.RoleCrudOperations.Fan;
import Server.BusinessLayer.RoleCrudOperations.Referee;
import Client.ServiceLayer.OurSystemClient;

import java.io.Serializable;
import java.sql.*;
import java.util.*;

public class Match implements Serializable
{

  //private Date date;
//  private int year;
//  private int month;
//  private int day;
  private String date;
  private Time time;
  private int awayScore;
  private int homeScore;
  private Team awayTeam;
  private Team homeTeam;
  private Referee mainReferee;
  private Referee lineRefereeOne;
  private Referee lineRefereeTwo;
  private Stadium stadium;
  private Season season;
  private EventCalender eventCalender;

  public Match(String date,Time aTime, int aAwayScore, int aHomeScore, Stadium aStadium, Season aSeason, Team awayTeam, Team homeTeam, Referee mainReferee, Referee lineRefereeOne, Referee lineRefereeTwo)
  {
    this.date=date;
    time = aTime;
    awayScore = aAwayScore;
    homeScore = aHomeScore;
    setAwayTeam(awayTeam);
    setHomeTeam(homeTeam);
    this.homeTeam = homeTeam;
    this.mainReferee= mainReferee;
    this.lineRefereeOne = lineRefereeOne;
    this.lineRefereeTwo = lineRefereeTwo;
    setStadium(aStadium);
    this.season = aSeason;
    eventCalender = new EventCalender(this);

    Fan.notifyFansAboutMatch(this);
  }

  public boolean setDate(String date)
  {
    boolean wasSet = false;
    this.date = date;
    mainReferee.notifyAccount(mainReferee.getUsername(),"Game between "+awayTeam.getName()+" and "+homeTeam.getName()+" update to date: "+date.toString());
    lineRefereeOne.notifyAccount(lineRefereeOne.getUsername(),"Game between "+awayTeam.getName()+" and "+homeTeam.getName()+" update to date: "+date);
    lineRefereeTwo.notifyAccount(lineRefereeTwo.getUsername(),"Game between "+awayTeam.getName()+" and "+homeTeam.getName()+" update to date: "+date);
    wasSet = true;
    return wasSet;
  }

  public boolean setTime(Time aTime)
  {
    boolean wasSet = false;
    time = aTime;
    mainReferee.notifyAccount(mainReferee.getUsername(),"Game between "+awayTeam.getName()+" and "+homeTeam.getName()+" update to time: "+time.toString());
    lineRefereeOne.notifyAccount(lineRefereeOne.getUsername(),"Game between "+awayTeam.getName()+" and "+homeTeam.getName()+" update to time: "+time.toString());
    lineRefereeTwo.notifyAccount(lineRefereeTwo.getUsername(),"Game between "+awayTeam.getName()+" and "+homeTeam.getName()+" update to time: "+time.toString());
    wasSet = true;
    return wasSet;
  }

  public boolean setAwayScore(int aAwayScore)
  {
    boolean wasSet = false;
    awayScore = aAwayScore;
    wasSet = true;
    return wasSet;
  }

  public boolean setHomeScore(int aHomeScore)
  {
    boolean wasSet = false;
    homeScore = aHomeScore;
    wasSet = true;
    return wasSet;
  }

  public String getDate()
  {
    return date;
  }

  public Time getTime()
  {
    return time;
  }

  public int getAwayScore()
  {
    return awayScore;
  }

  public int getHomeScore()
  {
    return homeScore;
  }

  public Team getAwayTeam() {
    return awayTeam;
  }

  public void setAwayTeam(Team awayTeam) {
    this.awayTeam = awayTeam;
    if(awayTeam==null) return;
    if(awayTeam.indexOfMatch(this)==-1)
      awayTeam.addMatch(this,"away");

  }

  public Team getHomeTeam() {
    return homeTeam;
  }

  public void setHomeTeam(Team homeTeam) {
    this.homeTeam = homeTeam;
    if(homeTeam==null) return;
    if(homeTeam.indexOfMatch(this)==-1)
      homeTeam.addMatch(this,"home");
  }

  public Referee getMainReferee() {
    return mainReferee;
  }

  public void setMainReferee(Referee mainReferee) {
    this.mainReferee = mainReferee;
  }

  public Referee getLineRefereeOne() {
    return lineRefereeOne;
  }

  public void setLineRefereeOne(Referee lineRefereeOne) {
    this.lineRefereeOne = lineRefereeOne;
  }

  public Referee getLineRefereeTwo() {
    return lineRefereeTwo;
  }

  public void setLineRefereeTwo(Referee lineRefereeTwo) {
    this.lineRefereeTwo = lineRefereeTwo;
  }

  public Stadium getStadium()
  {
    return stadium;
  }

  public Season getSeason()
  {
    return season;
  }

  public EventCalender getEventCalender()
  {
    return eventCalender;
  }

  public boolean setStadium(Stadium aStadium)
  {
    boolean wasSet = false;
    if (aStadium == null)
    {
      stadium=null;
      wasSet=true;
      return wasSet;
    }

    Stadium existingStadium = stadium;
    if (existingStadium != null && !existingStadium.equals(aStadium))
    {
      existingStadium.removeMatch(this);
    }
    stadium = aStadium;
    stadium.addMatch(this);
    if(mainReferee!=null&&lineRefereeOne!=null&lineRefereeTwo!=null) {

      //why is it OurSystemClient if its in the server?!

      mainReferee.notifyAccount(mainReferee.getUsername(),"Game between " + awayTeam.getName() + " and " + homeTeam.getName() + " update to stadium: " + stadium.getName());
      lineRefereeOne.notifyAccount(lineRefereeOne.getUsername(),"Game between " + awayTeam.getName() + " and " + homeTeam.getName() + " stadium: " + stadium.getName());
      lineRefereeTwo.notifyAccount(lineRefereeTwo.getUsername(),"Game between " + awayTeam.getName() + " and " + homeTeam.getName() + " stadium: " + stadium.getName());
    }
    wasSet = true;
    return wasSet;
  }

  public boolean setSeason(Season aSeason)
  {
    boolean wasSet = false;
    if (aSeason == null)
    {
      return wasSet;
    }

    Season existingSeason = season;
    season = aSeason;
    if (existingSeason != null && !existingSeason.equals(aSeason))
    {
      existingSeason.removeMatch(this);
    }
    season.addMatch(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    homeTeam.removeMatch(this);
    awayTeam.removeMatch(this);
    if(lineRefereeTwo!=null) lineRefereeTwo.removeMatch(this);
    if(lineRefereeOne!=null) lineRefereeOne.removeMatch(this);
    if(mainReferee!=null) mainReferee.removeMatch(this);
    Stadium placeholderStadium = stadium;
    this.stadium = null;
    if(placeholderStadium != null)
    {
      placeholderStadium.removeMatch(this);
    }
    Season placeholderSeason = season;
    this.season = null;
    if(placeholderSeason != null)
    {
      placeholderSeason.removeMatch(this);
    }
    EventCalender existingEventCalender = eventCalender;
    eventCalender = null;
    if (existingEventCalender != null)
    {
      existingEventCalender.delete();
    }
  }

  public String toString()
  {
    return super.toString() + "["+
            "awayScore" + ":" + getAwayScore()+ "," +
            "homeScore" + ":" + getHomeScore() +
            "  " + "date" + "=" + (getDate())+
            "  " + "time" + "=" + (getTime()) +
            "  " + "stadium = "+(getStadium())+
            "  " + "season = "+(getSeason())+
            "  " + "eventCalender = "+(getEventCalender());
  }


  public void removeSeason() {
    season = null;
  }

  public void ShowMatch(){
    System.out.println("Score:");
    System.out.println(this.getHomeTeam().getName()+": "+this.getHomeScore()+", "+this.getAwayTeam().getName()+": "+this.getAwayScore());
    System.out.println();
    System.out.println("Referees:");
    System.out.println("Main: "+this.getMainReferee().getName()+", Line1: "+this.getLineRefereeOne().getName()+", Line2: "+this.getLineRefereeTwo().getName());
    System.out.println();
    System.out.println("BusinessLayer.OtherCrudOperations.Stadium:");
    System.out.println(this.getStadium().getName());
    System.out.println();
    System.out.println("BusinessLayer.OtherCrudOperations.Season:");
    System.out.println(season.getName());
    System.out.println();
    List<GameEvent> gameEvent=eventCalender.getGameEvents();
    System.out.println("Game Events:");
    for (GameEvent event: gameEvent){
      event.getType().name();
      System.out.println(", ");
    }
  }
  public static Match convertStringToMatch(String match){
    String [] splitArr=match.split(",");
    String homeTeam=splitArr[0].substring(new String("Teams: ").length(),splitArr[0].indexOf(" against"));
    String awayTeam=splitArr[0].substring(splitArr[0].indexOf("against")+new String("against ").length());
    String date=splitArr[1].substring(splitArr[1].indexOf("Date:")+new String("Date: ").length());
    Team homeTeamObj=new Team(homeTeam,null,null);
    Team awayTeamObj=new Team(awayTeam,null,null);
    return new Match(date,null,0,0,null,null,awayTeamObj,homeTeamObj,null,null,null);
  }


}