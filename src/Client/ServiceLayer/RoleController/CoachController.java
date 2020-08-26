package Client.ServiceLayer.RoleController;
import Server.BusinessLayer.Controllers.CoachBusinessController;
import Server.BusinessLayer.RoleCrudOperations.Coach;

public class CoachController {
    CoachBusinessController coachBusinessController;
    public CoachController(Coach coach){
        coachBusinessController= new CoachBusinessController(coach);

    }
    public String updateDetails(String training,String teamRole)
    {
        return coachBusinessController.updateDetails(training,teamRole);
    }
}