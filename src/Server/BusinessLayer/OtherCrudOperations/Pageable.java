package Server.BusinessLayer.OtherCrudOperations;
import Server.BusinessLayer.Pages.Page;

public interface Pageable {

    public void removePage();
    public Page getPage();
    public void setPage(Page page);

}
