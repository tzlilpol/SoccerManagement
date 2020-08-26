package Server.BusinessLayer.RoleCrudOperations;
import Server.BusinessLayer.DataController;
import Server.BusinessLayer.Logger.Logger;
import Server.BusinessLayer.OtherCrudOperations.*;
import Server.BusinessLayer.Pages.Page;
import Client.ServiceLayer.OurSystemClient;

import java.io.Serializable;

import java.util.*;

public class Owner extends Role implements Serializable {
    private Team team;
    private Owner appointedBy;
    private HashMap<TeamManager,List<TeamManager.PermissionEnum>> managerPermissions;
    private List<Team> nonActiveTeams;

    public Owner(String name){setUsername(name);}

    public Owner(String aName, Team team, Owner appointer)
    {
        super(aName);
        setTeam(team);
        if(appointer==null)
            appointedBy=this;
        else
            appointedBy = appointer;
        managerPermissions=new HashMap<>();
        nonActiveTeams=new ArrayList<>();
    }

    public static Owner convertStringToOwner(String userName) {
        return null;
    }

    public Team getTeam()
    {
        return team;
    }

    public void setTeam(Team team)
    {
        this.team = team;
        if(team!=null && team.indexOfOwner(this)==-1)
            team.addOwner(this);
    }

    public Owner getAppointer()
    {
        return appointedBy;
    }

    public void setAppointer(Owner appointedBy)
    {
        this.appointedBy = appointedBy;
    }

    /**
     * add the entered asset to the owner's team
     * assets: coach, player, stadium
     */
    public boolean addAssetToTeam(Object o) throws Exception {
        if (this.team == null)
            throw new Exception("Owner does not have a team");

        else if (o instanceof Coach)
        {
            Logger.getInstance().writeNewLine(this.getName()+" set "+((Coach) o).getUsername() +" as a coach for the team:" + team.getName());
            return team.addCoach((Coach) o);
        }
        else if (o instanceof Player) {
            Logger.getInstance().writeNewLine(this.getName()+" set "+((Player) o).getUsername() +" as a player for the team:" + team.getName());
            return team.addPlayer((Player) o);
        }
        else if (o instanceof Stadium) {
            Logger.getInstance().writeNewLine(this.getName()+" set "+((Stadium) o).getName() +" as the stadium for the team:" + team.getName());
            return team.setStadium((Stadium) o);
        }
        else
            throw new Exception("Input is not a team asset");
    }

    /**
     * removes the entered asset from the owner's team
     * assets: coach, player, stadium
     */
    public boolean removeAssetFromTeam(Object o) throws Exception {
        boolean validRemoval = true;
        if (this.team == null)
            throw new Exception("Owner does not have a team");

        else if (o instanceof Coach) {
            if (team.getCoachs()==null || team.indexOfCoach((Coach) o) == -1)
                throw new Exception("The coach does no exist in the team");
            Logger.getInstance().writeNewLine(this.getUsername()+" removed the Coach"+((Coach) o).getUsername() +" from the team:" + team.getName());
            return team.removeCoach((Coach) o);
        }
        else if (o instanceof Player) {
            if (team.getPlayers()==null || team.indexOfPlayer((Player) o) == -1)
                throw new Exception("The player does no exist in the team");

            Logger.getInstance().writeNewLine(this.getUsername()+" removed the Player"+((Player) o).getUsername() +" from the team:" + team.getName());
            return team.removePlayer((Player) o);
        }
        else if (o instanceof Stadium) {
            if (team.getStadium()==null || !team.getStadium().equals(o))
                throw new Exception("The stadium does no exist in the team");

            Logger.getInstance().writeNewLine(this.getUsername()+" removed the BusinessLayer.OtherCrudOperations.Stadium"+((Stadium) o).getName() +" from the team:" + team.getName());
            return team.setStadium(null);
        }
        else
            throw new Exception("Input is not a team asset");

    }

