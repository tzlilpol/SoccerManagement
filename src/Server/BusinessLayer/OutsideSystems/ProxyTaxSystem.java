package Server.BusinessLayer.OutsideSystems;

public class ProxyTaxSystem implements TaxSystem {
    private TaxSystem taxSystem = new RealTaxSystem();
    public static String serverName;

    private ProxyTaxSystem(){}

    private static ProxyTaxSystem proxyTaxSystem=null;

    public static ProxyTaxSystem getInstance(){
        if(proxyTaxSystem==null)
            proxyTaxSystem=new ProxyTaxSystem();
        return proxyTaxSystem;
    }

    @Override
    public double getTaxRate(double revenueAmount) {
        if(revenueAmount>0) {
            connectTo(serverName);
            return taxSystem.getTaxRate(revenueAmount);
        }
        return -1;
    }

    @Override
    public void connectTo(String serverName) {
        if(serverName!=null){
            taxSystem.connectTo(serverName);
        }
        else{
            System.out.println("set server name to connect to, and try again");
        }
    }

    public static void setServerName(String serverName) {
        ProxyTaxSystem.serverName = serverName;
    }
}
