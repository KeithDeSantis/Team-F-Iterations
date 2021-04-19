package edu.wpi.fuchsiafalcons.controllers;

import edu.wpi.fuchsiafalcons.entities.NodeEntry;
import edu.wpi.fuchsiafalcons.pathfinding.Edge;
import edu.wpi.fuchsiafalcons.pathfinding.Vertex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class EditMapNodeControllerTest extends ApplicationTest {


    private ObservableList<NodeEntry> nodeList;

    @Override
    public void start(Stage primaryStage) throws IOException, NoSuchFieldException, IllegalAccessException {
        System.gc();

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/fuchsiafalcons/fxml/EditMapNodeView.fxml"));
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
        verifyThat("Welcome to the Navigation Page", Node::isVisible);
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
    public void testSaveToFileDisable() {
        verifyThat("Save to File", Node::isVisible);
        clickOn("#filenameField");
        verifyThat("Save to File", Node::isDisable);
    }

    @Test
    public void testDeleteNodeOnMap() {
        verifyThat("Reset Database", Node::isVisible);
        clickOn("Reset Database");
        verifyThat("ACONF00103", Node::isVisible);
        clickOn("ACONF00103");
        verifyThat("#ACONF00103", Node::isVisible);
        clickOn("Delete");
        // Could add a more thorough test though unsure how - KD and ZS
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
}

