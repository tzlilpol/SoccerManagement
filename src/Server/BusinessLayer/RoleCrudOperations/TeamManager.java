package Server.BusinessLayer.RoleCrudOperations;
import Server.BusinessLayer.Logger.Logger;
import Server.BusinessLayer.OtherCrudOperations.League;
import Server.BusinessLayer.OtherCrudOperations.Match;
import Server.BusinessLayer.OtherCrudOperations.Stadium;
import Server.BusinessLayer.OtherCrudOperations.Team;

import java.io.Serializable;

public class TeamManager extends Role implements Serializable
{


  public enum PermissionEnum{
    manageName,
    manageManagers,
    managePage,
    manageCoaches,
    managePlayers,
    manageLeague,
    manageMatches,
    manageStadium
  }

  private Team team;
  private Owner appointedBy;

  public TeamManager(String name){setUsername(name);}

  public TeamManager(String aName, Team team, Owner appointer) {
    super(aName);
    this.team = team;
    appointedBy = appointer;
  }

  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
    if(team==null) return;
    if(!team.getTeamManagers().contains(this))
      team.addTeamManager(this);
  }

  public Owner getAppointer() {
    return appointedBy;
  }

  /**
   * allows the team manager to change the name of the team
   */
  public boolean changeTeamName(String name) throws Exception {
    if(team==null)
      throw new Exception("Team manager does not have a team");
    if(!appointedBy.hasPermission(this,PermissionEnum.manageName))
      throw new Exception("The Team manager does not have permission to manage team name");


    Logger.getInstance().writeNewLine(this.getUsername()+" changed the name of the team "+team.getName() +" to be:" + name);

    return team.setName(name);
  }

  /**
   * allows the team manager to add a team manager
   */
  public boolean addTeamManager(TeamManager aTeamManager) throws Exception {
    if(team==null)
      throw new Exception("Team manager does not have a team");
    if(!appointedBy.hasPermission(this,PermissionEnum.manageManagers))
      throw new Exception("The Team manager does not have permission to manage team managers");

    Logger.getInstance().writeNewLine(this.getUsername()+" set "+aTeamManager.getUsername() +" as a team manager for the team:" + team.getName());

    return team.addTeamManager(aTeamManager);
  }

  /**
   * allows the team manager to remove a team manager
   */
  public boolean removeTeamManager(TeamManager aTeamManager) throws Exception {
    if(team==null)
      throw new Exception("Team manager does not have a team");
    if(!appointedBy.hasPermission(this,PermissionEnum.manageManagers))
      throw new Exception("The Team manager does not have permission to manage team managers");

    Logger.getInstance().writeNewLine(this.getUsername()+" removed the team manager"+(aTeamManager.getUsername() +" from the team:" + team.getName()));

    return team.removeTeamManager(aTeamManager);
  }

  /**
   * allows the team manager to add a coach
   */
  public boolean addCoach(Coach aCoach) throws Exception {
    if(team==null)
      throw new Exception("Team manager does not have a team");
    if(!appointedBy.hasPermission(this,PermissionEnum.manageCoaches))
      throw new Exception("The Team manager does not have permission to manage coaches");

    Logger.getInstance().writeNewLine(this.getUsername()+" set "+(aCoach.getUsername() +" as a coach for the team:" + team.getName()));

    return team.addCoach(aCoach);
  }

  /**
   * allows the team manager to remove a coach
   */
  public boolean removeCoach(Coach aCoach) throws Exception {
    if(team==null)
      throw new Exception("Team manager does not have a team");
    if(!appointedBy.hasPermission(this,PermissionEnum.manageCoaches))
      throw new Exception("The Team manager does not have permission to manage coaches");

    Logger.getInstance().writeNewLine(this.getUsername()+" removed the BusinessLayer.RoleCrudOperations.Coach"+(aCoach.getUsername() +" from the team:" + team.getName()));

    return team.removeCoach(aCoach);
  }

  /**
   * allows the team manager to add a player
   */
  public boolean addPlayer(Player aPlayer) throws Exception {
    if(team==null)
      throw new Exception("Team manager does not have a team");
    if(!appointedBy.hasPermission(this,PermissionEnum.managePlayers))
      throw new Exception("The Team manager does not have permission to manage players");

    Logger.getInstance().writeNewLine(this.getUsername()+" set "+aPlayer.getUsername() +" as a player for the team:" + team.getName());

    return team.addPlayer(aPlayer);
  }

  /**
   * allows the team manager to remove a player
   */
  public boolean removePlayer(Player aPlayer) throws Exception {
    if(team==null)
      throw new Exception("Team manager does not have a team");
    if(!appointedBy.hasPermission(this,PermissionEnum.managePlayers))
      throw new Exception("The Team manager does not have permission to manage players");

    Logger.getInstance().writeNewLine(this.getUsername()+" removed the BusinessLayer.RoleCrudOperations.Player"+(aPlayer.getUsername() +" from the team:" + team.getName()));

    return team.removePlayer(aPlayer);
  }

  /**
   * allows the team manager to change the league
   */
  public boolean setLeague(League aLeague) throws Exception {
    if(team==null)
      throw new Exception("Team manager does not have a team");
    if(!appointedBy.hasPermission(this,PermissionEnum.manageLeague))
      throw new Exception("The Team manager does not have permission to manage leauges");
    Logger.getInstance().writeNewLine(this.getUsername()+" set the BusinessLayer.OtherCrudOperations.League of the team "+team.getName()+" to be "+aLeague.getName());

    return team.setLeague(aLeague);
  }

  /**
   * allows the team manager to remove a match
   */
  public boolean removeMatch(Match aMatch) throws Exception {
    if(team==null)
      throw new Exception("Team manager does not have a team");
    if(!appointedBy.hasPermission(this,PermissionEnum.manageMatches))
      throw new Exception("The Team manager does not have permission to manage matches ");

    Logger.getInstance().writeNewLine(this.getUsername()+"removed a match on "+aMatch.getDate()+" for the team "+team.getName());

    return team.removeMatch(aMatch);
  }


  /**
   * allows the team manager to change the stadium
   */
  public boolean setStadium(Stadium aStadium) throws Exception {
    if(team==null)
      throw new Exception("Team manager does not have a team");
    if(!appointedBy.hasPermission(this,PermissionEnum.manageStadium))
      throw new Exception("The Team manager does not have permission to manage stadiums");

    Logger.getInstance().writeNewLine(this.getUsername()+" set "+(aStadium.getName() +" as the stadium for the team:" + team.getName()));

    return team.setStadium(aStadium);
  }

  public void delete()
  {
    team.removeTeamManager(this);
    setTeam(null);
  }

  public void ShowTeamManager(){
    System.out.println("Name:");
    System.out.println(this.getName());
    System.out.println();
    System.out.println("BusinessLayer.OtherCrudOperations.Team managed:");
    System.out.println(this.getTeam().getName());
  }




}