package edu.wpi.cs3733.D21.teamF;

import edu.wpi.cs3733.D21.teamF.database.ConnectionHandler;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.utils.CSVManager;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class  AppF extends Application {

  @Override
  public void init() {
    System.out.println("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    primaryStage.setTitle("Brigham and Women's Hospital Kiosk | Fuchsia Falcons ");
    primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagesAndLogos/BandWLogo.png")));
    if (DatabaseAPI.getDatabaseAPI().createNodesTable())
    {
        DatabaseAPI.getDatabaseAPI().populateNodes(CSVManager.load("MapfAllNodes.csv"));
    }
    if (DatabaseAPI.getDatabaseAPI().createEdgesTable())
    {
        DatabaseAPI.getDatabaseAPI().populateEdges(CSVManager.load("MapfAllEdges.csv"));
    }
    DatabaseAPI.getDatabaseAPI().createUserTable();
    DatabaseAPI.getDatabaseAPI().createServiceRequestTable();
    DatabaseAPI.getDatabaseAPI().createSystemTable();
    DatabaseAPI.getDatabaseAPI().createCollectionsTable(); //FIXME: DO BETTER

    SceneContext.getSceneContext().setStage(primaryStage);

    //ConnectionHandler.main(false);
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        ConnectionHandler.getConnection().close();
      } catch (SQLException exception) {
        exception.printStackTrace();
      }
    }));
    try {
      SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/CovidSurveyView.fxml");//DefaultPageView.fxml");
    } catch (IOException e) {
      e.printStackTrace();
      Platform.exit();
    }
  }


  @Override
  public void stop() {
    System.out.println("Shutting Down");
  }
}
