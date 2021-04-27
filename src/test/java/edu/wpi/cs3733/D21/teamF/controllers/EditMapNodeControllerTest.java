/*

package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.entities.NodeEntry;
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
import java.util.List;
import java.util.stream.Collectors;

public class EditMapNodeControllerTest extends ApplicationTest {


    private ObservableList<NodeEntry> nodeList;

    @Override
    public void start(Stage primaryStage) throws IOException, NoSuchFieldException, IllegalAccessException {
        System.gc();

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/EditMapNodeView.fxml"));
        Parent root = loader.load();

        final EditMapNodeController controller = loader.getController();

        //Uses JavaReflection to access classes so that we don't have to change their actual accessibility
        final Field nodeListField = controller.getClass().getDeclaredField("nodeList");

        //Set the fields to be accessible
        nodeListField.setAccessible(true);

        //Initialize our local lists
        this.nodeList = (ObservableList<NodeEntry>) nodeListField.get(controller);


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
        verifyThat("Save to File", Node::isVisible);
        clickOn("X");
        verifyThat("Welcome to Brigham and Women's Hospital!", Node::isVisible);
    }


    @Test
    public void testNewNodeFeature() {


        //USed to get anything matching b/c we seem to have multiple now? //FIXME: IMPROVE
        List<NodeEntry> query = this.nodeList.stream().filter(node ->
                node.getNodeID().equals("TestNode") && node.getShortName().equals("Testing1")
        ).collect(Collectors.toList());

        final int pre = query.size();

        verifyThat("New", Node::isVisible);
        clickOn("New");
        verifyThat("OK", Node::isVisible);
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

        query = this.nodeList.stream().filter(node ->
            node.getNodeID().equals("TestNode") && node.getShortName().equals("Testing1")
        ).collect(Collectors.toList());
        assertEquals(pre + 1, query.size());
        clickOn("Reset Database");
    }

    @Test
    public void testEditNodeFeature() {
        verifyThat("Edit", Node::isVisible);
        clickOn("Edit");
        clickOn("ACONF00102");
        clickOn("Edit");
        verifyThat("OK", Node::isVisible);
        doubleClickOn("#nodeIDField");
        write("ACONF00103");
        clickOn("OK");
        verifyThat("OK", Node::isVisible);
        doubleClickOn("#nodeIDField");
        write("ACONF00102");
        doubleClickOn("#floorField");
        write("f");
        clickOn("OK");
        verifyThat("OK", Node::isVisible);
        doubleClickOn("#floorField");
        write("G");
        clickOn("OK");
        verifyThat("#ACONF00102", Node::isVisible);
        verifyThat("G", Node::isVisible); // Testing changing of Floor
        clickOn("Reset Database");
    }

    @Test
    public void testEditValidity() {

        clickOn("ACONF00102");
        clickOn("Edit");
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
        verifyThat("#ACONF00102", Node::isVisible); // Testing changing position
        boolean oneCCONF001L1 = false;
        for(NodeEntry n : nodeList) { if(n.getNodeID().equals("ACONF00102")) oneCCONF001L1 = !oneCCONF001L1; }
        assertTrue(oneCCONF001L1);


        clickOn("ACONF00102");
        clickOn("Edit");
        verifyThat("OK", Node::isVisible);
        doubleClickOn("#nodeIDField");
        write("testID");
        clickOn("OK");
        verifyThat("#testID", Node::isVisible);
        verifyThat("testID", Node::isVisible);
        clickOn("Reset Database");
    }

    @Test
    public void testDeleteNodeOnMap() {
        //USed to get anything matching b/c we seem to have multiple now? //FIXME: IMPROVE
        List<NodeEntry> query = this.nodeList.stream().filter(node ->
                node.getNodeID().equals("ACONF00103")
        ).collect(Collectors.toList());

        final int pre = query.size();

        verifyThat("Reset Database", Node::isVisible);
        clickOn("Reset Database");
        verifyThat("ACONF00103", Node::isVisible);
        clickOn("ACONF00103");
        verifyThat("#ACONF00103", Node::isVisible);
        clickOn("Delete");
        // Could add a more thorough test though unsure how - KD and ZS
        clickOn("Reset Database");

        query = this.nodeList.stream().filter(node ->
                node.getNodeID().equals("ACONF00103")
        ).collect(Collectors.toList());
        assertEquals(pre, query.size());
        clickOn("Reset Database");
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
    public void testClickToMakeNode() {
        verifyThat("#deleteButton", Node::isVisible);
        moveTo("#map");
        moveBy(-100, -100);
        rightClickOn();
        //rightClickOn("#map");
        clickOn("Create Node Here");
        verifyThat("OK", Node::isVisible);
        clickOn("#nodeIDField");
        write("testClicking");
        doubleClickOn("#xCoordField"); // Change to test better (right now it can't accept doubles cause of db) - KD
        write("1500");
        doubleClickOn("#yCoordField");
        write("1500");
        verifyThat("1", Node::isVisible);
        clickOn("#buildingField");
        write(".");
        clickOn("#nodeTypeField");
        write(".");
        clickOn("#longNameField");
        write(".");
        clickOn("#shortNameField");
        write(".");
        clickOn("OK");
        boolean hasTestClicking = false;
        for(NodeEntry n : nodeList) {
            if (n.getNodeID().equals("testClicking")) {
                hasTestClicking = true;
            }
        }
        assertTrue(hasTestClicking);
        clickOn("Reset Database");
    }

    */
