package Server.BusinessLayer.OutsideSystems;

public interface TaxSystem {
    public double getTaxRate(double revenueAmount);
    public void connectTo(String serverName);
}
