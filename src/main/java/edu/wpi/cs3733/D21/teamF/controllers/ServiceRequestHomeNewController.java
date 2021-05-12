package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    @FXML private Label home;
    @FXML private Label facilities;
    @FXML private Label computer;
    @FXML private Label gift;
    @FXML private Label laundry;
    @FXML private Label extPatient;
    @FXML private Label langInterp;
    @FXML private Label intPatient;
    @FXML private Label floral;
    @FXML private Label food;
    @FXML private Label medicine;
    @FXML private Label sanitation;


    @FXML
    public void initialize(){
        Home.setDisableVisualFocus(true); // Disable visual focus on home button, occurs on returning to menu - LM
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
            System.out.println("MEDICINE!!!");
            goToScreen("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/MedicineDeliveryServiceRequestView.fxml");//"/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/MedicineDeliveryServiceRequestView.fxml");
        }
        else if (buttonPushed == giftDelivery){
            goToScreen("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/GiftDeliveryServiceRequest.fxml");
        }
        else if (buttonPushed == internalPatientTrans){
            goToScreen("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/InternalTransportationView.fxml");
        }
    }

    public void goToScreen(String URL)throws IOException{
        //System.out.println("GOTO " + URL);
        SceneContext.getSceneContext().switchScene(URL);
    }



    public void handleHoverOn(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();

        if (btn == Home){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/homeBlack.png.png"));
            homeImage.setImage(image);
            home.setStyle("-fx-text-fill: #000000");
        }
        else if (btn == facilitiesMaintenance){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/facilitiesBlack.png.png"));
            facilitiesImage.setImage(image);
            facilities.setStyle("-fx-text-fill: #000000");
        }
        else if (btn == laundryServices){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/laundryBlack.png.png"));
            laundryImage.setImage(image);
            laundry.setStyle("-fx-text-fill: #000000");
        }
        else if (btn == externalPatientTrans){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/extPatientBlack.png.png"));
            externalImage.setImage(image);
            extPatient.setStyle("-fx-text-fill: #000000");
        }
        else if (btn == floralDelivery){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/floralBlack.png.png"));
            floralImage.setImage(image);
            floral.setStyle("-fx-text-fill: #000000");
        }
        else if (btn == foodDelivery){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/foodBlack.png.png"));
            foodImage.setImage(image);
            food.setStyle("-fx-text-fill: #000000");
        }
        else if (btn == computerServices){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/computerBlack.png.png"));
            computerImage.setImage(image);
            computer.setStyle("-fx-text-fill: #000000");
        }
        else if (btn == languageInterpretation){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/langInterpBlack.png.png"));
            langImage.setImage(image);
            langInterp.setStyle("-fx-text-fill: #000000");
        }
        else if (btn == internalPatientTrans){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/intPatientBlack.png"));
            internalImage.setImage(image);
            intPatient.setStyle("-fx-text-fill: #000000");
        }
        else if (btn == sanitationService){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/sanitationBlack.png.png"));
            sanitationImage.setImage(image);
            sanitation.setStyle("-fx-text-fill: #000000");
        }
        else if (btn == medicineDelivery){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/medicineBlack.png.png"));
            medicineImage.setImage(image);
            medicine.setStyle("-fx-text-fill: #000000");
        }
        else if (btn == giftDelivery){
            btn.setStyle("-fx-background-color: #F0C808");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/giftBlack.png.png"));
            giftImage.setImage(image);
            gift.setStyle("-fx-text-fill: #000000");
        }
    }

    public void handleHoverOff(MouseEvent mouseEvent) {
        JFXButton btn = (JFXButton) mouseEvent.getSource();

        if (btn == Home){
            btn.setStyle("-fx-background-color: #94C4F1");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/homeBlack.png"));
            homeImage.setImage(image);
            home.setStyle("-fx-text-fill: #000000");
        }
        else if (btn == facilitiesMaintenance){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/facilitiesWhite.png"));
            facilitiesImage.setImage(image);
            facilities.setStyle("-fx-text-fill: #FFFFFF");
        }
        else if (btn == laundryServices){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/laundryWhite.png.png"));
            laundryImage.setImage(image);
            laundry.setStyle("-fx-text-fill: #FFFFFF");
        }
        else if (btn == externalPatientTrans){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/extPatientWhite.png.png"));
            externalImage.setImage(image);
            extPatient.setStyle("-fx-text-fill: #FFFFFF");
        }
        else if (btn == floralDelivery){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/floralWhite.png.png"));
            floralImage.setImage(image);
            floral.setStyle("-fx-text-fill: #FFFFFF");
        }
        else if (btn == foodDelivery){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/foodWhite.png.png"));
            foodImage.setImage(image);
            food.setStyle("-fx-text-fill: #FFFFFF");
        }
        else if (btn == computerServices){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/computerWhite.png.png"));
            computerImage.setImage(image);
            computer.setStyle("-fx-text-fill: #FFFFFF");
        }
        else if (btn == languageInterpretation){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/langInterpWhite.png.png"));
            langImage.setImage(image);
            langInterp.setStyle("-fx-text-fill: #FFFFFF");
        }
        else if (btn == internalPatientTrans){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/intPatientWhite.png.png"));
            internalImage.setImage(image);
            intPatient.setStyle("-fx-text-fill: #FFFFFF");
        }
        else if (btn == sanitationService){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/sanitationWhite.png.png"));
            sanitationImage.setImage(image);
            sanitation.setStyle("-fx-text-fill: #FFFFFF");
        }
        else if (btn == medicineDelivery){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/medicineWhite.png.png"));
            medicineImage.setImage(image);
            medicine.setStyle("-fx-text-fill: #FFFFFF");
        }
        else if (btn == giftDelivery){
            btn.setStyle("-fx-background-color: #03256c");
            Image image = new Image(getClass().getResourceAsStream("/imagesAndLogos/serviceButtonNoText/giftWhite.png.png"));
            giftImage.setImage(image);
            gift.setStyle("-fx-text-fill: #FFFFFF");
        }
    }
}