package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;

public interface IController {
    void goToPage(IController controller, String title, JFXButton buttonPushed);

    String getAddress();

    String getTitle();
}
