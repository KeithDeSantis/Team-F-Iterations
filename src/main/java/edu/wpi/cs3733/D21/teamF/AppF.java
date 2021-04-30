package edu.wpi.cs3733.D21.teamF;

import java.io.IOException;
import java.sql.SQLException;

import edu.wpi.cs3733.D21.teamF.database.ConnectionHandler;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.utils.SceneContext;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import edu.wpi.cs3733.D21.teamF.utils.CSVManager;
import javax.xml.crypto.Data;

public class  AppF extends Application {

  private static Stage primaryStage;

  @Override
  public void init() {
    System.out.println("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws SQLException, Exception {
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
    DatabaseAPI.getDatabaseAPI().createSystemTable(); //FIXME: DO BETTER

    AppF.primaryStage = primaryStage;

    SceneContext.getSceneContext().setStage(primaryStage);

    //ConnectionHandler.main(false);
    Runtime.getRuntime().addShutdownHook(new Thread(){
      @Override
      public void run() {
        try {
          ConnectionHandler.getConnection().close();
        } catch (SQLException throwables) {
          throwables.printStackTrace();
        }
      }
    });
    try {
      SceneContext.getSceneContext().switchScene("/edu/wpi/cs3733/D21/teamF/fxml/GoogleMapNavigationView.fxml");
    } catch (IOException e) {
      e.printStackTrace();
      Platform.exit();
    }
  }

  /**
   * Gets primary stage of AppF
   * @return primary stage
   * @author keithdesantis
   */
  public static Stage getPrimaryStage(){
    return primaryStage;
  }

  /**
   * Sets primary stage of AppF
   * @author keithdesantis
   */
  public static void setPrimaryStage(Stage stage) { primaryStage = stage; }


  @Override
  public void stop() {
    System.out.println("Shutting Down");
  }
}
