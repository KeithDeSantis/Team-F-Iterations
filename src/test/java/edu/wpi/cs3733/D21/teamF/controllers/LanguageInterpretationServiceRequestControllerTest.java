package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;

public class LanguageInterpretationServiceRequestControllerTest extends ApplicationTest{

    @Override
    public void start (Stage stage) throws Exception {
        System.gc();
        SceneContext.getSceneContext().setStage(stage);
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/LanguageInterpretationServiceRequestView.fxml");
    }


    @Test
    public void handleClose() {
        clickOn("#close");
        verifyThat("Jay", Node::isVisible);
    }

    @Test
    public void handleSubmit() {
        clickOn("#submit");
        verifyThat("#submit", Node::isVisible);
    }

    @Test
    public void initialize() {
        clickOn("#appointment");
        verifyThat("Non-Specific", Node::isVisible);
    }
}
