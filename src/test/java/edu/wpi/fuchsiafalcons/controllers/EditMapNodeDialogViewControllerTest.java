package edu.wpi.fuchsiafalcons.controllers;

import javafx.fxml.FXMLLoader;
import javafx.geometry.VerticalDirection;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import java.io.IOException;
import static org.testfx.api.FxAssert.verifyThat;

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
        write("3");
        clickOn("#yCoordField");
        write("2");
        clickOn("#floorField");
        write(".");
        clickOn("#buildingField");
        write(".");
        clickOn("#nodeTypeField");
        write(".");
        clickOn("#longNameField");
        write(".");
        clickOn("#shortNameField");
        write(".");
        clickOn("OK");
        moveTo("CCONF001L1");
        //scroll(10,DOWN);
        clickOn("Load From File/Reset Database");

    }

}
