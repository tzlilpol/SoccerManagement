package Server.BusinessLayer.OtherCrudOperations;
import Server.BusinessLayer.Pages.Page;
import Server.BusinessLayer.RoleCrudOperations.Coach;
import Server.BusinessLayer.RoleCrudOperations.Owner;
import Server.BusinessLayer.RoleCrudOperations.Player;
import Server.BusinessLayer.RoleCrudOperations.TeamManager;
import Server.BusinessLayer.DataController;

import java.io.Serializable;
import java.util.*;


public class Team implements Pageable, Serializable
{

  private String name;
  private List<TeamManager> teamManagers;
  private Page page;
  private List<Coach> coachs;
  private List<Owner> owners;
  private List<Player> players;//11 players
  private League league;
  private List<Match> matchs;
  private Stadium stadium;
  private int points;


  public Team(String aName,League aLeague,Stadium aStadium)
  {
    if(aLeague != null)
    {
      setLeague(aLeague);
    }
    if(aStadium != null)
    {
      setStadium(aStadium);
    }
    name = aName;
    teamManagers = new ArrayList<TeamManager>();
    coachs = new ArrayList<Coach>();
    owners = new ArrayList<Owner>();
    players = new ArrayList<Player>();
    matchs = new ArrayList<Match>();
    points=0;
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = true;
    name = aName;
    wasSet = true;
    pageUpdated();
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public TeamManager getTeamManager(int index)
  {
    TeamManager aTeamManager = teamManagers.get(index);
    return aTeamManager;
  }

  public List<TeamManager> getTeamManagers()
  {
    List<TeamManager> newTeamManagers = Collections.unmodifiableList(teamManagers);
    return newTeamManagers;
  }

  public int numberOfTeamManagers()
  {
    int number = teamManagers.size();
    return number;
  }

  public boolean hasTeamManagers()
  {
    boolean has = teamManagers.size() > 0;
    return has;
  }

  public int indexOfTeamManager(TeamManager aTeamManager)
  {
    int index = teamManagers.indexOf(aTeamManager);
    return index;
  }

  public Page getPage()
  {
    return page;
  }

  public Coach getCoach(int index)
  {
    Coach aCoach = coachs.get(index);
    return aCoach;
  }

  public List<Coach> getCoachs()
  {
    List<Coach> newCoachs = Collections.unmodifiableList(coachs);
    return newCoachs;
  }

  public int numberOfCoachs()
  {
    int number = coachs.size();
    return number;
  }

  public boolean hasCoachs()
  {
    boolean has = coachs.size() > 0;
    return has;
  }

  public int indexOfCoach(Coach aCoach)
  {
    int index = coachs.indexOf(aCoach);
    return index;
  }

  public Owner getOwner(int index)
  {
    Owner aOwner = owners.get(index);
    return aOwner;
  }

  public List<Owner> getOwners()
  {
    List<Owner> newOwners = Collections.unmodifiableList(owners);
    return newOwners;
  }

  public int numberOfOwners()
  {
    int number = owners.size();
    return number;
  }

  public boolean hasOwners()
  {
    boolean has = owners.size() > 0;
    return has;
  }

  public int indexOfOwner(Owner aOwner)
  {
    int index = owners.indexOf(aOwner);
    return index;
  }

  public Player getPlayer(int index)
  {
    Player aPlayer = players.get(index);
    return aPlayer;
  }

  public List<Player> getPlayers()
  {
    List<Player> newPlayers = Collections.unmodifiableList(players);
    return newPlayers;
  }

  public int numberOfPlayers()
  {
    int number = players.size();
    return number;
  }

  public boolean hasPlayers()
  {
    boolean has = players.size() > 0;
    return has;
  }

  public int indexOfPlayer(Player aPlayer)
  {
    int index = players.indexOf(aPlayer);
    return index;
  }

  public League getLeague()
  {
    return league;
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

  public Stadium getStadium()
  {
    return stadium;
  }

  public boolean addTeamManager(TeamManager aTeamManager)
  {
    if(aTeamManager==null)
      return false;
    if (!teamManagers.contains(aTeamManager))
    {
      teamManagers.add(aTeamManager);
    }
    if(aTeamManager.getTeam()==null || !aTeamManager.getTeam().equals(this))
      aTeamManager.setTeam(this);
    pageUpdated();
    return true;
  }

  public boolean removeTeamManager(TeamManager aTeamManager)
  {
    if (teamManagers.contains(aTeamManager))
    {
      teamManagers.remove(aTeamManager);
    }
    aTeamManager.setTeam(null);
    pageUpdated();
    return true;
  }

  public boolean addCoach(Coach aCoach)
  {
    boolean wasAdded = true;
    if (coachs.contains(aCoach)) { return true; }
    coachs.add(aCoach);
    if (aCoach.indexOfTeam(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aCoach.addTeam(this);
      if (!wasAdded)
      {
        coachs.remove(aCoach);
      }
    }
    pageUpdated();
    return wasAdded;
  }
   public boolean removeCoach(Coach aCoach)
  {
    boolean wasRemoved = true;
    if (!coachs.contains(aCoach))
    {
      return wasRemoved;
    }

    int oldIndex = coachs.indexOf(aCoach);
    coachs.remove(oldIndex);
    if (aCoach.indexOfTeam(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aCoach.removeTeam(this);
      if (!wasRemoved)
      {
        coachs.add(oldIndex,aCoach);
      }
    }
    pageUpdated();
    return wasRemoved;
  }

  public boolean addOwner(Owner aOwner)
  {
    if (!owners.contains(aOwner))
    {
      owners.add(aOwner);
    }
    if(aOwner.getTeam()==null || !aOwner.getTeam().equals(this))
      aOwner.setTeam(this);
    pageUpdated();
    return true;
  }

  public boolean removeOwner(Owner aOwner)
  {
    if (owners.contains(aOwner))
    {
      owners.remove(aOwner);
    }
    pageUpdated();
    return true;
  }


  /**
   * adds player to list
   */
  public boolean addPlayer(Player aPlayer)
  {
    boolean wasAdded = true;
    if (players.contains(aPlayer)) { return true; }

    Team existingTeam = aPlayer.getTeam();
    boolean isNewTeam = existingTeam != null && !this.equals(existingTeam);

    if (isNewTeam)
    {
      aPlayer.setTeam(this);
    }
    else
    {
      players.add(aPlayer);
    }
    wasAdded = true;
    pageUpdated();
    return wasAdded;
  }

  /**
   * removes player from list, makes player's list null, handels null.
   */
  public boolean removePlayer(Player aPlayer)
  {
    boolean wasRemoved = true;
    //Unable to remove aPlayer, as it must always have a team
    if (!this.equals(aPlayer.getTeam()))
    {
      return false;
    }

    players.remove(aPlayer);
    aPlayer.setTeam(null);
    wasRemoved = true;
    pageUpdated();
    return wasRemoved;
  }

  /**
   * adds league to team list, adds team to league, handels null
   */
  public boolean setLeague(League aLeague)
  {
    boolean wasSet = true;
    if (aLeague == null)
    {
      league=null;
      return wasSet;
    }

    league = aLeague;

    league.addTeam(this);
    wasSet = true;
    pageUpdated();
    return wasSet;
  }


  /**
   * adds match to team list, updates the home/away field in the match

   */
  public boolean addMatch(Match aMatch, String homeOrAway)
  {
    boolean wasAdded = true;
    if (matchs.contains(aMatch)) { return true; }
    matchs.add(aMatch);
    if(homeOrAway.equalsIgnoreCase("home"))
    {
      if(aMatch.getHomeTeam()==null)
        aMatch.setHomeTeam(this);
    }

    else if(homeOrAway.equalsIgnoreCase("away"))
    {
      if(aMatch.getAwayTeam()==null)
        aMatch.setAwayTeam(this);
    }
    pageUpdated();
    return wasAdded;
  }

  /**
   * removes the match from the list, and makes the corrasponding teams playing in the match null
   */
  public boolean removeMatch(Match aMatch)
  {
    boolean wasRemoved = true;
    if (!matchs.contains(aMatch))
    {
      return wasRemoved;
    }

    matchs.remove(aMatch);

    if(aMatch.getAwayTeam()!=null&&aMatch.getAwayTeam().equals(this)) aMatch.setAwayTeam(null);
    if(aMatch.getHomeTeam()!=null&&aMatch.getHomeTeam().equals(this)) aMatch.setHomeTeam(null);
    pageUpdated();
    return wasRemoved;
  }

  /**
   * sets stadium, updates stadium with team, handels null
   * @param aStadium
   * @return
   */
  public boolean setStadium(Stadium aStadium)
  {
    boolean wasSet = true;
    if (aStadium == null)
    {
      stadium=null;
      wasSet=true;
      return wasSet;
    }

    Stadium existingStadium = stadium;
    stadium = aStadium;
    if (existingStadium != null && !existingStadium.equals(aStadium))
    {
      boolean didRemove = existingStadium.removeTeam(this);
      if (!didRemove)
      {
        stadium = existingStadium;
        return wasSet;
      }
    }
    stadium.addTeam(this);
    wasSet = true;
    pageUpdated();
    return wasSet;
  }

  /**
   * delete the team, make nessecery changes to the team managers, page, coaches, owners, league and matches of the team
   */
  public void delete()
  {
    ArrayList<TeamManager> copyOfTeamManagers = new ArrayList<>(teamManagers);
    teamManagers.clear();
    for(TeamManager aTeamManager : copyOfTeamManagers)
    {
      aTeamManager.setTeam(null);
    }
    Page existingPage = page;
    page = null;
    if (existingPage != null)
    {
      existingPage.delete();
    }
    ArrayList<Coach> copyOfCoachs = new ArrayList<Coach>(coachs);
    coachs.clear();
    for(Coach aCoach : copyOfCoachs)
    {
      aCoach.removeTeam(this);
    }
    ArrayList<Owner> copyOfOwners = new ArrayList<Owner>(owners);
    owners.clear();
    for(Owner aOwner : copyOfOwners)
    {
      aOwner.setTeam(null);
    }
    ArrayList<Player> copyOfPlayers = new ArrayList<>(players);
    players.clear();
    for(int i=copyOfPlayers.size(); i > 0; i--)
    {
      Player aPlayer = copyOfPlayers.get(i - 1);
      aPlayer.setTeam(null);
    }
    League placeholderLeague = league;
    this.league = null;
    if(placeholderLeague != null)
    {
      placeholderLeague.removeTeam(this);
    }
    ArrayList<Match> copyOfMatchs = new ArrayList<Match>(matchs);
    matchs.clear();
    for(Match aMatch : copyOfMatchs)
    {
      aMatch.delete();
    }
    Stadium placeholderStadium = stadium;
    this.stadium=null;
    if(placeholderStadium != null)
    {
      placeholderStadium.removeTeam(this);
    }
  }

  /**
   * removes team from list, makes league field in that team null
   */
  public void removeLeauge(League league) {
    league.removeTeam(this);
    pageUpdated();
  }

  @Override
  public void removePage() {
    page=null;
  }

  public void setPage(Page page)
  {
    this.page = page;
    if(page==null) return;
    if(!page.getType().equals(this))
      page.setType(this);
  }

  public void ShowTeam()
  {
    System.out.println("Name:");
    System.out.println(this.getName());
    System.out.println();
    System.out.println("TeamManagers:");
    for(TeamManager teamManager:this.getTeamManagers())
      System.out.println(teamManager.getName());
    System.out.println();
    System.out.println("Coaches:");
    for(Coach coach:this.getCoachs())
      System.out.println(coach.getName());
    System.out.println();
    System.out.println("TeamOwners:");
    for(Owner owner:this.getOwners())
      System.out.println(owner.getName());
    System.out.println();
    System.out.println("Players:");
    for(Player player:this.getPlayers())
      System.out.println(player.getName());
    System.out.println();
    System.out.println("BusinessLayer.OtherCrudOperations.League:");
    System.out.println(this.getLeague().getName());
    System.out.println();
    System.out.println("Matches:");
    for(Match match:this.getMatchs()){
      System.out.print(this.getName()+" against ");
      if(match.getAwayTeam().getName().equals(this.getName()))
        System.out.println(match.getHomeTeam().getName());
      else
        System.out.println(match.getAwayTeam().getName());
    }
    System.out.println();
    System.out.println("BusinessLayer.OtherCrudOperations.Stadium:");
    System.out.println(this.getStadium().getName());
    System.out.println();
  }

  public void pageUpdated(){
    if(page!=null)
      page.notifyTrackingFans(new Alert(getName()+" page updated"));
  }

  public static Team convertStringToTeam(String teamName){
    for (Team team : DataController.getInstance().getTeams()){
      if(team.getName().equals(teamName)){
        return team;
      }
    }
    return null;
  }

}