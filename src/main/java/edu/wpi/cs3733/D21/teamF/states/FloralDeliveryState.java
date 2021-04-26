package edu.wpi.cs3733.D21.teamF.states;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class FloralDeliveryState implements IState {

    private static FloralDeliveryState floralDeliveryState = new FloralDeliveryState();

    private FloralDeliveryState() {}

    public static FloralDeliveryState getFloralDeliveryState() { return floralDeliveryState; }

    @Override
    public void switchScene(SceneContext sceneContext) throws IOException {
        Stage stage = sceneContext.getStage();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/FloralDeliveryServiceRequestView.fxml"));
        stage.getScene().setRoot(root);
        stage.show();
        sceneContext.setSceneState(this);
    }

}
