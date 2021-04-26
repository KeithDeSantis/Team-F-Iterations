package edu.wpi.cs3733.D21.teamF;

import java.io.IOException;
import java.sql.SQLException;

import edu.wpi.cs3733.D21.teamF.database.ConnectionHandler;
import edu.wpi.cs3733.D21.teamF.database.DatabaseAPI;
import edu.wpi.cs3733.D21.teamF.states.SceneContext;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.xml.crypto.Data;

public class  AppF extends Application {

  private static Stage primaryStage;

  @Override
  public void init() {
    System.out.println("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws SQLException {
    DatabaseAPI.getDatabaseAPI().createNodesTable();
    DatabaseAPI.getDatabaseAPI().createEdgesTable();
    DatabaseAPI.getDatabaseAPI().createUserTable();
    DatabaseAPI.getDatabaseAPI().createServiceRequestTable(); //FIXME: DO BETTER

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
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/DefaultPageView.fxml"));//DefaultPageView.fxml"));
      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
      //primaryStage.setMaximized(true);
      primaryStage.show();
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
