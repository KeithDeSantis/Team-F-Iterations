package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.states.SceneContext;
import edu.wpi.cs3733.D21.teamF.utils.CSVManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.testfx.api.FxAssert.verifyThat;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AStarDemoControllerTest extends ApplicationTest {


    @Override
    public void start(Stage primaryStage) throws IOException {
        setUp();

        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/AStarDemoView.fxml"));
        Scene scene = new Scene(root);
        SceneContext.getSceneContext().setStage(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @BeforeEach
    public void setUp() {
        DatabaseAPI.getDatabaseAPI().dropNodesTable();
        DatabaseAPI.getDatabaseAPI().dropEdgesTable();


        //FIXME: DO BETTER!
        DatabaseAPI.getDatabaseAPI().createNodesTable();

        List<String[]> nodeData = null;

        try {
            DatabaseAPI.getDatabaseAPI().populateNodes(CSVManager.load("MapfAllNodes.csv")); //NOTE: now can specify CSV arguments
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        DatabaseAPI.getDatabaseAPI().createEdgesTable();

        try {
            DatabaseAPI.getDatabaseAPI().populateEdges(CSVManager.load("MapfAllEdges.csv"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @Test
    public void testBackButton() {
        verifyThat("X", Node::isVisible);
        clickOn("X");
        verifyThat("Directions", Node::isVisible);
    }


    @Test
    public void testComboBox() {
        verifyThat("#startComboBox", Node::isVisible);
        clickOn("#startComboBox");
        sleep(100);
        verifyThat("ACONF00102", Node::isVisible);
        clickOn("ACONF00102");
        verifyThat("#endComboBox", Node::isVisible);
        clickOn("#endComboBox");
        sleep(100);
        verifyThat("ADEPT00301", Node::isVisible);
        clickOn("ADEPT00301");
    }
}

