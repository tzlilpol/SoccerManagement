package Client.ServiceLayer.RoleController;
import Client.Client;
import Server.BusinessLayer.Controllers.OwnerBusinessController;
import Server.BusinessLayer.RoleCrudOperations.Owner;
import javafx.util.Pair;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class OwnerController
{
    OwnerBusinessController ownerBusinessController;

//    public OwnerController(Owner owner)
//    {
//        ownerBusinessController=new OwnerBusinessController(owner);
//    }

    public OwnerController() {

    }


    /**
     * add the entered asset to the owner's team
     * assets: coach, player, stadium
     */
    public String addAssetToTeam(String identifier)
    {

        if(identifier==null)
            return "wrong asset identifier";
        return ownerBusinessController.addAssetToTeam(identifier);
    }

    /**
     * removes the entered asset from the owner's team
     * assets: coach, player, stadium
     */
    public String removeAssetFromTeam(String identifier)
    {
        if(identifier==null)
            return "wrong asset identifier";
        return ownerBusinessController.removeAssetFromTeam(identifier);
    }

    /**
     * edits a team asset, according to specified entry
     * assets: Server.BusinessLayer.OtherCrudOperations.Team manager, coach, player, stadium
     */
    public String editTeamAsset(String identifier, String actionString, String input)
    {
        if(identifier==null)
            return "wrong asset identifier";
        int action;
        try {
            action = Integer.parseInt(actionString);
        }
        catch (NumberFormatException e) {
            return "action is not a valid number";
        }
        return ownerBusinessController.editTeamAsset(identifier,action,input);
    }

    /**
     * returns a list of editing options for the asset
     * assets: Server.BusinessLayer.OtherCrudOperations.Team manager, coach, player, stadium
     * (for it to work, start the list at 1 in the UI)
     */
    public ArrayList<String> showEditingOptions(String identifier)
    {
        if(identifier==null)
        {
            ArrayList<String> tempList=new ArrayList<>();
            tempList.add("wrong asset identifier");
            return tempList;
        }
        return ownerBusinessController.showEditingOptions(identifier);
    }

    /**
     * adds a new owner to a team
     */
    public String appointOwnerToTeam(String userName)
    {
        if(userName==null)
            return "wrong input, username not valid";
        return ownerBusinessController.appointOwnerToTeam(userName);
    }

    /**
     * remove an owner from the team
     */
    public String removeOwnerFromTeam(String userName)
    {
        if(userName==null)
            return "wrong input, username not valid";
        return ownerBusinessController.removeOwnerFromTeam(userName);
    }

    /**
     * adds a new team manager to the team
     * permissions: manageName, manageManagers, managePage, manageCoaches, managePlayers, manageLeague, manageMatches, manageStadium
     */
    public String appointTeamManagerToTeam(String userName, List<String> permissions)
    {
        if(userName==null)
            return "wrong input, username not valid";
        if(permissions==null)
            return "wrong input, permissions are not valid";
        for(String permission:permissions)
            if(permission==null)
                return "wrong input, at least one permission is not valid";
        return ownerBusinessController.appointTeamManagerToTeam(userName,permissions);
    }

    /**
     * removes a team manager from the team
     */
    public String removeTeamManagerFromTeam(String userName)
    {
        if(userName==null)
            return "wrong input, username not valid";
        return ownerBusinessController.removeTeamManagerFromTeam(userName);
    }

    /**
     * checks if manager has a certain permission
     */
    public String hasPermission(String userName, String permission)
    {
        if(userName==null)
            return "wrong input, username not valid";
        if(permission==null)
            return "wrong input, permission is not valid";
        return ownerBusinessController.hasPermission(userName,permission);
    }

    /**
     * returns all the permissions of a manager
     */
    public List<String> getAllPermissions(String userName)
    {
        if(userName==null)
        {
            ArrayList<String> tempList=new ArrayList<>();
            tempList.add("wrong input, username not valid");
            return tempList;
        }
        return ownerBusinessController.getAllPermissions(userName);
    }

    /**
     * add a permission to a team manager
     */
    public String addPermissionToManager(String userName,String permission)
    {
        if(userName==null)
            return "wrong input, username not valid";
        if(permission==null)
            return "wrong input, permission is not valid";
        return ownerBusinessController.addPermissionToManager(userName,permission);
    }

    /**
     * remove a permission from a team manager
     */
    public String removePermissionFromManager(String userName,String permission)
    {
        if(userName==null)
            return "wrong input, username not valid";
        if(permission==null)
            return "wrong input, permission is not valid";
        return ownerBusinessController.removePermissionFromManager(userName,permission);
    }

    /**
     * deactivates the owner's team
     */
    public String deactivateTeam()
    {
        return ownerBusinessController.deactivateTeam();
    }

    /**
     * activates the owner's team
     */
    public String activateTeam(String teamName)
    {
        if(teamName==null)
            return "wrong input, team name not valid";
        return ownerBusinessController.activateTeam(teamName);
    }

    /**
     * return the chosen non active team
     */
    public String getNonActiveTeam(String teamName)
    {
        if(teamName==null)
            return "wrong input, team name not valid";
        return ownerBusinessController.getNonActiveTeam(teamName);
    }

    public void delete()
    {
        ownerBusinessController.delete();
    }

    /**
     * creates a new team, provided there is an authorisation from the Association
     */
    public String createTeam(String teamName, String leagueName, String stadiumName)
    {
        if(teamName==null)
            return "wrong input, team name not valid";
        if(leagueName==null)
            return "wrong input, league name not valid";
        if(stadiumName==null)
            return "wrong input, stadium name not valid";

        List<String> parameters = new LinkedList<>();
        parameters.add(teamName);
        parameters.add(leagueName);
        parameters.add(stadiumName);
        String sendToServer = "Owner@"+Client.getUserName();
        return (String) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("createTeam",parameters)));
        //return ownerBusinessController.createTeam(teamName,leagueName,stadiumName);
    }

    /**
     * creates a new player in the team
     */
    public String createPlayer(String aName, String ageString, String aBirthday, String aPosition, String userName, String password)
    {
        if(aName==null || ageString==null || aBirthday==null || aPosition==null || userName==null || password==null)
            return "wrong input, one of the inputs is not valid";
        int age;
        try {
            age = Integer.getInteger(ageString);
        }
        catch (Exception e) {
            return "wrong input, age is not valid";
        }
        java.util.Date date;
        try {
            date = new java.util.Date(aBirthday);
        }
        catch (Exception e) {
            return "wrong input, date is not valid";
        }

        return ownerBusinessController.createPlayer(userName,age,date,aPosition,userName,password);
    }

    /**
     * creates a new team manager in the team
     */
    public String createTeamManager(String aName, String ageString, List<String> permissions, String userName, String password)
    {
        if(aName==null || ageString==null || permissions==null ||userName==null || password==null)
            return "wrong input, one of the inputs is not valid";

        int age;
        try {
            age = Integer.getInteger(ageString);
        }
        catch (Exception e) {
            return "wrong input, age is not valid";
        }

        return ownerBusinessController.createTeamManager(aName,age,permissions,userName,password);
    }

    /**
     * creates a new coach in the team
     */
    public String createCoach(String aName,String ageString, String aTraining, String aTeamRole,String userName, String password)
    {
        if(aName==null || ageString==null || aTraining==null || aTeamRole==null || userName==null || password==null)
            return "wrong input, one of the inputs is not valid";

        int age;
        try {
            age = Integer.getInteger(ageString);
        }
        catch (Exception e) {
            return "wrong input, age is not valid";
        }
        return ownerBusinessController.createCoach(aName,age,aTraining,aTeamRole,userName,password);
    }


    public void ShowOwner(){
        ownerBusinessController.ShowOwner();
    }

    public HashMap<String, Pair<Method,List<String>>> showUserMethods() throws NoSuchMethodException
    {

        HashMap<String, Pair<Method,List<String>>> options=new HashMap<>();
        List<String> showUserList=new ArrayList<>();
        showUserList.add("Team name");
        showUserList.add("League name@League");
        showUserList.add("Stadium name@Stadium");
        options.put("Create new team",new Pair<>(this.getClass().getDeclaredMethod("createTeam",String.class,String.class,String.class),showUserList));
        return options;
    }

    public List<String> getAlerts()
    {
        List<String> parameters = new LinkedList<>();
        String sendToServer = "Owner@"+Client.getUserName();
        return ( List<String>) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("getAlerts",parameters)));
        //return ownerBusinessController.getAlerts();
    }
    public void logOff()
    {
        List<String> parameters = new LinkedList<>();
        String sendToServer = "Owner@"+Client.getUserName();
        Client.connectToServer(new Pair<>(sendToServer,new Pair<>("logOff",parameters)));
    }
}