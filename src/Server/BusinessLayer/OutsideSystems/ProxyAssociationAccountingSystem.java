package Server.BusinessLayer.OutsideSystems;

public class ProxyAssociationAccountingSystem implements AssociationAccountingSystem {
    private AssociationAccountingSystem associationAccountingSystem = new RealAssociationAccountingSystem();
    public static String serverName;

    private ProxyAssociationAccountingSystem(){}

    private static ProxyAssociationAccountingSystem proxyAssociationAccountingSystem=null;

    public static ProxyAssociationAccountingSystem getInstance(){
        if(proxyAssociationAccountingSystem==null)
            proxyAssociationAccountingSystem=new ProxyAssociationAccountingSystem();
        return proxyAssociationAccountingSystem;
    }

    @Override
    public boolean addPayment(String teamName, String date, double amount) {
        if(teamName!=null && date!=null && amount>0) {
            connectTo(serverName);
            return associationAccountingSystem.addPayment(teamName, date, amount);
        }
        return false;
    }

    @Override
    public void connectTo(String serverName) {
        if(serverName!=null){
            associationAccountingSystem.connectTo(serverName);
        }
        else{
            System.out.println("set server name to connect to, and try again");
        }
    }

    public static void setServerName(String serverName) {
        ProxyTaxSystem.serverName = serverName;
    }
}
