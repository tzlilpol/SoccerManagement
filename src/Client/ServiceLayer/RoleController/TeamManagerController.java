package Client.ServiceLayer.RoleController;
import Server.BusinessLayer.Controllers.TeamManagerBusinessController;
import Server.BusinessLayer.RoleCrudOperations.TeamManager;

public class TeamManagerController
{
    TeamManagerBusinessController teamManagerBusinessController;

    public TeamManagerController(TeamManager teamManager) {
//        teamManagerBusinessController = new TeamManagerBusinessController(teamManager);
    }

    public TeamManagerController() {

    }

    /**
     * allows the team manager to change the name of the team
     */
    public String changeTeamName(String name)
    {
        if(name==null)
            return "name is not valid";
        return teamManagerBusinessController.changeTeamName(name);
    }

    /**
     * allows the team manager to add a team manager
     */
    public String addTeamManager(String username)
    {
        if(username==null)
            return "username is not valid";
        return teamManagerBusinessController.addTeamManager(username);
    }

    /**
     * allows the team manager to remove a team manager
     */
    public String removeTeamManager(String username)
    {
        if(username==null)
            return "username is not valid";
        return teamManagerBusinessController.removeTeamManager(username);
    }

    /**
     * allows the team manager to add a coach
     */
    public String addCoach(String username)
    {
        if(username==null)
            return "username is not valid";
        return teamManagerBusinessController.addCoach(username);
    }

    /**
     * allows the team manager to remove a coach
     */
    public String removeCoach(String username)
    {
        if(username==null)
            return "username is not valid";
        return teamManagerBusinessController.removeCoach(username);
    }

    /**
     * allows the team manager to add a player
     */
    public String addPlayer(String username)
    {
        if(username==null)
            return "username is not valid";
        return teamManagerBusinessController.addPlayer(username);
    }

    /**
     * allows the team manager to remove a player
     */
    public String removePlayer(String username)
    {
        if(username==null)
            return "username is not valid";
        return teamManagerBusinessController.removePlayer(username);
    }

    /**
     * allows the team manager to change the league
     */
    public String setLeague(String leagueName)
    {
        if(leagueName==null)
            return "league name is not valid";
        return teamManagerBusinessController.setLeague(leagueName);
    }

    /**
     * allows the team manager to remove a match
     */
    public String removeMatch(String aMatch)
    {
        if(aMatch==null)
            return "match name is not valid";
        return teamManagerBusinessController.removeMatch(aMatch);
    }


    /**
     * allows the team manager to change the stadium
     */
    public String setStadium(String stadiumName)
    {
        if(stadiumName==null)
            return "stadium name is not valid";
        return teamManagerBusinessController.setStadium(stadiumName);
    }

    public void ShowTeamManager(){
        teamManagerBusinessController.ShowTeamManager();

    }

    public void delete()
    {
        teamManagerBusinessController.delete();
    }
}