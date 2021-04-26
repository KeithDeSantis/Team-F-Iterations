package edu.wpi.cs3733.D21.teamF.states;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class FoodDeliveryState implements IState {

    private static FoodDeliveryState foodDeliveryState = new FoodDeliveryState();

    private FoodDeliveryState() {}

    public static FoodDeliveryState getFoodDeliveryState() { return foodDeliveryState; }

    @Override
    public void switchScene(SceneContext sceneContext) throws IOException {
        Stage stage = sceneContext.getStage();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/FoodDeliveryServiceRequestView.fxml"));
        stage.getScene().setRoot(root);
        stage.show();
        sceneContext.setSceneState(this);
    }

}
