package Server.BusinessLayer.RoleCrudOperations;
import Server.BusinessLayer.DataController;
import Server.BusinessLayer.Logger.Logger;
import Server.BusinessLayer.OtherCrudOperations.Account;
import Server.BusinessLayer.OtherCrudOperations.Alert;
import Server.BusinessLayer.OtherCrudOperations.Pageable;
import Server.BusinessLayer.Pages.Page;
import Server.BusinessLayer.OtherCrudOperations.PositionEnum;
import Server.BusinessLayer.OtherCrudOperations.Team;

import java.io.Serializable;
import java.util.Date;

public class Player extends Role implements Pageable, Serializable
{

  private Date birthday;
  private PositionEnum position;
  private Team team;
  private Page page;

  public Player(String name){setUsername(name);}

  public Player(String aName, Date aBirthday, PositionEnum aPosition, Team aTeam, Page aPage)
  {
    super(aName);
    birthday = aBirthday;
    position = aPosition;
    if (aTeam != null) {
      setTeam(aTeam);
    }
    setPage(aPage);
  }

  public boolean setBirthday(Date aBirthday)
  {
    boolean wasSet = true;
    birthday = aBirthday;
    wasSet = true;
    return wasSet;
  }

  public boolean setPosition(PositionEnum aPosition)
  {
    boolean wasSet = true;
    position = aPosition;
    wasSet = true;
    pageUpdated();
    return wasSet;
  }

  public Date getBirthday()
  {
    return birthday;
  }

  public PositionEnum getPosition()
  {
    return position;
  }

  public Team getTeam()
  {
    return team;
  }

  public Page getPage()
  {
    return page;
  }

  public boolean setTeam(Team aTeam)
  {
    boolean wasSet = true;
    //Must provide team to player
    if (aTeam == null)
    {
      team=null;
      return wasSet;
    }

    
    Team existingTeam = team;
    if (existingTeam != null && !existingTeam.equals(aTeam))
    {
      boolean didRemove = existingTeam.removePlayer(this);
      if (!didRemove)
      {
        return wasSet;
      }
    }

      team = aTeam;

    team.addPlayer(this);
    wasSet = true;
    pageUpdated();
    return wasSet;
  }

  public void delete()
  {
    Team placeholderTeam = team;
    if(placeholderTeam != null)
    {
      placeholderTeam.removePlayer(this);
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
    return super.toString() + "["+ "]" +
            "  " + "birthday" + "=" + (getBirthday())+
            "  " + "position" + "=" + (getPosition()) +
            "  " + "team = "+(getTeam())+
            "  " + "page = "+(getPage());
  }

  @Override
  public void removePage() {
    page=null;
  }

  /*
    UC-4.1 update player details
     */
  public void updateDetails(Date birthday, PositionEnum position, Team team)
  {
    setBirthday(birthday);
    setPosition(position);
    setTeam(team);
    pageUpdated();
    Logger.getInstance().writeNewLine("BusinessLayer.RoleCrudOperations.Player "+super.getName()+" update details to "+birthday.toString()+", "+position.name()+", "+team.getName());
  }

  public void ShowPlayer() {
    System.out.println("Name:");
    System.out.println(this.getName());
    System.out.println();
    System.out.println("Age:");
    int age=0;
    for(Account account: DataController.getInstance().getAccounts()){
      for(Role role:account.getRoles()){
        if(role.equals(this))
          age=account.getAge();
      }
    }
    System.out.println(age);
    System.out.println();
    System.out.println("Position:");
    System.out.println(this.getPosition());
    System.out.println();
    System.out.println("BusinessLayer.OtherCrudOperations.Team:");
    System.out.println(this.getTeam().getName());
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