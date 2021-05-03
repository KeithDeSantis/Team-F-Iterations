package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.database.UserHandler;
import edu.wpi.cs3733.D21.teamF.entities.AccountEntry;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AccountManagerController implements Initializable {
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
    private JFXComboBox<String> selectUser;
    @FXML
    private JFXComboBox<String> changeUserType;
    @FXML
    private JFXComboBox<String> newUserType;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXTextField password;
    @FXML
    private JFXTextField addPassword;
    @FXML
    private JFXTextField addUsername;

    private String fieldChanged = "";
    @FXML
    private JFXTreeTableView<AccountEntry> accountView;
    private final ObservableList<AccountEntry> accounts = FXCollections.observableArrayList();

    public void initialize(URL location, ResourceBundle resources) {
        /*
        int colWidth = 300;
        JFXTreeTableColumn<AccountEntry, String> username = new JFXTreeTableColumn<>("Username");
        username.setPrefWidth(colWidth);
        username.setCellValueFactory(cellData -> cellData.getValue().getValue().getUsernameProperty());

        JFXTreeTableColumn<AccountEntry, String> password = new JFXTreeTableColumn<>("Password");
        password.setPrefWidth(colWidth);
        password.setCellValueFactory(cellData -> cellData.getValue().getValue().getPasswordProperty());

        JFXTreeTableColumn<AccountEntry, String> userType = new JFXTreeTableColumn<>("User Type");
        userType.setPrefWidth(colWidth);
        userType.setCellValueFactory(cellData -> cellData.getValue().getValue().getUserTypeProperty());

        final TreeItem<AccountEntry> root = new RecursiveTreeItem<AccountEntry>(accounts, RecursiveTreeObject::getChildren);
        accountView.setRoot(root);
        accountView.setShowRoot(false);
        accountView.getColumns().setAll(username, password, userType);

        List<AccountEntry> data;
        try {
            //FIXME: make accounts instead of services
            data = DatabaseAPI.getDatabaseAPI().genAccountEntries();
            for (AccountEntry e : data) {
                accounts.add(e);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

         */

        //add table entries like in account manager
        //syntax of adding item: services.add(new ServiceEntry("Request Type", "Assigned To", "Status));

        List<String> allUsers;
        try {
            UserHandler userHandler = new UserHandler();
            allUsers = userHandler.listAllUsers();
            for (String s : allUsers)
            {
                selectUser.getItems().add(s);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
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

    public void handleButtonPushed(ActionEvent actionEvent) throws Exception {
        JFXButton buttonPushed = (JFXButton) actionEvent.getSource();
        if (buttonPushed == quit){
            SceneContext.getSceneContext().loadDefault();
        }
        else if (buttonPushed == deleteUser){
            AccountEntry user = accountView.getSelectionModel().getSelectedItem().getValue();
            DatabaseAPI.getDatabaseAPI().deleteUser(user.getUsername());
            refreshPage();
        }
        else if (buttonPushed == addUser){
            //AccountEntry user = accountView.getSelectionModel().getSelectedItem().getValue();
            //AccountEntry newUser = new AccountEntry(user.getUsername(), user.getPassword(), user.getUserType());
            String userName = addUsername.getText();
            String pass = addPassword.getText();
            String type = newUserType.getValue();
//FIXME add user/account objects
            DatabaseAPI.getDatabaseAPI().addUser(userName, type, userName, pass);
            selectUser.getItems().add(userName);
            refreshPage();
        }
        else if (buttonPushed == saveChanges){
            String targetUser;
            String newVal;
            switch (fieldChanged) {
                case "username":
                    targetUser = selectUser.getValue();
                    newVal = username.getText();
                    DatabaseAPI.getDatabaseAPI().editUser(targetUser, newVal, "username");
                    break;
                case "password":
                    targetUser = selectUser.getValue();
                    newVal = password.getText();
                    DatabaseAPI.getDatabaseAPI().editUser(targetUser, newVal, "password");
                    break;
                case "type":
                    targetUser = selectUser.getValue();
                    newVal = changeUserType.getValue();
                    DatabaseAPI.getDatabaseAPI().editUser(targetUser, newVal, "type");
                    break;
            }
            fieldChanged = "";

            refreshPage(actionEvent);
        }
        else if (buttonPushed == home){
            SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageAdminView.fxml");
        }
    }

    private void refreshPage(ActionEvent actionEvent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AccountManagerView.fxml");
    }

    public void changingUsername(MouseEvent mouseEvent) throws SQLException{
        fieldChanged = "username";
    }

    public void changingPassword(MouseEvent mouseEvent) throws SQLException{
        fieldChanged = "password";
    }

    public void changingUserType(MouseEvent mouseEvent) throws SQLException{
        fieldChanged = "type";
    }
}
