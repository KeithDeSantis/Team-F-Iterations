package edu.wpi.fuchsiafalcons.controllers;

import static org.junit.Assert.*;

import javafx.fxml.FXMLLoader;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
public class LanguageInterpretationRequestControllerTest extends ApplicationTest{

    @Override
    public void start (Stage stage) throws Exception {
        Parent mainScreen = FXMLLoader.load(LanguageInterpretationRequestControllerTest.class.getResource("LanguageInterpretationRequestController.fxml"));
        stage.setScene(new Scene(mainScreen));
        stage.show();
        stage.toFront();
    }
    @Before
    public void setUp () throws Exception {
    }

    @After
    public void tearDown () throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void handleClose() {

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