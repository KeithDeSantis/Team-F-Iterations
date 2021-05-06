package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.AccountEntry;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;

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

    //private String fieldChanged = "";
    @FXML
    private JFXTreeTableView<AccountEntry> accountView;
    private final ObservableList<AccountEntry> accounts = FXCollections.observableArrayList();

    public void initialize(URL location, ResourceBundle resources) {

        int colWidth = 430;
        JFXTreeTableColumn<AccountEntry, String> username = new JFXTreeTableColumn<>("Username");
        username.setPrefWidth(colWidth);
        username.setCellValueFactory(cellData -> cellData.getValue().getValue().getUsernameProperty());

//        JFXTreeTableColumn<AccountEntry, String> password = new JFXTreeTableColumn<>("Password");
//        password.setPrefWidth(colWidth);
//        password.setCellValueFactory(cellData -> cellData.getValue().getValue().getPasswordProperty());

        JFXTreeTableColumn<AccountEntry, String> userType = new JFXTreeTableColumn<>("User Type");
        userType.setPrefWidth(colWidth);
        userType.setCellValueFactory(cellData -> cellData.getValue().getValue().getUserTypeProperty());

        final TreeItem<AccountEntry> root = new RecursiveTreeItem<>(accounts, RecursiveTreeObject::getChildren);
        accountView.setRoot(root);
        accountView.setShowRoot(false);
        accountView.getColumns().setAll(username, userType);

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



        //add table entries like in account manager
        //syntax of adding item: services.add(new ServiceEntry("Request Type", "Assigned To", "Status));

//        List<String> allUsers;
//        try {
//            UserHandler userHandler = new UserHandler();
//            allUsers = userHandler.listAllUsers();
//            for (String s : allUsers)
//            {
//                selectUser.getItems().add(s);
//            }
//        }
//        catch (SQLException e)
//        {
//            e.printStackTrace();
//        }

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
        else if (buttonPushed == deleteUser && accountView.getSelectionModel().getSelectedIndex() >= 0){
            AccountEntry user = accountView.getSelectionModel().getSelectedItem().getValue();
            DatabaseAPI.getDatabaseAPI().deleteUser(user.getUsername());
            refreshPage();
        }
        else if (buttonPushed == addUser && !addUsername.getText().isEmpty() && !addPassword.getText().isEmpty() && !(newUserType.getValue()==null)){

            String userName = addUsername.getText();
            String pass = addPassword.getText();
            String type = newUserType.getValue();

            DatabaseAPI.getDatabaseAPI().addUser(userName, type, userName, pass, "true");
            AccountEntry newUser = new AccountEntry(userName, pass, type, "true");
            accounts.add(newUser);
            refreshPage();
        }
        else if (buttonPushed == saveChanges && accountView.getSelectionModel().getSelectedIndex() >= 0){

            String  targetUser = accountView.getSelectionModel().getSelectedItem().getValue().getUsername();
            String newVal;

            if(!username.getText().isEmpty()) {
                newVal = username.getText();
                DatabaseAPI.getDatabaseAPI().editUser(targetUser, newVal, "username");
                accountView.getSelectionModel().getSelectedItem().getValue().setUsername(newVal);
            }
            if(!password.getText().isEmpty()) {
                newVal = password.getText();
                DatabaseAPI.getDatabaseAPI().editUser(targetUser, newVal, "password");
                accountView.getSelectionModel().getSelectedItem().getValue().setPassword(newVal);
            }
            if(!(changeUserType.getValue() == null)) {
                newVal = changeUserType.getValue();
                DatabaseAPI.getDatabaseAPI().editUser(targetUser, newVal, "type");
                accountView.getSelectionModel().getSelectedItem().getValue().setUserType(newVal);
            }

            /*
            switch (fieldChanged) {
                case "username":
                    newVal = username.getText();
                    DatabaseAPI.getDatabaseAPI().editUser(targetUser, newVal, "username");
                    accountView.getSelectionModel().getSelectedItem().getValue().setUsername(newVal);
                    break;
                case "password":
                    newVal = password.getText();
                    DatabaseAPI.getDatabaseAPI().editUser(targetUser, newVal, "password");
                    accountView.getSelectionModel().getSelectedItem().getValue().setPassword(newVal);
                    break;
                case "type":
                    newVal = changeUserType.getValue();
                    DatabaseAPI.getDatabaseAPI().editUser(targetUser, newVal, "type");
                    accountView.getSelectionModel().getSelectedItem().getValue().setUserType(newVal);
                    break;
            }
            fieldChanged = "";
             */
            refreshPage();
        }
        else if (buttonPushed == home){
            SceneContext.getSceneContext().loadDefault();
        }
    }

    private void refreshPage() throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AccountManagerView.fxml");
    }


    public void changingUsername() {
        //fieldChanged = "username";
    }

    public void changingPassword() {
        //fieldChanged = "password";
    }

    public void changingUserType() {
        //fieldChanged = "type";
    }

}
