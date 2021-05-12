package edu.wpi.cs3733.D21.teamF.controllers.ServiceRequestsControllers;

import edu.wpi.cs3733.D21.teamF.controllers.AbsController;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;

import java.io.IOException;

public class LanguageInterpretationHelpController extends AbsController {
    public void handleHome() throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml");
    }

    public void goBack() throws IOException{
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/LanguageInterpretationServiceRequestView.fxml");
    }
}
