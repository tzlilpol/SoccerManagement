package Server.BusinessLayer.RoleCrudOperations;
import Server.BusinessLayer.DataController;
import Server.BusinessLayer.Logger.Logger;
import Server.BusinessLayer.OtherCrudOperations.*;
import Server.BusinessLayer.Pages.Page;
import Client.ServiceLayer.OurSystemClient;

import java.io.Serializable;
import java.util.*;

public class Fan extends Role implements Serializable
{
  private List<Page> pages;
  private boolean trackPersonalPages;
  private boolean getMatchNotifications;
  private List<String[]> searchHistory; //String[0]: Criterion, String[1]: Query


  public Fan(String aName)
  {
    setUsername(aName);
    pages = new ArrayList<Page>();
    trackPersonalPages=false;
    getMatchNotifications=false;
    searchHistory=new LinkedList<>();
  }


  public List<String[]> getSearchHistory() {
    return searchHistory;
  }

  public Page getPage(int index)
  {
    Page aPage = pages.get(index);
    return aPage;
  }

  public List<Page> getPages()
  {
    return pages;
  }

  public int numberOfPages()
  {
    int number = pages.size();
    return number;
  }

  public boolean hasPages()
  {
    boolean has = pages.size() > 0;
    return has;
  }

  public int indexOfPage(Page aPage)
  {
    int index = pages.indexOf(aPage);
    return index;
  }
  public boolean addPage(Page aPage)
  {
    boolean wasAdded = false;
    if (pages.contains(aPage)) { return false; }
    pages.add(aPage);
    if (aPage.indexOfFan(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aPage.addFan(this);
      if (!wasAdded)
      {
        pages.remove(aPage);
      }
    }
    return wasAdded;
  }
  public boolean removePage(Page aPage)
  {
    boolean wasRemoved = false;
    if (!pages.contains(aPage))
    {
      return wasRemoved;
    }

    int oldIndex = pages.indexOf(aPage);
    pages.remove(oldIndex);
    if (aPage.indexOfFan(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aPage.removeFan(this);
      if (!wasRemoved)
      {
        pages.add(oldIndex,aPage);
      }
    }
    return wasRemoved;
  }

  public void deleteAllPages()
  {
    ArrayList<Page> copyOfPages = new ArrayList<Page>(pages);
    pages.clear();
    for(Page aPage : copyOfPages)
    {
      aPage.removeFan(this);
    }
  }

  public boolean isTrackPersonalPages() {
    return trackPersonalPages;
  }
  public void setTrackPersonalPages(boolean trackPersonalPages) {
    this.trackPersonalPages = trackPersonalPages;
  }

  public boolean isGetMatchNotifications() {
    return getMatchNotifications;
  }

  public void setGetGameNotifications(boolean getGameNotifications) {
    this.getMatchNotifications = getGameNotifications;
  }



  public void Logout(){
    System.out.println("Logged out");
    (Logger.getInstance()).writeNewLine("Fan "+this.getName()+" logged out");
  }

  public void  EditPersonalInfo(String newName, String newUserName, String newPassword) throws Exception {
    for(Account account: DataController.getInstance().getAccounts()){
      if(account.getUserName().equals(newUserName))
        throw new Exception("Username already exists");
    }
    Account account= DataController.getInstance().getAccountByRole(this);
    if(!(newName.length()==0)) account.setName(newName);
    if(!(newUserName.length()==0)) account.setUserName(newUserName);
    if(!(newPassword.length()==0)) account.setPassword((newPassword));
    (Logger.getInstance()).writeNewLine("Fan "+this.getName()+" eddited his personal infomation");
  }



//  public void ShowInfo(String InfoAbout){
//    new Guest().ShowInfo(InfoAbout);
//  }

//  public void Search(String criterion, String query){
//    new Guest().Search(criterion,query);
//    String[] savedSearch={criterion,query};
//    searchHistory.add(savedSearch);
//  }

//  public void Filter(String category,String roleFilter){
//    new Guest().Filter(category,roleFilter);
//  }



  public void SubscribeTrackPersonalPages(){
    trackPersonalPages=true;
  }

  public String SubscribeGetMatchNotifications(){
    DataController.getInstance().setGameNotificationSubscribtion(this,true);
    return "Subscribed successfully!";
  }
  public String unSubscribeGetMatchNotifications()
  {
    DataController.getInstance().setGameNotificationSubscribtion(this,false);
    return "Subscription removed successfully!";
  }

  public static void notifyFansAboutMatch(Match match){
    List<Fan> fans= DataController.getInstance().getFansFromAccounts();
    if(fans==null)return;
    for(Fan fan:fans){
      if(fan.isGetMatchNotifications())
        fan.addAlert(new Alert(match.getAwayTeam().getName()+" against "+match.getHomeTeam().getName()));
    }
  }



  public void ShowSearchHistory() throws Exception {
    if(searchHistory.size()==0)
      throw new Exception("You have not searched anything yet");
    for(String[] savedSearch:searchHistory){
      System.out.println("Criterion:");
      System.out.println(savedSearch[0]);
      System.out.println("Query:");
      System.out.println(savedSearch[1]);
      System.out.println();
    }
  }

  public void ShowPersonalInfo(){
    System.out.println("Name:");
    System.out.println(getName());
    List<Team> teams=new LinkedList<>();
    List<Player> players=new LinkedList<>();
    List<Coach> coaches=new LinkedList<>();
    for(Page page:pages){
      if(page.getType() instanceof Team)
        teams.add((Team)page.getType());
      if(page.getType() instanceof Player)
        players.add((Player)page.getType());
      if(page.getType() instanceof Coach)
        coaches.add((Coach)page.getType());
    }
    System.out.println();
    if (teams.size()>0) {
      System.out.println("Teams Tracked:");
      for(Team team:teams)
        team.ShowTeam();
    }
    if (players.size()>0) {
      System.out.println("Players Tracked:");
      for(Player player:players)
        player.ShowPlayer();
      System.out.println();
    }
    if (coaches.size()>0) {
      System.out.println("Coaches Tracked:");
      for(Coach coach:coaches)
        coach.ShowCoach();
    }
  }

  @Override
  public void logOff() {
    super.logOff();
    DataController.getInstance().setGameNotificationSubscribtion(this,false);
  }







}