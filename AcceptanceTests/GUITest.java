
import Client.Client;
import Client.PresentationLayer.Main;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.Point2D;
import javafx.geometry.VerticalDirection;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.service.query.NodeQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class GUITest extends ApplicationTest {
    Parent mainNode;

    @Override
    public void start (Stage stage) throws Exception {
        Thread.sleep(1500);
        mainNode = FXMLLoader.load(Main.class.getResource("sample.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @BeforeClass
    public static void setUp () throws Exception {
    }

    @After
    public void tearDown () throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void testEditEventAfterMatch() throws InterruptedException {
        Thread.sleep(1000);
        Node node=null;
        logInBot("Referee1X","Referee1X",3,true,false);

        node=lookup(".dialog-pane").query();
        clickOn(node);
        Thread.sleep(1000);
        clickOn(getNodeFromDialogWithScroll("Team1"));
        Thread.sleep(700);
        pressOkBot(node);

        node=lookup(".dialog-pane").query();
        clickOn(node);
        Thread.sleep(1000);
        clickOn(getNodeFromDialogWithScroll("80"));
        Thread.sleep(700);
        pressOkBot(node);

        node=lookup(".dialog-pane").query();
        clickOn(node);
        Thread.sleep(1000);
        clickOn(getNodeFromDialogWithScroll("red"));
        Thread.sleep(700);
        pressOkBot(node);

        Thread.sleep(700);
        write("Player1 got a red card");
        Thread.sleep(700);
        pressOkBot(node);
        Thread.sleep(700);
        pressOkBot(node);
        Thread.sleep(700);
        returnGameEvent();
    }

    @Test
    public void testAddEventDuringMatch () throws InterruptedException {
        Thread.sleep(1000);

        Node node=null;
        logInBot("Referee1X","Referee1X",2,true,false);

        node=lookup(".dialog-pane").query();
        clickOn(node);
        Thread.sleep(1000);
        clickOn(getNodeFromDialogWithScroll("Team1"));
        Thread.sleep(700);
        pressOkBot(node);

        node=lookup(".dialog-pane").query();
        clickOn(node);
        Thread.sleep(1000);
        clickOn(getNodeFromDialogWithScroll("injury"));
        Thread.sleep(700);
        pressOkBot(node);


        Thread.sleep(700);
        write("Player3 got an injury");
        Thread.sleep(700);
        pressOkBot(node);
        Thread.sleep(700);
        pressOkBot(node);
        Thread.sleep(700);
        logOutBot();
    }

    @Test
    public void testShowReportAfterGame () throws InterruptedException {
        Thread.sleep(1000);
        Node node=null;
        logInBot("Referee1X","Referee1X",1,true,false);
        node=lookup(".dialog-pane").query();
        clickOn(node);
        Thread.sleep(1000);
        node=getNodeFromDialogWithScroll("Team3");
        clickOn(node);
        Thread.sleep(700);
        pressOkBot(node);
        Thread.sleep(2000);
        pressOkBot(node);
        Thread.sleep(700);
        logOutBot();
    }

    @Test
    public void creteTeamTest() throws InterruptedException {
        Thread.sleep(1000);
        Node node=null;

        node=logInBot("Owner1X","Owner1X",1,true,false);

        Thread.sleep(700);
        write("Test team");
        Thread.sleep(700);
        pressOkBot(node);
        node=lookup(".dialog-pane").query();
        clickOn(node);
        Thread.sleep(1000);
        clickOn(getNodeFromDialogWithScroll("League1"));

        Thread.sleep(700);
        pressOkBot(node);

        node=lookup(".dialog-pane").query();
        clickOn(node);
        Thread.sleep(1000);
        clickOn(getNodeFromDialogWithScroll("Stadium3"));
        Thread.sleep(700);
        pressOkBot(node);
        Thread.sleep(700);
        pressOkBot(node);
        Thread.sleep(500);
        logOutBot();

        node=logInBot("AR1X","AR1X",5,false,true);
        Thread.sleep(700);
        write("Test team");
        Thread.sleep(700);
        pressOkBot(node);
        Thread.sleep(700);
        node=lookup(".dialog-pane").query();
        clickOn(node);
        Thread.sleep(700);
        moveBy(10,37);
        clickOn(getNodeFromDialogWithScroll("Owner1X"));
        Thread.sleep(700);
        pressOkBot(node);
        Thread.sleep(700);
        pressOkBot(node);
        Thread.sleep(700);
        logOutBot();


        node=logInBot("Owner1X","Owner1X",1,false,true);
        Thread.sleep(700);
        write("Test team");
        Thread.sleep(700);
        pressOkBot(node);
        node=lookup(".dialog-pane").query();
        clickOn(node);
        Thread.sleep(1000);
        clickOn(getNodeFromDialogWithScroll("League1"));
        Thread.sleep(700);
        pressOkBot(node);

        node=lookup(".dialog-pane").query();
        clickOn(node);
        Thread.sleep(700);
        clickOn(getNodeFromDialogWithScroll("Stadium3"));
        Thread.sleep(700);
        pressOkBot(node);
        Thread.sleep(700);
        pressOkBot(node);
        Thread.sleep(500);
        logOutBot();

        deleteTestTeam();
    }

    private void pressOkBot(Node node) {
        node = lookup((Button b) -> b.isDefaultButton()).from(node).query();
        clickOn(node);
    }

    private void logOutBot() {
        Node node;
        node=lookup("#logout_button").query();
        clickOn(node);
    }
    private Node logInBot(String username, String password, int option,boolean firstLogIn,boolean checkAlerts) throws InterruptedException {
        if(firstLogIn)
            clickOn("#login_button");
        clickOn("#username_input");
        write(username);
        Thread.sleep(700);
        clickOn("#password_input");
        write(password);
        Thread.sleep(700);
        clickOn("#submit_button");
        Thread.sleep(700);

        if(checkAlerts){
            Node alertNode=lookup("#alertLabel").query();
            clickOn(alertNode);
            Thread.sleep(2000);
        }

        Node node = lookup("#col_options").nth(option).query();
        clickOn(node);
        Thread.sleep(700);
        clickOn("#runMethod_button");
        return node;
    }



    private Node getNodeFromDialogWithScroll(String option) throws InterruptedException {
        boolean optionFound=false;
        int optionCounter=0;
        while (!optionFound) {
            Set nodeSet=lookup(".list-cell").queryAll();
            for(Object CS:nodeSet){
                if(CS.toString().contains(option)){
                    optionFound=true;
                    break;
                }
                else
                    optionCounter++;
            }
            if(!optionFound){
                optionCounter=0;
                scroll( 1, VerticalDirection.DOWN);
                Thread.sleep(1000);
            }
        }
        return lookup(".list-cell").nth(optionCounter).query();
    }


    private void deleteTestTeam(){
        String sendToServer="Data@"+ Client.getUserName();
        List<String> parameters=new ArrayList<>();
        Client.connectToServer(new Pair<>(sendToServer,new Pair<>("deleteTestTeam",parameters)));
    }

    private void returnGameEvent() throws InterruptedException {
        Node node=null;

        node = lookup("#col_options").nth(3).query();
        clickOn(node);
        Thread.sleep(700);
        clickOn("#runMethod_button");

        node=lookup(".dialog-pane").query();
        clickOn(node);
        Thread.sleep(1000);
        clickOn(getNodeFromDialogWithScroll("Team1"));
        Thread.sleep(700);
        pressOkBot(node);

        node=lookup(".dialog-pane").query();
        clickOn(node);
        Thread.sleep(1000);
        clickOn(getNodeFromDialogWithScroll("80"));
        Thread.sleep(700);
        pressOkBot(node);

        node=lookup(".dialog-pane").query();
        clickOn(node);
        Thread.sleep(1000);
        clickOn(getNodeFromDialogWithScroll("goal"));
        Thread.sleep(700);
        pressOkBot(node);

        Thread.sleep(700);
        write("Team2 scored");
        Thread.sleep(700);
        pressOkBot(node);
        Thread.sleep(700);
        pressOkBot(node);
        Thread.sleep(700);
        logOutBot();
    }




}