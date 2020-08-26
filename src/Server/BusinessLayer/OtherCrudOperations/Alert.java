package Server.BusinessLayer.OtherCrudOperations;

import java.io.Serializable;

public class Alert implements Serializable
{

  private String description;

  public Alert(String aDescription)
  {
    description = aDescription;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public String getDescription()
  {
    return description;
  }


  public String toString()
  {
    return "description" + ": " + getDescription();
  }






}