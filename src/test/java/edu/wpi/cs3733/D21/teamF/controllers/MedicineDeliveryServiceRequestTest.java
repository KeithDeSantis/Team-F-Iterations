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
     * Tests whether the form submits successfully if the
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
        clickOn("#submit");
        verifyThat("Request Submitted!", Node::isVisible);
    }
}
