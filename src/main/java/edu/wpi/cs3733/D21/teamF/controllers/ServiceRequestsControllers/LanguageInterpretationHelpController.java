package edu.wpi.cs3733.D21.teamF.controllers.ServiceRequestsControllers;

import edu.wpi.cs3733.D21.teamF.controllers.AbsController;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class LanguageInterpretationHelpController extends AbsController {
    public void handleHome(MouseEvent mouseEvent) throws IOException {
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequestHomeNewView.fxml");
    }

    public void goBack(ActionEvent actionEvent) throws IOException{
        SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/ServiceRequests/LanguageInterpretationServiceRequestView.fxml");
    }
}
