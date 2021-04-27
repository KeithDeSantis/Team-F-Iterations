package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
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

public class FloralDeliveryServiceTest extends ApplicationTest {

    @Override
    public void start(Stage primaryStage) throws IOException {
        SceneContext.getSceneContext().setStage(primaryStage);
        SceneContext.getSceneContext().switchScene("FloralDeliveryServiceRequestView.fxml");
    }

    @Test
    public void submitValidTest() {
        clickOn("#deliveryField");
        write("l");
        clickOn("#dateField");
        write("l");
        clickOn("#cardNumberField");
        write("l");
        clickOn("#cardCVCField");
        write("l");
        clickOn("#cardExpField");
        write("l");
        clickOn("#bouquetButton");
        clickOn("#sunflowerCheckBox");
        clickOn("#submitButton");
    }

    @Test
    public  void submitNotValid() {
        clickOn("#submitButton");
    }

    @Test
    public void goBackToMenu() {
        clickOn("#logoHome");
    }

}