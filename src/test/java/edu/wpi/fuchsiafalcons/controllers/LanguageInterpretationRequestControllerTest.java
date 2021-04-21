package edu.wpi.fuchsiafalcons.controllers;

import static org.junit.Assert.*;

import edu.wpi.fuchsiafalcons.entities.NodeEntry;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
public class LanguageInterpretationRequestControllerTest extends ApplicationTest{

    @Override
    public void start (Stage stage) throws Exception {
        System.gc();

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/LanguageInterpretationServiceRequestView.fxml"));
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