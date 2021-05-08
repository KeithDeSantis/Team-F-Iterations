package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.entities.AccountEntry;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.ComboBoxTreeTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
        username.setOnEditCommit(event -> {
            TreeItem<AccountEntry> selectedAccount = accountView.getTreeItem(event.getTreeTablePosition().getRow());
            try {
                DatabaseAPI.getDatabaseAPI().editUser(selectedAccount.getValue().getUsername(), event.getNewValue(), "username");
            } catch (Exception e) {
                e.printStackTrace();
            }
            selectedAccount.getValue().setUsername(event.getNewValue());
        });

        JFXTreeTableColumn<AccountEntry, String> password = new JFXTreeTableColumn<>("Password");
        password.setPrefWidth(colWidth);
        password.setCellValueFactory(cellData -> cellData.getValue().getValue().getPasswordProperty());

        password.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        password.setOnEditCommit(event -> {
            TreeItem<AccountEntry> selectedAccount = accountView.getTreeItem(event.getTreeTablePosition().getRow());
            try {
                String newPass = DatabaseAPI.getDatabaseAPI().getEncryptedPass(event.getNewValue(), selectedAccount.getValue().getSalt());
                DatabaseAPI.getDatabaseAPI().editUser(selectedAccount.getValue().getUsername(), newPass, "password");
            } catch (Exception e) {
                e.printStackTrace();
            }
            selectedAccount.getValue().setUsername(event.getNewValue());
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
        userType.setOnEditCommit(event -> {
            TreeItem<AccountEntry> selectedAccount = accountView.getTreeItem(event.getTreeTablePosition().getRow());
            try {
                DatabaseAPI.getDatabaseAPI().editUser(selectedAccount.getValue().getUsername(), event.getNewValue(), "type");
            } catch (Exception e) {
                e.printStackTrace();
            }
            selectedAccount.getValue().setUserType(event.getNewValue());
        });

        final TreeItem<AccountEntry> root = new RecursiveTreeItem<>(accounts, RecursiveTreeObject::getChildren);
        accountView.setRoot(root);
        accountView.setShowRoot(false);
        accountView.getColumns().setAll(username, password, userType);

        List<AccountEntry> data;
        try {
            //FIXME: make accounts instead of services
            data = DatabaseAPI.getDatabaseAPI().genAccountEntries();
            accounts.addAll(data);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        accountView.setEditable(true);
    }

    public void handleButtonPushed(ActionEvent actionEvent) throws SQLException, IOException {
        JFXButton buttonPushed = (JFXButton) actionEvent.getSource();
        if (buttonPushed == quit){
            SceneContext.getSceneContext().loadDefault();
        }
        else if (buttonPushed == deleteUser && accountView.getSelectionModel().getSelectedIndex() >= 0){
            AccountEntry user = accountView.getSelectionModel().getSelectedItem().getValue();
            DatabaseAPI.getDatabaseAPI().deleteUser(user.getUsername());
            accounts.remove(user);
        }
        else if (buttonPushed == addUser){
            AccountEntry newAccount = new AccountEntry("","","","", null);

            openNewDialog(newAccount);

            if(!(newAccount.getUsername().isEmpty() || newAccount.getPassword().isEmpty() || newAccount.getUserType().isEmpty() || newAccount.getCovidStatus().isEmpty())) {
                DatabaseAPI.getDatabaseAPI().addUser(newAccount.getUsername(), newAccount.getUserType(), newAccount.getUsername(), newAccount.getPassword(), newAccount.getCovidStatus());
                accounts.add(newAccount);
                SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AccountManagerView.fxml");
            }
        }
        else if (buttonPushed == home){
            SceneContext.getSceneContext().loadDefault();
        }
    }

    public void openNewDialog(AccountEntry newAccount) throws IOException {
        FXMLLoader dialogLoader = new FXMLLoader();
        dialogLoader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/AccountManagerNewUserDialog.fxml")); // load in Edit Dialog - KD
        Stage dialogStage = new Stage();
        Parent root = dialogLoader.load();
        AccountManagerNewUserDialogController dialogController = dialogLoader.getController();
        dialogStage.initModality(Modality.WINDOW_MODAL); // make window a pop up - KD
        dialogStage.initOwner(addUser.getScene().getWindow());
        dialogStage.setScene(new Scene(root)); // set scene - KD
        dialogController.setAccounts(accounts);
        dialogController.setNewAccount(newAccount);
        dialogStage.showAndWait(); // open pop up - KD
    }

    public void handleHome() throws IOException {
        SceneContext.getSceneContext().loadDefault();
    }

}
