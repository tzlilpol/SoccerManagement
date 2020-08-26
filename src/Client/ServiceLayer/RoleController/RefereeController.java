package Client.ServiceLayer.RoleController;
import Client.Client;
import Server.BusinessLayer.Controllers.RefereeBusinessController;
import Server.BusinessLayer.RoleCrudOperations.Referee;
import javafx.util.Pair;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class RefereeController {
    RefereeBusinessController refereeBusinessController;

    /**
     *
     * @param referee
     */
    public RefereeController(Referee referee) {
        refereeBusinessController=new RefereeBusinessController(referee);
    }

    public RefereeController() {

    }

    /**
     * update referee name uc 10.1
     * @param name string
     */
    public String updateDetails(String name){
        if(containsDigit(name))
            return "Referee name contains invalid characters";
        return refereeBusinessController.updateDetails(name);
    }

    /**
     * show all matches that referee taking part uc 10.2
     */
    public String displayAllMatches() throws Exception {
        return refereeBusinessController.displayAllMatches();
    }

    /**
     * uc 10.3
     * @param aMatch that going now
     * @param aType type of the event
     * @param aDescription of the event
     */
    public String updateEventDuringMatch(String aMatch, String aType, String aDescription) throws Exception {
        if (aMatch.equals(""))
            return "Please select match";
        else if (aType.equals(""))
            return "Please select game event you wont to add";
        else if(containsDigit(aType))
            return "The event type contains invalid characters";
        else if(aDescription.length()<5)
            return "Description must contain at least 5 characters";
        List<String> parameters = new LinkedList<>();
        parameters.add(aMatch);
        parameters.add(aType);
        parameters.add(aDescription);
        String sendToServer = "Referee@"+ Client.getUserName();
        return (String) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("updateEventDuringMatch",parameters)));
    }

    /**
     *uc 10.4
     * @param aMatch match that the event accrue
     * @param aGameEvent the event to edit
     * @param aType the new Enum type
     * @param aDescription the new description
     * @return
     */
    public String editEventAfterGame(String aMatch, String aGameEvent, String aType, String aDescription) throws Exception {
        if (aMatch.equals(""))
            return "Please select match";
        else if (aGameEvent.equals(""))
            return "Please select game event you want to change";
        else if (aType.equals(""))
            return "Please select updated game event";
        else if(containsDigit(aType))
            return "The event type contains invalid characters";
        else if(aDescription.length()<5)
            return "Description must contain at least 5 characters";
        List<String> parameters = new LinkedList<>();
        parameters.add(aMatch);
        parameters.add(aGameEvent);
        parameters.add(aType);
        parameters.add(aDescription);
        String sendToServer = "Referee@"+ Client.getUserName();
        return (String) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("editEventAfterGame",parameters)));
        //return refereeBusinessController.editEventAfterGame(aMatch,aGameEvent,aType,aDescription);
    }
    public List<String> getEvantsByMatch(String matchString)
    {
        List<String> parameters = new LinkedList<>();
        parameters.add(matchString);
        String sendToServer = "Referee@"+ Client.getUserName();
        return (List<String>) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("getEvantsByMatch",parameters)));
        //return refereeBusinessController.getEvantsByMatch(matchString);
    }
    public String gameReport(String matchString)
    {
        if (matchString.equals(""))
            return "Please select match";
        List<String> parameters = new LinkedList<>();
        parameters.add(matchString);
        String sendToServer = "Referee@"+ Client.getUserName();
        return (String) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("gameReport",parameters)));
    }
    public List<String> getMatchList(){
        List<String> parameters = new LinkedList<>();
        String sendToServer = "Referee@"+ Client.getUserName();
        return (List<String>) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("getMatchList",parameters)));
        //return refereeBusinessController.getMatchList();
    }
    public boolean containsDigit(String s) {
        boolean containsDigit = false;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
        }
        return containsDigit;
    }
    public void logOff()
    {
        List<String> parameters = new LinkedList<>();
        String sendToServer = "Referee@"+Client.getUserName();
        Client.connectToServer(new Pair<>(sendToServer,new Pair<>("logOff",parameters)));
    }

    public HashMap<String, Pair<Method,List<String>>> showUserMethods() throws NoSuchMethodException
    {
        HashMap<String, Pair<Method,List<String>>> options=new HashMap<>();
        List<String> showUserList=new ArrayList<>();
        showUserList.add("Match@Match");
        showUserList.add("Event type@EventEnum");
        showUserList.add("Description");
        options.put("Add event during a match",new Pair<>(this.getClass().getDeclaredMethod("updateEventDuringMatch",String.class,String.class,String.class),showUserList));

        showUserList=new ArrayList<>();
        showUserList.add("Match@Match");
        showUserList.add("Event@GameEvent");
        showUserList.add("Event type@EventEnum");
        showUserList.add("Description");
        options.put("Edit an event after the mach",new Pair<>(this.getClass().getDeclaredMethod("editEventAfterGame",String.class,String.class,String.class,String.class),showUserList));

        showUserList=new ArrayList<>();
        showUserList.add("Match@Match");
        options.put("Show a game report",new Pair<>(this.getClass().getDeclaredMethod("gameReport",String.class),showUserList));

        return options;
    }

}