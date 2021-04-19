package edu.wpi.fuchsiafalcons.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.fuchsiafalcons.database.DatabaseAPI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
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
    private JFXComboBox selectUser;
    @FXML
    private JFXComboBox changeUserType;
    @FXML
    private JFXComboBox newUserType;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXTextField password;
    @FXML
    private JFXTextField addPassword;
    @FXML
    private JFXTextField addUsername;

    private String fieldChanged = "";

    public void initialize(URL location, ResourceBundle resources) throws SQLException {
        ArrayList<String> allUsers = DatabaseAPI.getDatabaseAPI().listAllUsers();
        for (String s : allUsers)
        {
            selectUser.getItems().add(s);
        }
        changeUserType.getItems().add("guest");
        changeUserType.getItems().add("employee");
        changeUserType.getItems().add("admin");

        newUserType.getItems().add("guest");
        newUserType.getItems().add("employee");
        newUserType.getItems().add("admin");
    }

    public void handleUserSearch(ActionEvent actionEvent) {
    }

    public void handleQuit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void handleDeleteUser(ActionEvent actionEvent) throws SQLException {
        String target = username.getText();
        DatabaseAPI.getDatabaseAPI().deleteUser(target);
    }

    public void handleAddUser(ActionEvent actionEvent) throws SQLException{
        String user = addUsername.getText();
        String pass = addPassword.getText();
        String type = (String) newUserType.getValue();

        DatabaseAPI.getDatabaseAPI().addUser(type, user, pass);
    }

    public void handleSaveChanges(ActionEvent actionEvent) throws SQLException{
        String targetUser = "";
        String newVal = "";
        if (fieldChanged.equals("username")){
            targetUser = (String) selectUser.getValue();
            newVal = username.getText();
            DatabaseAPI.getDatabaseAPI().editUser(targetUser, "username", newVal);
        }
        else if (fieldChanged.equals("password")){
            targetUser = (String) selectUser.getValue();
            newVal = password.getText();
            DatabaseAPI.getDatabaseAPI().editUser(targetUser, "password", newVal);
        }
        else if (fieldChanged.equals("type")){
            targetUser = (String) selectUser.getValue();
            newVal = (String) changeUserType.getValue();
            DatabaseAPI.getDatabaseAPI().editUser(targetUser, "type", newVal);
        }
        fieldChanged = "";
    }

    public void handleAdminHome(ActionEvent actionEvent) throws IOException {
        Button buttonPushed = (Button) actionEvent.getSource();  //Getting current stage
        Stage stage;
        Parent root;
        stage = (Stage) buttonPushed.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/DefaultPageAdminController.fxml"));
        stage.getScene().setRoot(root);
        stage.setTitle("Admin Home");
        stage.show();
    }

    public void changingUsername(MouseEvent mouseEvent) throws SQLException{
        fieldChanged = "username";
    }

    public void changingPassword(MouseEvent mouseEvent) throws SQLException{
        fieldChanged = "password";
    }

    public void changingUserType(MouseEvent mouseEvent) {
        fieldChanged = "type";
    }
}
