package Client.ServiceLayer.RoleController;
import Server.BusinessLayer.Controllers.SystemManagerBusinessController;
import Server.BusinessLayer.RoleCrudOperations.SystemManager;

import java.util.HashMap;
import java.util.List;

public class SystemManagerController {
    SystemManagerBusinessController systemManagerBusinessController;

    public SystemManagerController(SystemManager systemManager) {
        systemManagerBusinessController = new SystemManagerBusinessController(systemManager);
    }

    public SystemManagerController() {

    }

    public HashMap<String, String> getComplaintAndComments() {
        return systemManagerBusinessController.getComplaintAndComments();
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
        if(teamName==null){
            return "";
        }else {
            return systemManagerBusinessController.DeleteTeamPermanently(teamName);
        }
    }

    /**
     * remove account from system
     */
    public String deleteAccount(String username){
        if (username == null) {
            return "";
        }else{
            return systemManagerBusinessController.deleteAccount(username);
        }
    }

    /**
     * show all the complaints of the accounts in the system
     */
    public void showComplaints(){
        systemManagerBusinessController.showComplaints();
    }

    /**
     * add comment to the complaint
     */
    public void addComplain(String complain){
        systemManagerBusinessController.addComplain(complain);
    }

    /**
     * add comment to the complaint
     */
    public String addComment(String comment,String acomplain){
        if (comment == null) {
            return "";
        }else{
            return systemManagerBusinessController.addComment(comment,acomplain);
        }
    }

    /**
     * show system logger
     */
    public String showSystemLog(){
        return systemManagerBusinessController.showSystemLog();
    }

    /**
     * build recommendation system
     */
    public void buildRecommendationSystem(){
        systemManagerBusinessController.buildRecommendationSystem();
    }

    public List<String> getAlerts()
    {
        return systemManagerBusinessController.getAlerts();
    }

}