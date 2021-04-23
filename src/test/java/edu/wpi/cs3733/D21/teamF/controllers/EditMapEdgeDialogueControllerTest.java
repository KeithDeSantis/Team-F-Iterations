package edu.wpi.cs3733.D21.teamF.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.testfx.framework.junit5.ApplicationTest;
import java.io.IOException;
import java.util.Objects;

import static org.testfx.api.FxAssert.verifyThat;

public class EditMapEdgeDialogueControllerTest extends ApplicationTest{

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/EditMapEdgesView.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
