import Server.BusinessLayer.DataController;
import Server.BusinessLayer.Logger.Logger;
import Server.BusinessLayer.OtherCrudOperations.*;
import Server.BusinessLayer.OurSystemServer;
import Server.BusinessLayer.RoleCrudOperations.*;
import javafx.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.Assert.*;

public class OwnerTest {
    private final ByteArrayOutputStream OS = new ByteArrayOutputStream();
    private final PrintStream PS = System.out;

    Owner owner;
    HashMap<Owner,String> owners;
    List<String> teams;
    HashMap<Pair<Owner,String>,Boolean> approvedTeams;

    @Before
    public void setUp() throws Exception {
        owner = new Owner("sean", null, null);
        owners=new HashMap<>();
        teams=new ArrayList<>();
        approvedTeams=new HashMap<>();
    }

    @Test
    public void createTeam() {
        League league=new League("someLeague");
        Stadium stadium=new Stadium("someStadium");
        //positive
        owners.put(owner,null);
        //1.
        assertEquals("Request sent, waiting for approval",createTeam("myNewTeam",league,stadium,owner));
        assertTrue(approvedTeams.containsKey(new Pair<>(owner,"myNewTeam")));
        //2.
        assertEquals("waiting for approval",createTeam("myNewTeam",league,stadium,owner));
        //3.
        approvedTeams.put(new Pair<>(owner,"myNewTeam"),true);
        assertEquals("Team successfully added",createTeam("myNewTeam",league,stadium,owner));
        assertEquals(owners.get(owner),"myNewTeam");
        assertTrue(teams.contains("myNewTeam"));

        //negative
        assertEquals("Wrong input, team already exists",createTeam("myNewTeam",league,stadium,owner));;
    }
    //*----------------------------------------------stubs--------------------------------------------------------------------*/

    /**
     * creates a new team, provided there is an authorisation from the Association
     */
    public String createTeam(String teamName, League league, Stadium stadium, Owner owner) {
        boolean teamExists = false;
        for (String t : teams) {
            if (t.equals(teamName)) {
                teamExists = true;
                break;
            }
        }
        if (teamExists)
            return "Wrong input, team already exists";

        else if (!checkIfRequestExistsStub(owner, teamName)) {
//            for (AssociationRepresentative ar : DataController.getInstance().getAssiciationRepresentivesFromAccounts()) {
//                OurSystemServer.notifyOtherRole(owner.getName()+" is requesting to create a new team, teamName: "+teamName,ar);
//            }
                addOpenTeamRequestStub(owner, teamName);
            return "Request sent, waiting for approval";
        } else {
            if (!getRequestStatusStub(owner, teamName))
                return "waiting for approval";
            else {
                Team team = new Team(teamName, league, stadium);
                addOwnerStub(owner, team);
                addTeamStub(team);
                removeOpenTeamRequestStub(owner, teamName);
//                Logger.getInstance().writeNewLine(owner.getName() + " just opened the team: " + teamName);
                return "Team successfully added";
            }
        }

    }

    private void addTeamStub(Team team) {
        teams.add(team.getName());
    }

    private void removeOpenTeamRequestStub(Owner owner, String teamName) {
        approvedTeams.remove(new Pair<>(owner,teamName));
    }

    private boolean getRequestStatusStub(Owner owner, String teamName) {
        return approvedTeams.get(new Pair<>(owner,teamName));
    }

    private void addOpenTeamRequestStub(Owner owner, String teamName) {
        approvedTeams.put(new Pair<>(owner,teamName),false);
    }

    private void addOwnerStub(Owner owner, Team team) {
        owners.put(owner,team.getName());
    }

    private boolean checkIfRequestExistsStub(Owner owner, String teamName) {
        return approvedTeams.containsKey(new Pair<>(owner,teamName));
    }

}


    //*------------------------------------------------------------------------------------------------------------------------*/