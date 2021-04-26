package edu.wpi.cs3733.D21.teamF.states;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class MarkRequestsCompleteState implements IState {

    private static MarkRequestsCompleteState markRequestsCompleteState = new MarkRequestsCompleteState();

    private MarkRequestsCompleteState() {}

    public static MarkRequestsCompleteState getMarkRequestsCompleteState() { return markRequestsCompleteState; }

    @Override
    public void switchScene(SceneContext sceneContext) throws IOException {
        Stage stage = sceneContext.getStage();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/MarkRequestsCompleteView.fxml"));
        stage.getScene().setRoot(root);
        stage.show();
        sceneContext.setSceneState(this);
    }

}
