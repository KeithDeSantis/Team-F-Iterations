package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.fxml.FXML;

import java.io.IOException;

public class AboutPageController extends AbsController {
    @FXML JFXButton back;



    public void goBack() throws IOException {
        SceneContext.getSceneContext().loadDefault();
    }
}
