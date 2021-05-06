package edu.wpi.cs3733.D21.teamF.controllers.ServiceRequestsControllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamF.Translation.Translator;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ComputerServiceHelpController {

    @FXML
    public GridPane gridPane;

    @FXML
    public JFXButton back;

    @FXML
    public void initialize(){
        for(Node n : gridPane.getChildren())
        {
            if(n instanceof Label)
            {
                Label label = (Label) n;
                label.textProperty().bind(Translator.getTranslator().getTranslationBinding(label.getText()));
            }
        }

        back.textProperty().bind(Translator.getTranslator().getTranslationBinding(back.getText()));

    }

    public void goBack() throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/ComputerServiceRequestView.fxml");
    }
}