/**
     * Test that the drag node feature updates the node entry in nodelist
     * @author KD
     *//*

    @Test
    public void testDragNode() {
        drag("#FHALL01201").dropBy(-40,-10);
        for(NodeEntry nodeEntry : nodeList) {
            if(nodeEntry.getNodeID().equals("FHALL01201")) {
                assertEquals(Integer.parseInt(nodeEntry.getXcoord()), 646, 5000); // Could do better, coordinates get a bit funky with the dropBy function but this is right - KD
                assertEquals(Integer.parseInt(nodeEntry.getYcoord()), 1720, 5000);
            }
        }
        clickOn("Reset Database");
    }

    */
/**
     * Make sure dropping on another node has no effect
     * @author KD
     *//*

    @Test
    public void testDragNodeOverNode() {
        drag("#fWALK00201").dropTo("#fWALK00301");
        for(NodeEntry nodeEntry : nodeList) {
            if(nodeEntry.getNodeID().equals("fWALK00201")) {
                assertTrue(Integer.parseInt(nodeEntry.getXcoord()) == 646); // Could do better, coordinates get a bit funky with the dropBy function but this is right - KD
                assertTrue(Integer.parseInt(nodeEntry.getYcoord()) == 1674);
            }
        }
        clickOn("Reset Database");
    }

    */
/*  Tests work but for some reason are causing that Java Heap space thing - KD
    @Test
    public void testFilteringByNodeID() {
        clickOn("#searchComboBox");
        clickOn("Node ID");
        clickOn("#searchField");
        write("fW");
        sleep(500);
        verifyThat("fWALK00101", Node::isVisible);
        clickOn("#searchField");
        write("@!$");
        sleep(500);
        verifyThat("No content in table", Node::isVisible);
    }

    @Test
    public void testFilteringByFloor() {
        clickOn("#searchComboBox");
        clickOn("Floor");
        clickOn("#searchField");
        write("L2");
        sleep(500);
        verifyThat("AELEV00SL2", Node::isVisible);
        clickOn("#searchField");
        write("@!$");
        sleep(500);
        verifyThat("No content in table", Node::isVisible);
    }

    @Test
    public void testFilteringByBuilding() {
        clickOn("#searchComboBox");
        clickOn("Building");
        clickOn("#searchField");
        write("Shapiro");
        sleep(500);
        verifyThat("GCONF02001", Node::isVisible);
        clickOn("#searchField");
        write("@!$");
        sleep(500);
        verifyThat("No content in table", Node::isVisible);
    }

    @Test
    public void testFilteringByLongName() {
        clickOn("#searchComboBox");
        clickOn("Long Name");
        clickOn("#searchField");
        write("BTM Conference Center");
        sleep(500);
        verifyThat("ACONF00103", Node::isVisible);
        clickOn("#searchField");
        write("@!$");
        sleep(500);
        verifyThat("No content in table", Node::isVisible);
    }

    @Test
    public void testFilteringByShortName() {
        clickOn("#searchComboBox");
        clickOn("Short Name");
        clickOn("#searchField");
        write("Hallway B2702");
        sleep(500);
        verifyThat("BHALL02702", Node::isVisible);
        clickOn("#searchField");
        write("@!$");
        sleep(500);
        verifyThat("No content in table", Node::isVisible);
    }
     *//*


}

*/