    /**
     * edits a team asset, according to specified entry
     * assets: BusinessLayer.OtherCrudOperations.Team manager, coach, player, stadium
     * 1:  change name, 2: training for coach, birthday fr player (dd-mm-yyyy), 3:team role for coach, position for player
     */
    public boolean editTeamAsset(Object o, int action, String input) throws Exception {
        if (this.team == null)
            throw new Exception("Owner does not have a team");

        if (o instanceof TeamManager)
        {
            if(action==1)
            {
                Logger.getInstance().writeNewLine(this.getUsername()+" changed the name of the BusinessLayer.OtherCrudOperations.Team manager "+((TeamManager) o).getUsername() +" to be:" + input);
                return ((TeamManager)o).setName(input);
            }
            else
                throw new Exception("a team manager does not have functionality for this action");
        }
        else if (o instanceof Coach)
        {
            if(action==1)
            {
                Logger.getInstance().writeNewLine(this.getUsername()+" changed the name of the BusinessLayer.RoleCrudOperations.Coach "+((Coach) o).getUsername() +" to be:" + input);
                return ((Coach)o).setName(input);
            }
            else if(action==2)
            {
                Logger.getInstance().writeNewLine(this.getUsername()+" changed the training of the BusinessLayer.RoleCrudOperations.Coach "+((Coach) o).getUsername() +" from "+((Coach) o).getTraining()+" to be:" + input);
                return ((Coach)o).setTraining(input);
            }
            else if(action==3)
            {
                Logger.getInstance().writeNewLine(this.getUsername()+" changed the team role of the BusinessLayer.RoleCrudOperations.Coach "+((Coach) o).getUsername() +" from "+((Coach) o).getTeamRole()+" to be:" + input);
                return ((Coach)o).setTeamRole(input);
            }
            else
                throw new Exception("a coach does not have functionality for this action");
        }
        else if (o instanceof Player)
        {
            if(action==1)
            {
                Logger.getInstance().writeNewLine(this.getUsername()+" changed the name of the BusinessLayer.RoleCrudOperations.Player "+((Player) o).getUsername() +" to be:" + input);
                return ((Player)o).setName(input);
            }
            else if(action==2)
            {
                Date newDate= null;
                try {
                    String[] parsedDate=input.split("-");
                    Logger.getInstance().writeNewLine(this.getUsername()+" changed the birthday of the BusinessLayer.RoleCrudOperations.Player "+((Player) o).getUsername() +" from "+((Player) o).getBirthday()+" to be:" + input);
                    newDate = new Date(Integer.parseInt(parsedDate[2]),Integer.parseInt(parsedDate[1]),Integer.parseInt(parsedDate[0]));
                }
                catch (Exception e) {
                    throw new Exception("The date entered is not valid");
                }
                return ((Player)o).setBirthday(newDate);
            }
            else if(action==3)
            {
                try
                {
                    Logger.getInstance().writeNewLine(this.getUsername()+" changed the position of the BusinessLayer.RoleCrudOperations.Player "+((Player) o).getUsername() +" from "+((Player) o).getPosition()+" to be:" + input);
                    return ((Player)o).setPosition(PositionEnum.valueOf(input));
                }
                catch (Exception e)
                {
                    throw new Exception("The position entered is not valid");
                }
            }
            else
                throw new Exception("a player does not have functionality for this action");
        }
        else if (o instanceof Stadium) {
            if(action==1)
            {
                Logger.getInstance().writeNewLine(this.getUsername()+" changed the name of the BusinessLayer.OtherCrudOperations.Stadium "+((Stadium) o).getName() +" to be:" + input);
                return ((Stadium)o).setName(input);
            }
            else
                throw new Exception("a stadium does not have functionality for this action");
        }
        else
            throw new Exception("Input is not a team asset");
    }

