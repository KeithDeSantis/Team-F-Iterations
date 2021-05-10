package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXTreeView;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;


public class NavTreeTableController {

    @FXML
    private JFXTreeView<String> treeView;

    @FXML
    private JFXTreeView<String> instructionTreeView;

    private AStarDemoController realController;

    public void setRealController(AStarDemoController aStarDemoController) { this.realController = aStarDemoController; }

    public JFXTreeView<String> getTreeView() {
        return treeView;
    }

    public JFXTreeView<String> getInstructionTreeView() {
        return instructionTreeView;
    }

    public AStarDemoController getRealController() {
        return realController;
    }

    public void setTreeView(JFXTreeView<String> treeView) {
        this.treeView = treeView;
    }

    public void setInstructionTreeView(JFXTreeView<String> instructionTreeView) {
        this.instructionTreeView = instructionTreeView;
    }

    public void handleListSelection() {
        this.realController.handleListSelection();
    }

    public void handleInstructionListSelection(MouseEvent mouseEvent) {
        this.realController.handleInstructionListSelection(mouseEvent);
    }
}
