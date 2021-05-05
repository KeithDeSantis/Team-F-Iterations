package edu.wpi.cs3733.D21.teamF.utils;

import edu.wpi.cs3733.D21.teamF.controllers.IController;
import edu.wpi.cs3733.D21.teamF.entities.AccountEntry;
import edu.wpi.cs3733.D21.teamF.entities.CurrentUser;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneContext {

    private Stage stage;

    private IController controller;

    private SceneContext() {}

    private static class SceneContextSingletonHelper {
        private static final SceneContext sceneContext = new SceneContext();
    }

    public static SceneContext getSceneContext() {
        return SceneContextSingletonHelper.sceneContext;
    }

    public void switchScene(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        this.controller = loader.getController();
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void loadDefault() throws IOException {
        AccountEntry user = CurrentUser.getCurrentUser().getLoggedIn();
        if(user != null) {
            switch (user.getUserType()){
                case "administrator":
                    this.switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageAdminView.fxml");
                    break;

                case "employee":
                    this.switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageEmployeeView.fxml");
                    break;

                default:
                    this.switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
            }

        } else {
            switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
        }
    }

    public IController getController() {return controller; }

    public void setController(IController controller) { this.controller = controller; }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
