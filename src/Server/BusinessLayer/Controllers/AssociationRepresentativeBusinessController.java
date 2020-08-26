package Server.BusinessLayer.Controllers;

import Server.BusinessLayer.OtherCrudOperations.*;
import Server.BusinessLayer.RoleCrudOperations.AssociationRepresentative;
import Server.BusinessLayer.RoleCrudOperations.Owner;
import Server.BusinessLayer.RoleCrudOperations.Referee;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AssociationRepresentativeBusinessController {

    AssociationRepresentative associationRepresentative;

    public AssociationRepresentativeBusinessController(AssociationRepresentative associationRepresentative) {
        this.associationRepresentative = associationRepresentative;
    }

    /**
     * create new league and put teams inside
     */
    public String createNewLeague(String name, List<String> teamNames) {
        List<Team> teams = new LinkedList<>();
        for (String teamName : teamNames) {
            Team team = Team.convertStringToTeam(teamName);
            teams.add(team);
        }
        return associationRepresentative.createNewLeague(name, teams);
    }

    /**
     * set season to league by year
     */
    public String setYearToLeague(String leagueName, String year) {
        League league = League.convertStringToLeague(leagueName);
        return associationRepresentative.setYearToLeague(league, year);
    }

    /**
     * remove specific referee
     */
    public String deleteReferee(String userName) {
        Referee referee = RefereeBusinessController.convertStringToReferee(userName);
        return associationRepresentative.deleteReferee(referee);
    }

    /**
     * creates an account and adds it as a referee to the system
     */
    public String addNewReferee(String training, String name, String stringAge, String userName, String password) {
        int age = Integer.getInteger(stringAge);
        return associationRepresentative.addNewReferee(training, name, age, userName, password);
    }

    /**
     * add referee to a league in specific season , add referee to SLsetting referee list
     */
    public String addRefereeToLeague(String userName, String leagueName, String seasonName) { // to fix uc
        Referee referee = new Referee(userName);
        referee.setUsername(userName);
        League league = new League(leagueName);
        Season season = new Season(seasonName);
        return associationRepresentative.addRefereeToLeague(referee, league, season);
    }

    /**
     * set league pointCalc policy in specific season
     */
    public String setLeaguePointCalcPolicy(String leagueName, String seasonName, String pointCalc) {
        League league = new League(leagueName);
        Season season = new Season(seasonName);
        return associationRepresentative.setLeaguePointCalcPolicy(league, season, pointCalc);
    }

    /**
     * set league game Schedule policy in specific season
     */
    public String setLeagueGameSchedualPolicy(String leagueName, String seasonName, String gameSchedule) {
        League league = new League(leagueName);
        Season season = new Season(seasonName);
        return associationRepresentative.setLeagueGameSchedualPolicy(league, season, gameSchedule);
    }

    /**
     * Sets new season in specific year
     *
     * @param year the year in which the season will take place
     * @return
     */
    public String setNewSeason(String year) {
        return associationRepresentative.setNewSeason(year);
    }

    public double getTaxRate(String amount){
        double revenueAmount = Double.parseDouble(amount);
        return associationRepresentative.getTaxRate(revenueAmount);
    }

    public void addAmountToAssociationBudget(String teamName, String date,String amountName) {
        double amount = Double.parseDouble(amountName);
        associationRepresentative.addAmountToAssociationBudget(teamName, date,amount);
    }

    public void subtractAmountToAssociationBudget(String teamName, String date,String amountName) {
        double amount = Double.parseDouble(amountName);
        associationRepresentative.subtractAmountFromAssociationBudget(teamName, date,amount);
    }

    public String approveTeam(String teamName, String userName) {
        Owner owner = new Owner(userName);
        owner.setUsername(userName);
        return associationRepresentative.approveTeam(teamName, owner);
    }

    public List<String> getAlerts() {
        List<Alert> alerts = associationRepresentative.getAlertList();
        List<String> strings = new ArrayList<>();
        for (Alert alert : alerts) {
            strings.add(alert.getDescription());
        }
        return strings;
    }

    //new!!
    public String scheduleGamesInSeason(String leagueName, String seasonName) {
        League league = new League(leagueName);
        Season season = new Season(seasonName);
        return associationRepresentative.scheduleGamesInSeason(league,season);
    }

    public void logOff()
    {
        associationRepresentative.logOff();
    }
}