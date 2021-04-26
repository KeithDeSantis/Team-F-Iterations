package edu.wpi.cs3733.D21.teamF.states;

import javafx.scene.Node;

import java.io.IOException;

public interface IState {

    public void switchScene(SceneContext sceneContext) throws IOException;
}
