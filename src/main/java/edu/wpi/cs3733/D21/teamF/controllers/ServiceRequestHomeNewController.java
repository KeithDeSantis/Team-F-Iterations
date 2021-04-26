package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

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
    @FXML private ImageView homeImage;
    @FXML private ImageView facilitiesImage;
    @FXML private ImageView laundryImage;
    @FXML private ImageView externalImage;
    @FXML private ImageView floralImage;
    @FXML private ImageView foodImage;
    @FXML private ImageView computorImage;
    @FXML private ImageView langImage;
    @FXML private ImageView medicineImage;
    @FXML private ImageView internalImage;
    @FXML private ImageView sanitationImage;



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

        else if (buttonPushed == launguageInterpretation){
            goToScreen(actionEvent,"/edu/wpi/cs3733/D21/teamF/fxml/LanguageInterpretationServiceRequestView.fxml","Language Interpretation",buttonPushed);
        }

        else if (buttonPushed == foodDelivery){
            goToScreen(actionEvent,"/edu/wpi/cs3733/D21/teamF/fxml/FoodDeliveryServiceRequestView.fxml","Food Delivery",buttonPushed);
        }
        else if (buttonPushed == externalPaitintTrans){
            goToScreen(actionEvent,"/edu/wpi/cs3733/D21/teamF/fxml/ExternalTrans.fxml","External Patient Transportation",buttonPushed);
        }
        else if (buttonPushed == faciliteisMaitence){
            goToScreen(actionEvent, "/edu/wpi/cs3733/D21/teamF/fxml/maintenanceRequest.fxml", "Facilities Maintenance", buttonPushed);
        }
        else if (buttonPushed == computorServices){
            goToScreen(actionEvent, "/edu/wpi/cs3733/D21/teamF/fxml/ComputerServiceRequestView.fxml", "IT Services", buttonPushed);
        }
        /* FIXME Waiting on ben to fix this page
        else if (buttonPushed == sanitationService){
            goToScreen(actionEvent, "/edu/wpi/cs3733/D21/teamF/fxml/SanitationRequest.fxml", "Sanitation Services", buttonPushed);
        }
         */
        else if (buttonPushed == laundryServices){
            goToScreen(actionEvent, "/edu/wpi/cs3733/D21/teamF/fxml/LaundryRequest.fxml", "Laundry Service", buttonPushed);
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
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/HomeBlackText.png"));
            homeImage.setImage(image);
        }
        else if (btn == faciliteisMaitence){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/FacilitiesBlackText.png"));
            facilitiesImage.setImage(image);
        }
        else if (btn == laundryServices){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/LaundryServicesBlackText.png"));
            laundryImage.setImage(image);
        }
        else if (btn == externalPaitintTrans){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/ExtPatientTransportBlackText.png"));
            externalImage.setImage(image);
        }
        else if (btn == floralDelivery){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/FloralDeliveryBlackText.png"));
            floralImage.setImage(image);
        }
        else if (btn == foodDelivery){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/FoodDeliveryBlackText.png"));
            foodImage.setImage(image);
        }
        else if (btn == computorServices){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/ComputerBlackText.png"));
            computorImage.setImage(image);
        }
        else if (btn == launguageInterpretation){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/LangInterpBlackText.png"));
            langImage.setImage(image);
        }
        else if (btn == internalPatientTrans){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/IntPatientTransportBlackText.png"));
            internalImage.setImage(image);
        }
        else if (btn == sanitationService){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/SanitationServicesBlackText.png"));
            sanitationImage.setImage(image);
        }
        else if (btn == medicineDelivery){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/MedDeliveryBlackText.png"));
            medicineImage.setImage(image);
        }
    }

    public void handleHoverOff(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();

        if (btn == Home){
            btn.setStyle("-fx-background-color: #03256c");
            Image home = new Image(getClass().getResourceAsStream("/imagesAndLogos/homeWhiteText.png"));
            homeImage.setImage(home);
        }
        else if (btn == faciliteisMaitence){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/FacilitiesWhiteText.png"));
            facilitiesImage.setImage(image);
        }
        else if (btn == laundryServices){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/LaundryServicesWhiteText.png"));
            laundryImage.setImage(image);
        }
        else if (btn == externalPaitintTrans){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/ExtPatientTransportWhiteText.png"));
            externalImage.setImage(image);
        }
        else if (btn == floralDelivery){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/FloralDeliveryWhiteText.png"));
            floralImage.setImage(image);
        }
        else if (btn == foodDelivery){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/FoodDeliveryWhiteText.png"));
            foodImage.setImage(image);
        }
        else if (btn == computorServices){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/ComputerWhiteText.png"));
            computorImage.setImage(image);
        }
        else if (btn == launguageInterpretation){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/LangInterpWhiteText.png"));
            langImage.setImage(image);
        }
        else if (btn == internalPatientTrans){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/IntPatientTransportWhiteText.png"));
            internalImage.setImage(image);
        }
        else if (btn == sanitationService){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/SanitationServicesWhiteText.png"));
            sanitationImage.setImage(image);
        }
        else if (btn == medicineDelivery){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/MedDeliveryWhiteText.png"));
            medicineImage.setImage(image);
        }
    }
}