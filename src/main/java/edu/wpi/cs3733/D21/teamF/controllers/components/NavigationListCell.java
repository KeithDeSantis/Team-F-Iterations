package edu.wpi.cs3733.D21.teamF.controllers.components;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 * Used to store the stops in AStarDemo
 * @author ahf kd
 */
public class NavigationListCell extends ListCell<String> {

    /**
     * Used to store the other components
     */
    private final HBox hbox;

    /**
     * Used to store the stop name
     */
    private final Label label;

    /**
     * Used to remove the list element
     */
    private final JFXButton remBtn;

    public NavigationListCell() {
        super();

        hbox = new HBox();
        final Pane pane = new Pane();

        label = new Label("TEST");
        remBtn = new JFXButton("X");

        hbox.getChildren().addAll(label, pane, remBtn);

        HBox.setHgrow(pane, Priority.ALWAYS);
        hbox.setAlignment(Pos.CENTER);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        setGraphic(null);

        if(item != null && !empty)
        {
            label.setText(item);
            setGraphic(hbox);
        }
    }

    public Label getLabel() {
        return label;
    }

    public JFXButton getCloseBtn()
    {
        return remBtn;
    }
}
