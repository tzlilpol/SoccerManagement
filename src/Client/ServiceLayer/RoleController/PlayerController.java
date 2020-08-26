package Client.ServiceLayer.RoleController;
import Server.BusinessLayer.Controllers.PlayerBusinessController;
import Server.BusinessLayer.RoleCrudOperations.Player;

public class PlayerController {
    PlayerBusinessController playerBusinessController;
    public PlayerController(Player player) {
        playerBusinessController=new PlayerBusinessController(player);
    }

    public PlayerController() {

    }

    public String updateDetails(String birthday, String position, String team)
    {
        return  playerBusinessController.updateDetails(birthday,position,team);
    }
}