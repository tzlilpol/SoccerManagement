package Server.BusinessLayer.Controllers;

import Server.BusinessLayer.DataController;
import Server.BusinessLayer.OtherCrudOperations.*;
import Server.BusinessLayer.RoleCrudOperations.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OwnerBusinessController
{
    Owner owner;

    public OwnerBusinessController(Owner owner)
    {
        this.owner = owner;
    }

    public List<String> getAlerts() {
        List<Alert> alerts=owner.getAlertList();
        List<String> strings=new ArrayList<>();
        for(Alert alert:alerts)
        {
            strings.add(alert.getDescription());
        }
        return strings;
    }
    public void logOff()
    {
        owner.logOff();
    }

    public Team getTeam()
    {
        return owner.getTeam();
    }

    public void setTeam(String teamName)
    {
        Team team = Team.convertStringToTeam(teamName);
        owner.setTeam(team);
    }

    public Owner getAppointer()
    {
        return owner.getAppointer();
    }

    public void setAppointer(String userName)
    {
        Owner appointedBy = convertStringToOwner(userName);
        owner.setAppointer(appointedBy);
    }

    /**
     * add the entered asset to the owner's team
     * assets: coach, player, stadium
     */
    public String addAssetToTeam(String identifier)
    {
        Object coach=CoachBusinessController.convertStringToCoach(identifier);
        Object player=PlayerBusinessController.convertStringToPlayer(identifier);
        Object stadium=Stadium.convertStringToStadium(identifier);

        try {
            if(coach!=null)
                owner.addAssetToTeam(coach);
            else if(player!=null)
                owner.addAssetToTeam(player);
            else if(stadium!=null)
                owner.addAssetToTeam(stadium);
            else
                return "an not applicable asset was inserted";
        }
        catch (Exception e) {
            return e.getMessage();
        }

        return "Asset was added to the team successfully";
    }

    /**
     * removes the entered asset from the owner's team
     * assets: coach, player, stadium
     */
    public String removeAssetFromTeam(String identifier)
    {
        Object coach=CoachBusinessController.convertStringToCoach(identifier);
        Object player=PlayerBusinessController.convertStringToPlayer(identifier);
        Object stadium=Stadium.convertStringToStadium(identifier);

        try {
            if(coach!=null)
                owner.removeAssetFromTeam(coach);
            else if(player!=null)
                owner.removeAssetFromTeam(player);
            else if(stadium!=null)
                owner.removeAssetFromTeam(stadium);
            else
                return "an not applicable asset was inserted";
        }
        catch (Exception e) {
            return e.getMessage();
        }

        return "Asset was removed to the team successfully";
    }

    /**
     * edits a team asset, according to specified entry
     * assets: Team manager, coach, player, stadium
     */
    public String editTeamAsset(String identifier, int action, String input)
    {
        Object coach=CoachBusinessController.convertStringToCoach(identifier);
        Object player=PlayerBusinessController.convertStringToPlayer(identifier);
        Object teamManager=TeamManagerBusinessController.convertStringToTeamManager(identifier);
        Object stadium=Stadium.convertStringToStadium(identifier);

        try {
            if(coach!=null)
                owner.editTeamAsset(coach,action,input);
            else if(player!=null)
                owner.editTeamAsset(player,action,input);
            else if(teamManager!=null)
                owner.editTeamAsset(teamManager,action,input);
            else if(stadium!=null)
                owner.editTeamAsset(stadium,action,input);
            else
                return "an not applicable asset was inserted";
        }
        catch (Exception e) {
            return e.getMessage();
        }

        return "Asset was edited successfully";
    }

    /**
     * returns a list of editing options for the asset
     * assets: Team manager, coach, player, stadium
     * (for it to work, start the list at 1 in the UI)
     */
    public ArrayList<String> showEditingOptions(String identifier)
    {
        ArrayList<String> ans=new ArrayList<>();
        Object coach=CoachBusinessController.convertStringToCoach(identifier);
        Object player=PlayerBusinessController.convertStringToPlayer(identifier);
        Object teamManager=TeamManagerBusinessController.convertStringToTeamManager(identifier);
        Object stadium=Stadium.convertStringToStadium(identifier);

        try {
            if(coach!=null)
                ans=owner.showEditingOptions(coach);
            else if(player!=null)
                ans=owner.showEditingOptions(player);
            else if(teamManager!=null)
                ans=owner.showEditingOptions(teamManager);
            else if(stadium!=null)
                ans=owner.showEditingOptions(stadium);
            else
                ans.add("an not applicable asset was inserted");
        }
        catch (Exception e) {
            ans.add(e.getMessage());
        }

        return ans;
    }

    /**
     * adds a new owner to a team
     */
    public String appointOwnerToTeam(String userName)
    {
        Account account = Account.convertStringToAccount(userName);
        if(account==null)
            return "not a valid account";

        try {
            owner.appointOwnerToTeam(account);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "The owner was successfully appointed to the team";
    }

    /**
     * remove an owner from the team
     */
    public String removeOwnerFromTeam(String userName)
    {
        Owner owner = convertStringToOwner(userName);
        if(owner==null)
            return "not a valid account";
        try {
            owner.removeOwnerFromTeam(owner);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "The owner was successfully removed from the tea ";
    }

    /**
     * adds a new team manager to the team
     * permissions: manageName, manageManagers, managePage, manageCoaches, managePlayers, manageLeague, manageMatches, manageStadium
     */
    public String appointTeamManagerToTeam(String userName, List<String> permissions)
    {
        Account account = Account.convertStringToAccount(userName);
        if(account==null)
            return "not a valid account";

        List<TeamManager.PermissionEnum> permissionEnums=new ArrayList<>();
        for(String permission:permissions)
        {
            TeamManager.PermissionEnum currPer=TeamManagerBusinessController.convertStringToPermissionEnum(permission);
            if(currPer==null)
                return "one of the permissions is not valid";
            permissionEnums.add(currPer);
        }

        try {
            owner.appointTeamManagerToTeam(account,permissionEnums);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "The team manager was successfully appointed to the team";
    }

    /**
     * removes a team manager from the team
     */
    public String removeTeamManagerFromTeam(String userName)
    {
        TeamManager teamManager=TeamManagerBusinessController.convertStringToTeamManager(userName);
        if(teamManager==null)
            return "not a valid account";
        try {
            owner.removeTeamManagerFromTeam(teamManager);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "The team manager was successfully removed from the team";
    }

    /**
     * checks if manager has a certain permission
     */
    public String hasPermission(String userName, String permission)
    {
        TeamManager teamManager=TeamManagerBusinessController.convertStringToTeamManager(userName);
        TeamManager.PermissionEnum currPer=TeamManagerBusinessController.convertStringToPermissionEnum(permission);
        if(teamManager==null)
            return "not a valid account";
        else if(currPer==null)
            return "not a valid permission";
        else if(owner.hasPermission(teamManager,currPer))
            return "true";
        else
            return "false";
    }

    /**
     * returns all the permissions of a manager
     */
    public List<String> getAllPermissions(String userName)
    {
        List<String> ans=new ArrayList<>();
        List<TeamManager.PermissionEnum> permissionEnums;
        TeamManager teamManager=TeamManagerBusinessController.convertStringToTeamManager(userName);
        if(teamManager==null)
            ans.add("not a valid account");
        permissionEnums=owner.getAllPermitions(teamManager);
        for(TeamManager.PermissionEnum permissionEnum:permissionEnums)
            ans.add(permissionEnum.name());
        return ans;
    }

    /**
     * add a permission to a team manager
     */
    public String addPermissionToManager(String userName,String permission)
    {
        TeamManager teamManager=TeamManagerBusinessController.convertStringToTeamManager(userName);
        TeamManager.PermissionEnum currPer=TeamManagerBusinessController.convertStringToPermissionEnum(permission);
        if(teamManager==null)
            return "not a valid account";
        else if(currPer==null)
            return "not a valid permission";
        try {
            owner.addPermissionToManager(teamManager,currPer);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "Permission was added successfully to the team manager";

    }

    /**
     * remove a permission from a team manager
     */
    public String removePermissionFromManager(String userName,String permission)
    {
        TeamManager teamManager=TeamManagerBusinessController.convertStringToTeamManager(userName);
        TeamManager.PermissionEnum currPer=TeamManagerBusinessController.convertStringToPermissionEnum(permission);
        if(teamManager==null)
            return "not a valid account";
        else if(currPer==null)
            return "not a valid permission";
        try {
            owner.removePermissionFromManager(teamManager,currPer);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "Permission was removed successfully from the team manager";
    }

    /**
     * deactivates the owner's team
     */
    public String deactivateTeam()
    {
       if(owner.deactivateTeam())
           return "The team deactivated successfully";
       else
           return "The team failed to deactivate";
    }

    /**
     * activates the owner's team
     */
    public String activateTeam(String teamName)
    {
        Team ans;
        try {
            ans=owner.activateTeam(teamName);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "The team activated successfully";
    }

    /**
     * return the chosen non active team
     */
    public String  getNonActiveTeam(String teamName)
    {
        Team ans;
        try {
            ans=owner.getNonActiveTeam(teamName);
        } catch (Exception e) {
            return e.getMessage();
        }
        return ans.getName();
    }

    public void delete()
    {
        owner.delete();
    }

    /**
     * creates a new team, provided there is an authorisation from the Association
     */
    public String createTeam(String teamName, String leagueName, String stadiumName)
    {
        League league = new League(leagueName);
        Stadium stadium=new Stadium(stadiumName);
        if(league==null)
            return "Wrong input, not such league";
        if(stadium==null)
            return "Wrong input, not such stadium";

        return owner.createTeam(teamName,league,stadium);
    }

    /**
     * creates a new player in the team
     */
    public String createPlayer(String aName, int age, Date aBirthday, String aPosition, String userName, String password)
    {
        Account ans;
//        int age = Integer.getInteger(ageString);
        PositionEnum position=PlayerBusinessController.convertStringToPositionEnum(aPosition);

        try {
            ans = owner.createPlayer(userName,age,aBirthday,position,userName,password);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "Player was successfully created";
    }

    /**
     * creates a new team manager in the team
     */
    public String createTeamManager(String aName, int age, List<String> permissionsStrings, String userName, String password)
    {
        Account ans;
        List<TeamManager.PermissionEnum> permissionEnums=new ArrayList<>();
//        int age = Integer.getInteger(ageString);
        for(String permission:permissionsStrings)
        {
            TeamManager.PermissionEnum currPer=TeamManagerBusinessController.convertStringToPermissionEnum(permission);
            if(currPer==null)
                return "one of the permissions is not valid";
            permissionEnums.add(currPer);
        }
        try {
            ans = owner.createTeamManager(aName,age,permissionEnums,userName,password);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "Team manager was successfully created";
    }

    /**
     * creates a new coach in the team
     */
    public String createCoach(String aName,int age, String aTraining, String aTeamRole,String userName, String password)
    {
        Account ans;
        try {
            ans = owner.createCoach(aName,age,aTraining,aTeamRole,userName,password);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "Coach was successfully created";
    }


    public void ShowOwner(){
        owner.ShowOwner();
    }

    /**
     * converts a string into an owner
     * @param userName
     */
    public static Owner convertStringToOwner(String userName){
        for (Account account : DataController.getInstance().getAccounts()){
            if(account.getUserName().equals(userName)){
                for(Role role : account.getRoles()){
                    if(role instanceof Owner){
                        return (Owner) role;
                    }
                }
            }
        }
        return null;
    }


}
