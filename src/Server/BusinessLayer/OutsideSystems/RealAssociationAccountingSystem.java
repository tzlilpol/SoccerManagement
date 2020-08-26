package Server.BusinessLayer.OutsideSystems;

public class RealAssociationAccountingSystem implements AssociationAccountingSystem {


    @Override
    public boolean addPayment(String teamName, String date, double amount) {
        return false;
    }

    @Override
    public void connectTo(String serverName) {
        System.out.println("Connecting to accounting system...");
    }
}
