package edu.wpi.fuchsiafalcons.uicomponents;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

public interface IMapDrawable {

    /*
    private IMouseClickedBehavior mouseClickedBehavior;
    private IMouseEnteredBehavior mouseEnteredBehavior;
    private IMouseExitBehavior mouseExitBehavior;
    */

    void bindLocation(DoubleProperty zoomLevel);

    BooleanProperty shouldDisplay();

    StringProperty getFloor();


}
