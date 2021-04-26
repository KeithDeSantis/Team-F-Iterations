package edu.wpi.cs3733.D21.teamF.states;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class ServiceRequestHomeState implements IState {

    private static ServiceRequestHomeState serviceRequestHomeState = new ServiceRequestHomeState();

    private ServiceRequestHomeState() {}

    public static ServiceRequestHomeState getServiceRequestHomeState() { return serviceRequestHomeState; }

    @Override
    public void switchScene(SceneContext sceneContext) throws IOException {
        Stage stage = sceneContext.getStage();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeView.fxml"));
        stage.getScene().setRoot(root);
        stage.show();
        sceneContext.setSceneState(this);
    }
}
