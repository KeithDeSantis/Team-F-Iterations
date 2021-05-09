package edu.wpi.cs3733.D21.teamF.controllers.ServiceRequestsControllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamF.controllers.ServiceRequests;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ComputerServiceHelpController extends ServiceRequests  {

    @FXML
    public GridPane gridPane;

    @FXML
    public JFXButton back;

    @FXML
    public void initialize(){
    }

    public void goToRequest() throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/ComputerServiceRequestView.fxml");
    }
}
