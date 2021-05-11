package edu.wpi.cs3733.D21.teamF.controllers.ServiceRequestsControllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733.D21.teamF.Translation.Translator;
import edu.wpi.cs3733.D21.teamF.controllers.ServiceRequests;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.UUID;


public class LanguageInterpretationServiceRequestController extends ServiceRequests implements Initializable {
    @FXML private JFXTextField name;
    @FXML private JFXDatePicker date;
    @FXML private JFXTimePicker time;
    @FXML private JFXComboBox<String> appointment;
    @FXML private JFXComboBox<String> language;



    public LanguageInterpretationServiceRequestController() {
    }

    /**
     * Opens the help window
     * @param actionEvent
     * @throws IOException
     * @author Jay Yen
     */
    public void handleHelp(ActionEvent actionEvent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/LanguageInterpretationHelpView.fxml");
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
                    " Name: " + name.getText() + " Appointment: " + appointment.getValue() + " Language: " + language.getValue();
            ServiceEntry newServiceRequest = new ServiceEntry(uuid,"Language Interpretation Request", " ", "false", additionalInstr);
            DatabaseAPI.getDatabaseAPI().addServiceReq(newServiceRequest.getUuid(), newServiceRequest.getRequestType(),
                    newServiceRequest.getAssignedTo(), newServiceRequest.getCompleteStatus(), newServiceRequest.getAdditionalInstructions());
            // Loads form submitted window and passes in current stage to return to request home
            openSuccessWindow();
        }

    }

    @Override
    public void handleClear(){
        name.setText("");
        date.setValue(null);
        time.setValue(null);
        appointment.setValue(null);
        language.setValue(null);
        setNormalStyle(name, date, time, appointment, language);
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
        appointment.getItems().add("Lung Cancer Screening (Low Dose CT)");
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

        language.getItems().addAll(Translator.getTranslator().getLanguages());
        //
    }

    public boolean formFilled(){
        boolean isFilled = true;
        if(name.getText().trim().isEmpty()){
            setTextErrorStyle(name);
            isFilled = false;
        }
        if(date.getValue() == null){
            setTextErrorStyle(date);
            isFilled = false;
        }
        if(time.getValue() == null){
            setTextErrorStyle(time);
            isFilled = false;
        }
        if(language.getValue() == null)
        {
            setTextErrorStyle(language);
            isFilled = false;
        }
        if(appointment.getValue() == null)
        {
            setTextErrorStyle(appointment);
            isFilled = false;
        }
        return isFilled;
    }

}

