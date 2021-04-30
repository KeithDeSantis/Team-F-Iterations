package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.pathfinding.NodeEntry;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Controller for Food Delivery Service View
 * @author karenhou
 */
public class FoodDeliveryServiceRequestController {

    @FXML private JFXButton xButton;
    @FXML private JFXButton cancelButton;
    @FXML private JFXButton helpButton;
    @FXML private Button helpXButton;
    @FXML private JFXButton submitButton;
    @FXML private JFXComboBox<String> deliveryLocationField;
    @FXML private JFXTimePicker deliveryTimeField;
    @FXML private JFXTextField allergyField;
    @FXML private JFXTextField specialInstructionsField;
    @FXML private JFXRadioButton rButtonFood1;
    @FXML private JFXRadioButton rButtonFood2;
    @FXML private JFXRadioButton rButtonFood3;
    @FXML private JFXRadioButton rButtonFood4;
    @FXML private JFXRadioButton rButtonDrink1;
    @FXML private JFXRadioButton rButtonDrink2;
    @FXML private JFXRadioButton rButtonDrink3;
    @FXML private JFXRadioButton rButtonDrink4;
    @FXML private JFXCheckBox cbSide1;
    @FXML private JFXCheckBox cbSide2;
    @FXML private JFXCheckBox cbSide3;
    @FXML private JFXCheckBox cbSide4;
    @FXML private Label title;
    @FXML private Label locLabel;
    @FXML private Label delLabel;
    @FXML private Label allLabel;
    @FXML private Label siLabel;
    @FXML private Label mealLabel;
    @FXML private Label sideLabel;
    @FXML private Label drinkLabel;
    @FXML private HBox header;


    @FXML
    private void initialize(){

        try{
            List<NodeEntry> nodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries();

            final ObservableList<String> nodeList = FXCollections.observableArrayList();
            for(NodeEntry n: nodeEntries){
                nodeList.add(n.getShortName());
            }
            //nodeList.addAll(nodeEntries.stream().map(NodeEntry::getShortName).sorted().collect(Collectors.toList()));
            this.deliveryLocationField.setItems(nodeList);

        } catch(Exception e){

        }
    }


    public void handleBack(MouseEvent mouseEvent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml");
    }

