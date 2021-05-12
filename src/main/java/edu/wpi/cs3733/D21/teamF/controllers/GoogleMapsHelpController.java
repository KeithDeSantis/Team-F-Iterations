package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.Translation.Translator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GoogleMapsHelpController extends AbsController {
    @FXML private Label helpTextLabel;
    /**
     * Handles the about functionality to display information
     */
    public void handleAbout() {
        showText("This page uses the google directions API to direct you to the closest guest hospital" +
                " parking lot. It will tell you step by step directions to get to the lot, as well as your ETA.");
    }

    /**
     * Handles the how to use this page
     **/
    public void handleHowTo() {
        showText("To use this page, simply enter all your information about your starting address"
        + " into the page, then click submit and view the results!");
    }

    private void showText(String text)
    {
        helpTextLabel.textProperty().unbind();
        helpTextLabel.textProperty().bind(Translator.getTranslator().getTranslationBinding(text));
    }
    /**
     * On OK press, changes the scene
     */
    public void handleOk() {
        ((Stage) helpTextLabel.getScene().getWindow()).close();
    }
}
