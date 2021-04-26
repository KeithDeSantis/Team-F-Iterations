package edu.wpi.cs3733.D21.teamF.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import static org.testfx.api.FxAssert.verifyThat;

import java.sql.SQLException;
import java.util.UUID;

class MarkRequestsCompleteControllerTest extends ApplicationTest {
    @Override
    public void start (Stage stage) throws Exception {
        System.gc();

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/MarkRequestsCompleteView.fxml");
        Parent root = loader.load();

        final MarkRequestsCompleteController controller = loader.getController();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void handleMarkAsComplete() throws SQLException {
        String uuid = UUID.randomUUID().toString();
        DatabaseAPI.getDatabaseAPI().addServiceReq(uuid, "Language Interpretation Request", " ", "false", "dtgfvhabkdsjfgd");
        clickOn("Language Interpretation Request");
        clickOn("#markAsComplete");
        verifyThat("false", Node::isVisible);

    }

    @Test
    void handleHome() {
    }
}