package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.D21.teamF.Translation.Translator;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.stream.Collectors;

public abstract class AbsController {

    @FXML
    public JFXComboBox<String> languageSelect;

    public void hoverOnHome(MouseEvent e) {
        ((Node) e.getSource()).getScene().setCursor(Cursor.HAND);
    }

    public void hoverOffHome(MouseEvent e) {
        if(!(((Node) e.getSource()).getScene()==null)) ((Node) e.getSource()).getScene().setCursor(Cursor.DEFAULT);
    }

    @FXML
    public void changeLanguage(){ } //TODO: Remove?

    /**
     * Used to initialize the lang combo box
     */
    public void initLanguage(){
        languageSelect.getItems().addAll(Translator.getTranslator().getLanguages().stream().map(x -> Translator.getTranslator().getLangCode(x)).collect(Collectors.toList()));

        languageSelect.setConverter(Translator.getLanguageCodeConverter());

        languageSelect.valueProperty().bindBidirectional(Translator.getTranslator().languageProperty());
    }
}
