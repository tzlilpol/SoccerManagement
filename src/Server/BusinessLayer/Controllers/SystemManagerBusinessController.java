package Server.BusinessLayer.Controllers;

import Server.BusinessLayer.OtherCrudOperations.Account;
import Server.BusinessLayer.OtherCrudOperations.Alert;
import Server.BusinessLayer.OtherCrudOperations.Team;
import Server.BusinessLayer.RoleCrudOperations.SystemManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SystemManagerBusinessController {

    SystemManager systemManager;

    public SystemManagerBusinessController(SystemManager systemManager) {
        this.systemManager = systemManager;
    }

    public HashMap<String, String> getComplaintAndComments() {
        return systemManager.getComplaintAndComments();
    }

    public void setComplaintAndComments(HashMap<String, String> complaintAndComments) {
        setComplaintAndComments(complaintAndComments);
    }

    /**
     * delete team for Permanently, saving all her actions, notify owner and team manger and delete team's
     * page from all fans.
     */
    public String DeleteTeamPermanently(String teamName)
    {
        Team team= Team.convertStringToTeam(teamName);
        return systemManager.DeleteTeamPermanently(team);
    }

    /**
     * remove account from system
     */
    public String deleteAccount(String username){
        Account account = Account.convertStringToAccount(username);
        return systemManager.deleteAccount(account);
    }

    /**
     * show all the complaints of the accounts in the system
     */
    public void showComplaints(){
        systemManager.showComplaints();
    }

    /**
     * add comment to the complaint
     */
    public void addComplain(String complain){
        systemManager.addComplain(complain);
    }

    /**
     * add comment to the complaint
     */
    public String addComment(String comment,String acomplain){
        return  systemManager.addComment(comment,acomplain);
    }

    /**
     * show system logger
     */
    public String showSystemLog(){
        return systemManager.showSystemLog();
    }

    /**
     * build recommendation system
     */
    public void buildRecommendationSystem(){
        systemManager.buildRecommendationSystem();
    }

    public List<String> getAlerts() {
        List<Alert> alerts=systemManager.getAlertList();
        List<String> strings=new ArrayList<>();
        for(Alert alert:alerts)
        {
            strings.add(alert.getDescription());
        }
        return strings;
    }

}
