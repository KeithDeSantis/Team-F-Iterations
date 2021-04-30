package edu.wpi.cs3733.D21.teamF.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class GoogleMapNavigationController implements Initializable {

    @FXML
    private WebView googleMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WebEngine webEngine = googleMap.getEngine();

        final URL urlGoogleMaps = getClass().getResource("/edu/wpi/cs3733/D21/teamF/fxml/uicomponents/GoogleMap.html");
        webEngine.load(urlGoogleMaps.toExternalForm());
    }
}
