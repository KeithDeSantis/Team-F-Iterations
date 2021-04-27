/*
package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.entities.EdgeEntry;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;

import java.io.IOException;
import java.lang.reflect.Field;

public class EditMapEdgesControllerTest extends ApplicationTest{

    private ObservableList<EdgeEntry> edgeList;

    @Override
    public void start(Stage primaryStage) throws IOException, NoSuchFieldException, IllegalAccessException {
        System.gc();

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/EditMapEdgesView.fxml"));
        Parent root = loader.load();

        final EditMapEdgesController controller = loader.getController();

        //Uses JavaReflection to access classes so that we don't have to change their actual accessibility
        final Field edgeListField = controller.getClass().getDeclaredField("edgeEntryObservableList");

        //Set the fields to be accessible
        edgeListField.setAccessible(true);

        //Initialize our local lists
        this.edgeList = (ObservableList<EdgeEntry>) edgeListField.get(controller);


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @BeforeEach
    public void beforeTest()
    {
        clickOn("Reset Database");
    }

    @Test
    public void testBackButton() {
        verifyThat("Save to CSV", Node::isVisible);
        clickOn("HOME");
        verifyThat("Welcome to Brigham and Women's Hospital!", Node::isVisible);
    }

    @Test
    public void testNewEdgeFeature() {
        verifyThat("New...", Node::isVisible);
        clickOn("New...");
        verifyThat("OK", Node::isVisible);
        clickOn("#startNode");
        clickOn("ADEPT00101");
        clickOn("#endNode");
        clickOn("AELEV00S01");

        clickOn("OK");
//        verifyThat("#ADEPT00101_AELEV00S01", Node::isVisible);

        boolean hasTestClicking = false;
        for(EdgeEntry n : edgeList) {
            if (n.getEdgeID().equals("ADEPT00101_AELEV00S01")) {
                hasTestClicking = true;
            }
        }
        assertTrue(hasTestClicking);
    }

    @Test
    public void testEditEdgeFeature() {
        verifyThat("Edit...", Node::isVisible);
        clickOn("ADEPT00201_AHALL00601");
        clickOn("Edit...");
        verifyThat("OK", Node::isVisible);
        clickOn("#endNode");
        clickOn("ACONF00103");
        clickOn("OK");
        verifyThat("#ADEPT00201_ACONF00103", Node::isVisible);
    }

    @Test
    public void testDeleteEdgeOnMap() {
        verifyThat("ADEPT00201_AHALL00601", Node::isVisible);
        clickOn("ADEPT00201_AHALL00601");
        verifyThat("#ADEPT00201_AHALL00601", Node::isVisible);
        clickOn("Delete");

        boolean hasTestClicking = true;
        for(EdgeEntry n : edgeList) {
            if (n.getEdgeID().equals("ADEPT00201_AHALL00601")) {
                hasTestClicking = false;
            }
        }
        assertTrue(hasTestClicking);
    }

    @Test
    public void testSwitchFloors() {
        verifyThat("1", Node::isVisible);
        clickOn("#floorComboBox");
        verifyThat("1", Node::isVisible);
        verifyThat("2", Node::isVisible);
        verifyThat("3", Node::isVisible);
        verifyThat("L1", Node::isVisible);
        verifyThat("L2", Node::isVisible);
        verifyThat("G", Node::isVisible);
        clickOn("3");
        verifyThat("3", Node::isVisible);
    }

    @Test
    public void testClickToMakeEdge() {
        clickOn("ADEPT00201_AHALL00601");
        clickOn("#AHALL00601");
        clickOn("#AHALL00401");
        verifyThat("OK", Node::isVisible);
        verifyThat("Edge ID: AHALL00601_AHALL00401", Node::isVisible);
        clickOn("OK");
        boolean hasTestClicking = false;
        for(EdgeEntry n : edgeList) {
            if (n.getEdgeID().equals("AHALL00601_AHALL00401")) {
                hasTestClicking = true;
            }
        }
        assertTrue(hasTestClicking);
    }
}
*/
