package edu.wpi.fuchsiafalcons.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import java.io.IOException;
import java.util.Objects;

import static org.testfx.api.FxAssert.verifyThat;

public class EditMapEdgeDialogueControllerTest extends ApplicationTest{

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/EditMapEdgesView.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
