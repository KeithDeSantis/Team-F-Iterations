package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamF.Translation.Translator;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ServiceRequestHomeNewController extends AbsController {
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

//        Home.textProperty().bind(Translator.getTranslator().getTranslationBinding(Home.getText()));
//        facilitiesMaintenance.textProperty().bind(Translator.getTranslator().getTranslationBinding(facilitiesMaintenance.getText()));
//        laundryServices.textProperty().bind(Translator.getTranslator().getTranslationBinding(laundryServices.getText()));
//        externalPatientTrans.textProperty().bind(Translator.getTranslator().getTranslationBinding(externalPatientTrans.getText()));
//        floralDelivery.textProperty().bind(Translator.getTranslator().getTranslationBinding(floralDelivery.getText()));
//        foodDelivery.textProperty().bind(Translator.getTranslator().getTranslationBinding(foodDelivery.getText()));
//        computerServices.textProperty().bind(Translator.getTranslator().getTranslationBinding(computerServices.getText()));
//        languageInterpretation.textProperty().bind(Translator.getTranslator().getTranslationBinding(languageInterpretation.getText()));
//        internalPatientTrans.textProperty().bind(Translator.getTranslator().getTranslationBinding(internalPatientTrans.getText()));
//        sanitationService.textProperty().bind(Translator.getTranslator().getTranslationBinding(sanitationService.getText()));
//        medicineDelivery.textProperty().bind(Translator.getTranslator().getTranslationBinding(medicineDelivery.getText()));
//        giftDelivery.textProperty().bind(Translator.getTranslator().getTranslationBinding(giftDelivery.getText()));
    }

    @FXML
    private void handleButtonPushed(ActionEvent actionEvent) throws IOException {

        Button buttonPushed = (Button) actionEvent.getSource();  //Getting current stage

        if(buttonPushed == floralDelivery){
            goToScreen("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/FloralDeliveryServiceRequestView.fxml");
        }
        else if (buttonPushed == Home) {
            SceneContext.getSceneContext().loadDefault();
        }

        else if (buttonPushed == languageInterpretation){
            goToScreen("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/LanguageInterpretationServiceRequestView.fxml");
        }

        else if (buttonPushed == foodDelivery){
            goToScreen("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/FoodDeliveryServiceRequestView.fxml");
        }
        else if (buttonPushed == externalPatientTrans){
            goToScreen("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/ExternalTrans.fxml");
        }
        else if (buttonPushed == facilitiesMaintenance){
            goToScreen("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/maintenanceRequest.fxml");
        }
        else if (buttonPushed == computerServices){
            goToScreen("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/ComputerServiceRequestView.fxml");
        }
        else if (buttonPushed == sanitationService){
            goToScreen("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/SanitationRequest.fxml");
        }
        else if (buttonPushed == laundryServices){
            goToScreen("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/LaundryRequest.fxml");
        }
        else if (buttonPushed == medicineDelivery){
            goToScreen("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/MedicineDeliveryServiceRequestView.fxml");
        }
        else if (buttonPushed == giftDelivery){
            goToScreen("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/GiftDeliveryServiceRequest.fxml");
        }
        else if (buttonPushed == internalPatientTrans){
            goToScreen("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/InternalTransportationView.fxml");
        }
    }

    public void goToScreen(String URL)throws IOException{
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
            btn.setStyle("-fx-background-color: #94C4F1");
            Image home = new Image(getClass().getResourceAsStream("/imagesAndLogos/homeBlackText.png"));
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