package edu.wpi.fuchsiafalcons.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.testfx.api.FxAssert.verifyThat;

import java.io.IOException;

public class AStarDemoControllerTest extends ApplicationTest {


    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/AStarDemoView.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Test
    public void testBackButton() {
        verifyThat("X", Node::isVisible);
        clickOn("X");
        verifyThat("Welcome to the Navigation Page", Node::isVisible);
    }


    @Test
    public void testComboBox() {
        verifyThat("#startComboBox", Node::isVisible);
        clickOn("#startComboBox");
        sleep(100);
        verifyThat("ADEPT00101", Node::isVisible);
        clickOn("ADEPT00101");
        verifyThat("#endComboBox", Node::isVisible);
        clickOn("#endComboBox");
        sleep(100);
        verifyThat("ADEPT00102", Node::isVisible);
        clickOn("ADEPT00102");
    }

    @Test
    public void testGoButtonWithoutInput() {
        verifyThat("Go", Node::isDisable);
    }

    @Test
    public void testGoButton() {
        verifyThat("Go", Node::isDisable);
        testComboBox();
        verifyThat("Go", Node::isVisible);
        clickOn("Go");
    }



}

