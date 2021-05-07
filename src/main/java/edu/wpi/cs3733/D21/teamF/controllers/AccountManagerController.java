package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.database.UserHandler;
import edu.wpi.cs3733.D21.teamF.entities.AccountEntry;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.ComboBoxTreeTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AccountManagerController extends AbsController implements Initializable {
    @FXML
    private JFXButton quit;
    @FXML
    private JFXButton deleteUser;
    @FXML
    private JFXButton addUser;
    @FXML
    private JFXButton home;

    @FXML
    private JFXTreeTableView<AccountEntry> accountView;
    private final ObservableList<AccountEntry> accounts = FXCollections.observableArrayList();

    public void initialize(URL location, ResourceBundle resources) {

        int colWidth = 286;
        JFXTreeTableColumn<AccountEntry, String> username = new JFXTreeTableColumn<>("Username");
        username.setPrefWidth(colWidth);
        username.setCellValueFactory(cellData -> cellData.getValue().getValue().getUsernameProperty());

        username.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        username.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<AccountEntry, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<AccountEntry, String> event) {
                TreeItem<AccountEntry> selectedAccount = accountView.getTreeItem(event.getTreeTablePosition().getRow());
                try {
                    DatabaseAPI.getDatabaseAPI().editUser(selectedAccount.getValue().getUsername(), event.getNewValue(), "username");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                selectedAccount.getValue().setUsername(event.getNewValue());
            }
        });

        JFXTreeTableColumn<AccountEntry, String> password = new JFXTreeTableColumn<>("Password");
        password.setPrefWidth(colWidth);
        password.setCellValueFactory(cellData -> cellData.getValue().getValue().getPasswordProperty());

        password.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        password.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<AccountEntry, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<AccountEntry, String> event) {
                TreeItem<AccountEntry> selectedAccount = accountView.getTreeItem(event.getTreeTablePosition().getRow());
                try {
                    DatabaseAPI.getDatabaseAPI().editUser(selectedAccount.getValue().getUsername(), "", "password");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                selectedAccount.getValue().setUsername(event.getNewValue());
            }
        });

        ObservableList<String> typeList = FXCollections.observableArrayList();
        typeList.add("administrator");
        typeList.add("employee");
        typeList.add("visitor");
        JFXTreeTableColumn<AccountEntry, String> userType = new JFXTreeTableColumn<>("User Type");
        userType.setPrefWidth(colWidth);
        userType.setCellValueFactory(cellData -> cellData.getValue().getValue().getUserTypeProperty());


        userType.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(typeList));
        //userType.setCellValueFactory(cellData -> cellData.getValue().getValue().getUserTypeProperty());
        userType.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<AccountEntry, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<AccountEntry, String> event) {
                TreeItem<AccountEntry> selectedAccount = accountView.getTreeItem(event.getTreeTablePosition().getRow());
                try {
                    DatabaseAPI.getDatabaseAPI().editUser(selectedAccount.getValue().getUsername(), event.getNewValue(), "type");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                selectedAccount.getValue().setUserType(event.getNewValue());
            }
        });

        final TreeItem<AccountEntry> root = new RecursiveTreeItem<>(accounts, RecursiveTreeObject::getChildren);
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

        accountView.setEditable(true);
    }

    public void handleButtonPushed(ActionEvent actionEvent) throws Exception {
        JFXButton buttonPushed = (JFXButton) actionEvent.getSource();
        if (buttonPushed == quit){
            SceneContext.getSceneContext().loadDefault();
        }
        else if (buttonPushed == deleteUser && accountView.getSelectionModel().getSelectedIndex() >= 0){
            AccountEntry user = accountView.getSelectionModel().getSelectedItem().getValue();
            DatabaseAPI.getDatabaseAPI().deleteUser(user.getUsername());
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
        else if (buttonPushed == home){
            SceneContext.getSceneContext().loadDefault();
        }
    }

    private void refreshPage() throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AccountManagerView.fxml");
    }

}
