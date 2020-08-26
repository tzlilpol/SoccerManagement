package Server.BusinessLayer.OtherCrudOperations;

import Server.BusinessLayer.DataController;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Policy implements Serializable
{
  private String pointCalc;
  private String gameSchedual;
  private SLsettings sLsettings;
  private String id;
  private static int policyIDCounter=DataController.getInstance().getPolicyCounter();

  public Policy(String aPointCalc, String aGameSchedual)
  {
    pointCalc = aPointCalc;
    gameSchedual = aGameSchedual;
    id=(++policyIDCounter)+"";
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public SLsettings getsLsettings() {
    return sLsettings;
  }

  public void setsLsettings(SLsettings sLsettings) {
    this.sLsettings = sLsettings;
  }

  public boolean setPointCalc(String aPointCalc)
  {
    boolean wasSet = false;
    pointCalc = aPointCalc;
    wasSet = true;
    return wasSet;
  }

  public boolean setGameSchedual(String aGameSchedual)
  {
    boolean wasSet = false;
    gameSchedual = aGameSchedual;
    wasSet = true;
    return wasSet;
  }

  public String getPointCalc()
  {
    return pointCalc;
  }

  public String getGameSchedual()
  {
    return gameSchedual;
  }

  public String toString()
  {
    return super.toString() + "["+
            "pointCalc" + ":" + getPointCalc()+ "," +
            "gameSchedual" + ":" + getGameSchedual()+ "]";
  }


  public static Policy convertStringToPolicy(String policyID){
    for (League league : DataController.getInstance().getLeagues()){
      HashMap<Season,SLsettings> sLsetting = league.getsLsetting();
        for(Map.Entry<Season,SLsettings> entry: sLsetting.entrySet()){
          if(entry.getValue().getPolicy().getId().equals(policyID)){
            return entry.getValue().getPolicy();
          }
        }
      }
    return null;
  }

}