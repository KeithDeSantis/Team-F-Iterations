package edu.wpi.cs3733.D21.teamF.utils;

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

    //    public void switchScene(String fxml) throws IOException {
//        System.out.println("SW " + fxml);
//        final Task<Parent> task = new Task<Parent>() {
//            @Override
//            public Parent call() {
//                FXMLLoader loader = new FXMLLoader();
//                loader.setLocation(getClass().getResource(fxml));
//                controller = loader.getController();
//                System.out.println("CTRL " + controller);
//                try {
//                    return loader.load();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return  null;
//            }
//        };
//        task.setOnSucceeded( e-> {
//            try {
//                stage.setScene(new Scene(task.get()));
//                System.out.println("SUCCESS");
//                stage.show();
//            } catch (InterruptedException | ExecutionException interruptedException) {
//                interruptedException.printStackTrace();
//            }
//        });
//
//        task.setOnScheduled( e -> {
//            final PauseTransition timeOut = new PauseTransition(Duration.seconds(0.2));
//            timeOut.setOnFinished((timeout) -> {
//                System.out.println(fxml + " " + task.isRunning());
//                if(task.isRunning()) {
//                    try {
//                        showLoadingPage();
//                    } catch (IOException ioException) {
//                        ioException.printStackTrace();
//                    }
//                }
//            });
//            timeOut.play();
//
//        });
//
//        Thread t = new Thread(task);
//        t.start();
//    }
    public void switchScene(String fxml) throws IOException {
        System.out.println("SW " + fxml);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        this.controller = loader.getController();
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();

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
