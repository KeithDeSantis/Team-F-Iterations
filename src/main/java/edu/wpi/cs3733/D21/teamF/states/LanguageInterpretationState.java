package edu.wpi.cs3733.D21.teamF.states;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class LanguageInterpretationState implements IState {

    private static LanguageInterpretationState languageInterpretationState = new LanguageInterpretationState();

    private LanguageInterpretationState() {}

    public static LanguageInterpretationState getLanguageInterpretationState() { return languageInterpretationState; }

    @Override
    public void switchScene(SceneContext sceneContext) throws IOException {
        Stage stage = sceneContext.getStage();
        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/LanguageInterpretationServiceRequestView.fxml"));
        stage.getScene().setRoot(root);
        stage.show();
        sceneContext.setSceneState(this);
    }

}
