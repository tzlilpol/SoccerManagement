package Server.BusinessLayer.OtherCrudOperations;

import Server.BusinessLayer.DataController;

import java.io.Serializable;
import java.util.*;


public class Season implements Serializable
{
  private String name;
  private List<Match> matchs;
  private HashMap<League,SLsettings> sLsettings;

  public Season(String aName)
  {
    name = aName;
    sLsettings = new HashMap<>();
    matchs = new ArrayList<Match>();
  }

  public HashMap<League, SLsettings> getsLsettings() {
    return sLsettings;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public void setMatchs(List<Match> matchs) {
    this.matchs = matchs;
  }

  public String getName()
  {
    return name;
  }

  public SLsettings getSLsettingsByLeague(League aLeague)
  {
    SLsettings sLsetting = sLsettings.get(aLeague);
    return sLsetting;
  }

  public Match getMatch(int index)
  {
    Match aMatch = matchs.get(index);
    return aMatch;
  }

  public List<Match> getMatchs()
  {
    List<Match> newMatchs = Collections.unmodifiableList(matchs);
    return newMatchs;
  }

  public int numberOfMatchs()
  {
    int number = matchs.size();
    return number;
  }

  public boolean hasMatchs()
  {
    boolean has = matchs.size() > 0;
    return has;
  }

  public boolean addSLsettingsToLeague(League aLeague, SLsettings asLsettings)
  {
    sLsettings.put(aLeague,asLsettings);
    if(!aLeague.hasPolicy(this,asLsettings)){
      aLeague.addSLsettingsToSeason(this, asLsettings);
    }
    return true;
  }

  public boolean removeSLsettingsFromLeague(League aLeague,Boolean bool)
  {
    if (!sLsettings.containsKey(aLeague)) {
      return true;
    }
    if(aLeague.hasPolicy(this,sLsettings.get(aLeague)) && bool){
      aLeague.removeSLsettingsFromSeason(this,false);
    }
    sLsettings.remove(aLeague);
    return true;
  }

  public boolean addMatch(Match aMatch)
  {
    boolean wasAdded = false;
    if (matchs.contains(aMatch)) { return false; }
    Season existingSeason = aMatch.getSeason();
    boolean isNewSeason = existingSeason != null && !this.equals(existingSeason);
    if (isNewSeason)
    {
      aMatch.setSeason(this);
    }
    else
    {
      matchs.add(aMatch);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeMatch(Match aMatch)
  {
    boolean wasRemoved = true;
      matchs.remove(aMatch);
    return wasRemoved;
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName();
  }

  public boolean hasPolicy(League league, SLsettings AslSLsettings) {
    if(sLsettings.containsKey(league)){
      if (sLsettings.get(league).equals(AslSLsettings)) {
        return true;
      }
    }
    return false;
  }

  public void deleteLeague(League league,SLsettings asLsettings) {

    if(sLsettings.containsKey(league)){
      if(sLsettings.get(league).equals(asLsettings)){
        sLsettings.remove(league);
      }
    }

  }

  public boolean hasLeague(League league){
    if(sLsettings.containsKey(league)){
      return true;
    }
    return false;
  }

  public void setsLsettings(HashMap<League, SLsettings> sLsettings) {
    this.sLsettings = sLsettings;
  }

  public void ShowSeason() {
    System.out.println("Name:");
    System.out.println(this.getName());
    System.out.println();
    System.out.println("Matches:");
    for(Match match:this.getMatchs())
      System.out.println(match.getAwayTeam().getName()+" against "+match.getHomeTeam().getName());
  }


  private boolean compareTwoMatchLists(List<Match> a, List<Match> b){
    if(a.size()!=b.size()) return false;
    for(int i=0;i<a.size();i++){
      if(!a.get(i).equals(b.get(i)))
        return false;
    }
    return true;
  }

  public static Season convertStringToSeason(String seasonName){
    for (Season season : DataController.getInstance().getSeasons()){
      if(season.getName().equals(seasonName)){
        return season;
      }
    }
    return null;
  }



}