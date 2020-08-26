package Server.BusinessLayer.OtherCrudOperations;

import java.io.Serializable;
import java.util.*;

public class EventCalender implements Serializable
{

  private Match match;
  private List<GameEvent> gameEvents;


  public EventCalender(Match aMatch)
  {
    match = aMatch;
    gameEvents = new ArrayList<GameEvent>();
  }


  public Match getMatch()
  {
    return match;
  }

  public GameEvent getGameEvent(int index)
  {
    GameEvent aGameEvent = gameEvents.get(index);
    return aGameEvent;
  }

  public List<GameEvent> getGameEvents()
  {
    List<GameEvent> newGameEvents = Collections.unmodifiableList(gameEvents);
    return newGameEvents;
  }

  public int numberOfGameEvents()
  {
    int number = gameEvents.size();
    return number;
  }

  public boolean hasGameEvents()
  {
    boolean has = gameEvents.size() > 0;
    return has;
  }

  public int indexOfGameEvent(GameEvent aGameEvent)
  {
    int index = gameEvents.indexOf(aGameEvent);
    return index;
  }

  public static int minimumNumberOfGameEvents()
  {
    return 0;
  }

  /**
   * adds an event to the calender
   */
  public boolean addGameEvent(GameEvent aGameEvent)
  {
    boolean wasAdded = true;
    if (gameEvents.contains(aGameEvent)) { return true; }
    EventCalender existingEventCalender = aGameEvent.getEventCalender();
    boolean isNewEventCalender = existingEventCalender != null && !this.equals(existingEventCalender);
    if (isNewEventCalender)
    {
      aGameEvent.setEventCalender(this);
    }
    else
    {
      gameEvents.add(aGameEvent);
    }
    wasAdded = true;
    return wasAdded;
  }

  /**
   * removes an event from the calender, and sets it's calender to null
   * @param aGameEvent
   * @return
   */
  public boolean removeGameEvent(GameEvent aGameEvent)
  {
    gameEvents.remove(aGameEvent);
//    aGameEvent.setEventCalender(null);
    return true;
  }

  /**
   * deletes all the events from the calender
   */
  public void delete()
  {
    for(int i=gameEvents.size(); i > 0; i--)
    {
      GameEvent aGameEvent = gameEvents.get(i - 1);
      aGameEvent.delete();
    }
  }



}