    /**
     * returns a list of editing options for the asset
     * assets: BusinessLayer.OtherCrudOperations.Team manager, coach, player, stadium
     * (for it to work, start the list at 1 in the UI)
     */
    public ArrayList<String> showEditingOptions(Object o) throws Exception {
        ArrayList<String> options=new ArrayList();
        if (this.team == null)
            throw new Exception("Owner does not have a team");

        if (o instanceof TeamManager) {
            options.add("name");
        }
        else if (o instanceof Coach) {
            options.add("name");
            options.add("training");
            options.add("teamRole");
        }
        else if (o instanceof Player) {
            options.add("name");
            options.add("birthday");
            options.add("position");
        }
        else if (o instanceof Stadium) {
            options.add("name");
        }
        else
            throw new Exception("Input is not a team asset");
        return options;
    }

    /**
     * adds a new owner to a team
     */
    public boolean appointOwnerToTeam(Account account) throws Exception {
        if(account.hasRoles() && account.checkIfOwner()==null &&account.checkIfTeamManagr()==null && account.checkIfCoach()==null && account.checkIfPlayer()==null)
            throw new Exception("The account already has a role");
        Owner checkOwner = account.checkIfOwner();
        if (checkOwner != null) {
            if (team.indexOfOwner(checkOwner) != -1)
                throw new Exception("The account is already an owner for the team");
        }
        else
        {
            checkOwner = new Owner(account.getRole(0).getName(), this.team, this);
            account.addRole(checkOwner);
        }
        Logger.getInstance().writeNewLine(this.getUsername()+" appointed "+account.getUserName()+" to be an owner on team "+ team.getName());
        return team.addOwner(checkOwner);
    }

    /**
     * remove an owner from the team
     */
    public boolean removeOwnerFromTeam(Owner owner) throws Exception {
        if (owner.team.numberOfOwners() == 1)
            throw new Exception("The team has only one owner");

        if (!owner.getTeam().equals(this.team) || !owner.appointedBy.equals(this))
            throw new Exception(getUsername()+" is not the appointer of "+owner.getUsername());

        owner.team.removeOwner(owner);

        for (Account account : DataController.getInstance().getAccounts())
            for (Role role : account.getRoles())
                if (role instanceof Owner && role.equals(owner))
                {
                    account.removeRole(role);
                    account.removeRole(account.checkIfPlayer());
                    account.removeRole(account.checkIfCoach());
                    account.removeRole(account.checkIfTeamManagr());

                    if(account.checkIfPlayer()!=null) account.checkIfPlayer().delete();
                    if(account.checkIfCoach()!=null) account.checkIfCoach().delete();
                    if(account.checkIfTeamManagr()!=null) account.checkIfTeamManagr().delete();
                    ((Owner) role).delete();

                    Logger.getInstance().writeNewLine(this.getUsername()+" removed "+account.getUserName()+"'s entire permissions");
                    return true;
                }

        return true;
    }

    /**
     * adds a new team manager to the team
     * permissions: manageName, manageManagers, managePage, manageCoaches, managePlayers, manageLeague, manageMatches, manageStadium
     */
    public boolean appointTeamManagerToTeam(Account account,List<TeamManager.PermissionEnum> permissions) throws Exception {
        TeamManager tempTeamManager=account.checkIfTeamManagr();
        if(account.hasRoles())
        {
            if((account.checkIfOwner()!=null && team.getOwners().contains(account.checkIfOwner())) || ((account.checkIfTeamManagr()!=null && team.getTeamManagers().contains(account.checkIfTeamManagr()))))
                throw new Exception("The account is already an owner or a team manager in the team");
            if(!(account.checkIfTeamManagr()!=null || account.checkIfOwner()!=null))
                throw new Exception("The account already has another role");
        }

        if(tempTeamManager==null)
        {
            tempTeamManager=new TeamManager(account.getUserName(),this.team,this);
            account.addRole(tempTeamManager);
        }
        this.managerPermissions.put(tempTeamManager,permissions);

        Logger.getInstance().writeNewLine(this.getUsername()+" appointed "+account.getUserName()+" to be an BusinessLayer.OtherCrudOperations.Team manager on team "+ team.getName());

        return this.team.addTeamManager(tempTeamManager);
    }

