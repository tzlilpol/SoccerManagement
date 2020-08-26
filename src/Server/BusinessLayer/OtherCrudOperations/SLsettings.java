package Server.BusinessLayer.OtherCrudOperations;
import Server.BusinessLayer.RoleCrudOperations.Referee;

import java.io.Serializable;
import java.util.*;

public class SLsettings implements Serializable {

    private List<Referee> referees;
    private Policy policy;

    public SLsettings(Policy policy)
    {
        this.policy = policy;
        referees = new ArrayList<>();
        policy.setsLsettings(this);
    }

    public void setRefereeList(List<Referee> refereeList) {
        this.referees = refereeList;
    }

    public Policy getPolicy()
    {
        if(policy.getGameSchedual()==null && policy.getPointCalc()==null)
            return null;
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }


    public Referee getReferee(int index)
    {
        Referee aReferee = referees.get(index);
        return aReferee;
    }

    public List<Referee> getReferees()
    {
        List<Referee> newReferees = Collections.unmodifiableList(referees);
        return newReferees;
    }

    public int numberOfReferees()
    {
        int number = referees.size();
        return number;
    }

    public boolean hasReferees()
    {
        boolean has = referees.size() > 0;
        return has;
    }

    public int indexOfReferee(Referee aReferee)
    {
        int index = referees.indexOf(aReferee);
        return index;
    }

    /**
     * adds referee to team, adds the team to the referee
     * @param aReferee
     * @return
     */
    public boolean addReferee(Referee aReferee)
    {
        boolean wasAdded = false;
        if (referees.contains(aReferee)) { return false; }
        referees.add(aReferee);
        return wasAdded;
    }

    /**
     * removes referee from the team, removes the team from the referee
     * @param aReferee
     * @return
     */
    public boolean removeReferee(Referee aReferee)
    {
        boolean wasRemoved = false;
        if (!referees.contains(aReferee))
        {
            return wasRemoved;
        }

        int oldIndex = referees.indexOf(aReferee);
        referees.remove(oldIndex);
        return wasRemoved;
    }




}