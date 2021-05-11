package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.Translation.Translator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AccountManagerHelpController extends AbsController {

    @FXML
    private Label helpTextLabel;

    public void handleOk() {
        ((Stage) helpTextLabel.getScene().getWindow()).close();
    }

    public void handleUsername() {
        showText("Each account has a unique username, for security and management purposes we ask that every account uses a different username.  Try making it something easy to remember!");
    }

    public void handlePasswords() {
        showText("Passwords are stored securely in our database.  The text you see in the table on the account manager shows its encryption.  For your security we don't store your password in plain text (so even we couldn't get into your account if we tried)!  Try to make your password more secure by making it at least 8 characters long and including a mix of letters, numbers, and symbols.");
    }

    public void handleUserType() {
        showText("There are three user types: Administrator, Employee, and Visitor.  Visitors are for non-employed personnel and they have access to the navigation page, service request page, and the COVID-19 vaccine info page.  Employees have access to just as much as visitors, as well as the map editor and service manager.  Administrator have access to all the above as well as the account manager and the pathfinding settings.");
    }

    public void handleDefaultUsers() {
        showText("There are three \"default\" users, admin, staff, and guest.  While they appear in the table, any edits you make to them will have no effect (other than deletion).  These three accounts are meant to ensure the user never gets totally locked out of the account manager.");
    }

    public void handleEmail() {
        showText("Enter the email associated with the new account.");
    }

    /**
     * Used to easily update the translated text
     * @param text The text to translate
     */
    private void showText(String text)
    {
        helpTextLabel.textProperty().unbind();
        helpTextLabel.textProperty().bind(Translator.getTranslator().getTranslationBinding(text));
    }


}
