package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;

public class AccountManagerControllerTest extends ApplicationTest {

    @Override
    public void start (Stage stage) throws Exception {
        System.gc();
        SceneContext.getSceneContext().setStage(stage);
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AccountManagerView.fxml");
    }

    @Test
    public void initialize() {
        clickOn("#selectUser");
        verifyThat("admin", Node::isVisible);
    }

    @Test
    public void handleUserSearch() {
        clickOn("#selectUser");
        verifyThat("admin", Node::isVisible);
    }

//    @Test
//    public void handleDeleteUser() {
//        clickOn("#selectUser");
//        clickOn("admin");
//        clickOn("#deleteUser");
//        clickOn("#selectUser");
//        assertEquals(null, #selectUser.getItems());
//    }

    @Test
    public void handleAddUser() {
    }

    @Test
    public void handleSaveChanges() {
    }

    @Test
    public void handleAdminHome() {
    }

    @Test
    public void changingUsername() {
    }

    @Test
    public void changingPassword() {
    }

    @Test
    public void changingUserType() {
    }
}