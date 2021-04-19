package edu.wpi.fuchsiafalcons.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.fuchsiafalcons.database.DatabaseAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

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

    public void initialize(URL location, ResourceBundle resources) throws SQLException {
        ArrayList<String> allUsers = DatabaseAPI.getDatabaseAPI().listAllUsers();
        for (String s : allUsers)
        {
            name.getItems().add(s);
        }
    }

    public void handleUserSearch(ActionEvent actionEvent) {
    }

    public void handleQuit(ActionEvent actionEvent) {
    }

    public void handleDeleteUser(ActionEvent actionEvent) throws SQLException {
        String target = username.getText();
        DatabaseAPI.getDatabaseAPI().deleteUser(target);
    }

    public void handleAddUser(ActionEvent actionEvent) throws SQLException{
        String user = addUsername.getText();
        String pass = addPassword.getText();
        String type = addName.getText();

        DatabaseAPI.getDatabaseAPI().addUser(type, user, pass);
    }

    public void handleSaveChanges(ActionEvent actionEvent) {
        String user = username.getText();
        String type = (String) existingUserType.getValue();
        String pass = password.getText();
    }

    public void handleAdminHome(ActionEvent actionEvent) {
    }
}