    /**
     * removes a team manager from the team
     */
    public boolean removeTeamManagerFromTeam(TeamManager teamManager) throws Exception {
        if (!teamManager.getTeam().equals(this.team))
            throw new Exception("The team does not contain the entered team manager");
        if(!teamManager.getAppointer().equals(this))
            throw new Exception(getUsername()+" is no the oppinter of "+teamManager.getUsername());

        teamManager.delete();

        List<Account> tamp= DataController.getInstance().getAccounts();

        for (Account account : DataController.getInstance().getAccounts())
        {
            if(account.checkIfTeamManagr()!=null && account.checkIfTeamManagr().equals(teamManager))
            {
                account.removeRole(teamManager);
                break;
            }
        }

        Logger.getInstance().writeNewLine(this.getUsername()+" removed "+teamManager.getUsername()+"'s permission as a BusinessLayer.OtherCrudOperations.Team manager on team "+team.getName());

        return true;
    }

    /**
     * checks if manager has a certain permission
     */
    public boolean hasPermission(TeamManager teamManager, TeamManager.PermissionEnum permission)
    {
        return managerPermissions.containsKey(teamManager) && managerPermissions.get(teamManager).contains(permission);
    }

    /**
     * returns all the permissions of a manager
     */
    public List<TeamManager.PermissionEnum> getAllPermitions(TeamManager teamManager)
    {
        return managerPermissions.get(teamManager);
    }

    /**
     * add a permission to a team manager
     */
    public boolean addPermissionToManager(TeamManager teamManager, TeamManager.PermissionEnum permissionEnum) throws Exception {

        if(!teamManager.getAppointer().equals(this))
            throw new Exception(getUsername()+" is no the oppinter of "+teamManager.getUsername());
        List<TeamManager.PermissionEnum> permissions=managerPermissions.get(teamManager);
        if(!permissions.contains(permissionEnum))
            permissions.add(permissionEnum);
        managerPermissions.put(teamManager,permissions);
        return true;
    }

    /**
     * remove a permission from a team manager
     */
    public boolean removePermissionFromManager(TeamManager teamManager, TeamManager.PermissionEnum permissionEnum) throws Exception {

        if(!teamManager.getAppointer().equals(this))
            throw new Exception(getUsername()+" is no the oppinter of "+teamManager.getUsername());
        List<TeamManager.PermissionEnum> permissions=managerPermissions.get(teamManager);
        permissions.remove(permissionEnum);
        managerPermissions.put(teamManager,permissions);
        return true;
    }

    /**
     * deactivates the owner's team
     */
    public boolean deactivateTeam()
    {
        String notification=this.getUsername()+" has deactivated team: "+team.getName();
        for(Owner owner:team.getOwners())
            owner.notifyAccount(owner.getUsername(),notification);
        for(TeamManager teamManager:team.getTeamManagers())
            teamManager.notifyAccount(teamManager.getUsername(),notification);
        for(SystemManager systemManager: DataController.getInstance().getSystemManagersFromAccounts())
            systemManager.notifyAccount(systemManager.getUsername(),notification);

        nonActiveTeams.add(team);
        Logger.getInstance().writeNewLine(this.getUsername()+" has deactivated "+team.getName());
        team.delete();

        return true;
    }

    /**
     * activates the chosen team
     */
    public Team activateTeam(String teamName) throws Exception {
        Team teamToActivate=getNonActiveTeam(teamName);
        if(teamToActivate==null)
            throw new Exception("the entered team is not a previous team for the owner");
        nonActiveTeams.remove(teamToActivate);

        String notification=this.getUsername()+" has activated team: "+teamName;

        for(SystemManager systemManager: DataController.getInstance().getSystemManagersFromAccounts())
            systemManager.notifyAccount(systemManager.getUsername(),notification);

        Logger.getInstance().writeNewLine(this.getUsername()+" has activated "+teamName);

        return teamToActivate;
    }

    /**
     * return the chosen non active team
     */
    public Team getNonActiveTeam(String teamName) throws Exception {
        for(Team team:nonActiveTeams)
        {
            if(team.getName().equals(teamName))
                return team;
        }
        throw new Exception("the entered team is not a previous team for the owner");
    }

