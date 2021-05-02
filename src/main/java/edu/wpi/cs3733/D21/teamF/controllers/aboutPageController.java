package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.fxml.FXML;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class aboutPageController {
    @FXML JFXButton back;
    public void goBack(ActionEvent actionevent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/PreferredPathfindingAlgoView");
    }


}
