package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.pathfinding.EdgeEntry;
import edu.wpi.cs3733.D21.teamF.pathfinding.NodeEntry;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;

import java.io.IOException;
import java.lang.reflect.Field;

public class MapEditViewControllerTest extends ApplicationTest {

    private ObservableList<EdgeEntry> edgeEntryObservableList;
    private ObservableList<NodeEntry> nodeEntryObservableList;

    @Override
    public void start(Stage primaryStage) throws IOException, NoSuchFieldException, IllegalAccessException {
        System.gc();

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/mapEditView.fxml"));
        Parent root = loader.load();

        final MapEditViewController controller = loader.getController();

        //Uses JavaReflection to access classes so that we don't have to change their actual accessibility
        final Field edgeListField = controller.getClass().getDeclaredField("edgeEntryObservableList");
        final Field nodeListField = controller.getClass().getDeclaredField("nodeEntryObservableList");

        //Set the fields to be accessible
        edgeListField.setAccessible(true);
        nodeListField.setAccessible(true);

        //Initialize our local lists
        this.edgeEntryObservableList = (ObservableList<EdgeEntry>) edgeListField.get(controller);
        this.nodeEntryObservableList = (ObservableList<NodeEntry>) nodeListField.get(controller);


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @BeforeEach
    public void beforeTest() {
        System.gc();
    }

    @AfterEach
    public void afterTest()
    {
        clickOn("Reset Database");
        System.gc();
    }

    @Test
    public void clickOnNodeTest() {
        clickOn("#FEXIT00201");
        verifyThat("FEXIT00201", Node::isVisible);
    }

    @Test
    public void deleteNodeTest() {
        clickOn("#FEXIT00201");
        verifyThat("FEXIT00201", Node::isVisible);
        clickOn("Delete");
        for(int index = 0; index<nodeEntryObservableList.size(); index++) {
            assertFalse(nodeEntryObservableList.get(index).getNodeID().equals("FEXIT00201"));
        }
        for(int index = 0; index<edgeEntryObservableList.size(); index++) {
            assertFalse(edgeEntryObservableList.get(index).getStartNode().equals("FEXIT00201"));
            if(edgeEntryObservableList.get(index).getEndNode().equals("FEXIT00201"))
                assertFalse(edgeEntryObservableList.get(index).getEndNode().equals("FEXIT00201"));
        }
    }

    @Test
    public void newNodeTest() {
        clickOn("#nodesTab");
        clickOn("New...");
        clickOn("#nodeIDField");
        write("TestNode");
        clickOn("#xCoordField");
        write("100");
        clickOn("#yCoordField");
        write("200");
        clickOn("#floorField");
        write("bad");
        clickOn("#buildingField");
        write("TestBuilding");
        clickOn("#nodeTypeField");
        write("TestType");
        clickOn("#longNameField");
        write("Testing Node one");
        clickOn("#shortNameField");
        write("Testing1");
        clickOn("OK");
        verifyThat("OK", Node::isVisible);
        doubleClickOn("#floorField");
        write("1");
        clickOn("OK");
        verifyThat("#TestNode", Node::isVisible);
        boolean isMade = false;
        for(int index = 0; index<nodeEntryObservableList.size(); index++) {
            if(nodeEntryObservableList.get(index).getNodeID().equals("TestNode")) isMade = true;
        }
        assertTrue(isMade);
    }

    @Test
    public void rightClickNewNodeTest() {
        rightClickOn("#mapPanel");
        verifyThat("Create Node Here", Node::isVisible);
        clickOn("Create Node Here");
        clickOn("#nodeIDField");
        write("testRightClick");
        clickOn("#buildingField");
        write(".");
        clickOn("#nodeTypeField");
        write(".");
        clickOn("#longNameField");
        write(".");
        clickOn("#shortNameField");
        write(".");
        clickOn("OK");
        verifyThat("#testRightClick", Node::isVisible);
    }

    @Test
    public void editNodeTest() {
        clickOn("#nodesTab");
        clickOn("Edit...");
        clickOn("#FDEPT00201");
        clickOn("Edit...");
        verifyThat("OK", Node::isVisible);
        doubleClickOn("#nodeIDField");
        write("ACONF00103");
        clickOn("OK");
        verifyThat("OK", Node::isVisible);
        doubleClickOn("#nodeIDField");
        write("FDEPT00201");
        doubleClickOn("#floorField");
        write("f");
        clickOn("OK");
        verifyThat("OK", Node::isVisible);
        doubleClickOn("#floorField");
        write("G");
        clickOn("OK");
        verifyThat("#FDEPT00201", Node::isVisible);
        verifyThat("G", Node::isVisible);

    }

    @Test
    public void editNodeValidityTest() {
        clickOn("#nodesTab");
        clickOn("#FEXIT00201");
        clickOn("Edit...");
        verifyThat("OK", Node::isVisible);
        doubleClickOn("#xCoordField");
        write("-1");
        doubleClickOn("#yCoordField");
        write("-1");
        clickOn("OK");
        verifyThat("OK", Node::isVisible);
        doubleClickOn("#xCoordField");
        write("0");
        doubleClickOn("#yCoordField");
        write("0");
        clickOn("OK");
        verifyThat("#FEXIT00201", Node::isVisible); // Testing changing position
        boolean oneFEXIT00201 = false;
        for(NodeEntry n : nodeEntryObservableList) { if(n.getNodeID().equals("FEXIT00201")) oneFEXIT00201 = !oneFEXIT00201; }
        assertTrue(oneFEXIT00201);

        clickOn("#FEXIT00201");
        clickOn("Edit...");
        verifyThat("OK", Node::isVisible);
        doubleClickOn("#nodeIDField");
        write("testID");
        clickOn("OK");
        verifyThat("#testID", Node::isVisible);
        verifyThat("testID", Node::isVisible);
    }

    @Test
    public void searchByNodesTest() {
        clickOn("#nodesTab");
        clickOn("#searchField");
        write("BREST00402");
        clickOn("#searchComboBox");
        clickOn("Floor");
        doubleClickOn("#searchField");
        write("3");
        clickOn("#searchComboBox");
        clickOn("Building");
        doubleClickOn("#searchField");
        write("Shapiro");
        clickOn("#searchComboBox");
        clickOn("Node Type");
        doubleClickOn("#searchField");
        write("REST");
        clickOn("#searchComboBox");
        clickOn("Long Name");
        doubleClickOn("#searchField");
        write("CART Waiting");
        clickOn("#searchComboBox");
        clickOn("Short Name");
        doubleClickOn("#searchField");
        write("MS");
    }

    @Test
    public void clickOnEdgeTest() {
        clickOn("#fWALK00101_fWALK00201");
        verifyThat("fWALK00101_fWALK00201", Node::isVisible);
    }

    @Test
    public void deleteEdgeTest() {
        clickOn("#FHALL01401_FHALL01101");
        clickOn("Delete");
        for (int index = 0; index < edgeEntryObservableList.size(); index++) {
            assertFalse(edgeEntryObservableList.get(index).getEdgeID().equals("FHALL01401_FHALL01101"));
        }
    }

    @Test
    public void newEdgeTest() {
        clickOn("#edgesTab");
        clickOn("New...");
        clickOn("#startNode");
        clickOn("ACONF00102");
        clickOn("#endNode");
        clickOn("ACONF00103");
        clickOn("OK");
        boolean wasAdded = false;
        for (int index = 0; index < edgeEntryObservableList.size(); index++) {
            if(edgeEntryObservableList.get(index).getEdgeID().equals("ACONF00102_ACONF00103")) wasAdded = true;
        }
        assertTrue(wasAdded);
    }

    @Test
    public void editEdgeTest() {
        clickOn("#edgesTab");
        clickOn("Edit...");
        clickOn("ADEPT00301_AHALL00701");
        clickOn("Edit...");
        clickOn("#endNode");
        clickOn("ADEPT00101");
        clickOn("OK");
        boolean wasEdited = false;
        for (int index = 0; index < edgeEntryObservableList.size(); index++) {
            if(edgeEntryObservableList.get(index).getEdgeID().equals("ADEPT00301_ADEPT00101")) wasEdited = true;
        }
        assertTrue(wasEdited);
    }

    @Test
    public void searchByEdgesTest() {
        clickOn("#edgesTab");
        clickOn("#searchField");
        write("ACONF");
        clickOn("#searchComboBox");
        clickOn("Start Node");
        doubleClickOn("#searchField");
        write("ELEV");
        clickOn("#searchComboBox");
        clickOn("End Node");
        doubleClickOn("#searchField");
        write("DEPT");
    }

    @Test
    public void clickOnToggle() {
        clickOn("#edgeCreationToggle");
    }
}