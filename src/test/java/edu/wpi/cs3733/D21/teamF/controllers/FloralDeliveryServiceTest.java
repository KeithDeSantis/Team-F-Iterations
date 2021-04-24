package edu.wpi.cs3733.D21.teamF.controllers;

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
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/FloralDeliveryServiceRequestView.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
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
        verifyThat("Request Submitted!", Node::isVisible);
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