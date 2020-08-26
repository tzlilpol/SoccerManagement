package Server.BusinessLayer.Pages;
import Server.BusinessLayer.DataController;
import Server.BusinessLayer.OtherCrudOperations.Alert;
import Server.BusinessLayer.OtherCrudOperations.Pageable;
import Server.BusinessLayer.RoleCrudOperations.Fan;

import java.io.Serializable;
import java.util.*;

public class Page implements Serializable
{

  private static int pageIDcounter=DataController.getInstance().getNewPageCounter();
  private Pageable type;
  private List<Fan> fans;
  private int pageID;

  public Page(Pageable pageable)
  {
    setType(pageable);
    fans = new ArrayList<Fan>();
    pageID=(++pageIDcounter);
//    DataController.getInstance().addPage(pageIDcounter);
  }

  public int getPageID() {
    return pageID;
  }

  public Pageable getType() {
    return type;
  }

  public void setType(Pageable type)
  {
    this.type = type;
    if(type==null) return;
    if(type.getPage()==null || !type.getPage().equals(this))
      type.setPage(this);
  }

  public Fan getFan(int index)
  {
    Fan aFan = fans.get(index);
    return aFan;
  }
  public List<Fan> getFans()
  {
    List<Fan> newFans = Collections.unmodifiableList(fans);
    return newFans;
  }

  public int numberOfFans()
  {
    int number = fans.size();
    return number;
  }

  public boolean hasFans()
  {
    boolean has = fans.size() > 0;
    return has;
  }

  public int indexOfFan(Fan aFan)
  {
    int index = fans.indexOf(aFan);
    return index;
  }

  public static int minimumNumberOfFans()
  {
    return 0;
  }

  public boolean addFan(Fan aFan)
  {
    boolean wasAdded = true;
    if (fans.contains(aFan)) { return true; }
    fans.add(aFan);
    if (aFan.indexOfPage(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aFan.addPage(this);
      if (!wasAdded)
      {
        fans.remove(aFan);
      }
    }
    return wasAdded;
  }

  public boolean removeFan(Fan aFan)
  {
    boolean wasRemoved = true;
    if (!fans.contains(aFan))
    {
      return wasRemoved;
    }

    int oldIndex = fans.indexOf(aFan);
    fans.remove(oldIndex);
    if (aFan.indexOfPage(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aFan.removePage(this);
      if (!wasRemoved)
      {
        fans.add(oldIndex,aFan);
      }
    }
    return wasRemoved;
  }

  public void delete()
  {
    ArrayList<Fan> copyOfFans = new ArrayList<Fan>(fans);
    fans.clear();
    for(Fan aFan : copyOfFans)
    {
      aFan.removePage(this);
    }
    type.removePage();
  }

  public void notifyTrackingFans(Alert alert){
    for(Fan fan:fans){
      if(fan.isTrackPersonalPages()){
        fan.addAlert(alert);
      }
    }
  }




}