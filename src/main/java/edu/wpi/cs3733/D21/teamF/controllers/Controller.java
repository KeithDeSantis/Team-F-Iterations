package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    private String address;
    private String title;


    public void goToPage(Controller controller, String title, JFXButton buttonPushed) throws IOException{
        Stage stage;
        Parent root;
        stage = (Stage) buttonPushed.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(controller.getAddress()));
        stage.getScene().setRoot(root);
        stage.setTitle(controller.getTitle());
        stage.show();
    }

    public String getAddress() {
        return address;
    }

    public String getTitle() {
        return title;
    }
}
