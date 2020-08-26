package Server.BusinessLayer.Controllers;

import Server.BusinessLayer.OtherCrudOperations.Account;
import Server.BusinessLayer.OtherCrudOperations.Alert;
import Server.BusinessLayer.RoleCrudOperations.Fan;
import Server.BusinessLayer.RoleCrudOperations.Role;
import Client.ServiceLayer.OurSystemClient;

import java.util.ArrayList;
import java.util.List;

public class FanBusinessController {

    private Fan fan;

    public FanBusinessController(Fan fan){
        this.fan=fan;
    }

    public Fan getFan() {
        return fan;
    }

    public void setFan(Fan fan) {
        this.fan = fan;
    }

//    public void LogOut(){
//        fan.Logout();
//        for(Account account: OurSystemClient.getCurrAccounts()){
//            for(Role role:account.getRoles()){
//                if(role.equals(fan)){
//                    OurSystemClient.removeAccount(account);
//                    return;
//                }
//            }
//        }
//    }

    public String EditPersonalInfo(String newName,String newUserName,String newPassword){
        try {
            fan.EditPersonalInfo(newName,newUserName,newPassword);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "";
    }

//    public void ShowInfo(String InfoAbout){
//        fan.ShowInfo(InfoAbout);
//    }
//
//    public void Search(String criterion, String query){
//        fan.Search(criterion,query);
//    }
//
//    public void  Filter(String category, String roleFilter){
//        fan.Filter(category,roleFilter);
//    }



    public void SubscribeTrackPersonalPages(){
        fan.SubscribeTrackPersonalPages();
    }


    public String SubscribeGetMatchNotifications(){
        return fan.SubscribeGetMatchNotifications();
    }
    public String unSubscribeGetMatchNotifications(){
        return fan.unSubscribeGetMatchNotifications();
    }
//    public boolean Report(String report){
//        if(report.length()==0)
//            return false;
//        fan.Report(report);
//        return true;
//    }

    public String ShowSearchHistory() throws Exception {
        try {
            fan.ShowSearchHistory();
        } catch (Exception e) {
            return e.getMessage();
        }
        return "";
    }

    public void ShowPersonalInfo(){
        fan.ShowPersonalInfo();
    }
	
	public List<String> getAlerts()
	{
        List<Alert> alerts=fan.getAlertList();
        List<String> strings=new ArrayList<>();
        for(Alert alert:alerts)
        {
            strings.add(alert.getDescription());
        }
        return strings;
    }

    public void logOff()
    {
        fan.logOff();
    }


}
