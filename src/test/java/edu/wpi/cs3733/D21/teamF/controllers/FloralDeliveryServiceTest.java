package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;

public class FloralDeliveryServiceTest extends ApplicationTest {

    @Override
    public void start(Stage primaryStage) throws IOException {
        SceneContext.getSceneContext().setStage(primaryStage);
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/FloralDeliveryServiceRequestView.fxml");
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