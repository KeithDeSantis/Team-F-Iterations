package edu.wpi.cs3733.D21.teamF.states;

import javafx.stage.Stage;

public class SceneContext {

    private static SceneContext sceneContext = new SceneContext();
    private IState sceneState;
    private Stage stage;

    private SceneContext() {}

    public static SceneContext getSceneContext() { return sceneContext; }

    public IState getSceneState() {
        return sceneState;
    }
    public Stage getStage() { return stage; }

    public void setSceneState(IState sceneState) {
        this.sceneState = sceneState;
    }
    public void setStage(Stage stage) { this.stage = stage; }
}
