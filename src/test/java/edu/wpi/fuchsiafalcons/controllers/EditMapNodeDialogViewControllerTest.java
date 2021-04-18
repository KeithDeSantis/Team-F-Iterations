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

        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/EditMapNodeView.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Test
    public void testIsFilledOut() {
        clickOn("New");
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
        clickOn("OK");
        verifyThat("Test", Node::isVisible);
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
