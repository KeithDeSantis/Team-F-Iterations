package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class DefaultPageAdminController {

    @FXML
    private JFXButton editMap;
    @FXML
    private JFXButton manageServices;
    @FXML
    private JFXButton navigation;
    @FXML
    private JFXButton serviceRequest;
    @FXML
    private JFXButton manageAccount;
    @FXML
    private JFXButton quit;
    @FXML
    private JFXButton signOut;
    @FXML
    private Text title;
    @FXML
    private void initialize(){
        // Set fonts - LM
        title.setFont(Font.loadFont("file:src/main/resources/fonts/Volkhov-Regular.ttf", 40));
        Font buttonDefault = Font.loadFont("file:src/main/resources/fonts/Montserrat-SemiBold.ttf", 20);
        navigation.setFont(buttonDefault);
        quit.setFont(buttonDefault);
        serviceRequest.setFont(buttonDefault);
        manageAccount.setFont(buttonDefault);
        manageServices.setFont(buttonDefault);
        signOut.setFont(buttonDefault);
        editMap.setFont(buttonDefault);

        // Disable focus on edit button - LM
        editMap.setDisableVisualFocus(true);
    }
    /**
     * Handles the pushing of a button on the screen
     * @param actionEvent the button's push
     * @throws IOException in case of scene switch, if the next fxml scene file cannot be found
     * @author ZheCheng Song
     */
    @FXML
    private void handleButtonPushed(ActionEvent actionEvent) throws IOException {
        Button buttonPushed = (Button) actionEvent.getSource();  //Getting current stage
        Stage stage;
        Parent root;

        if (buttonPushed == editMap) {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/mapEditView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("Edit Map Menu");
            stage.show();
        } else if (buttonPushed == manageServices) {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/MarkRequestsCompleteView.fxml"));
            stage.getScene().setRoot(root);  //Changing the stage
            stage.setTitle("Service Request Manager");
            stage.show();
        } else if (buttonPushed == navigation) {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/AStarDemoView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("AStar Pathfinding Demo");
            stage.show();

        } else if (buttonPushed == serviceRequest) {

            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("Service Request Home");
            stage.show();

        } else if (buttonPushed == manageAccount) {
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/AccountManagerView.fxml"));
            //Scene scene = new Scene(root);
            //stage.setScene(scene);
            stage.getScene().setRoot(root);
            stage.setTitle("Account Manager");
            stage.show();

        } else if (buttonPushed == quit) {
            Platform.exit();

        } else if (buttonPushed == signOut){
            stage = (Stage) buttonPushed.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml"));
            stage.getScene().setRoot(root);
            stage.setTitle("");
            stage.show();
        }
    }

    /** handles highlighting buttons when hovering
     * @author Keith Desantis, insterted by Leo Morris
     * @param mouseEvent The button hovered over
     */
    public void handleHoverOn(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #F0C808; -fx-text-fill: #000000;");
    }

    /** handles removing highlight on buttons when not hovering
     * @author Keith Desantis, insterted by Leo Morris
     * @param mouseEvent The button hovered off
     */
    public void handleHoverOff(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #03256C; -fx-text-fill: #FFFFFF;");
    }
}
