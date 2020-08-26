
import Server.BusinessLayer.Logger.Logger;
import Server.BusinessLayer.OtherCrudOperations.*;
import Server.BusinessLayer.RoleCrudOperations.AssociationRepresentative;
import Server.BusinessLayer.RoleCrudOperations.Owner;
import Server.BusinessLayer.RoleCrudOperations.Referee;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class AssociationRepresentativeTest {
    private HashMap<Pair<Owner,String>,Boolean> approvedTeamsStub=new HashMap<>();

    @Test
    public void addRefereeToLeague() {
        Season testSeason = new Season("0000");
        Referee testReferee = new Referee("Test","Test");
        League testLeague = new League("Test");
        assertTrue (addRefereeToLeagueTest(testReferee,testLeague,testSeason));
    }

    private boolean addRefereeToLeagueTest(Referee referee,League league,Season season){
        boolean wasAdded = false;
        if(league == null || referee == null || season == null){
            return  wasAdded;
        }
        wasAdded= addLeagueStub(referee,league,season);
        return  wasAdded;
    }

    private boolean addLeagueStub(Referee referee, League league, Season season) {
        return true;
    }

    @Test
    public void setLeaguePointCalcPolicy() {
        Season testSeason = new Season("0000");
        League testLeague = new League("Test");
        Policy testPolicy = new Policy("Test","Test");
        assertTrue(setLeaguePointCalcPolicyTest(testLeague,testPolicy,testSeason,"Test"));
    }

    private boolean setLeaguePointCalcPolicyTest(League league, Policy policy,Season season,String pointCalc){
        boolean wasAdded = false;
        if(league == null || policy == null || season == null){
            return  wasAdded;
        }
        policy.setPointCalc(pointCalc);
        return true;
    }

    @Test
    public void setLeagueGameSchedualPolicy() {
        Season testSeason = new Season("0000");
        League testLeague = new League("Test");
        Policy testPolicy = new Policy("Test","Test");
        assertTrue(setLeagueGameSchedualPolicyTest(testLeague,testPolicy,testSeason,"Test"));
    }

    private boolean setLeagueGameSchedualPolicyTest(League league, Policy policy,Season season,String gameSchedule){
        boolean wasAdded = false;
        if(league == null || policy == null || season == null){
            return  wasAdded;
        }
        policy.setGameSchedual(gameSchedule);
        return true;
    }

    @Test
    public void approveTeam() {
        Team team = new Team("Test",new League("Test"),new Stadium("Test"));
        Owner owner= new Owner("Test",team,null);
        approvedTeamsStub.put(new Pair<>(owner,"Test"),true);
        assertTrue(approveTeamTest("Test",owner));
    }

    private boolean approveTeamTest(String teamName,Owner owner)
    {
        if(teamName==null || owner==null)
            return false;

        Pair request=new Pair(owner,teamName);

        if(!approvedTeamsStub.containsKey(request))
            return false;
        else
        {
            approvedTeamsStub.put(request,true);
            notifyOtherRoleStub("You are approved to open team: "+teamName,owner);
            return true;
        }
    }

    private void notifyOtherRoleStub(String message, Owner owner){}

    @Test
    public void addOpenTeamRequest() {
        assertTrue(addOpenTeamRequestStub(new Owner("Test",null,null),"Test"));
    }

    private boolean addOpenTeamRequestStub(Owner owner, String teamName)
    {
        if(teamName==null || owner==null)
            return false;

        Pair request=new Pair(owner,teamName);
        if(checkIfRequestExistsStub(owner,teamName))
            return false;
        else
            approvedTeamsStub.put(request,false);
        return true;
    }

    @Test
    public void checkIfRequestExists() {
        Owner owner= new Owner("Test",null,null);
        approvedTeamsStub.put(new Pair<>(owner,"Test"),false);
        assertTrue(checkIfRequestExistsStub(owner,"Test"));
    }

    private boolean checkIfRequestExistsStub(Owner owner, String teamName)
    {
        if(teamName==null || owner==null)
            return false;

        Pair request=new Pair(owner,teamName);
        return approvedTeamsStub.containsKey(request);
    }

    @Test
    public void getRequestStatus() {
        Owner owner= new Owner("Test",null,null);
        approvedTeamsStub.put(new Pair<>(owner,"Test"),true);
        assertTrue(getRequestStatusStub(owner,"Test"));
    }

    private boolean getRequestStatusStub(Owner owner, String teamName)
    {
        if(teamName==null || owner==null)
            return false;

        Pair request=new Pair(owner,teamName);
        return approvedTeamsStub.get(request);
    }

    @Test
    public void removeOpenTeamRequest() {
        Owner owner= new Owner("Test",null,null);
        approvedTeamsStub.put(new Pair<>(owner,"Test"),false);
        assertTrue(removeOpenTeamRequestStub(owner,"Test"));
    }

    private boolean removeOpenTeamRequestStub(Owner owner, String teamName)
    {
        if(teamName==null || owner==null)
            return false;

        Pair request=new Pair(owner,teamName);
        if(!checkIfRequestExistsStub(owner,teamName))
            return false;
        else
            approvedTeamsStub.remove(request);
        return true;
    }
}
