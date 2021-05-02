package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ServiceRequestHomeNewController {
    @FXML private JFXButton Home;
    @FXML private JFXButton facilitiesMaintenance;
    @FXML private JFXButton laundryServices;
    @FXML private JFXButton externalPatientTrans;
    @FXML private JFXButton floralDelivery;
    @FXML private JFXButton foodDelivery;
    @FXML private JFXButton computerServices;
    @FXML private JFXButton languageInterpretation;
    @FXML private JFXButton internalPatientTrans;
    @FXML private JFXButton sanitationService;
    @FXML private JFXButton medicineDelivery;
    @FXML private JFXButton giftDelivery;
    @FXML private ImageView homeImage;
    @FXML private ImageView facilitiesImage;
    @FXML private ImageView laundryImage;
    @FXML private ImageView externalImage;
    @FXML private ImageView floralImage;
    @FXML private ImageView foodImage;
    @FXML private ImageView computerImage;
    @FXML private ImageView langImage;
    @FXML private ImageView medicineImage;
    @FXML private ImageView internalImage;
    @FXML private ImageView sanitationImage;
    @FXML private ImageView giftImage;

    @FXML
    public void initialize(){
        Home.setDisableVisualFocus(true); // Disable visual focus on home button, occurs on returning to menu - LM
    }

    @FXML
    private void handleButtonPushed(ActionEvent actionEvent) throws IOException {

        Button buttonPushed = (Button) actionEvent.getSource();  //Getting current stage
        Stage stage;
        Parent root;

        if(buttonPushed == floralDelivery){
            goToScreen(actionEvent, "/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/FloralDeliveryServiceRequestView.fxml","Floral Delivery",buttonPushed);
        }
        else if (buttonPushed == Home) {
            goToScreen(actionEvent,"/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml","Home Page",buttonPushed);
        }

        else if (buttonPushed == languageInterpretation){
            goToScreen(actionEvent, "/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/LanguageInterpretationServiceRequestView.fxml","Language Interpretation",buttonPushed);
        }

        else if (buttonPushed == foodDelivery){
            goToScreen(actionEvent, "/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/FoodDeliveryServiceRequestView.fxml","Food Delivery",buttonPushed);
        }
        else if (buttonPushed == externalPatientTrans){
            goToScreen(actionEvent, "/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/ExternalTrans.fxml","External Patient Transportation",buttonPushed);
        }
        else if (buttonPushed == facilitiesMaintenance){
            goToScreen(actionEvent, "/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/maintenanceRequest.fxml", "Facilities Maintenance", buttonPushed);
        }
        else if (buttonPushed == computerServices){
            goToScreen(actionEvent, "/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/ComputerServiceRequestView.fxml", "IT Services", buttonPushed);
        }
        else if (buttonPushed == sanitationService){
            goToScreen(actionEvent, "/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/SanitationRequest.fxml", "Sanitation Services", buttonPushed);
        }
        else if (buttonPushed == laundryServices){
            goToScreen(actionEvent, "/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/LaundryRequest.fxml", "Laundry Service", buttonPushed);
        }
        else if (buttonPushed == medicineDelivery){
            goToScreen(actionEvent, "/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/MedicineDeliveryServiceRequestView.fxml", "Medicine Delivery", buttonPushed);
        }
        else if (buttonPushed == giftDelivery){
            goToScreen(actionEvent, "/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/GiftDeliveryServiceRequest.fxml", "Gift Delivery", buttonPushed);
        }
        else if (buttonPushed == internalPatientTrans){
            goToScreen(actionEvent, "/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/InternalTransportationView.fxml", "Internal Patient Transport", buttonPushed);
        }
    }

    public void goToScreen(ActionEvent e, String URL, String Title, Button currentScreen)throws IOException{
        SceneContext.getSceneContext().switchScene(URL);
    }



    public void handleHoverOn(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();

        if (btn == Home){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/HomeBlackText.png"));
            homeImage.setImage(image);
        }
        else if (btn == facilitiesMaintenance){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/FacilitiesBlackText.png"));
            facilitiesImage.setImage(image);
        }
        else if (btn == laundryServices){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/LaundryServicesBlackText.png"));
            laundryImage.setImage(image);
        }
        else if (btn == externalPatientTrans){
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
        else if (btn == computerServices){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/ComputerBlackText.png"));
            computerImage.setImage(image);
        }
        else if (btn == languageInterpretation){
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
        else if (btn == giftDelivery){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/GiftDeliveryBlackText.png"));
            giftImage.setImage(image);
        }
    }

    public void handleHoverOff(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();

        if (btn == Home){
            btn.setStyle("-fx-background-color: #03256c");
            Image home = new Image(getClass().getResourceAsStream("/imagesAndLogos/homeWhiteText.png"));
            homeImage.setImage(home);
        }
        else if (btn == facilitiesMaintenance){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/FacilitiesWhiteText.png"));
            facilitiesImage.setImage(image);
        }
        else if (btn == laundryServices){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/LaundryServicesWhiteText.png"));
            laundryImage.setImage(image);
        }
        else if (btn == externalPatientTrans){
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
        else if (btn == computerServices){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/ComputerWhiteText.png"));
            computerImage.setImage(image);
        }
        else if (btn == languageInterpretation){
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
        else if (btn == giftDelivery){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/GiftDeliveryWhiteText.png"));
            giftImage.setImage(image);
        }
    }
}