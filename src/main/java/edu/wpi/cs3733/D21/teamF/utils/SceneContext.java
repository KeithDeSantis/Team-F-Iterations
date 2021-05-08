package edu.wpi.cs3733.D21.teamF.utils;

import com.jfoenix.controls.JFXDecorator;
import edu.wpi.cs3733.D21.teamF.controllers.AbsController;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

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

    private void showLoadingPage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/LoadPage.fxml"));
        controller = loader.getController();
        stage.setScene(new Scene(loader.load()));
        stage.show();
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
                Stage stage = new Stage();
                JFXDecorator decorator = new JFXDecorator(stage, task.get());
                decorator.setCustomMaximize(true);
                //decorator.setContent(task.get());
                decorator.setTitle("TEST");

                stage.setScene(new Scene(decorator));//task.get()));
                stage.show();
            } catch (InterruptedException | ExecutionException interruptedException) {
                interruptedException.printStackTrace();
            }
        });

        task.setOnScheduled( e -> {
            final PauseTransition timeOut = new PauseTransition(Duration.seconds(0.2));
            timeOut.setOnFinished((timeout) -> {
                if(task.isRunning()) {
                    try {
                        showLoadingPage();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });
            timeOut.play();

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
