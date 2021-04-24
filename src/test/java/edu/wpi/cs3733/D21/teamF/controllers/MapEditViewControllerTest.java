package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.entities.EdgeEntry;
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
    public void beforeTest()
    {
        clickOn("Reset From Database");
    }

    @Test
    public void clickOnNodeTest() {
        clickOn("#FEXIT00201");
        verifyThat("FEXIT00201", Node::isVisible);
    }

    @Test
    public void clickOnEdgeTest() {
        clickOn("#fWALK00501_FEXIT00301");
        verifyThat("fWALK00501_FEXIT00301", Node::isVisible);
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
}