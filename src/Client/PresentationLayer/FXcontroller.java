package Client.PresentationLayer;

import Client.Client;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.Pair;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;

//import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.ResourceBundle;


public class FXcontroller implements Initializable,Observer {

    @FXML
    public AnchorPane starter_pane;
    public AnchorPane login_pane;
    public AnchorPane options_pane;

    public TableView<String> options_table;
    public TextField username_input;
    public PasswordField password_input;
    public Button login_button;
    public Button submit_button;
    public TableColumn<String,String> col_options;
    public Button runMethod_button;
    public Label alertLabel;

    GuiMediator guiMediator;
    Timeline alertTimeLine = new Timeline(new KeyFrame(Duration.seconds(0.3), evt -> alertLabel.setStyle("-fx-background-color:RED")),
            new KeyFrame(Duration.seconds( 0.1), evt -> alertLabel.setStyle("-fx-background-color:White")));

//    public static Object realTime=null;

    public void chooseLogin()
    {
        starter_pane.setVisible(false);
        login_pane.setVisible(true);
    }

    public void logIn() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        alertTimeLine.setCycleCount(Animation.INDEFINITE);

        String userName=username_input.getText();
        String password=password_input.getText();

        String result=guiMediator.getUserControllers(userName,password);
        if(!result.equals("true"))
        {
            showAlert(result, Alert.AlertType.WARNING);
        }
        else
        {
            login_pane.setVisible(false);
            options_pane.setVisible(true);
            createOptionsForUser();
            setAlerts();
        }
    }

    public void createOptionsForUser() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        List<String> options=guiMediator.createOptionsForUser();
        ObservableList<String> details = FXCollections.observableArrayList(options);
        col_options.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        options_table.setItems(details);

    }

    public void executeOption() throws InvocationTargetException, IllegalAccessException {
        String choice=options_table.getSelectionModel().getSelectedItem();
        List<String> arguments=new ArrayList<>();
        Pair<Method, List<String>> methodPair=guiMediator.getUserChoice(choice);
        for(String arg:methodPair.getValue())
        {
            Dialog dialog;
            if(arg.contains("@"))
            {
                List<String> choices = guiMediator.getDropDownList(arg,arguments);
                dialog=new ChoiceDialog<String>("", choices);
                dialog.setHeaderText("list Input");
                dialog.setContentText(arg.substring(0,arg.indexOf("@"))+":");
            }
            else
            {
                dialog = new TextInputDialog("");
                dialog.setHeaderText("Text Input");
                dialog.setContentText(arg+":");
            }
            dialog.setTitle("");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                arguments.addAll(Collections.singleton(result.get()));
            }
            else
            {
                showAlert("Operation Cancelled", Alert.AlertType.INFORMATION);
                return;
            }
        }
        String answer= guiMediator.executeMethod(methodPair,arguments);
        showAlert(answer, Alert.AlertType.INFORMATION);
    }





    /**
     * The function shows an alert with the given text
     * @param context
     */
    public void showAlert(String context, Alert.AlertType type)
    {
        Alert a=new Alert(type);
        a.setContentText(context);
        a.getDialogPane().setMinWidth(600);
        a.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        guiMediator=GuiMediator.getInstance();
//        guiMediator.addObserver(this);
        Client.getInstance().addObserver(this);
    }

    public void showRunQueryButton()
    {
        runMethod_button.setVisible(true);
    }


    public void setAlerts() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<String> alerts=guiMediator.getAllAlerts();

        if(alerts.size()>0)
        {
            alertLabel.setVisible(true);
            alertTimeLine.play();
            int i=1;
            List<Label> labels=new ArrayList<>();
            for(String alert:alerts)
            {
                labels.add(new Label(""));
                labels.add(new Label("   "+i+")   "+alert+"   "));
                labels.add(new Label(""));
                i++;
            }
            VBox vBox=new VBox();
            vBox.getChildren().addAll(labels);
            PopOver popOver = new PopOver(vBox);
            alertLabel.setOnMouseEntered(mouseEvent -> {
                alertTimeLine.stop();
                popOver.show(alertLabel);
            });
            alertLabel.setOnMouseExited(mouseEvent ->{
                popOver.hide();
//                alertLabel.setStyle("-fx-background-color:White");
            });
        }
        else
        {
            alertLabel.setVisible(false);
        }

    }

    public void logout()
    {
        guiMediator.logOff();
        options_pane.setVisible(false);
        login_pane.setVisible(true);
        username_input.setText("");
        password_input.setText("");
    }

    public void showRealTimeAlert(String alert)
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                Notifications notificationBuilder=Notifications.create()
                        .title("You have a new alert")
                        .text(alert)
                        .graphic(null)
                        .hideAfter(Duration.minutes(2))
                        .position(Pos.TOP_CENTER);
                notificationBuilder.showInformation();
                notificationBuilder.onAction((event) -> notificationBuilder.hideAfter(Duration.seconds(0)));
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg !=null && arg instanceof Pair &&((Pair<String,String>)arg).getKey().equals(guiMediator.getCurrentUserName()))
        {
            showRealTimeAlert(((Pair<String,String>)arg).getValue());
        }
    }


}
