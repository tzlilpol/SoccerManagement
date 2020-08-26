package Server.BusinessLayer.OutsideSystems;

public class RealTaxSystem implements TaxSystem {



    @Override
    public double getTaxRate(double revenueAmount) {
        return 0;
    }

    @Override
    public void connectTo(String serverName) {
        System.out.println("Connecting to tax system...");
    }


}