    /**
     * handles submit being pressed
     * @param e is the button being pushed
     * @throws IOException
     * @author KH
     */
    @FXML
    private void handleSubmitPushed(ActionEvent e) throws IOException, SQLException {
        if (formFilledOut()) {
            String uuid = UUID.randomUUID().toString();
            String type = "Food Delivery";
            String person = "";
            String completed = "false";
            String additionalInfo = "Delivery Location: " + deliveryLocationField.getValue() + "Delivery time: " + deliveryTimeField.getValue();
            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, person, completed, additionalInfo);

            // Loads form submitted window and passes in current stage to return to request home
            FXMLLoader submitedPageLoader = new FXMLLoader();
            submitedPageLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/FormSubmittedView.fxml"));
            Stage submittedStage = new Stage();
            Parent root = submitedPageLoader.load();
            FormSubmittedViewController formSubmittedViewController = submitedPageLoader.getController();
            formSubmittedViewController.changeStage((Stage) rButtonFood1.getScene().getWindow());
            Scene submitScene = new Scene(root);
            submittedStage.setScene(submitScene);
            submittedStage.setTitle("Submission Complete");
            submittedStage.initModality(Modality.APPLICATION_MODAL);
            submittedStage.showAndWait();
        }
    }


    /**
     * handles cancel and help button being pushed
     * @param e is the button being pressed
     * @throws IOException
     * @author KH
     */
    @FXML
    private void handleButtonPushed(ActionEvent e) throws IOException{
        Button buttonPushed = (Button) e.getSource();

        if (buttonPushed == cancelButton) { // is cancel button
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml");
        } else if (buttonPushed == helpButton){
            Stage helpPopUpStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/FoodDeliveryHelpView.fxml"));
            Scene helpPopUpScene = new Scene(root);
            helpPopUpStage.setScene(helpPopUpScene);
            helpPopUpStage.setTitle("Food Delivery Request Help Menu");
            helpPopUpStage.initModality(Modality.APPLICATION_MODAL);
            helpPopUpStage.initOwner(buttonPushed.getScene().getWindow());
            helpPopUpStage.showAndWait();
        } else if (buttonPushed == helpXButton){
            Stage popUpStage = (Stage) helpXButton.getScene().getWindow();
            popUpStage.close();
        }
    }

    /**
     * Helper that checks if form is acceptably filled out
     * @return true if form is filled out
     * @author KH
     */
    private boolean formFilledOut() {
        boolean isFilled = true;
        if(! (rButtonFood1.isSelected() || rButtonFood2.isSelected() || rButtonFood3.isSelected() || rButtonFood4.isSelected())){
            isFilled = false;
            rButtonFood1.setStyle("-fx-text-fill: #e8321e");
            rButtonFood2.setStyle("-fx-text-fill: #e8321e");
            rButtonFood3.setStyle("-fx-text-fill: #e8321e");
            rButtonFood4.setStyle("-fx-text-fill: #e8321e");
        } else {
            rButtonFood1.setStyle("-fx-text-fill: #000000");
            rButtonFood2.setStyle("-fx-text-fill: #000000");
            rButtonFood3.setStyle("-fx-text-fill: #000000");
            rButtonFood4.setStyle("-fx-text-fill: #000000");
        }
        if(! (rButtonDrink1.isSelected() || rButtonDrink2.isSelected() || rButtonDrink3.isSelected() || rButtonDrink4.isSelected())){
            isFilled = false;
            rButtonDrink1.setStyle("-fx-text-fill: #e8321e");
            rButtonDrink2.setStyle("-fx-text-fill: #e8321e");
            rButtonDrink3.setStyle("-fx-text-fill: #e8321e");
            rButtonDrink4.setStyle("-fx-text-fill: #e8321e");
        } else {
            rButtonDrink1.setStyle("-fx-text-fill: #000000");
            rButtonDrink2.setStyle("-fx-text-fill: #000000");
            rButtonDrink3.setStyle("-fx-text-fill: #000000");
            rButtonDrink4.setStyle("-fx-text-fill: #000000");
        }
        if(! (cbSide1.isSelected() || cbSide2.isSelected() || cbSide3.isSelected() || cbSide4.isSelected())){
            isFilled = false;
            cbSide1.setStyle("-fx-text-fill: #e8321e");
            cbSide2.setStyle("-fx-text-fill: #e8321e");
            cbSide3.setStyle("-fx-text-fill: #e8321e");
            cbSide4.setStyle("-fx-text-fill: #e8321e");
        } else {
            cbSide1.setStyle("-fx-text-fill: #000000");
            cbSide2.setStyle("-fx-text-fill: #000000");
            cbSide3.setStyle("-fx-text-fill: #000000");
            cbSide4.setStyle("-fx-text-fill: #000000");
        }
        if(deliveryLocationField.getValue() == null){
            isFilled = false;
            deliveryLocationField.setStyle("-fx-background-color: #ffbab8");
        } else {
            deliveryLocationField.setStyle("-fx-background-color: transparent");
        }
        if(deliveryTimeField.getValue() == null){
            isFilled = false;
            deliveryTimeField.setStyle("-fx-background-color: #ffbab8");
        } else {
            deliveryTimeField.setStyle("-fx-background-color: transparent");
        }

        return isFilled;
    }

    /**
     * Handles radial button groups
     * @param e is the button being pushed
     * @author KH
     */
    @FXML
    private void handleRadialButtonPushed(ActionEvent e){
        ToggleGroup foodGroup = new ToggleGroup(); //group for foods
        rButtonFood1.setToggleGroup(foodGroup);
        rButtonFood2.setToggleGroup(foodGroup);
        rButtonFood3.setToggleGroup(foodGroup);
        rButtonFood4.setToggleGroup(foodGroup);

        ToggleGroup drinkGroup = new ToggleGroup(); //group for drinks
        rButtonDrink1.setToggleGroup(drinkGroup);
        rButtonDrink2.setToggleGroup(drinkGroup);
        rButtonDrink3.setToggleGroup(drinkGroup);
        rButtonDrink4.setToggleGroup(drinkGroup);

    }

    @FXML
    public void handleHoverOn(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #F0C808; -fx-text-fill: #000000;");
    }

    @FXML
    public void handleHoverOff(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();
        btn.setStyle("-fx-background-color: #03256C; -fx-text-fill: #FFFFFF;");
    }



}
