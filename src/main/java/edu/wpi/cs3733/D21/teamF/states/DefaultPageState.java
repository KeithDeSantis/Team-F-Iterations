package edu.wpi.cs3733.D21.teamF.states;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class DefaultPageState implements IState {

    private static DefaultPageState defaultPageState = new DefaultPageState();

    private DefaultPageState() {}

    public static DefaultPageState getDefaultPageState() { return defaultPageState; }

    @Override
    public void switchScene(SceneContext sceneContext) throws IOException {
        Stage stage = sceneContext.getStage();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml"));
        stage.getScene().setRoot(root);
        stage.show();
        sceneContext.setSceneState(this);
    }
}
