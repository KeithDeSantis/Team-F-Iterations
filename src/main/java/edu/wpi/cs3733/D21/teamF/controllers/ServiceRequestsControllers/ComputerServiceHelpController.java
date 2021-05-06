package edu.wpi.cs3733.D21.teamF.controllers.ServiceRequestsControllers;

import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.event.ActionEvent;

import java.io.IOException;

public class ComputerServiceHelpController {
    public void goBack() throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/ComputerServiceRequestView.fxml");
    }
}
