package Server.BusinessLayer.OtherCrudOperations;

import Server.BusinessLayer.DataController;

import java.io.Serializable;
import java.util.*;

public class Stadium implements Serializable
{

  private String name;
  private List<Team> teams;
  private List<Match> matchs;

  public Stadium(String aName)
  {
    name = aName;
    teams = new ArrayList<Team>();
    matchs = new ArrayList<Match>();
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public Team getTeam(int index)
  {
    Team aTeam = teams.get(index);
    return aTeam;
  }

  public List<Team> getTeams()
  {
    List<Team> newTeams = Collections.unmodifiableList(teams);
    return newTeams;
  }

  public int numberOfTeams()
  {
    int number = teams.size();
    return number;
  }

  public boolean hasTeams()
  {
    boolean has = teams.size() > 0;
    return has;
  }

  public int indexOfTeam(Team aTeam)
  {
    int index = teams.indexOf(aTeam);
    return index;
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

  public int indexOfMatch(Match aMatch)
  {
    int index = matchs.indexOf(aMatch);
    return index;
  }

  public boolean addTeam(Team aTeam)
  {
    boolean wasAdded = true;
    if (teams.contains(aTeam)) { return true; }

    Stadium existingStadium = aTeam.getStadium();
    boolean isNewStadium = existingStadium != null && !this.equals(existingStadium);
    if (isNewStadium)
    {
      return wasAdded;
    }

    if (isNewStadium)
    {
      aTeam.setStadium(this);
    }
    else
    {
      teams.add(aTeam);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTeam(Team aTeam)
  {
    boolean wasRemoved = true;
    if (this.equals(aTeam.getStadium()))
    {
      return wasRemoved;
    }

    teams.remove(aTeam);
    wasRemoved = true;
    return wasRemoved;
  }
  public static int minimumNumberOfMatchs()
  {
    return 0;
  }

  public boolean addMatch(Match aMatch)
  {
    boolean wasAdded = true;
    if (matchs.contains(aMatch)) { return true; }
    Stadium existingStadium = aMatch.getStadium();
    boolean isNewStadium = existingStadium != null && !this.equals(existingStadium);
    if (isNewStadium)
    {
      aMatch.setStadium(this);
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
    if (!this.equals(aMatch.getStadium()))
    {
      matchs.remove(aMatch);
      aMatch.setStadium(null);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public void delete()
  {
    for(int i=teams.size(); i > 0; i--)
    {
      Team aTeam = teams.get(i - 1);
      aTeam.setStadium(null);
    }
    for(int i=matchs.size(); i > 0; i--)
    {
      Match aMatch = matchs.get(i - 1);
      aMatch.setStadium(null);
    }
  }

  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "]";
  }

  public static Stadium convertStringToStadium(String stadiumName){
    for (Stadium stadium : DataController.getInstance().getStadiums()){
        if (stadium.getName().equals(stadiumName))
          return stadium;
      }
    return null;
  }


}