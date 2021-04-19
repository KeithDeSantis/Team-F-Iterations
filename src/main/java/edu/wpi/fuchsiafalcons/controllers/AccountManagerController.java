package edu.wpi.fuchsiafalcons.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AccountManagerController {
    @FXML
    private JFXButton quit;
    @FXML
    private JFXButton deleteUser;
    @FXML
    private JFXButton addUser;
    @FXML
    private JFXButton saveChanges;
    @FXML
    private JFXButton home;
    @FXML
    private JFXComboBox name;
    @FXML
    private JFXComboBox existingUserType;
    @FXML
    private JFXComboBox newUserType;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXTextField password;
    @FXML
    private JFXTextField addName;
    @FXML
    private JFXTextField addPassword;
    @FXML
    private JFXTextField addUsername;

    public void handleUserSearch(ActionEvent actionEvent) {
    }

    public void handleQuit(ActionEvent actionEvent) {
    }

    public void handleDeleteUser(ActionEvent actionEvent) {
    }

    public void handleAddUser(ActionEvent actionEvent) {
    }

    public void handleSaveChanges(ActionEvent actionEvent) {
    }

    public void handleAdminHome(ActionEvent actionEvent) {
    }
}
