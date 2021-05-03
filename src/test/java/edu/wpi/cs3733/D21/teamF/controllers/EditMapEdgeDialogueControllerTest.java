package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.stage.Stage;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;

public class EditMapEdgeDialogueControllerTest extends ApplicationTest{

    @Override
    public void start(Stage primaryStage) throws IOException {
        SceneContext.getSceneContext().setStage(primaryStage);
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/EditMapEdgesView.fxml");
    }
}
