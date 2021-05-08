package edu.wpi.cs3733.D21.teamF.controllers.ServiceRequestsControllers;


import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamF.Translation.Translator;
import edu.wpi.cs3733.D21.teamF.controllers.ServiceRequests;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class InternalTransportationController extends ServiceRequests {

    @FXML private JFXButton clear;

    @FXML private JFXComboBox<String> deliverLocation;

    @FXML private JFXDatePicker movingDate;

    @FXML private JFXTextField patientName;

    @FXML private JFXComboBox<String> patientRoom;

    @FXML private JFXCheckBox relativesCheckBox;

    @FXML private JFXCheckBox doctorCheckBox;

    @FXML private JFXButton cancel;

    @FXML private JFXButton submitButton;

    @FXML private Label patientNameLbl;

    @FXML private Label locLbl;

    @FXML private Label roomLbl;

    @FXML private Label dateLbl;

    @FXML
    public void initialize(){

        patientName.textProperty().bind(Translator.getTranslator().getTranslationBinding(patientName.getPromptText()));
        patientNameLbl.textProperty().bind(Translator.getTranslator().getTranslationBinding(patientNameLbl.getText()));
        locLbl.textProperty().bind(Translator.getTranslator().getTranslationBinding(locLbl.getText()));
        roomLbl.textProperty().bind(Translator.getTranslator().getTranslationBinding(roomLbl.getText()));
        dateLbl.textProperty().bind(Translator.getTranslator().getTranslationBinding(dateLbl.getText()));
        relativesCheckBox.textProperty().bind(Translator.getTranslator().getTranslationBinding(relativesCheckBox.getText()));
        doctorCheckBox.textProperty().bind(Translator.getTranslator().getTranslationBinding(doctorCheckBox.getText()));

        cancel.textProperty().bind(Translator.getTranslator().getTranslationBinding(cancel.getText()));
        clear.textProperty().bind(Translator.getTranslator().getTranslationBinding(clear.getText()));
        submitButton.textProperty().bind(Translator.getTranslator().getTranslationBinding(submitButton.getText()));
        //cancel.setDisableVisualFocus(true); // Clears visual focus from cancel button, cause unknown - LM

        try{
            List<NodeEntry> nodeEntries = DatabaseAPI.getDatabaseAPI().genNodeEntries();

            final ObservableList<String> nodeList = FXCollections.observableArrayList();
            for(NodeEntry n: nodeEntries){
                nodeList.add(n.getShortName());
            }
            this.patientRoom.setItems(nodeList);
            this.deliverLocation.setItems(nodeList);

        } catch(Exception e){ }
    }

    public boolean formFilled() {

        boolean isFilled = true;
        setNormalStyle(deliverLocation, movingDate, patientName, patientRoom);

        if(deliverLocation.getValue() == null) {
            isFilled = false;
            setTextErrorStyle(deliverLocation);
        }
        if(movingDate.getValue() == null) {
            isFilled = false;
            setTextErrorStyle(movingDate);
        }
//        if(movingTime.getValue() == null) {
//            isFilled = false;
//            setTextErrorStyle(movingTime);
//        }
        if(patientName.getText().length() <= 0) {
            isFilled = false;
            setTextErrorStyle(patientName);
        }
        if(patientRoom.getValue() == null) {
            isFilled = false;
            setTextErrorStyle(patientRoom);
        }

        return isFilled;
    }

    public void handleSubmit(ActionEvent e) throws IOException, SQLException {
        if(formFilled()) // form is complete
        {
            String uuid = UUID.randomUUID().toString();
            String type = "Internal Transport";
            String person = "";
            String completed = "false";
            String additionalInfo = "Delivery Location: " + deliverLocation.getValue() + "Delivery Date: " + movingDate.getValue()
                    + "Patient Room: " + patientRoom.getValue();

            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, person, completed, additionalInfo);

            // Loads form submitted window and passes in current stage to return to request home
            openSuccessWindow();
        }
    }


    public void handleClear() {
        deliverLocation.setValue(null);
        movingDate.setValue(null);
        patientName.setText("");
        patientRoom.setValue(null);
        relativesCheckBox.setSelected(false);
        doctorCheckBox.setSelected(false);
        setNormalStyle(deliverLocation, movingDate, patientName, patientRoom, relativesCheckBox, doctorCheckBox);
    }

    public void handleHelp(ActionEvent actionEvent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/InternalTransportationHelpView.fxml");
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/InternalTransportationView.fxml");
    }

}
