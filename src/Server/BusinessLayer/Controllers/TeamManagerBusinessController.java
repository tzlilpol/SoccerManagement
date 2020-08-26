package Server.BusinessLayer.Controllers;

import Server.BusinessLayer.DataController;
import Server.BusinessLayer.OtherCrudOperations.*;
import Server.BusinessLayer.RoleCrudOperations.*;

public class TeamManagerBusinessController
{
    TeamManager teamManager;

    public TeamManagerBusinessController(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    public Team getTeam() {
        return teamManager.getTeam();
    }

    public void setTeam(Team team) {
        teamManager.setTeam(team);
    }

    public Owner getAppointer() {
        return teamManager.getAppointer();
    }


    /**
     * allows the team manager to change the name of the team
     */
    public String changeTeamName(String name)
    {
        try {
            teamManager.changeTeamName(name);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "Team name successfully changed";
    }

    /**
     * allows the team manager to add a team manager
     */
    public String addTeamManager(String username)
    {
        TeamManager teamManager=convertStringToTeamManager(username);
        if(teamManager==null)
            return "Wrong input, user name is does not exist";

        try {
            teamManager.addTeamManager(teamManager);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "Team manager successfully added";
    }

    /**
     * allows the team manager to remove a team manager
     */
    public String removeTeamManager(String username)
    {
        TeamManager teamManager=convertStringToTeamManager(username);
        if(teamManager==null)
            return "Wrong input, user name is does not exist";

        try {
            teamManager.removeTeamManager(teamManager);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "Team manager successfully removed";
    }

    /**
     * allows the team manager to add a coach
     */
    public String addCoach(String username)
    {
        Coach coach=CoachBusinessController.convertStringToCoach(username);
        if(coach==null)
            return "Wrong input, user name is does not exist";
        try {
            teamManager.addCoach(coach);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "Coach successfully added";
    }

    /**
     * allows the team manager to remove a coach
     */
    public String removeCoach(String username)
    {
        Coach coach=CoachBusinessController.convertStringToCoach(username);
        if(coach==null)
            return "Wrong input, user name is does not exist";
        try {
            teamManager.removeCoach(coach);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "Coach successfully removed";
    }

    /**
     * allows the team manager to add a player
     */
    public String addPlayer(String username)
    {
        Player player=PlayerBusinessController.convertStringToPlayer(username);
        if(player==null)
            return "Wrong input, user name is does not exist";
        try {
            teamManager.addPlayer(player);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "Player successfully added";
    }

    /**
     * allows the team manager to remove a player
     */
    public String removePlayer(String username)
    {
        Player player=PlayerBusinessController.convertStringToPlayer(username);
        if(player==null)
            return "Wrong input, user name is does not exist";
        try {
            teamManager.removePlayer(player);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "Player successfully removed";
    }

    /**
     * allows the team manager to change the league
     */
    public String setLeague(String leagueName)
    {
        League league=League.convertStringToLeague(leagueName);
        if(league==null)
            return "Wrong input, league name is does not exist";

        try {
            teamManager.setLeague(league);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "League was successfully set";
    }

    /**
     * allows the team manager to remove a match
     */
    public String removeMatch(String aMatch)
    {
        Match match=Match.convertStringToMatch(aMatch);
        if(match==null)
            return "Wrong input, match name is does not exist";

        try {
            teamManager.removeMatch(match);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "Match was successfully removed";
    }


    /**
     * allows the team manager to change the stadium
     */
    public String setStadium(String stadiumName)
    {
        Stadium stadium=Stadium.convertStringToStadium(stadiumName);
        if(stadium==null)
            return "Wrong input, match name is does not exist";

        try {
            teamManager.setStadium(stadium);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "Stadium was successfully set";
    }

    public void ShowTeamManager(){
        teamManager.ShowTeamManager();

    }

    public void delete()
    {
        teamManager.delete();
    }

    public static TeamManager convertStringToTeamManager(String userName){
        for (Account account : DataController.getInstance().getAccounts()){
            if(account.getUserName().equals(userName)){
                for(Role role : account.getRoles()){
                    if(role instanceof TeamManager){
                        return (TeamManager) role;
                    }
                }
            }
        }
        return null;
    }

    public static TeamManager.PermissionEnum convertStringToPermissionEnum(String permission){
        TeamManager.PermissionEnum permissionEnum;
        try {
            permissionEnum=TeamManager.PermissionEnum.valueOf(permission);
        }
        catch (IllegalArgumentException e) {
           return null;
        }
        return permissionEnum;
    }
}
