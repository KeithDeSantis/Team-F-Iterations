package edu.wpi.fuchsiafalcons.controllers;

import edu.wpi.fuchsiafalcons.entities.EdgeEntry;
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

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/EditMapEdgesView.fxml"));
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
        clickOn("Load CSV");
    }

    @Test
    public void testBackButton() {
        verifyThat("Save to CSV", Node::isVisible);
        clickOn("HOME");
        verifyThat("Welcome to the Navigation Page", Node::isVisible);
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
        verifyThat("OK", Node::isVisible);
        clickOn("#edgeID");
        write("ADEPT00101_AELEV00S01");
        clickOn("OK");
        verifyThat("#ADEPT00101_AELEV00S01", Node::isVisible);

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
        clickOn("AHALL00502_ADEPT00102");
        clickOn("Edit...");
        verifyThat("OK", Node::isVisible);
        clickOn("#endNode");
        clickOn("ACONF00103");
        doubleClickOn("#edgeID");
        write("ACONF00103");
        clickOn("OK");
        verifyThat("#AHALL00502_ACONF00103", Node::isVisible);
        verifyThat("2", Node::isVisible);
    }

    @Test
    public void testEditEdgeNameChange() {
        verifyThat("Edit...", Node::isVisible);
        clickOn("AHALL00502_ADEPT00102");
        clickOn("Edit...");
        verifyThat("OK", Node::isVisible);
        doubleClickOn("#endNode");
        write("AHALL00402");
        clickOn("OK");
        clickOn("Yes");

        verifyThat("#AHALL00502_AHALL00402", Node::isVisible);
    }

    @Test
    public void testSaveToFileDisable() {
        verifyThat("Save to CSV", Node::isVisible);
        clickOn("#CSVFile");
        verifyThat("Save to CSV", Node::isDisable);
    }

    @Test
    public void testDeleteEdgeOnMap() {
        verifyThat("AHALL01001_ASTAI00101", Node::isVisible);
        clickOn("AHALL01001_ASTAI00101");
        verifyThat("#AHALL01001_ASTAI00101", Node::isVisible);
        clickOn("Delete");

        boolean hasTestClicking = true;
        for(EdgeEntry n : edgeList) {
            if (n.getEdgeID().equals("AHALL01001_ASTAI00101")) {
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
        clickOn("AREST00103_AHALL00603");
        clickOn("#AHALL00603");
        clickOn("#ACONF00103");
        verifyThat("OK", Node::isVisible);
        verifyThat("AHALL00603_ACONF00103", Node::isVisible);
        clickOn("OK");
        boolean hasTestClicking = false;
        for(EdgeEntry n : edgeList) {
            if (n.getEdgeID().equals("AHALL00603_ACONF00103")) {
                hasTestClicking = true;
            }
        }
        assertTrue(hasTestClicking);
    }
}
