package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;

public abstract class ServiceRequests {

    public void handleSubmit(ActionEvent e) throws IOException, SQLException {}

    public void handleCancel(ActionEvent e) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml");
    }

    public void handleBack(MouseEvent e) throws IOException {
        SceneContext.getSceneContext().loadDefault();
    }

    public void handleHelp(ActionEvent e) {}

    public boolean formFilled() { return false; }

}
