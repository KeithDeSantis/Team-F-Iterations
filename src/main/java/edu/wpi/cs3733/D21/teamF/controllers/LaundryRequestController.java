package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;


public class LaundryRequestController extends ServiceRequests {

    @FXML private JFXButton help;
    @FXML private JFXRadioButton darks;
    @FXML private JFXRadioButton lights;
    @FXML private JFXRadioButton both;
    @FXML private JFXRadioButton hot;
    @FXML private JFXRadioButton cold;
    @FXML private JFXRadioButton folded;

    @FXML public TextField employeeID;
    @FXML public TextField clientName;
    @FXML public TextField additionalInstructions;

    public TextField getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(TextField employeeID) {
        this.employeeID = employeeID;
    }

    public TextField getClientName() {
        return clientName;
    }

    public void setClientName(TextField clientName) {
        this.clientName = clientName;
    }

    public TextField getAdditionalInstructions() {
        return additionalInstructions;
    }

    public void setAdditionalInstructions(TextField additionalInstructions) {
        this.additionalInstructions = additionalInstructions;
    }

    @FXML
    public void handleSubmit(ActionEvent e) throws IOException, SQLException {
        if(formFilled()) {
            // Loads form submitted window and passes in current stage to return to request home
            String uuid = UUID.randomUUID().toString();
            String type = "Laundry Request";
            String person = "";
            String completed = "false";
            DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, type, person, completed, additionalInformation());

            openSuccessWindow();
        }
    }

    private String additionalInformation(){
        ArrayList<JFXRadioButton> rButtons = new ArrayList<>();
        rButtons.add(darks);
        rButtons.add(lights);
        rButtons.add(both);
        rButtons.add(hot);
        rButtons.add(cold);
        rButtons.add(folded);
        String additionalInfo = "Laundry Instructions: ";

        for(JFXRadioButton r: rButtons){
            if(r.isSelected()){
                additionalInfo = additionalInfo + ", " + r.getText();
            }
        }

        return additionalInfo;
    }

    /**
     * handles radial button groups
     * @param e
     */
    @FXML
    private void handleRadialButtonPushed(ActionEvent e){
        ToggleGroup tempGroup = new ToggleGroup();
        hot.setToggleGroup(tempGroup);
        cold.setToggleGroup(tempGroup);

        ToggleGroup colorGroup = new ToggleGroup();
        darks.setToggleGroup(colorGroup);
        lights.setToggleGroup(colorGroup);
        both.setToggleGroup(colorGroup);
    }


    @Override
    public boolean formFilled() {
        boolean isFilled = true;

        setNormalStyle(employeeID, clientName, hot, cold, darks, lights, both);

        if(employeeID.getText().length() == 0){
            isFilled = false;
            setTextErrorStyle(employeeID);
        }
        if(clientName.getText().length() == 0){
            isFilled = false;
            setTextErrorStyle(clientName);
        }
        if(! (hot.isSelected() || cold.isSelected())){
            isFilled = false;
            setButtonErrorStyle(hot, cold);
        }
        if (! (darks.isSelected() || lights.isSelected() || both.isSelected())) {
            isFilled = false;
            setButtonErrorStyle(darks, lights, both);
        }

        return isFilled;
    }

    @Override
    public void handleClear() {
        both.setSelected(false);
        lights.setSelected(false);
        darks.setSelected(false);
        hot.setSelected(false);
        cold.setSelected(false);
        folded.setSelected(false);
        employeeID.setText("");
        clientName.setText("");
        additionalInstructions.setText("");
        setNormalStyle(both, lights, darks, hot, cold, folded, employeeID, clientName, additionalInstructions);
    }
}
