package Server.BusinessLayer.RoleCrudOperations;
import Server.BusinessLayer.Logger.Logger;
import Server.BusinessLayer.OtherCrudOperations.Alert;
import Server.BusinessLayer.OtherCrudOperations.Pageable;
import Server.BusinessLayer.OtherCrudOperations.Team;
import Server.BusinessLayer.Pages.Page;


import java.io.Serializable;
import java.util.*;

public class Coach extends Role implements Pageable, Serializable
{


  private String training;
  private String teamRole;
  private List<Team> teams;
  private Page page;

  public Coach(String username){setUsername(username);}

  public Coach(String aName, String aTraining, String aTeamRole, Page aPage)
  {
    super(aName);
    training = aTraining;
    teamRole = aTeamRole;
    teams = new ArrayList<Team>();
    setPage(aPage);
  }

  public boolean setTraining(String aTraining)
  {
    boolean wasSet = false;
    training = aTraining;
    wasSet = true;
    pageUpdated();
    return wasSet;
  }

  public boolean setTeamRole(String aTeamRole)
  {
    boolean wasSet = false;
    teamRole = aTeamRole;
    wasSet = true;
    pageUpdated();
    return wasSet;
  }

  public String getTraining()
  {
    return training;
  }

  public String getTeamRole()
  {
    return teamRole;
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

  /**
   * returns the number of teams that the coach coaches
   */
  public int numberOfTeams()
  {
    int number = teams.size();
    return number;
  }

  /**
   * checks if the coach is coaching any team
   * @return
   */
  public boolean hasTeams()
  {
    boolean has = teams.size() > 0;
    return has;
  }

  /**
   * returns the index of the BusinessLayer.OtherCrudOperations.Team in the list
   */
  public int indexOfTeam(Team aTeam)
  {
    int index = teams.indexOf(aTeam);
    return index;
  }

  public Page getPage()
  {
    return page;
  }

  /**
   * sets the page of the coach to null
   */
  public void clear_page()
  {
    page = null;
  }

  public static int minimumNumberOfTeams()
  {
    return 0;
  }

  /**
   * adds a team to te coach's list
   */
  public boolean addTeam(Team aTeam)
  {
    boolean wasAdded = false;
    if (teams.contains(aTeam)) { return true; }
    teams.add(aTeam);
    if (aTeam.indexOfCoach(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      aTeam.addCoach(this);
    }
    pageUpdated();
    return wasAdded;
  }

  /**
   * removes a team from the coach
   */
  public boolean removeTeam(Team aTeam)
  {
    boolean wasRemoved = true;
    if (!teams.contains(aTeam))
    {
      return wasRemoved;
    }

    int oldIndex = teams.indexOf(aTeam);
    teams.remove(oldIndex);
    if (aTeam.indexOfCoach(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      aTeam.removeCoach(this);
    }
    pageUpdated();
    return wasRemoved;
  }

  /**
   * deletes the coach, removes him from all his teams, deletes his page
   */
  public void delete()
  {
    ArrayList<Team> copyOfTeams = new ArrayList<Team>(teams);
    teams.clear();
    for(Team aTeam : copyOfTeams)
    {
      aTeam.removeCoach(this);
    }
    Page existingPage = page;
    page = null;
    if (existingPage != null)
    {
      existingPage.delete();
    }
  }

  public String toString()
  {
    return super.toString() + "["+
            "training" + ":" + getTraining()+ "," +
            "teamRole" + ":" + getTeamRole() +
            "  " + "page = "+(getPage())+
            "  " + "page = "+(getPage());
  }

  @Override
  public void removePage() {
    page=null;
  }

  /*
  UC-5.1 update coach details
   */
  public void updateDetails(String training,String teamRole)
  {
    setTraining(training);
    setTeamRole(teamRole);
    Logger.getInstance().writeNewLine("Coach "+super.getName()+" update details to : "+training+","+teamRole);
  }

  public void ShowCoach() {
    System.out.println("Name:");
    System.out.println(this.getName());
    System.out.println();
    System.out.println("Training:");
    System.out.println(this.getTraining());
    System.out.println();
    System.out.println("TeamRole:");
    System.out.println(this.getTeamRole());
    System.out.println();
    System.out.println("TeamsCoaching:");
    for(Team team:this.getTeams())
      System.out.println(team.getName());
  }

  public void pageUpdated()
  {
    if(page!=null)
      page.notifyTrackingFans(new Alert(getName()+" page updated"));
  }

  @Override
  public void setPage(Page page)
  {
    this.page = page;
    if(page==null) return;
    if(!page.getType().equals(this))
      page.setType(this);
  }

}