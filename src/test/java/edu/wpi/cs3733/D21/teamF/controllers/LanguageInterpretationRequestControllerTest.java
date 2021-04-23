package edu.wpi.cs3733.D21.teamF.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
public class LanguageInterpretationRequestControllerTest extends ApplicationTest{

    @Override
    public void start (Stage stage) throws Exception {
        System.gc();

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/LanguageInterpretationServiceRequestView.fxml"));
        Parent root = loader.load();

        final LanguageInterpretationRequestController controller = loader.getController();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @Test
    public void handleClose() {
        clickOn("#close");
        verifyThat("Welcome to the Service Request Application Home Menu", Node::isVisible);
    }

    @Test
    public void handleHelp() {
        clickOn("#help");
        verifyThat("Help", Node::isVisible);
    }

//    @Test
//    public void handleTranslate() {
//        clickOn("#translate");
//        verifyThat("Translate", Node::isVisible);
//    }

    @Test
    public void handleSubmit() {
        clickOn("#submit");
        verifyThat("#submit", Node::isVisible);
    }

    @Test
    public void initialize() {
        clickOn("#appointment");
        verifyThat("Non-Specific", Node::isVisible);
    }
}