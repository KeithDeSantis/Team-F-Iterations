package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
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
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/mapEditView.fxml");
        } else if (buttonPushed == manageServices) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/MarkRequestsCompleteView.fxml");
        } else if (buttonPushed == navigation) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AStarDemoView.fxml");
        } else if (buttonPushed == serviceRequest) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeView.fxml");
        } else if (buttonPushed == manageAccount) {
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AccountManagerView.fxml");
        } else if (buttonPushed == quit) {
            Platform.exit();
        } else if (buttonPushed == signOut){
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
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
