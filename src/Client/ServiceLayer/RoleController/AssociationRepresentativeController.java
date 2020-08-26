package Client.ServiceLayer.RoleController;
import Client.Client;
import Server.BusinessLayer.Controllers.AssociationRepresentativeBusinessController;
import Server.BusinessLayer.RoleCrudOperations.AssociationRepresentative;
import javafx.util.Pair;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AssociationRepresentativeController {

    AssociationRepresentativeBusinessController associationRepresentativeBusinessController;

//    public AssociationRepresentativeController(AssociationRepresentative associationRepresentative) {
//        associationRepresentativeBusinessController = new AssociationRepresentativeBusinessController(associationRepresentative);
//    }

    public AssociationRepresentativeController() {

    }

    /**
     * create new league and put teams inside
     */
    public String createNewLeague(String name, List<String> teamNames) {
        if(teamNames == null || name == null){
            return  "null";
        }else {
            return associationRepresentativeBusinessController.createNewLeague(name, teamNames);
        }
    }

    /**
     * set season to league by year
     */
    public String setYearToLeague(String leagueName, String year) {
        if(leagueName == null || year == null){
            return  "";
        }else{

            return associationRepresentativeBusinessController.setYearToLeague(leagueName, year);
        }
    }

    /**
     * remove specific referee
     */
    public String deleteReferee(String userName) {
        if(userName == null){
            return "";
        }else{
            return associationRepresentativeBusinessController.deleteReferee(userName);
        }
    }

    /**
     * creates an account and adds it as a referee to the system
     */
    public String addNewReferee(String training, String name, String stringAge, String userName, String password) {
        if(training == null || name == null || stringAge==null || userName==null || password ==null)  {
            return "";
        }else{
            return associationRepresentativeBusinessController.addNewReferee(training, name, stringAge, userName, password);
        }

    }

    /**
     * add referee to a league in specific season , add referee to SLsetting referee list
     */
    public String addRefereeToLeague(String userName, String leagueName, String seasonName) {
        if(userName == null || leagueName == null || seasonName == null){
            return  "";
        }
        List<String> parameters = new LinkedList<>();
        parameters.add(userName);
        parameters.add(leagueName);
        parameters.add(seasonName);
        String sendToServer = "AssociationRepresentative@"+ Client.getUserName();
        return (String) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("addRefereeToLeague",parameters)));
        //return associationRepresentativeBusinessController.addRefereeToLeague(userName, leagueName, seasonName);
    }

    /**
     * set league pointCalc policy in specific season
     */
    public String setLeaguePointCalcPolicy(String leagueName, String seasonName, String pointCalc) {
        if(leagueName == null || seasonName == null || pointCalc ==null){
            return  "";
        }else{
            List<String> parameters = new LinkedList<>();
            parameters.add(leagueName);
            parameters.add(seasonName);
            parameters.add(pointCalc);
            String sendToServer = "AssociationRepresentative@"+ Client.getUserName();
            return (String) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("setLeaguePointCalcPolicy",parameters)));
            //return associationRepresentativeBusinessController.setLeaguePointCalcPolicy(leagueName, policyID, seasonName, pointCalc);
        }
    }

    /**
     * set league game Schedule policy in specific season
     */
    public String setLeagueGameSchedualPolicy(String leagueName, String seasonName, String gameSchedule) {
        if(leagueName == null || seasonName == null || gameSchedule ==null){
            return  "";

        }else{
            List<String> parameters = new LinkedList<>();
            parameters.add(leagueName);
            parameters.add(seasonName);
            parameters.add(gameSchedule);
            String sendToServer = "AssociationRepresentative@"+ Client.getUserName();
            return (String) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("setLeagueGameSchedualPolicy",parameters)));
            //return associationRepresentativeBusinessController.setLeagueGameSchedualPolicy(leagueName, policyID, seasonName, gameSchedule);
        }
    }

    /**
     * Sets new season in specific year
     *
     * @param year the year in which the season will take place
     * @return
     */
    public String setNewSeason(String year) {
        if(year==null){
            return "";
        }
        return associationRepresentativeBusinessController.setNewSeason(year);
    }

    public void getTaxRate(String amountName) {
        associationRepresentativeBusinessController.getTaxRate(amountName);
    }

    public void addAmountToAssociationBudget(String teamName, String date,String amountName) {
        associationRepresentativeBusinessController.addAmountToAssociationBudget(teamName, date,amountName);
    }

    public void subtractAmountToAssociationBudget(String teamName, String date,String amountName) {
        associationRepresentativeBusinessController.subtractAmountToAssociationBudget(teamName, date,amountName);
    }

    public String approveTeam(String teamName, String userName) {
        if (teamName == null || userName == null) {
            return "";
        } else {
            List<String> parameters = new LinkedList<>();
            parameters.add(teamName);
            parameters.add(userName);
            String sendToServer = "AssociationRepresentative@"+ Client.getUserName();
            return (String) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("approveTeam",parameters)));

            //return associationRepresentativeBusinessController.approveTeam(teamName, userName);
        }
    }

    public HashMap<String, Pair<Method,List<String>>> showUserMethods() throws NoSuchMethodException
    {
        HashMap<String, Pair<Method,List<String>>> options=new HashMap<>();
        List<String> showUserList=new ArrayList<>();
        showUserList.add("League name@League");
        showUserList.add("Season name@Season");
        showUserList.add("Game Schedule@GameSchedual");
        options.put("Set Schedule Policy to League",new Pair<>(this.getClass().getDeclaredMethod("setLeagueGameSchedualPolicy",String.class,String.class,String.class),showUserList));

        showUserList=new ArrayList<>();
        showUserList.add("League name@League");
        showUserList.add("Season name@Season");
        showUserList.add("Point Calculation@PointCalc");
        options.put("Set Point Calculation Policy to League",new Pair<>(this.getClass().getDeclaredMethod("setLeaguePointCalcPolicy",String.class,String.class,String.class),showUserList));

        showUserList=new ArrayList<>();
        showUserList.add("Referee name@Referee");
        showUserList.add("League name@League");
        showUserList.add("Season name@Season");
        options.put("Add a Referee to a League",new Pair<>(this.getClass().getDeclaredMethod("addRefereeToLeague",String.class,String.class,String.class),showUserList));

        showUserList=new ArrayList<>();
        showUserList.add("Team name");
        showUserList.add("Owner username@Account");
        options.put("Approve the opening of a team",new Pair<>(this.getClass().getDeclaredMethod("approveTeam",String.class,String.class),showUserList));

        showUserList=new ArrayList<>();
        showUserList.add("League name@League");
        showUserList.add("Season name@Season");
        options.put("Activate game scheduling algorithm",new Pair<>(this.getClass().getDeclaredMethod("scheduleGamesInSeason",String.class,String.class),showUserList));

        return options;
    }

    public List<String> getAlerts()
    {
        List<String> parameters = new LinkedList<>();
        String sendToServer = "AssociationRepresentative@"+Client.getUserName();
        return ( List<String>) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("getAlerts",parameters)));
    }

    //new!!
    public String scheduleGamesInSeason(String leagueName, String seasonName) {
        if(leagueName == null || seasonName == null){
            return  "";

        }else{
            List<String> parameters = new LinkedList<>();
            parameters.add(leagueName);
            parameters.add(seasonName);
            String sendToServer = "AssociationRepresentative@"+ Client.getUserName();
            return (String) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("scheduleGamesInSeason",parameters)));
        }
    }
        public void logOff()
    {
        List<String> parameters = new LinkedList<>();
        String sendToServer = "AssociationRepresentative@"+Client.getUserName();
        Client.connectToServer(new Pair<>(sendToServer,new Pair<>("logOff",parameters)));
    }

}
