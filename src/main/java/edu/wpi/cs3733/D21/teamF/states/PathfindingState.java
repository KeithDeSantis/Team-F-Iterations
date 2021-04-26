package edu.wpi.cs3733.D21.teamF.states;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class PathfindingState implements IState {

    private static PathfindingState pathfindingStateHomeState = new PathfindingState();

    private PathfindingState() {}

    public static PathfindingState getPathfindingState() { return pathfindingStateHomeState; }

    @Override
    public void switchScene(SceneContext sceneContext) throws IOException {
        Stage stage = sceneContext.getStage();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/AStarDemoView.fxml"));
        stage.getScene().setRoot(root);
        stage.show();
        sceneContext.setSceneState(this);
    }

}
