package edu.wpi.fuchsiafalcons.controllers;

import com.jfoenix.controls.*;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;


public class LanguageInterpretationRequestController implements Initializable {
    @FXML private JFXButton close;
    @FXML private JFXTextField name;
    @FXML private JFXDatePicker date;
    @FXML private JFXTimePicker time;
    @FXML private JFXComboBox<String> appointment;
    @FXML private JFXTextField language;
    @FXML private JFXButton help;
    @FXML private JFXButton translate;
    @FXML private JFXButton submit;

    /**
     * closes the Language Interpretation Request form and returns to home
     * @param actionEvent
     * @throws IOException
     * @author Jay
     */
    public void handleClose(ActionEvent actionEvent) throws IOException {
        Stage currentStage = (Stage)close.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/ServiceRequestHomeView.fxml"));
        Scene homeScene = new Scene(root);
        currentStage.setScene(homeScene);
        currentStage.show();
    }

    /**
     * Opens the help window
     * @param actionEvent
     * @throws IOException
     * @author Jay Yen
     */
    public void handleHelp(ActionEvent actionEvent) throws IOException {
        Stage currentStage = (Stage)help.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/LanguageInterpretationHelpView.fxml"));
        Scene homeScene = new Scene(root);
        currentStage.setScene(homeScene);
        currentStage.show();
    }

    public void handleTranslate(ActionEvent actionEvent) throws IOException{
    }

    /**
     * Opens a window that shows a request received message
     * @param actionEvent
     * @throws IOException
     * @author Jay Yen
     */
    public void handleSubmit(ActionEvent actionEvent) throws IOException{
        Stage currentStage = (Stage)submit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/LanguageInterpretationSubmitView.fxml"));
        Scene homeScene = new Scene(root);
        currentStage.setScene(homeScene);
        currentStage.show();
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
    }

    private boolean formFilledOut(){
        //ObjectProperty<LocalTime> lt = time.valueProperty();
        //ObjectProperty<LocalDate> ld = date.valueProperty();
        boolean fullName = name.getText().length() > 0;
        boolean properDate = date.getValue() != null;
        boolean properTime = time.getValue() != null;
        boolean lang = language.getText().length() > 0;
        return fullName && properDate && properTime && lang;
    }
}
