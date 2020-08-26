import Server.BusinessLayer.DataController;
import Server.BusinessLayer.Logger.Logger;
import Server.BusinessLayer.OtherCrudOperations.*;
import Server.BusinessLayer.OurSystemServer;
import Server.BusinessLayer.RoleCrudOperations.*;
import javafx.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.Assert.*;

public class FanTest {

    Fan fan;
    HashMap<Fan,Boolean> fanNot;

    @Before
    public void setUp() throws Exception {
        fan = new Fan("newFan");
        fanNot=new HashMap<>();
    }

    @Test
    public void subscribeGetMatchNotificationsStub()
    {
        assertEquals(SubscribeGetMatchNotifications(),"Subscribed successfully!");
        assertTrue(fanNot.get(fan));
    }
    @Test
    public void unSubscribeGetMatchNotificationsStub()
    {
        assertEquals(unSubscribeGetMatchNotifications(),"Subscription removed successfully!");
        assertFalse(fanNot.get(fan));
    }
    //*----------------------------------------------stubs--------------------------------------------------------------------*/

    public String SubscribeGetMatchNotifications(){
        fanNot.put(fan,true);
        return "Subscribed successfully!";
    }
    public String unSubscribeGetMatchNotifications()
    {
        fanNot.put(fan,false);
        return "Subscription removed successfully!";
    }

}


//*------------------------------------------------------------------------------------------------------------------------*/