package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

public class EditMapNodeDialogViewControllerTest extends ApplicationTest {

    @Override
    public void start(Stage primaryStage) throws IOException {
        SceneContext.getSceneContext().setStage(primaryStage);
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/EditMapNodeDialogView.fxml");
    }

    @Test
    public void testIsFilledOut() {
        verifyThat("OK", Node::isVisible);
        clickOn("OK");
        verifyThat("OK", Node::isVisible);
        clickOn("#nodeIDField");
        write("Test");
        clickOn("#xCoordField");
        write("1");
        clickOn("#yCoordField");
        write("2");
        clickOn("#floorField");
        write("2");
        clickOn("#buildingField");
        write(".");
        clickOn("#nodeTypeField");
        write(".");
        clickOn("#longNameField");
        write(".");
        clickOn("#shortNameField");
        write(".");
    }

    @Test
    public void testIsProperFloor() {
        EditMapNodeDialogViewController dialogViewController = new EditMapNodeDialogViewController();
        assertTrue(dialogViewController.isProperFloor("1"));
        assertTrue(dialogViewController.isProperFloor("2"));
        assertTrue(dialogViewController.isProperFloor("3"));
        assertTrue(dialogViewController.isProperFloor("L1"));
        assertTrue(dialogViewController.isProperFloor("L2"));
        assertTrue(dialogViewController.isProperFloor("G"));
        assertFalse(dialogViewController.isProperFloor("4"));
        assertFalse(dialogViewController.isProperFloor("test"));
        assertFalse(dialogViewController.isProperFloor(""));
    }
}
