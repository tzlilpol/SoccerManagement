package Server.DataLayer;

import Server.BusinessLayer.OtherCrudOperations.*;
import Server.BusinessLayer.RoleCrudOperations.Referee;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DBAdapter implements IDatabase {

    public Connection connectToDB(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost;databaseName=DB2020;integratedSecurity=true";
            Connection con = DriverManager.getConnection(connectionUrl);
            return con;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
        return null;
    }

    public void addAccount(String userName, String password, String name, int age) {
        try {
            Connection con=connectToDB();
            PreparedStatement st=con.prepareStatement("insert into Account values(?,?,?,?,?)");
            st.setString(1,userName);
            st.setString(2,password);
            st.setString(3,name);
            st.setInt(4,age);
            st.setString(5,"False");
            st.executeUpdate();
            st.close();
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public List<String> getNames(String Object) {
        try {
            Connection con = connectToDB();
            PreparedStatement st = null;
            if(Object.equals("Team"))
                st=con.prepareStatement("select * from Team");
            else if(Object.equals("League"))
                st=con.prepareStatement("select * from League");
            else if(Object.equals("Stadium"))
                st=con.prepareStatement("select * from Stadium");
            else if(Object.equals("Season"))
                st=con.prepareStatement("select * from Season");

            ResultSet resultSet = st.executeQuery();
            List <String> list=new ArrayList<>();
            while (resultSet.next()) {
                list.add(resultSet.getString("Name"));
            }
            con.close();
            return list;
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
        return null;
    }

    public List<String> getUserNames(String Object) {
        try {
            Connection con = connectToDB();
            PreparedStatement st = null;
            if(Object.equals("Account"))
                st=con.prepareStatement("select * from Account");
            else if(Object.equals("Owner"))
                st=con.prepareStatement("select * from Owner");
            else if(Object.equals("Referee"))
                st=con.prepareStatement("select * from Referee");
            else if(Object.equals("AssociationRepresentative"))
                st=con.prepareStatement("select * from AssociationRepresentative");

            ResultSet resultSet = st.executeQuery();
            List <String> list=new ArrayList<>();
            while (resultSet.next()) {
                list.add(resultSet.getString("UserName"));
            }
            con.close();
            return list;
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
        return null;
    }


    public void addOwnerRole(String userName, String name, String teamName, String appointedUserName) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("insert into Owner values(?,?,?,?) ");
            ps.setString(1,userName);
            ps.setString(2,name);
            ps.setString(3,teamName);
            ps.setString(4,appointedUserName);
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();

        }
    }

    public void addPlayerRole(String userName, String name, Date birthday, String position, String teamName, int pageID) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("insert into Player values(?,?,?,?,?,?) ");
            ps.setString(1,userName);
            ps.setString(2,name);
            ps.setDate(3,birthday);
            ps.setString(4,position);
            ps.setString(5,teamName);
            ps.setInt(6,pageID);
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();

        }

    }

    public void addFanRole(String userName, String name, boolean trackPersonalPages, boolean getMatchNotifications, List<Integer> pagesIDs) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps1=con.prepareStatement("insert into Fan values(?,?,?,?) ");
            ps1.setString(1,userName);
            ps1.setString(2,name);
            ps1.setBoolean(3,trackPersonalPages);
            ps1.setBoolean(4,getMatchNotifications);
            ps1.executeUpdate();

            for(Integer id:pagesIDs){
                PreparedStatement ps2=con.prepareStatement("insert into FansAndPages values(?,?)");
                ps2.setInt(1,id);
                ps2.setString(2,userName);
                ps2.executeUpdate();
            }
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }

    }

    public void addRefereeRole(String userName, String name, String training, HashMap<String, String> refLeaguesAndSeasons) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps1=con.prepareStatement("insert into Referee values(?,?,?) ");
            ps1.setString(1,userName);
            ps1.setString(2,name);
            ps1.setString(3,training);
            ps1.executeUpdate();

            for(Map.Entry<String,String> entry:refLeaguesAndSeasons.entrySet()){
                PreparedStatement pr2=con.prepareStatement("insert into LeaguesForReferee values (?,?,?)");
                pr2.setString(1,userName);
                pr2.setString(2,entry.getKey());
                pr2.setString(3,entry.getValue());
                pr2.executeUpdate();
            }
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();

        }
    }

    public void addTeamManagerRole(String userName, String name, String teamName, String appointerUserName, List<String> permissions) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps1=con.prepareStatement("insert into TeamManager values(?,?,?,?) ");
            ps1.setString(1,userName);
            ps1.setString(2,name);
            ps1.setString(3,teamName);
            ps1.setString(4,appointerUserName);
            ps1.executeUpdate();

            for(String permission:permissions){
                PreparedStatement pr2=con.prepareStatement("insert into TeamManagerPermissions values(?,?)");
                pr2.setString(1,userName);
                pr2.setString(2,permission);
                pr2.executeUpdate();
            }
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void addSystemManagerRole(String userName, String name, HashMap<String, String> complaintAndComments) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps1=con.prepareStatement("insert into SystemManager values(?,?) ");
            ps1.setString(1,userName);
            ps1.setString(2,name);
            ps1.executeUpdate();

            for(Map.Entry<String,String> entry:complaintAndComments.entrySet()){
                PreparedStatement pr2=con.prepareStatement("insert into ComplainAndComments values(?,?,?)");
                pr2.setString(1,userName);
                pr2.setString(2,entry.getKey());
                pr2.setString(3,entry.getValue());
                pr2.executeUpdate();
            }
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void addAssociationRepresentativeRole(String userName, String name) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("insert into AssociationRepresentative values(?,?)");
            ps.setString(1,userName);
            ps.setString(2,name);
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void addCoachRole(String userName, String name, String training,String teamRole, int pageID, String teamName) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("insert into Coach values(?,?,?,?,?,?) ");
            ps.setString(1,userName);
            ps.setString(2,name);
            ps.setString(3,training);
            ps.setString(4,teamRole);
            ps.setInt(5,pageID);
            ps.setString(6,teamName);
            ps.executeUpdate();

            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void addLeague(String name, List<String> seasonList, List<String[]> policyList) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps1=con.prepareStatement("insert into League values(?) ");
            ps1.setString(1,name);
            ps1.executeUpdate();

            for(int i=0;i<seasonList.size();i++){

                String[] policy=policyList.get(i);

                try {
                    PreparedStatement ps2=con.prepareStatement("insert into Season values(?)");
                    ps2.setString(1,seasonList.get(i));
                    ps2.executeUpdate();
                } catch (SQLException e) {
                    if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                        e.printStackTrace();
                }


                try {
                    PreparedStatement ps3=con.prepareStatement("insert into Policy values (?,?,?,?,?)");
                    ps3.setString(1,policy[2]);
                    ps3.setString(2,policy[0]);
                    ps3.setString(3,policy[1]);
                    ps3.setString(4,name);
                    ps3.setString(5,seasonList.get(i));
                    ps3.executeUpdate();
                } catch (SQLException e) {
                    if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                        e.printStackTrace();
                }


                try {
                    PreparedStatement ps4=con.prepareStatement("insert into SLsettings values(?,?,?)");
                    ps4.setString(1,name);
                    ps4.setString(2,seasonList.get(i));
                    ps4.setString(3,policy[2]);
                    ps4.executeUpdate();
                } catch (SQLException e) {
                    if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                        e.printStackTrace();
                }


            }
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void addSeason(String name, List<String> leagueList, List<String[]> policyList) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps1=con.prepareStatement("insert into Season values(?) ");
            ps1.setString(1,name);
            ps1.executeUpdate();

            for(int i=0;i<leagueList.size();i++){
                String[] policy=policyList.get(i);
                PreparedStatement ps2=con.prepareStatement("insert into SLsettings values(?,?,?)");
                ps2.setString(1,leagueList.get(i));
                ps2.setString(2,name);
                ps2.setString(3,policy[2]);
                ps2.executeUpdate();

                PreparedStatement ps3=con.prepareStatement("inserto into Policy values (?,?,?,?,?)");
                ps3.setString(1,policy[2]);
                ps3.setString(2,policy[0]);
                ps3.setString(3,policy[1]);
                ps3.setString(4,leagueList.get(i));
                ps3.setString(5,name);
                ps3.executeUpdate();
            }
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace(); }
    }

    public void addStadium(String name) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("insert into Stadium values(?) ");
            ps.setString(1,name);
            ps.executeUpdate();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void addTeam(String name, int pageID, String leagueName, String stadiumName,int points) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("insert into Team values(?,?,?,?,?)");
            ps.setString(1,name);
            if(pageID!=-1)
                ps.setInt(2,pageID);
            else
                ps.setNull(2, Types.INTEGER);
            ps.setString(3,leagueName);
            ps.setString(4,stadiumName);
            ps.setInt(5,points);
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void addMatch(String date, Time time, int awayScore, int homeScore, String awayTeamName, String homeTeamName,
                         String mainRefUN, String lineRefUN1, String lineRefUN2, String stadiumName,String seasonName) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("insert into Match values(?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1,date);
            ps.setTime(2,time);
            ps.setInt(3,awayScore);
            ps.setInt(4,homeScore);
            ps.setString(5,awayTeamName);
            ps.setString(6,homeTeamName);
            ps.setString(7,mainRefUN);
            ps.setString(8,lineRefUN1);
            ps.setString(9,lineRefUN2);
            ps.setString(10,stadiumName);
            ps.setString(11,seasonName);
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void addAlert(String userName, List<String> alerts) {
        try {
            Connection con=connectToDB();
            for(String alert:alerts){
                PreparedStatement ps= con.prepareStatement("insert into Alert values(?,?)");
                ps.setString(1,alert);
                ps.setString(2,userName);
                ps.executeUpdate();
            }
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void addPage(int pageID){
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("insert into Page values(?)");
            ps.setInt(1,pageID);
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void addRefsToMatch(String date, Time time, String awayTeamName, String homeTeamName, String username1,String username2, String username3) {
        try {
            Connection con=connectToDB();
            PreparedStatement pr=con.prepareStatement("update Match set MainReferee=?, LineRefereeOne=?, LineRefereeTwo=? where Date=? and AwayTeam=? and homeTeam=?");
            pr.setString(1,username1);
            pr.setString(2,username2);
            pr.setString(3,username3);
            pr.setString(4,date);
            pr.setString(5,awayTeamName);
            pr.setString(6,homeTeamName);
            pr.executeUpdate();
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void addGameEvent(String eventType, Time hour, String description, int gameMinute, String date, String awayTeamName, String homeTeamName) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("insert into GameEvent values (?,?,?,?,?,?,?)");
            ps.setString(1,eventType);
            String strTime=hour.getHours()+":"+hour.getMinutes()+":"+hour.getSeconds();
            ps.setString(2,strTime);
            ps.setString(3,description);
            ps.setInt(4,gameMinute);
            ps.setString(5,date);
            ps.setString(6,awayTeamName);
            ps.setString(7,homeTeamName);
            ps.executeUpdate();
            if(eventType.equals("goal"))
            {
                if(description.contains(homeTeamName))
                    increaseMatchScore("home",date,awayTeamName,homeTeamName);
                else if(description.contains(awayTeamName))
                    increaseMatchScore("away",date,awayTeamName,homeTeamName);

            }
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void increaseMatchScore(String teamScored, String date, String awayTeamName, String homeTeamName1) {
        int currScore=0;
        String whoScored="";
        if(teamScored.equals("home"))
            whoScored="homeScore";
        else if(teamScored.equals("away"))
            whoScored="AwayScore";
        try {
            Connection con=connectToDB();
            PreparedStatement st=con.prepareStatement("select * from MATCH where Date=? AND AwayTeam=? AND homeTeam=?");
            st.setString(1,date);
            st.setString(2,awayTeamName);
            st.setString(3,homeTeamName1);
            ResultSet RS=st.executeQuery();
            while(RS.next()){
                currScore=RS.getInt(whoScored);
            }
            PreparedStatement st1;
            if(whoScored.equals("homeScore"))
                st1 =con.prepareStatement("update Match set homeScore=? where Date=? AND AwayTeam=? AND homeTeam=?");
            else
                st1 =con.prepareStatement("update Match set AwayScore=? where Date=? AND AwayTeam=? AND homeTeam=?");
            st1.setInt(1,++currScore);
            st1.setString(2,date);
            st1.setString(3,awayTeamName);
            st1.setString(4,homeTeamName1);
            st1.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[] getUserNamePasswordDC(String userName) {
        String[] arr=new String[4];
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("select * from Account where UserName=?");
            ps.setString(1,userName);
            ResultSet RS=ps.executeQuery();
            boolean sizeZero=true;
            while(RS.next()){
                sizeZero=false;
                arr[0]=RS.getString(1);
                arr[1]=RS.getString(2);
                arr[2]=RS.getString(3);
                arr[3]=RS.getInt(4)+"";
            }
            if(sizeZero) return null;
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arr;
    }

    public List<String> getAccountRoles(String userName) {
        List<String> roleList=new ArrayList<>();
        try {
            Connection con=connectToDB();
            String[] roleClasses={"AssociationRepresentative","Coach","Fan","Owner","Player","Referee","SystemManager","TeamManager"};
            for(String role:roleClasses){
                PreparedStatement ps=con.prepareStatement("select * from "+role+" where UserName=?");
                ps.setString(1,userName);
                ResultSet RS=ps.executeQuery();
                while(RS.next())
                    roleList.add(role);
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roleList;
    }

    public void setTeamToOwner(String username, String teamname) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("UPDATE Owner SET Team=(?) WHERE UserName=(?)");
            ps.setString(1,teamname);
            ps.setString(2,username);
            ps.executeUpdate();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void approveTeam(String ownerUsername, String ARusername, String teamName) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("UPDATE ApprovedTeams SET Approved=(?),AssociationRepresentative=(?) WHERE Owner=(?)");
            ps.setString(1,"True");
            ps.setString(2,ARusername);
            ps.setString(3,ownerUsername);
            ps.executeUpdate();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void addOpenTeamRequest(String username, String teamName) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("insert into ApprovedTeams (Owner,TeamName,Approved) values(?,?,?) ");
            ps.setString(1,username);
            ps.setString(2,teamName);
            ps.setString(3,"False");
            ps.executeUpdate();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void removeOpenTeamRequest(String username, String teamName) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("DELETE FROM ApprovedTeams WHERE Owner=(?) AND TeamName=(?)");
            ps.setString(1,username);
            ps.setString(2,teamName);
            ps.executeUpdate();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public String checkIfRequestExists(String username, String teamName) {
        String ans="";
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("select Approved from ApprovedTeams WHERE Owner=(?) AND TeamName=(?)");
            ps.setString(1,username);
            ps.setString(2,teamName);
            ResultSet RS=ps.executeQuery();
            while(RS.next()){
                ans=RS.getString("Approved");
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public void setAccountLogIn(String username,String isLoggedIn) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("UPDATE Account SET isLoggedIn=(?) WHERE UserName=(?)");
            ps.setString(1,isLoggedIn);
            ps.setString(2,username);
            ps.executeUpdate();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public List<String> getAlertsForAccount(String username) {
        List<String> ans=new ArrayList<>();
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("select Description from Alert WHERE UserName=(?)");
            ps.setString(1,username);
            ResultSet RS=ps.executeQuery();
            while(RS.next()){
                ans.add(RS.getString("Description"));
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public void clearAlertsForAccount(String username) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("DELETE FROM Alert WHERE UserName=(?)");
            ps.setString(1,username);
            ps.executeUpdate();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void addAlertToAccount(String userName, String notification) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("insert into Alert (Description,UserName) values(?,?) ");
            ps.setString(1,notification);
            ps.setString(2,userName);
            ps.executeUpdate();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public String isAccountloggedIn(String userName) {
        String ans="false";
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("select isLoggedIn from Account WHERE UserName=(?)");
            ps.setString(1,userName);
            ResultSet RS=ps.executeQuery();
            while(RS.next()){
                ans=RS.getString("isLoggedIn");
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public void addRefereeToLeague(String referee, String league, String season) {
        try {
            Connection con=connectToDB();
            PreparedStatement st=con.prepareStatement("insert into LeaguesForReferee values(?,?,?)");
            st.setString(1,referee);
            st.setString(2,league);
            st.setString(3,season);
            st.executeUpdate();
            st.close();
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public int getNewPageCounter() {
        int ans=0;
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("select MAX(ID) from Page");
            ResultSet RS=ps.executeQuery();
            while(RS.next()){
                ans=RS.getInt(1);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public void addGameSchedualPolicy(String name) {
        try {
            Connection con=connectToDB();
            PreparedStatement st=con.prepareStatement("insert into GameSchedualPolicies values(?)");
            st.setString(1,name);
            st.executeUpdate();
            st.close();
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void addPointCalcPolicy(String name) {
        try {
            Connection con=connectToDB();
            PreparedStatement st=con.prepareStatement("insert into PointCalcPolicies values(?)");
            st.setString(1,name);
            st.executeUpdate();
            st.close();
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void updateLeaguePolicy(String season,String league, String policyType,String policyValue) {
        try
        {
            Connection con=connectToDB();
            String policyID="";
            PreparedStatement ps=con.prepareStatement("select Policy from SLsettings WHERE Season=(?) and League=(?)");
            ps.setString(1,season);
            ps.setString(2,league);
            ResultSet RS=ps.executeQuery();
            while(RS.next()){
                policyID=RS.getString("Policy");
            }
            PreparedStatement pr=null;
            if(policyID=="")
            {
                policyID=getPolicyCounter()+1+"";

                PreparedStatement pr1=con.prepareStatement("insert into Policy(ID,League,Season) values (?,?,?)");
                pr1.setString(1,policyID);
                pr1.setString(2,league);
                pr1.setString(3,season);
                pr1.executeUpdate();

                pr=con.prepareStatement("insert into SLsettings values (?,?,?)");
                pr.setString(1,league);
                pr.setString(2,season);
                pr.setString(3,policyID);
                pr.executeUpdate();

            }

            if(policyType=="GameSchedual")
                pr=con.prepareStatement("update Policy set GameSchedual=? where ID=?");
            else if(policyType=="PointCalc")
                pr=con.prepareStatement("update Policy set PointCalc=? where ID=?");
            pr.setString(1,policyValue);
            pr.setString(2,policyID);
            pr.executeUpdate();
            con.close();

        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public int getPolicyCounter() {
        int ans=0;
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("select MAX(ID) from Policy");
            ResultSet RS=ps.executeQuery();
            while(RS.next()){
                ans=RS.getInt(1);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public List<String> getGameSchedualPolicies(String policy) {
        try {
            Connection con = connectToDB();
            PreparedStatement st = null;
            if(policy.equals("GameSchedual"))
                st=con.prepareStatement("select * from GameSchedualPolicies");
            else if(policy.equals("PointCalc"))
                st=con.prepareStatement("select * from PointCalcPolicies");

            ResultSet resultSet = st.executeQuery();
            List <String> list=new ArrayList<>();
            while (resultSet.next()) {
                list.add(resultSet.getString("Name"));
            }
            con.close();
            return list;
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
        return null;
    }
    public void removeAlertsFromAccount(String username) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("DELETE FROM Alert WHERE UserName=(?)");
            ps.setString(1,username);
            ps.executeUpdate();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }
    public Time getMatchTime(String awayTeam,String homeTeam,String date)
    {
        Time time=null;
        try {
            Connection con = connectToDB();
            PreparedStatement st=con.prepareStatement("select * from MATCH where Date=? AND AwayTeam=? AND homeTeam=?");
            st.setString(1,date);
            st.setString(2,awayTeam);
            st.setString(3,homeTeam);
            ResultSet resultSet = st.executeQuery();

            while (resultSet.next()) {
                time=resultSet.getTime(2);
            }
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();

        }
        return time;
    }
    public List<String> getMatch(String awayTeam,String homeTeam,String date)
    {
        List<String> match=new ArrayList<>();
        try {
            Connection con=connectToDB();
            PreparedStatement st=con.prepareStatement("select * from MATCH where Date=? AND AwayTeam=? AND homeTeam=?");
            st.setString(1,date);
            st.setString(2,awayTeam);
            st.setString(3,homeTeam);
            ResultSet RS=st.executeQuery();
            while(RS.next()){
                match.add(RS.getString(1));
                String s=RS.getString(2);
                match.add(s.substring(0,s.indexOf(".")));
                match.add(RS.getString(3));
                match.add(RS.getString(4));
                match.add(RS.getString(5));
                match.add(RS.getString(6));
                match.add(RS.getString(7));
                match.add(RS.getString(8));
                match.add(RS.getString(9));
                match.add(RS.getString(10));

            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return match;
    }
    public String getMainRefereeInMatch(String awayTeam,String homeTeam,String date)
    {
        String name="";
        try {
            Connection con = connectToDB();
            PreparedStatement st=con.prepareStatement("select * from MATCH where Date=? AND AwayTeam=? AND homeTeam=?");
            st.setString(1,date);
            st.setString(2,awayTeam);
            st.setString(3,homeTeam);
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()) {
                name=resultSet.getString("MainReferee");
            }
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();

        }
        return name;
    }

    public void updateGameEvent(String event,String description,String awayTeam,String homeTeam,String date) {
        try {
            Connection con=connectToDB();
            PreparedStatement pr=con.prepareStatement("update GameEvent set Description=?, EventType=? where Date=? and AwayTeam=? and homeTeam=?");
            pr.setString(1,description);
            pr.setString(2,event);
            pr.setString(3,date);
            pr.setString(4,awayTeam);
            pr.setString(5,homeTeam);
            pr.executeUpdate();
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }
    public List<String> getGameEvents(String event,String minute,String description)
    {
        List<String> events=new LinkedList<>();
        try {
            Connection con=connectToDB();
            PreparedStatement st=con.prepareStatement("select * from GameEvent where EventType=? AND GameMinute=? AND Description=?");
            st.setString(1,event);
            st.setString(2,minute);
            st.setString(3,description);
            ResultSet RS=st.executeQuery();
            while(RS.next()){
                events.add(RS.getString(1));
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    public List<String> getAllMatches()
    {
        List<String> ans=new ArrayList<>();
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("select * from Match");
            ResultSet RS=ps.executeQuery();
            while(RS.next()){
                String s="Teams: "+RS.getString("homeTeam")+" against "+RS.getString("AwayTeam")+
                        ", Date: "+RS.getString("Date");
                ans.add(s);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public List<String> getGameEventsByMatch(String homeTeam,String awayTeam,String date)
    {
        List<String> ans=new ArrayList<>();
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("select * from GameEvent where homeTeam=? and AwayTeam=? and Date=?");
            ps.setString(1,homeTeam);
            ps.setString(2,awayTeam);
            ps.setString(3,date);
            ResultSet RS=ps.executeQuery();
            while(RS.next()){
                String s="EventType: "+RS.getString("EventType")+", Description: "+RS.getString("Description")+", Minute: "+RS.getString("GameMinute");
                ans.add(s);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public List<String> getNotifiedFans()
    {
        List<String> ans=new ArrayList<>();
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("select * from Fan where GetMatchNotifications=?");
            ps.setString(1,"True");
            ResultSet RS=ps.executeQuery();
            while(RS.next()){
                String s=RS.getString("UserName");
                ans.add(s);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public void setGameNotificationSubscribtion(String username, String assignment) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("UPDATE Fan SET GetMatchNotifications=(?) WHERE UserName=(?)");
            ps.setString(1,assignment);
            ps.setString(2,username);
            ps.executeUpdate();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void addMatches(List<List<String>> matches) {
        try {
            Connection con=connectToDB();
            for(List<String> match : matches) {
                PreparedStatement st = con.prepareStatement("insert into MATCH values(?,?,?,?,?,?,?,?,?,?,?)");
                st.setString(1, match.get(0));
                st.setTime(2, Time.valueOf(match.get(1)));
                st.setInt(3, Integer.valueOf(match.get(2)));
                st.setInt(4, Integer.valueOf(match.get(3)));
                st.setString(5, match.get(4));
                st.setString(6, match.get(5));
                st.setString(7, match.get(6));
                st.setString(8, match.get(7));
                st.setString(9, match.get(8));
                st.setString(10, match.get(9));
                st.setString(11, match.get(10));
                st.executeUpdate();
                st.close();
            }
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public String getGameSchedulePolicy(String leagueName, String seasonName) {
        String policy="";
        try{
            Connection con=connectToDB();
            PreparedStatement ps = con.prepareStatement("select * FROM Policy WHERE League=? and Season=?");
            ps.setString(1,leagueName);
            ps.setString(2,seasonName);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                policy=resultSet.getString("GameSchedual");
            }
            con.close();
        }
        catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
        return policy;
    }

    public List<List<String>> getTeamsInLeague(String leagueName) {
        List<List<String>> teams=new LinkedList<>();
        try{
            Connection con=connectToDB();
            PreparedStatement ps = con.prepareStatement("select * FROM Team WHERE League=?");
            ps.setString(1,leagueName);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                List<String> team=new LinkedList<>();
                String s="";
                s=resultSet.getString("Name");
                team.add(s);
                s=resultSet.getString("League");
                team.add(s);
                s=resultSet.getString("Stadium");
                team.add(s);
                teams.add(team);
            }
            con.close();
        }
        catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
        return teams;
    }

    public List<String> getRefereesInLeague(String leagueName, String seasonName) {
        List<String> referees=new LinkedList<>();
        try{
            Connection con=connectToDB();
            PreparedStatement ps = con.prepareStatement("select * FROM LeaguesForReferee WHERE League=? and Season=?");
            ps.setString(1,leagueName);
            ps.setString(2,seasonName);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                String s=resultSet.getString("Referee");
                referees.add(s);
            }
            con.close();
        }
        catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
        return referees;
    }

    public void deleteTestTeam() {
        try{
            Connection con=connectToDB();
            PreparedStatement ps = con.prepareStatement("update Owner set Team=? where UserName=?");
            ps.setString(1,"Team1");
            ps.setString(2,"Owner1X");
            ps.execute();

            PreparedStatement ps2=con.prepareStatement("delete from Team where Name=?");
            ps2.setString(1,"Test team");
            ps2.execute();
            con.close();
        }
        catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }

    public void addTestMatch(String date, String time, String awayScore, String homeScore, String awayTeamName, String homeTeamName,
                             String mainRefUN, String lineRefUN1, String lineRefUN2, String stadiumName,String seasonName) {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("insert into Match values(?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1,date);
            ps.setString(2,time);
            ps.setString(3,awayScore);
            ps.setString(4,homeScore);
            ps.setString(5,awayTeamName);
            ps.setString(6,homeTeamName);
            ps.setString(7,mainRefUN);
            ps.setString(8,lineRefUN1);
            ps.setString(9,lineRefUN2);
            ps.setString(10,stadiumName);
            ps.setString(11,seasonName);
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }
    public void deleteTestMatch(String date,String awayTeamName, String homeTeamName)
    {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("DELETE FROM Match WHERE Date=(?) AND AwayTeam=(?) AND homeTeam=(?)");
            ps.setString(1,date);
            ps.setString(2,awayTeamName);
            ps.setString(3,homeTeamName);
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }
    public void deleteTestEvent(String date,String awayTeamName, String homeTeamName,String Description)
    {
        try {
            Connection con=connectToDB();
            PreparedStatement ps=con.prepareStatement("DELETE FROM GameEvent WHERE Date=(?) AND AwayTeam=(?) AND homeTeam=(?) AND Description=(?)");
            ps.setString(1,date);
            ps.setString(2,awayTeamName);
            ps.setString(3,homeTeamName);
            ps.setString(4,Description);
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            if(!e.getMessage().contains("Violation of PRIMARY KEY"))
                e.printStackTrace();
        }
    }
}
