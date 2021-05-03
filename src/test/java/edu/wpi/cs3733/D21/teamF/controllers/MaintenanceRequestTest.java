package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;

import static org.testfx.api.FxAssert.verifyThat;

public class MaintenanceRequestTest extends ApplicationTest {
    @Override
    public void start(Stage primaryStage) throws IOException {
        System.gc();
        SceneContext.getSceneContext().setStage(primaryStage);
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/maintenanceRequest.fxml");
    }

    @Test
    public void testMaintenanceIsFilled(){
        clickOn("Submit");
        verifyThat("Submit", Node::isVisible);
        clickOn("#urgencyComboBox");
        clickOn("URGENT");
        clickOn("#typeComboBox");
        clickOn("Electrical");
        clickOn("#locationField");
        clickOn("75 Lobby");
        clickOn("#descriptionField");
        write("Description");
        clickOn("Submit");
        verifyThat("#okButton", Node::isVisible);
    }

    @Test
    public void testMaintenanceCancel(){
        clickOn("#cancel");
        verifyThat("Jay", Node::isVisible);
    }

    @Test
    public void testHome(){
        clickOn("#goBack");
        verifyThat("Login", Node::isVisible);
    }
}
