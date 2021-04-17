package edu.wpi.fuchsiafalcons.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import java.io.IOException;
import static org.testfx.api.FxAssert.verifyThat;
import static org.junit.jupiter.api.Assertions.*;

public class EditMapNodeDialogViewControllerTest extends ApplicationTest {

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/EditMapNodeDialogView.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
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

    @Test
    public void testHelpMenu() {
        verifyThat("?", Node::isVisible);
        clickOn("?");
        verifyThat("Back", Node::isVisible);
        clickOn("#nodeIDButton");
        verifyThat("An identifying key for your node, of a format similar to: CCONF001L1.", Node::isVisible);
        clickOn("#xCoordButton");
        verifyThat("A non-negative number between _ and _ that is the x-position of your node.", Node::isVisible);
        clickOn("#yCoordButton");
        verifyThat("A non-negative number between _ and _ that is the y-position of your node.", Node::isVisible);
        clickOn("#floorButton");
        verifyThat("The floor your node is on out of the options L2, L1, G, 1, 2, or 3.", Node::isVisible);
        clickOn("#buildingButton");
        verifyThat("The building your node is in.", Node::isVisible);
        clickOn("#nodeTypeButton");
        verifyThat("The type of room your node represents, for example a restroom has node type \"REST\".", Node::isVisible);
        clickOn("#longNameButton");
        verifyThat("A descriptive name of your node.", Node::isVisible);
        clickOn("#shortNameButton");
        verifyThat("A short name for your node that will be displayed.", Node::isVisible);
        clickOn("Back");
        verifyThat("?", Node::isVisible);
    }
}
