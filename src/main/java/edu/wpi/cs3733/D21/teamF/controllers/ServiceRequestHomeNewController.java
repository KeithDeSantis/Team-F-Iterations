package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ServiceRequestHomeNewController {
    @FXML private JFXButton Home;
    @FXML private JFXButton faciliteisMaitence;
    @FXML private JFXButton laundryServices;
    @FXML private JFXButton externalPaitintTrans;
    @FXML private JFXButton floralDelivery;
    @FXML private JFXButton foodDelivery;
    @FXML private JFXButton computorServices;
    @FXML private JFXButton launguageInterpretation;
    @FXML private JFXButton internalPatientTrans;
    @FXML private JFXButton sanitationService;
    @FXML private JFXButton medicineDelivery;

    @FXML
    private void handleButtonPushed(ActionEvent actionEvent) throws IOException {

        Button buttonPushed = (Button) actionEvent.getSource();  //Getting current stage
        Stage stage;
        Parent root;

        if(buttonPushed == floralDelivery){
            goToScreen(actionEvent,"/edu/wpi/cs3733/D21/teamF/fxml/FloralDeliveryServiceRequestView.fxml","Floral Delivery",buttonPushed);
        }
        else if (buttonPushed == Home) {
            goToScreen(actionEvent,"/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml","Home Page",buttonPushed);
        }

        else if (buttonPushed == laundryServices){
            goToScreen(actionEvent,"/edu/wpi/cs3733/D21/teamF/fxml/LanguageInterpretationServiceRequestView.fxml","Language Interpretation",buttonPushed);
        }

        else if (buttonPushed == foodDelivery){
            goToScreen(actionEvent,"/edu/wpi/cs3733/D21/teamF/fxml/FoodDeliveryServiceRequestView.fxml","Food Delivery",buttonPushed);
        }
        else if (buttonPushed == externalPaitintTrans){
            goToScreen(actionEvent,"/edu/wpi/cs3733/D21/teamF/fxml/ExternalTrans.fxml","Food Delivery",buttonPushed);
        }

    }

    public void goToScreen(ActionEvent e, String URL, String Title, Button currentScreen)throws IOException{
        Stage stage;
        Parent root;
        stage = (Stage) currentScreen.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(URL));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(Title);
        stage.show();
    }



    public void handleHoverOn(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();

        if (btn == Home){
            btn.setStyle("-fx-background-color: #F0C808; -fx-graphic: url('/imagesAndLogos/HomeBlackText.png')");
        }
        else if (btn == faciliteisMaitence){
            btn.setStyle("-fx-background-color: #F0C808; -fx-graphic: url('/imagesAndLogos/FacilitiesBlackText.png')");
        }
        else if (btn == laundryServices){
            btn.setStyle("-fx-background-color: #F0C808; -fx-graphic: url('/imagesAndLogos/LaundryServicesBlackText.png')");
        }
        else if (btn == externalPaitintTrans){
            btn.setStyle("-fx-background-color: #F0C808; -fx-graphic: url('/imagesAndLogos/ExtPatientTransportBlackText.png')");
        }
        else if (btn == floralDelivery){
            btn.setStyle("-fx-background-color: #F0C808; -fx-graphic: url('/imagesAndLogos/FloralDeliveryBlackText.png')");
        }
        else if (btn == foodDelivery){
            btn.setStyle("-fx-background-color: #F0C808; -fx-graphic: url('/imagesAndLogos/FoodDeliveryBlackText.png')");
        }
        else if (btn == computorServices){
            btn.setStyle("-fx-background-color: #F0C808; -fx-graphic: url('/imagesAndLogos/ComputerBlackText.png')");
        }
        else if (btn == launguageInterpretation){
            btn.setStyle("-fx-background-color: #F0C808; -fx-graphic: url('/imagesAndLogos/LangInterpBlackText.png')");
        }
        else if (btn == internalPatientTrans){
            btn.setStyle("-fx-background-color: #F0C808; -fx-graphic: url('/imagesAndLogos/IntPatientTransportBlackText.png')");
        }
        else if (btn == sanitationService){
            btn.setStyle("-fx-background-color: #F0C808; -fx-graphic: url('/imagesAndLogos/SanitationServicesBlackText.png')");
        }
        else if (btn == medicineDelivery){
            btn.setStyle("-fx-background-color: #F0C808; -fx-graphic: url('/imagesAndLogos/MedDeliveryBlackText.png')");
        }
    }

    public void handleHoverOff(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();

        if (btn == Home){
            btn.setStyle("-fx-background-color: #03256c; -fx-graphic: url('/imagesAndLogos/homeWhiteText.png')");
        }
        else if (btn == faciliteisMaitence){
            btn.setStyle("-fx-background-color: #03256c; -fx-graphic: url('/imagesAndLogos/FacilitiesWhiteText.png')");
        }
        else if (btn == laundryServices){
            btn.setStyle("-fx-background-color: #03256c; -fx-graphic: url('/imagesAndLogos/LaundryServicesWhiteText.png')");
        }
        else if (btn == externalPaitintTrans){
            btn.setStyle("-fx-background-color: #03256c; -fx-graphic: url('/imagesAndLogos/ExtPatientTransportWhiteText.png')");
        }
        else if (btn == floralDelivery){
            btn.setStyle("-fx-background-color: #03256c; -fx-graphic: url('/imagesAndLogos/FloralDeliveryWhiteText.png')");
        }
        else if (btn == foodDelivery){
            btn.setStyle("-fx-background-color: #03256c; -fx-graphic: url('/imagesAndLogos/FoodDeliveryWhiteText.png')");
        }
        else if (btn == computorServices){
            btn.setStyle("-fx-background-color: #03256c; -fx-graphic: url('/imagesAndLogos/ComputerWhiteText.png')");
        }
        else if (btn == launguageInterpretation){
            btn.setStyle("-fx-background-color: #03256c; -fx-graphic: url('/imagesAndLogos/LangInterpWhiteText.png')");
        }
        else if (btn == internalPatientTrans){
            btn.setStyle("-fx-background-color: #03256c; -fx-graphic: url('/imagesAndLogos/IntPatientTransportWhiteText.png')");
        }
        else if (btn == sanitationService){
            btn.setStyle("-fx-background-color: #03256c; -fx-graphic: url('/imagesAndLogos/SanitationServicesWhiteText.png')");
        }
        else if (btn == medicineDelivery){
            btn.setStyle("-fx-background-color: #03256c; -fx-graphic: url('/imagesAndLogos/MedDeliveryWhiteText.png')");
        }
    }
}