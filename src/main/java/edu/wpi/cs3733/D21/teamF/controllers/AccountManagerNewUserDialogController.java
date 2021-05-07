package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamF.entities.AccountEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class AccountManagerNewUserDialogController {

    @FXML
    private JFXButton okButton;
    @FXML
    private JFXTextField userNameField;
    @FXML
    private JFXTextField passwordField;
    @FXML
    private JFXComboBox<String> userTypeComboBox;

    private AccountEntry newAccount = new AccountEntry("","","","");

    private ObservableList<AccountEntry> accounts = FXCollections.observableArrayList();

    public void initialize() {
        ObservableList<String> typeList = FXCollections.observableArrayList();
        typeList.add("administrator");
        typeList.add("employee");
        typeList.add("visitor");
        userTypeComboBox.setItems(typeList);
    }

    public void handleOk() {
        if(isFilledOut()) {
            newAccount.setUsername(userNameField.getText());
            newAccount.setPassword(passwordField.getText());
            newAccount.setUserType(userTypeComboBox.getValue());
            newAccount.setCovidStatus("true");
            ((Stage) userNameField.getScene().getWindow()).close();
        }
    }

    private boolean isFilledOut() {
        userNameField.setStyle("-fx-background-color: transparent"); // set all text field backgrounds to clear to reset any fields that were marked as incomplete - KD
        passwordField.setStyle("-fx-background-color: transparent");
        userTypeComboBox.setStyle("-fx-border-color: transparent");
        boolean isFilled = true;
        if(userNameField.getText().isEmpty() || isDuplicateUsername(userNameField.getText())) {
            userNameField.setStyle("-fx-background-color:  #ff000088");
            isFilled = false;
        }
        if(passwordField.getText().isEmpty()) {
            passwordField.setStyle("-fx-background-color:  #ff000088");
            isFilled = false;
        }
        if(userTypeComboBox.getValue().isEmpty()) {
            userTypeComboBox.setStyle("-fx-border-color:  #ff000088");
            userTypeComboBox.setStyle("-fx-border-width:  2px");
            isFilled = false;
        }
        return isFilled;
    }

    private boolean isDuplicateUsername(String username) {
        for(AccountEntry accountEntry : accounts) {
            if(accountEntry.getUsername().equals(username)) return true;
        }
        return false;
    }


    public void setAccounts(ObservableList<AccountEntry> accounts) {
        this.accounts = accounts;
    }

    public void setNewAccount(AccountEntry newAccount) {
        this.newAccount = newAccount;
    }
}
