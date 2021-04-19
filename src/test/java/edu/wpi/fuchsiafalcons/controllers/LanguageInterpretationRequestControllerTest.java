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

    private ObservableList<NodeEntry> nodeList;

    @Override
    public void start (Stage stage) throws Exception {
        System.gc();

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/LanguageInterpretationServiceRequestView.fxml"));
        Parent root = loader.load();

        final LanguageInterpretationRequestController controller = loader.getController();

        //Uses JavaReflection to access classes so that we don't have to change their actual accessibility
        final Field nodeListField = controller.getClass().getDeclaredField("nodeList");

        //Set the fields to be accessible
        nodeListField.setAccessible(true);

        //Initialize our local lists
        this.nodeList = (ObservableList<NodeEntry>) nodeListField.get(controller);


        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @Test
    public void handleClose() {
        clickOn("close");
        verifyThat("Welcome to the Service Request Application Home Menu", Node::isVisible);
    }

    @Test
    public void handleHelp() {
    }

    @Test
    public void handleTranslate() {
    }

    @Test
    public void handleSubmit() {
    }

    @Test
    public void initialize() {
    }
}