package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.ServiceEntry;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.text.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.*;


public class LanguageInterpretationServiceRequestController extends ServiceRequests implements Initializable {
    @FXML private JFXButton close;
    @FXML private JFXTextField name;
    @FXML private JFXDatePicker date;
    @FXML private JFXTimePicker time;
    @FXML private JFXComboBox<String> appointment;
    @FXML private JFXComboBox<String> language;
    @FXML private JFXButton help;
    @FXML private JFXButton translate;
    @FXML private JFXButton submit;
    @FXML private Label nameLabel;
    @FXML private Label dtLabel;
    @FXML private Label appointmentLabel;
    @FXML private Label languageLabel;

    /**
     * Opens the help window
     * @param actionEvent
     * @throws IOException
     * @author Jay Yen
     */
    public void handleHelp(ActionEvent actionEvent) throws IOException {
        Stage submittedStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/LanguageInterpretationHelpView.fxml"));
        Scene helpPopUp = new Scene(root);
        submittedStage.setScene(helpPopUp);
        submittedStage.setTitle("Language Interpretation Help");
        submittedStage.initModality(Modality.APPLICATION_MODAL);
        submittedStage.initOwner(((Button) actionEvent.getSource()).getScene().getWindow());
        submittedStage.showAndWait();
    }
    /**
     * Calls translate function when translate button is clicked
     * @param actionEvent
     * @throws IOException
     * @author Johvanni Perez
     */
    public void handleTranslate(ActionEvent actionEvent) throws IOException{
        if(language.getValue() != null) {
            List<Label> labelList = new ArrayList<Label>(); //list of labels that need to get fixed
            labelList.add(nameLabel);
            labelList.add(dtLabel);
            labelList.add(appointmentLabel);
            labelList.add(languageLabel);

            HashMap<String, String> codes = codeMap();
            String src = "";                                //empty string that translator uses to autodetect src lang
            String target = codes.get(language.getValue()); //gets lang code of the lang specified
            String text;
            String transText;
            for (Label aLabel : labelList) {
                text = aLabel.getText();
                transText = translate(src, target, text);
                aLabel.setText(transText);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner((Stage) ( (Button) actionEvent.getSource()).getScene().getWindow());  // open alert
            alert.setTitle("Language Missing");
            alert.setHeaderText("Specify language");
            alert.setContentText("Please select a language from the dropdown list.");
            alert.showAndWait();
        }
    }

    /**
     * Opens a window that shows a request received message, and adds a service request to the database
     * @param actionEvent
     * @throws IOException
     * @author Jay Yen
     */
    public void handleSubmit(ActionEvent actionEvent) throws IOException, SQLException {
        if(formFilled()) {
            String uuid = UUID.randomUUID().toString();
            String additionalInstr = "Date: " + date.getValue().toString() + " Time: " + time.getValue() +
                    " Name: " + name.getText() + " Appointment: " + (String) appointment.getValue() + " Language: " + language.getValue();
            ServiceEntry newServiceRequest = new ServiceEntry(uuid,"Language Interpretation Request", " ", "false", additionalInstr);
            DatabaseAPI.getDatabaseAPI().addServiceReq(newServiceRequest.getUuid(), newServiceRequest.getRequestType(),
                    newServiceRequest.getAssignedTo(), newServiceRequest.getCompleteStatus(), newServiceRequest.getAdditionalInstructions());
            // Loads form submitted window and passes in current stage to return to request home
            FXMLLoader submitedPageLoader = new FXMLLoader();
            submitedPageLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/FormSubmittedView.fxml"));
            Stage submittedStage = new Stage();
            Parent root = submitedPageLoader.load();
            FormSubmittedViewController formSubmittedViewController = submitedPageLoader.getController();
            formSubmittedViewController.changeStage((Stage) submit.getScene().getWindow());
            Scene submitScene = new Scene(root);
            submittedStage.setScene(submitScene);
            submittedStage.setTitle("Submission Complete");
            submittedStage.initModality(Modality.APPLICATION_MODAL);
            submittedStage.showAndWait();
        }

    }

    /**
     * Sets the drop down options for appointment type and languages
     * @param location
     * @param resources
     * @author Jay Yen
     */
    public void initialize(URL location, ResourceBundle resources){
        appointment.getItems().add("Non-Specific");
        appointment.getItems().add("Multiple Departments");
        appointment.getItems().add("Allergy and Clinical Immunology");
        appointment.getItems().add("Alzheimer's Center");
        appointment.getItems().add("Anesthesiology");
        appointment.getItems().add("Arrhythmia Services");
        appointment.getItems().add("Arthritis and Joint Diseases Center");
        appointment.getItems().add("Asthma Center");
        appointment.getItems().add("Bone Marrow Transplant Program");
        appointment.getItems().add("Brain Tumor Program");
        appointment.getItems().add("Breast Center");
        appointment.getItems().add("Cardiac Surgery");
        appointment.getItems().add("Cardiology");
        appointment.getItems().add("Cardiomyopathy and Cardiac Transplantation");
        appointment.getItems().add("CAT Scan (CT Imaging");
        appointment.getItems().add("DF/BW Cancer Center");
        appointment.getItems().add("Dental");
        appointment.getItems().add("Dermatology");
        appointment.getItems().add("Diabetes");
        appointment.getItems().add("Ear, Nose and Throat");
        appointment.getItems().add("Emergency Medicine");
        appointment.getItems().add("Endocrinology, Diabetes and Hypertension");
        appointment.getItems().add("Epilepsy");
        appointment.getItems().add("Foot and Ankle Center/Faulkner");
        appointment.getItems().add("Gastroenterology");
        appointment.getItems().add("General and Gastrointestinal Surgery");
        appointment.getItems().add("Genetics");
        appointment.getItems().add("Gynecologic Oncology");
        appointment.getItems().add("Gynecology (General)");
        appointment.getItems().add("Hematology");
        appointment.getItems().add("Infectious Disease");
        appointment.getItems().add("Interventional Cardiology");
        appointment.getItems().add("Interventional Radiology");
        appointment.getItems().add("Lung Dancer Sceening (Low Dose CT)");
        appointment.getItems().add("Lung Transplantation Program");
        appointment.getItems().add("Lupus Center");
        appointment.getItems().add("Magnetic Resonance Imaging (MRI)");
        appointment.getItems().add("Mammography");
        appointment.getItems().add("Maternal-Fetal Medicine");
        appointment.getItems().add("Medicine");
        appointment.getItems().add("Metabolic and Nutrition Support Service");
        appointment.getItems().add("Multiple Sclerosis");
        appointment.getItems().add("Neurology");
        appointment.getItems().add("Neuroradiology");
        appointment.getItems().add("Newborn Medicine");
        appointment.getItems().add("Nuclear Medicine");
        appointment.getItems().add("Nutrition Consultation");
        appointment.getItems().add("Obstetric Anesthesia Service");
        appointment.getItems().add("Obstetrics");
        appointment.getItems().add("Ophthalmology");
        appointment.getItems().add("Oral Medicine, Oral and Maxillofacial Surgery and Dentistry");
        appointment.getItems().add("Orthopaedics");
        appointment.getItems().add("Osteoporosis Center");
        appointment.getItems().add("Otolaryngology");
        appointment.getItems().add("Pain Management Center");
        appointment.getItems().add("Pathology Department");
        appointment.getItems().add("Pediatric and Adolescent Gynecology");
        appointment.getItems().add("Pituitary Program");
        appointment.getItems().add("Plastic Surgery");
        appointment.getItems().add("Podiatry");
        appointment.getItems().add("Primary Care");
        appointment.getItems().add("Prostate Center");
        appointment.getItems().add("Psychiatry");
        appointment.getItems().add("Pulmonary and Critical Care Medicine");
        appointment.getItems().add("Radiation Oncology");
        appointment.getItems().add("Radiology");
        appointment.getItems().add("Renal (Kidney)");
        appointment.getItems().add("Renal (Kidney) Transplantation");
        appointment.getItems().add("Reproductive Medicine");
        appointment.getItems().add("Rheumatology, Inflammation and Immunity");
        appointment.getItems().add("Sleep Medicine");
        appointment.getItems().add("Spine Center");
        appointment.getItems().add("Sports Medicine and Rehabilitation Center");
        appointment.getItems().add("Surgery");
        appointment.getItems().add("Surgery Oncology");
        appointment.getItems().add("Thoracic Surgery");
        appointment.getItems().add("Thyroid");
        appointment.getItems().add("Trauma and Burn Center");
        appointment.getItems().add("Urogynecology");
        appointment.getItems().add("Urology");
        appointment.getItems().add("Vascular Medicine Services");
        appointment.getItems().add("Vascular Surgery");
        appointment.getItems().add("Weight Management");
        appointment.getItems().add("Women's Health");
        appointment.getItems().add("Other");

       // language.getItems().add("Arabic");
       // language.getItems().add("Dutch");
        language.getItems().add("English");
        language.getItems().add("French");
       // language.getItems().add("German");
       // language.getItems().add("Greek");
       // language.getItems().add("Haitian Creole");
        language.getItems().add("Italian");
       // language.getItems().add("Japanese");
       // language.getItems().add("Korean");
        language.getItems().add("Portuguese");
       // language.getItems().add("Russian");
        language.getItems().add("Spanish");
       // language.getItems().add("Vietnamese");

        nameLabel.setText(StringEscapeUtils.unescapeJava("\u0627\u0633\u0645"));
    }
    
    public boolean formFilled(){
        boolean isFilled = true;
        if(name.getText().trim().isEmpty()){
            name.setStyle("-fx-border-color: red");
            isFilled = false;
        }
        if(date.getValue() == null){
            date.setStyle("-fx-border-color: red");
            isFilled = false;
        }
        if(time.getValue() == null){
            time.setStyle("-fx-border-color: red");
            isFilled = false;
        }
        if(language.getValue() == null)
        {
            language.setStyle("-fx-border-color: red");
            isFilled = false;
        }
        return isFilled;
    }

    /**
     * HashMap containing languages and their lang codes
     * @return
     * @author Johvanni Perez
     */
    public static HashMap<String, String> codeMap(){
        HashMap<String, String> langCodes = new HashMap<String, String>();

        langCodes.put("Arabic", "ar");
        langCodes.put("Dutch", "nl");
        langCodes.put("English", "en");
        langCodes.put("French", "fr");
        langCodes.put("German", "de");
        langCodes.put("Greek", "el");
        langCodes.put("Haitian Creole", "ht");
        langCodes.put("Italian", "it");
        langCodes.put("Japanese", "ja");
        langCodes.put("Korean", "ko");
        langCodes.put("Portuguese", "pt");
        langCodes.put("Russian", "ru");
        langCodes.put("Spanish", "es");
        langCodes.put("Vietnamese", "vi");

        return langCodes;
    }

    /**
     * web scraper that uses javascript to access a translation generator and return translations
     * @param src the original language that needs to be translated
     * @param target the language that the translation will be provided in
     * @param text the string that needs to translated
     * @return a translated string
     * @throws IOException
     * @author Johvanni Perez
     */
    public String translate(String src, String target, String text) throws IOException {


        String urlStr = "https://script.google.com/macros/s/AKfycbzk_1ZP98MqQNuWvs_Yo3UamuN7WCABIG3UiUUighYgCeqIf4ha4qUzubb2jxopuTP7/exec"
                + "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + target +
                "&source=" + src;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputline;
        while((inputline = in.readLine()) != null){
            response.append(inputline);
        }
        in.close();
        return response.toString();
    }

}
