package edu.wpi.cs3733.D21.teamF.controllers;

import edu.wpi.cs3733.D21.teamF.utils.SceneContext;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class CreditsController extends AbsController {

    public void handleBack() throws IOException {
        SceneContext.getSceneContext().loadDefault();
    }

    public void handleJFoenix() throws IOException {
        Desktop.getDesktop().browse(URI.create("http://www.jfoenix.com"));
    }

    public void handleApacheDerby() throws IOException {
        Desktop.getDesktop().browse(URI.create("https://db.apache.org/derby/"));
    }

    public void handleSlf4j() throws IOException {
        Desktop.getDesktop().browse(URI.create("http://www.slf4j.org"));
    }

    public void handleApacheCommonsText() throws IOException {
        Desktop.getDesktop().browse(URI.create("https://commons.apache.org/proper/commons-text/"));
    }

    public void handleOrgJson() throws IOException {
        Desktop.getDesktop().browse(URI.create("https://stleary.github.io/JSON-java/index.html"));
    }

    public void handleApachePDFBox() throws IOException {
        Desktop.getDesktop().browse(URI.create("https://pdfbox.apache.org/"));
    }

    public void handleJavaxMail() throws IOException {
        Desktop.getDesktop().browse(URI.create("https://docs.oracle.com/javaee/7/api/javax/mail/package-summary.html"));
    }

    public void handleGmailAPI() throws IOException {
        Desktop.getDesktop().browse(URI.create("https://developers.google.com/gmail/api/guides"));
    }

    public void handleGoogleMaps() throws IOException {
        Desktop.getDesktop().browse(URI.create("https://developers.google.com/maps/web-services/client-library"));
    }

    public void handleGoogleTranslate() throws IOException {
        Desktop.getDesktop().browse(URI.create("https://cloud.google.com/translate"));
    }

    public void handleApacheHTTPClient() throws IOException {
        Desktop.getDesktop().browse(URI.create("https://hc.apache.org/httpcomponents-client-5.0.x/"));
    }
}
