package Client.ServiceLayer;
import Client.Client;
import Server.BusinessLayer.DataController;
import Server.BusinessLayer.Logger.Logger;
import Server.BusinessLayer.OtherCrudOperations.*;
import Server.BusinessLayer.Pages.Page;
import Server.BusinessLayer.RoleCrudOperations.*;
import Client.ServiceLayer.GuestController.GuestController;
import Client.ServiceLayer.RoleController.*;
import javafx.util.Pair;


import java.io.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OurSystemClient {



    public OurSystemClient(){
    }

//    public static ArrayList<String> getOptions(String substring) {
//        ArrayList<String> strings=new ArrayList<>();
//        if(substring.equals("League"))
//        {
//            strings.add("LeagueOne");
//            strings.add("LeagueTwo");
//            strings.add("LeagueThree");
//            strings.add("LeagueFour");
//        }
//        else if(substring.equals("Season"))
//        {
//            strings.add("SeasonOne");
//            strings.add("SeasonTwo");
//            strings.add("SeasonThree");
//            strings.add("SeasonFour");
//        }
//
//        return strings;
//    }

    public void Initialize() {
    }




    public static GuestController makeGuestController(){
        return new GuestController();
    }


    public static List<String> getDropList(String string,List<Object> controllers,List<String> arguments){
        List<String> list=new ArrayList<>();
        String sendToServer="Data@"+ Client.getUserName();
        List<String> parameters=new ArrayList<>();
        parameters.add(string);
        if(string.equals("Team")){
            List<String> teamNames= (List<String>) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("getNames",parameters)));
            for(String team:teamNames)
                list.add(team);
        }
        else if(string.equals("Season")){
            //List<String> seasons=DataController.getInstance().getNames(string);
            List<String> seasons=(List<String>) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("getNames",parameters)));
            for(String season:seasons)
                list.add(season);
        }
        else if(string.equals("League")){
            List<String> leagues=(List<String>) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("getNames",parameters)));
            for(String league:leagues)
                list.add(league);
    }
        else if(string.equals("Stadium")){
            List<String> stadiums=(List<String>) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("getNames",parameters)));
            for(String stadium:stadiums)
                list.add(stadium);
        }
        else if(string.equals("EventEnum")){
            List<String> eventEnums=(List<String>) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("getNames",parameters)));
            for(String eventEnum:eventEnums)
                list.add(eventEnum);
        }
        else if(string.equals("Match")){
            list=((RefereeController)controllers.get(0)).getMatchList();
        }
        else if(string.equals("GameEvent")){
            list=((RefereeController)controllers.get(0)).getEvantsByMatch(arguments.get(0));
        }
        else if(string.equals("Referee")){
            List<String> refs=(List<String>) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("getUserNames",parameters)));
            for(String ref:refs)
                list.add(ref);
        }
        else if(string.equals("Account")){
            List<String> accounts=(List<String>) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("getUserNames",parameters)));
            for(String account:accounts)
                list.add(account);
        }
        else if(string.equals("GameSchedual")){
            List<String> policies=(List<String>) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("getPolicies",parameters)));
            for(String policy:policies)
                list.add(policy);
        }
        else if(string.equals("PointCalc")){
            List<String> policies=(List<String>) Client.connectToServer(new Pair<>(sendToServer,new Pair<>("getPolicies",parameters)));
            for(String policy:policies)
                list.add(policy);
        }

        return list;

    }

}
