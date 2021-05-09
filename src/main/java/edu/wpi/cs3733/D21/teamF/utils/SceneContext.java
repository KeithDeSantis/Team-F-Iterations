package edu.wpi.cs3733.D21.teamF.utils;

import edu.wpi.cs3733.D21.teamF.Translation.Translator;
import edu.wpi.cs3733.D21.teamF.controllers.AbsController;
import edu.wpi.cs3733.D21.teamF.controllers.ServiceRequestsControllers.*;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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




        Parent root = loader.load();

        System.out.println("CTRL: " + loader.getController());
        this.controller = loader.getController();
        if(this.controller instanceof MaintenanceRequestController || this.controller instanceof GiftDeliveryServiceRequestController
        || this.controller instanceof LaundryRequestController || this.controller instanceof ExternalTransController || this.controller instanceof LanguageInterpretationServiceRequestController)
            autoTranslate(root);

        stage.setScene(new Scene(root));
        stage.show();

    }

    private static void autoTranslate(Parent root)
    {
        final List<Node> children = new ArrayList<>();
        getAllChildren(root, children);

        for(Node n : children)
        {
            if(n instanceof Labeled)
            {
                final Labeled labeled = (Labeled) n;

                if(labeled.getText().toLowerCase().startsWith("brigham and women's hospital"))
                    continue;
                labeled.textProperty().bind(Translator.getTranslator().getTranslationBinding(labeled.getText()));
            }
            else if(n instanceof Text)
            {
                final Text text = (Text) n;

                if(text.getText().toLowerCase().startsWith("brigham and women's hospital"))
                    continue;
                text.textProperty().bind(Translator.getTranslator().getTranslationBinding(text.getText()));
            }
            else if(n instanceof TextInputControl)
            {
                final TextInputControl textInputControl = (TextInputControl) n;

                if(textInputControl.getPromptText().toLowerCase().startsWith("brigham and women's hospital"))
                    continue;
                textInputControl.promptTextProperty().bind(Translator.getTranslator().getTranslationBinding(textInputControl.getPromptText()));
            }
            else if(n instanceof ComboBoxBase)
            {
                final ComboBoxBase comboBoxBase = (ComboBoxBase) n;

                if(comboBoxBase.getPromptText().toLowerCase().startsWith("brigham and women's hospital"))
                    continue;
                comboBoxBase.promptTextProperty().bind(Translator.getTranslator().getTranslationBinding(comboBoxBase.getPromptText()));
            }
        }
    }

    private static void getAllChildren(Parent root, List<Node> children)
    {
        if(root == null || children == null)
            return;
        for(Node node : root.getChildrenUnmodifiable())
        {
            children.add(node);
            if(node instanceof Parent)
            {
                getAllChildren((Parent) node, children);
            }
            //Should not be else
            if(node instanceof ButtonBar)
            {
                children.addAll(((ButtonBar)node).getButtons());
            }
        }
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