    public void ShowOwner(){
        System.out.println("Name:");
        System.out.println(this.getUsername());
        System.out.println("BusinessLayer.OtherCrudOperations.Team owned:");
        System.out.println(this.getTeam().getName());
    }

    public void delete()
    {
        team.removeOwner(this);
    }

    /**
     * creates a new team, provided there is an authorisation from the Association
     */
    public String createTeam(String teamName, League league, Stadium stadium)
    {
        //Server server=Server.getInstance();
        //server.sendMessageToClient("Hiiiiiiiiii");
//        Server.getInstance().sendMessageToClient(this.getUsername(),"INFOOOOOOOOOOO");
        boolean teamExists=false;
        for(String team: DataController.getInstance().getNames("Team"))
        {
            if(team.equals(teamName))
            {
                teamExists=true;
                break;
            }
        }

        if(teamExists)
        {
            (Logger.getInstanceError()).writeNewLineError(this.getUsername()+" tried adding the team "+teamName+" ,but it already exists");
            return "Wrong input, team already exists";
        }

        else if(!AssociationRepresentative.checkIfRequestExists(this,teamName))
        {
            for(String ARuser: DataController.getInstance().getUserNames("AssociationRepresentative"))
            {
                notifyAccount(ARuser,getUsername()+" is requesting to create a new team, teamName: "+teamName);
//                OurSystem.notifyOtherRole(getUsername()+" is requesting to create a new team, teamName: "+teamName,ar);
            }
            AssociationRepresentative.addOpenTeamRequest(this,teamName);
            return "Request sent, waiting for approval";
        }
        else
        {
            if(!AssociationRepresentative.getRequestStatus(this,teamName))
                return "waiting for approval";
            else
            {

                Team team=new Team(teamName,league,stadium);
                new Page(team);
                team.addOwner(this);
                DataController.getInstance().addTeamDC(team);
                DataController.getInstance().setTeamToOwner(this,team);
//                DataController.getInstance().addTeamDC(team);
                AssociationRepresentative.removeOpenTeamRequest(this,teamName);
                Logger.getInstance().writeNewLine(getUsername()+" just opened the team: "+teamName);
                return "Team successfully created";
            }
        }

    }

    /**
     * creates a new player in the team
     */
    public Account createPlayer(String aName, int age, Date aBirthday, PositionEnum aPosition, String userName, String password) throws Exception {
        if(getTeam()==null)
            throw new Exception("Owner does not have a team");
        Player player=new Player(aName,aBirthday,aPosition,getTeam(),null);
        new Page(player);
        Account account=new Account(aName,age,userName,password);
        account.addRole(player);
        SystemManager.createAccount(account);
        Logger.getInstance().writeNewLine(getUsername()+" just created a new player: "+aName+" to team: "+getTeam());
        return account;
    }

    /**
     * creates a new team manager in the team
     */
    public Account createTeamManager(String aName, int age, List<TeamManager.PermissionEnum> permissions, String userName, String password) throws Exception {
        if(getTeam()==null)
            throw new Exception("Owner does not have a team");
        Account account=new Account(aName,age,userName,password);
        appointTeamManagerToTeam(account,permissions);
        SystemManager.createAccount(account);
        Logger.getInstance().writeNewLine(getUsername()+" just a new team manager: "+aName+" to team: "+getTeam());
        return account;
    }

    /**
     * creates a new coach in the team
     */
    public Account createCoach(String aName,int age, String aTraining, String aTeamRole,String userName, String password) throws Exception {
        if(getTeam()==null)
            throw new Exception("Owner does not have a team");
        Coach coach=new Coach(aName,aTraining,aTeamRole,null);
        coach.addTeam(getTeam());
        new Page(coach);
        Account account=new Account(aName,age,userName,password);
        account.addRole(coach);
        SystemManager.createAccount(account);
        Logger.getInstance().writeNewLine(getUsername()+" just a new coach: "+aName+" to team: "+getTeam());
        return account;
    }





}