import Server.BusinessLayer.DataController;
import Server.BusinessLayer.Logger.Logger;
import Server.BusinessLayer.OtherCrudOperations.EventEnum;
import Server.BusinessLayer.OtherCrudOperations.GameEvent;
import Server.BusinessLayer.OtherCrudOperations.Match;
import Server.BusinessLayer.OtherCrudOperations.Team;
import Server.BusinessLayer.RoleCrudOperations.Referee;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RefereeTest {
//    private final ByteArrayOutputStream OS=new ByteArrayOutputStream();
//    private final PrintStream PS=System.out;
//    @Before
//    public void init(){
//        System.setOut(new PrintStream(OS));
//    }

    @Test
    public void gameReportTest(){

        Match match=Match.convertStringToMatch("Teams: Team1 against Team2, Date: 29/05/2020");
        assertTrue(gameReport(match).equals("\tMatch Location:  Stadium1           Date:  29/05/2020           Start Time:  12:00:00\n"+
                "\tHome Team : Team2                     Score:  0\n"+
                "\tAway  Team : Team1                     Score:  0\n"+
                "\tMain Referee :  Referee1X\n"+"" +
                "\tLine Referee 1:  Referee1X         Line Referee 2:  Referee1X\n"+
                "\tGame Events:\n"+
                "\tEventType: Red card, Description: Messei got, Minute: 89\n"));
    }
    public String gameReport(Match match){
        List<String> matchList=getMatchListStub(match.getAwayTeam().getName(),match.getHomeTeam().getName(),match.getDate());
        String report= "\tMatch Location:  "+matchList.get(9)+"           Date:  "+matchList.get(0)+"           Start Time:  "+matchList.get(1)+"\n"+
                "\tHome Team : "+matchList.get(5)+"                     Score:  "+matchList.get(3)+"\n"+
                "\tAway  Team : "+matchList.get(4)+"                     Score:  "+matchList.get(2)+"\n"+
                "\tMain Referee :  "+matchList.get(6)+"\n"+"" +
                "\tLine Referee 1:  "+matchList.get(7)+"         Line Referee 2:  "+matchList.get(8)+"\n"+
                "\tGame Events:\n";
        List<String> events=getEventList(match.getAwayTeam().getName(),match.getHomeTeam().getName(),match.getDate());
        for (String event:events) {
            report+= "\t"+event+"\n";
        }
        return report;
    }
    @Test
    public void updateEventDuringMatchTest(){
        Match match=convertStringToMatchStub("Teams: Team1 against Team2, Date: 29/05/2020");
        try {
            assertTrue(updateEventDuringMatch(match,EventEnum.redCard,"Messei got"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean updateEventDuringMatch(Match match, EventEnum aType, String aDescription) throws Exception {
        boolean wasUpdate=false;
        if(!getMatchStub(match.getAwayTeam().getName(),match.getHomeTeam().getName(),match.getDate()).isEmpty()){
            Date currDate=new Date(System.currentTimeMillis());
            Date gameDate=new SimpleDateFormat("dd/MM/yyyy").parse(match.getDate());
            Time time =getMatchTimeStub(match.getAwayTeam().getName(),match.getHomeTeam().getName(),match.getDate());
            gameDate.setHours(time.getHours());
            gameDate.setMinutes(time.getMinutes());
            gameDate.setSeconds(time.getSeconds());
            if (getDateDiffStub(gameDate,currDate, TimeUnit.MINUTES)<90)
            {
                Time currTime=new Time(Calendar.getInstance().getTimeInMillis());
                GameEvent event=new GameEvent(aType,currDate,currTime,aDescription,(int)(getDateDiffStub(gameDate,currDate,TimeUnit.MINUTES)),match.getEventCalender());
                addGameEventStub(aType.toString(),currTime,aDescription,(int)(getDateDiffStub(gameDate,currDate,TimeUnit.MINUTES)),convertDateToStringStub(gameDate),match.getAwayTeam().getName(),match.getHomeTeam().getName());
                List<String> fans=getNotifiedFansStub();
                for (String fan:fans
                ) {
                    if (isAccountloggedInStub(fan))
                        notifyAccount(fan,"During Match between "
                                +match.getAwayTeam().getName()+" and "+match.getHomeTeam().getName()+" occur event : "+aType.toString()+" at minute : "+event.getGameMinute()+"\nDescription : "+aDescription);
                }
                wasUpdate=true;
                writeNewLine("BusinessLayer.RoleCrudOperations.Referee "+getNameStub()+" update event during the match between: "+match.getHomeTeam().getName()+","+match.getAwayTeam().getName()+" to "+event.getType());
            }
            else {
                writeNewLineError("Referee tried to add event not during the match");
                throw new Exception("Referee tried to add event not during the match");
            }
        }
        else{
            writeNewLineError("Referee didnt take part in this match");
            throw new Exception("Referee didnt take part in this match");
        }
        return wasUpdate;
    }
    @Test
    public void editEventAfterGameTest(){
        Match match=convertStringToMatchStub("Teams: Team1 against Team2, Date: 29/05/2020");
        GameEvent gameEvent=convertStringToGameEventStub("EventType: Red card, Description: Messei got, Minute: 89");
        try {
            assertTrue(editEventAfterGame(match,gameEvent,EventEnum.foul,"Messei got fuol"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean editEventAfterGame(Match match, GameEvent gameEvent, EventEnum aType, String aDescription) throws Exception {
        boolean wasEdit = false;
        if(!getMatchStub(match.getAwayTeam().getName(),match.getHomeTeam().getName(),match.getDate()).isEmpty())
        {
            if(getMainRefereeInMatchStub(match.getAwayTeam().getName(),match.getHomeTeam().getName(),match.getDate()).equals(getNameStub()))
            {
                Date currDate=new Date(System.currentTimeMillis());
                Date gameDate=new SimpleDateFormat("dd/MM/yyyy").parse(match.getDate());
                Time time =getMatchTimeStub(match.getAwayTeam().getName(),match.getHomeTeam().getName(),match.getDate());
                gameDate.setHours(time.getHours());
                gameDate.setMinutes(time.getMinutes());
                gameDate.setSeconds(time.getSeconds());
                if (getDateDiffStub2(gameDate, currDate, TimeUnit.MINUTES) > 390) {
                    if (!getGameEventsStub(gameEvent.getType().toString(),gameEvent.getGameMinute()+"",gameEvent.getDescription()).isEmpty()) {
                        updateGameEventStub(aType.toString(),aDescription,match.getAwayTeam().getName(),match.getHomeTeam().getName(),match.getDate());
                        wasEdit = true;
                        writeNewLine("Referee " + getNameStub() + " edit event after the match between: " + match.getHomeTeam().getName() + "," + match.getAwayTeam().getName() + " to " + aType);

                    }
                    else{
                        writeNewLineError("This match don't contain given game event");
                        throw new Exception("This match don't contain given game event");
                    }
                }
                else{
                    writeNewLineError("Referee can edit event only after 5 hours");
                    throw new Exception("Referee can edit event only after 5 hours");
                }
            }
            else{
                writeNewLineError("Referee is not a main referee");
                throw new Exception("Referee is not the main referee in this match");
            }
        }
        else{
            writeNewLineError("Referee didnt take part in this match");
            throw new Exception("Referee didnt take part in this match");
        }
        return wasEdit;
    }
    //*----------------------------------------------stubs--------------------------------------------------------------------*/
    public List<String> getMatchListStub(String awayTeam,String homeTeam,String date){
        List<String> matchList=new LinkedList<>();
        matchList.add("29/05/2020");
        matchList.add("12:00:00");
        matchList.add("0");
        matchList.add("0");
        matchList.add("Team1");
        matchList.add("Team2");
        matchList.add("Referee1X");
        matchList.add("Referee1X");
        matchList.add("Referee1X");
        matchList.add("Stadium1");
        return matchList;
    }
    public List<String> getEventList(String homeTeam,String awayTeam,String date){
        List<String> eventList=new LinkedList<>();
        eventList.add("EventType: Red card, Description: Messei got, Minute: 89");
        return eventList;
    }
    public List<String>getMatchStub(String awayTeam,String homeTeam,String date){
        List <String> match=new LinkedList<>();
        match.add("Teams: Team1 against Team2, Date: 29/05/2020");
        return match;
    }
    public  long getDateDiffStub(Date date1, Date date2, TimeUnit timeUnit) {
        return 20;
    }
    public  long getDateDiffStub2(Date date1, Date date2, TimeUnit timeUnit) {
        return 500;
    }
    public Time getMatchTimeStub(String awayTeam,String homeTeam,String date)
    {
        return new Time(System.currentTimeMillis());
    }

    public String getNameStub()
    {
        return "Referee1X";
    }
    public String convertDateToStringStub(Date date)
    {
        return "29/05/2020";
    }
    public boolean isAccountloggedInStub(String account)
    {
        return true;
    }
    public void notifyAccount(String fan,String message)
    {

    }
    public void addGameEventStub(String eventType, Time hour, String description, int gameMinute, String date, String awayTeamName, String homeTeamName)
    {

    }
    public List<String> getNotifiedFansStub()
    {
        return new LinkedList<>();
    }
    public void writeNewLine(String message){

    }
    public void writeNewLineError(String message){

    }
    public String getMainRefereeInMatchStub(String awayTeam,String homeTeam,String date){
        return "Referee1X";
    }
    public List<String> getGameEventsStub(String event,String minute,String description)
    {
        List<String> events=new LinkedList<>();
        events.add("Red card, Description: Messei got, Minute: 89");
        return  events;
    }
    public void updateGameEventStub(String event,String description,String awayTeam,String homeTeam,String date) {
    }
    public  GameEvent convertStringToGameEventStub(String gameEvent){
        String [] splitArr=gameEvent.split(",");
        String eventType=splitArr[0].substring(new String("EventType: ").length());
        String description=splitArr[1].substring(new String(" Description: ").length());
        String minute=splitArr[2].substring(new String(" Minute: ").length());
        GameEvent event=new GameEvent(EventEnum.redCard,new Date(),new Time(System.currentTimeMillis()),description,Integer.parseInt(minute),null);
        return event;
    }
    public  Match convertStringToMatchStub(String match){
        String [] splitArr=match.split(",");
        String homeTeam=splitArr[0].substring(new String("Teams: ").length(),splitArr[0].indexOf(" against"));
        String awayTeam=splitArr[0].substring(splitArr[0].indexOf("against")+new String("against ").length());
        String date=splitArr[1].substring(splitArr[1].indexOf("Date:")+new String("Date: ").length());
        Team homeTeamObj=new Team(homeTeam,null,null);
        Team awayTeamObj=new Team(awayTeam,null,null);
        return new Match(date,null,0,0,null,null,awayTeamObj,homeTeamObj,null,null,null);
    }
}