package edu.wpi.cs3733.D21.teamF.controllers;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public abstract class AbsController {

    public void hoverOnHome(MouseEvent e) {
        ((Node) e.getSource()).getScene().setCursor(Cursor.HAND);
    }

    public void hoverOffHome(MouseEvent e) {
        ((Node) e.getSource()).getScene().setCursor(Cursor.DEFAULT);
    }

}
