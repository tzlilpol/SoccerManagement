package Server.BusinessLayer.Controllers;

import Server.BusinessLayer.DataController;
import Server.BusinessLayer.OtherCrudOperations.*;
import Server.BusinessLayer.RoleCrudOperations.Referee;
import Server.BusinessLayer.RoleCrudOperations.Role;

import java.util.HashMap;
import java.util.List;

public class RefereeBusinessController {
    Referee referee;

    public RefereeBusinessController(Referee referee) {
        this.referee = referee;
    }

    public SLsettings getsLsettings() {
        return referee.getsLsettings();
    }

    public String getTraining() {
        return referee.getTraining();
    }

    public void setMatchs(List<Match> matchs) {
        referee.setMatchs(matchs);
    }

    public List<Match> getMatchs() {
        return referee.getMatchs();
    }

    public HashMap<League, Season> getLeagues() {
        return referee.getLeagues();
    }

    public void setLeagues(HashMap<League, Season> leagues) {
        referee.setLeagues(leagues);
    }

    public static int minimumNumberOfLeagues() {
        return 0;
    }

    public boolean addLeague(League aLeague, Season aSeason) {
        return referee.addLeague(aLeague, aSeason);
    }

    /**
     * remove the policy of the season
     */
    public boolean removeLeague(League league, Season aSeason) {
        return removeLeague(league, aSeason);
    }

    public static int minimumNumberOfMatchs() {
        return 0;
    }

    /**
     * add match to referee, is the match is full return false
     *
     * @param aMatch
     * @param mainORline
     * @return
     */
    public boolean addMatch(Match aMatch, String mainORline) {
        return referee.addMatch(aMatch, mainORline);
    }

    /**
     * remove match from referee, remove referee from match
     *
     * @param aMatch
     * @return
     */
    public boolean removeMatch(Match aMatch) {
        return referee.removeMatch(aMatch);
    }

    public void delete() {
        referee.delete();
    }

    public String toString() {
        return referee.toString();
    }

    /**
     * update referee name
     *
     * @param name string
     */
    public String updateDetails(String name) {
        referee.updateDetails(name);
        return "Update successful";
    }

    /**
     * show all matches that referee taking part
     */
    public String displayAllMatches() throws Exception {
        try {
            referee.displayAllMatches();
        } catch (Exception e) {
            e.getMessage();
        }
        return "Display successful";
    }

    /**
     * @param aMatch       that going now
     * @param aType        type of the event
     * @param aDescription of the event
     */
    public String updateEventDuringMatch(String aMatch, String aType, String aDescription) throws Exception {
        try {
            Match match = Match.convertStringToMatch(aMatch);
            EventEnum eventEnum = convertStringToEventEnum(aType);
            referee.updateEventDuringMatch(match, eventEnum, aDescription);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Add successful";
    }

    /**
     * @param aMatch       match that the event accrue
     * @param aGameEvent   the event to edit
     * @param aType        the new Enum type
     * @param aDescription the new description
     * @return
     */
    public String editEventAfterGame(String aMatch, String aGameEvent, String aType, String aDescription) throws Exception {
        Match match = Match.convertStringToMatch(aMatch);
        GameEvent gameEvent = convertStringToGameEvent(aGameEvent);
        EventEnum eventEnum = convertStringToEventEnum(aType);
        try {
            referee.editEventAfterGame(match, gameEvent, eventEnum, aDescription);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Update successful";
    }

    /**
     * show all referee details
     */
    public void ShowReferee() {
        referee.ShowReferee();
    }

    public static Referee convertStringToReferee(String userName) {
        for (Account account : DataController.getInstance().getAccounts()) {
            if (account.getUserName().equals(userName)) {
                for (Role role : account.getRoles()) {
                    if (role instanceof Referee) {
                        return (Referee) role;
                    }
                }
            }
        }
        return null;
    }

    public static EventEnum convertStringToEventEnum(String eventEnum) {
        return EventEnum.valueOf(eventEnum);
    }
    public static GameEvent convertStringToGameEvent(String gameEvent){
        String [] splitArr=gameEvent.split(",");
        String eventType=splitArr[0].substring(new String("EventType: ").length());
        String description=splitArr[1].substring(new String(" Description: ").length());
        String minute=splitArr[2].substring(new String(" Minute: ").length());
        return new GameEvent(convertStringToEventEnum(eventType),null,null,description,Integer.parseInt(minute),null);
    }
    public List<String> getEvantsByMatch(String matchString){
//        List<String> list=new ArrayList<>();

//
//        List<Match> matches=referee.getMatchs();
//
//        for(Match match:matches){
//            if(match.getHomeTeam().getName().equals(homeTeam)&&match.getAwayTeam().getName().equals(awayTeam)
//                    &&match.getDate().getDay()==Integer.parseInt(day)&&match.getDate().getMonth()==Integer.parseInt(month)
//                    &&match.getDate().getYear()==Integer.parseInt(year)){
//                for(GameEvent gameEvent:match.getEventCalender().getGameEvents()){
//                    list.add("EventType: "+gameEvent.getType().toString()+", Description: "+gameEvent.getDescription()+", Minute: "+gameEvent.getGameMinute());
//                }
//            }
//        }
//
//        return list;
        String homeTeam=matchString.substring(matchString.indexOf("Teams")+7,matchString.indexOf("against")-1);
        String awayTeam=matchString.substring(matchString.indexOf("against")+8,matchString.indexOf("Date:")-2);
        String date=matchString.substring(matchString.lastIndexOf(":")+2);
        return DataController.getInstance().getGameEventsByMatch(homeTeam,awayTeam,date);

    }
    public List<String> getMatchList(){
//        List<String> list=new ArrayList<>();
//        for(Match match:referee.getMatchs())
//            list.add("Teams: "+match.getHomeTeam().getName()+" against "+match.getAwayTeam().getName()+
//                    ", Date: "+match.getDate().getDay()+"/"+match.getDate().getMonth()+"/"+match.getDate().getYear());
//        return list;
//
//        return null;
        return DataController.getInstance().getAllMatches();
    }

    public String gameReport(String match){
        Match m=Match.convertStringToMatch(match);
        return referee.gameReport(m);
    }
    public void logOff()
    {
        referee.logOff();
    }
}