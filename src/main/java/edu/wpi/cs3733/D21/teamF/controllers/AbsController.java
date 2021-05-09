package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javax.annotation.PostConstruct;

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
    public void changeLanguage(){
        System.out.println("Hello");
    }


    public void initLanguage(){
        System.out.println(languageSelect);
    }
}
