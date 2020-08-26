package Server.BusinessLayer.OutsideSystems;

public interface AssociationAccountingSystem {
    public boolean addPayment(String teamName, String date, double amount);
    public void connectTo(String serverName);
}
