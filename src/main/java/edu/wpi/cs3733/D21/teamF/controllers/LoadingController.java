package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController implements Initializable{

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new SplashScreen().start();
    }

    class SplashScreen extends Thread {
        @Override
        public void run(){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                try {
                    SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/AStarDemoView.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
