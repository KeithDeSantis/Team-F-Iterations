package edu.wpi.cs3733.D21.teamF.states;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class DefaultPageAdminState implements IState {

    private static DefaultPageAdminState defaultPageAdminState = new DefaultPageAdminState();

    private DefaultPageAdminState() {}

    public static DefaultPageAdminState getDefaultPageAdminState() { return defaultPageAdminState; }

    @Override
    public void switchScene(SceneContext sceneContext) throws IOException {
        Stage stage = sceneContext.getStage();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageAdminView.fxml"));
        stage.getScene().setRoot(root);
        stage.show();
        sceneContext.setSceneState(this);
    }

}
