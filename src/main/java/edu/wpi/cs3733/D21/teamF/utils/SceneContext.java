package edu.wpi.cs3733.D21.teamF.utils;

import edu.wpi.cs3733.D21.teamF.controllers.AbsController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class SceneContext {

    private Stage stage;

    private AbsController controller;

    private SceneContext() {}

    private static class SceneContextSingletonHelper {
        private static final SceneContext sceneContext = new SceneContext();
    }

    public static SceneContext getSceneContext() {
        return SceneContextSingletonHelper.sceneContext;
    }

    public void switchScene(String fxml) throws IOException {
        final Task<Parent> task = new Task<Parent>() {
            @Override
            public Parent call() throws IOException {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(fxml));
                controller = loader.getController();
                return loader.load();
            }
        };
        task.setOnSucceeded( e-> {
            try {
                stage.setScene(new Scene(task.get()));
                stage.show();
            } catch (InterruptedException | ExecutionException interruptedException) {
                interruptedException.printStackTrace();
            }
        });

        Thread t = new Thread(task);
        t.start();
    }

    public void loadDefault() throws IOException {
        this.switchScene("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml");
    }

    public AbsController getController() {return controller; }

    public void setController(AbsController controller) { this.controller = controller; }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
