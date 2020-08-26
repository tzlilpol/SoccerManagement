package Server.BusinessLayer.OtherCrudOperations;

import Server.BusinessLayer.RoleCrudOperations.Referee;
import Server.BusinessLayer.DataController;

import java.io.Serializable;
import java.util.Date;
import java.sql.Time;

public class GameEvent implements Serializable
{

  private EventEnum type;
  private java.util.Date date;
  private Time hour;
  private String description;
  private int gameMinute;
  private EventCalender eventCalender;



  public GameEvent(EventEnum aType, java.util.Date aDate, Time aHour, String aDescription, int aGameMinute, EventCalender aEventCalender)
  {
    type = aType;
    date = aDate;
    hour = aHour;
    description = aDescription;
    gameMinute = aGameMinute;
    if(aEventCalender!=null){
      setEventCalender(aEventCalender);
    }
  }

  public boolean setType(EventEnum aType)
  {
    boolean wasSet = false;
    type = aType;
    wasSet = true;
    return wasSet;
  }

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setHour(Time aHour)
  {
    boolean wasSet = false;
    hour = aHour;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setGameMinute(int aGameMinute)
  {
    boolean wasSet = false;
    gameMinute = aGameMinute;
    wasSet = true;
    return wasSet;
  }

  public EventEnum getType()
  {
    return type;
  }

  public java.util.Date getDate()
  {
    return date;
  }

  public Time getHour()
  {
    return hour;
  }

  public String getDescription()
  {
    return description;
  }

  public int getGameMinute()
  {
    return gameMinute;
  }

  public EventCalender getEventCalender()
  {
    return eventCalender;
  }

  public boolean setEventCalender(EventCalender aEventCalender)
  {
    boolean wasSet = false;
    if (aEventCalender == null)
    {
      eventCalender=null;
      return wasSet;
    }

    EventCalender existingEventCalender = eventCalender;
    eventCalender = aEventCalender;
    if (existingEventCalender != null && !existingEventCalender.equals(aEventCalender))
    {
      existingEventCalender.removeGameEvent(this);
    }
    eventCalender.addGameEvent(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    EventCalender placeholderEventCalender = eventCalender;
    if(placeholderEventCalender != null)
    {
      placeholderEventCalender.removeGameEvent(this);
      eventCalender=null;
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "description" + ":" + getDescription()+ "," +
            "gameMinute" + ":" + getGameMinute()+
            "  " + "type" + "=" + (getType())+
            "  " + "date" + "=" + (getDate())+
            "  " + "hour" + "=" + (getHour())+
            "  " + "eventCalender = "+(getEventCalender());
  }
  public void ShowGameEvent() {
    System.out.println("Type:");
    System.out.println(this.getType());
    System.out.println();
    System.out.println("Date:");
    System.out.println(this.getDate());
    System.out.println();
    System.out.println("Hour:");
    System.out.println(this.getHour());
    System.out.println();
    System.out.println("Description:");
    System.out.println(getDescription());
    System.out.println();
    System.out.println("Game Minute");
    System.out.println(getGameMinute());
    System.out.println();
  }

  public static GameEvent convertStringToGameEvent(String gameEvent){
    String [] splitArr=gameEvent.split(",");
    String eventType=splitArr[0].substring(new String("EventType: ").length());
    String description=splitArr[1].substring(new String(" Description: ").length());
    String minute=splitArr[2].substring(new String(" Minute: ").length());

    for (Referee referee : DataController.getInstance().getRefereesFromAccounts()){
      for (Match match:referee.getMatchs()) {
          for (GameEvent event : match.getEventCalender().getGameEvents()) {
            if (event.gameMinute == Integer.parseInt(minute)&&convertStringToGameEvent(eventType).equals(event.type)&&event.getDescription().equals(description))
              return event;
          }
        }
      }
      return null;
  }
  public static EventEnum  convertStringToEventEnum(String eventEnum){
    if (eventEnum.equals("Red card"))
      return EventEnum.redCard;
    else if(eventEnum.equals("Goal"))
      return EventEnum.goal;
    else if(eventEnum.equals("Foul"))
      return EventEnum.foul;
    else if(eventEnum.equals("Yellow Card"))
      return EventEnum.yellowCard;
    else if(eventEnum.equals("Offside"))
      return EventEnum.offside;
    else if(eventEnum.equals("Substitution Player"))
      return EventEnum.substitutionPlayer;
    else if(eventEnum.equals("Injury"))
      return EventEnum.injury;
    return null;
}

}