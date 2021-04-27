package edu.wpi.cs3733.D21.teamF.controllers;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import java.io.IOException;
import static org.testfx.api.FxAssert.verifyThat;
public class MedicineDeliveryServiceRequestTest extends ApplicationTest {

    /**
     * Starts the application
     * @param primaryStage the stage to be set
     * @throws IOException if the starting stage file is invalid
     * @author Tony Vuolo (bdane)
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/Service Requests/MedicineDeliveryServiceRequestView.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Tests whether the form submits successfully if all the fields are filled
     * @author Tony Vuolo (bdane)
     */
    @Test
    public void testFormSubmission() {
        clickOn("#employeeName");
        write("j");
        clickOn("#clientName");
        write("j");
        clickOn("#clientRoom");
        write("j");
        clickOn("#medicineInformation");
        write("j");
        clickOn("#cardNumber");
        write("j");
        clickOn("#cvc");
        write("j");
        clickOn("#expirationDate");
        write("j");
        clickOn("#cardholder");
        write("j");
        clickOn("SUBMIT");
        verifyThat("Request Submitted!", Node::isVisible);
    }

    /**
     * Tests that the test fails if any field is left blank
     * @author Tony Vuolo (bdane)
     */
    @Test
    public void testFailSubmission() {
        clickOn("SUBMIT");
        verifyThat("#employeeName", Node::isVisible);

        clickOn("#clientRoom");
        write("j");
        clickOn("SUBMIT");
        verifyThat("#employeeName", Node::isVisible);
    }
}
