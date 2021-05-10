package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.D21.teamF.Translation.Translator;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.util.stream.Collectors;

public abstract class AbsController {

    @FXML
    private JFXComboBox<String> languageSelect;

    public void hoverOnHome(MouseEvent e) {
        ((Node) e.getSource()).getScene().setCursor(Cursor.HAND);
    }

    public void hoverOffHome(MouseEvent e) {
        ((Node) e.getSource()).getScene().setCursor(Cursor.DEFAULT);
    }

    @FXML
    public void changeLanguage(){ } //TODO: Remove?

    /**
     * Used to initialize the lang combo box
     */
    public void initLanguage(){
        languageSelect.getItems().addAll(Translator.getTranslator().getLanguages().stream().map(x -> Translator.getTranslator().getLangCode(x)).collect(Collectors.toList()));

        languageSelect.setConverter(Translator.getLanguageCodeConverter());

        //languageSelect.valueProperty().bindBidirectional(Translator.getTranslator().languageProperty());
        languageSelect.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                SceneContext.getSceneContext().asyncUpdate();
                Translator.getTranslator().setLanguage(newValue);
                System.out.println("hello?");
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }
}
