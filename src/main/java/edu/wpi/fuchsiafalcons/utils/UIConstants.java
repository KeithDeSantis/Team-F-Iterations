package edu.wpi.fuchsiafalcons.utils;

import javafx.scene.paint.Color;

/**
 * This class is used to store constant values used across
 * the UI until we are able to better methodize things.
 *
 * @author Alex Friedman (ahf)
 */
public class UIConstants {
    public static final Color NODE_COLOR = Color.BLUE;

    public static final Color NODE_COLOR_HIGHLIGHT = Color.RED;

    public static final Color NODE_COLOR_SELECTED = Color.GREEN; //FIXME: DO better for colorblindness

    public static final double NODE_RADIUS = 7.0;



    public static final Color LINE_COLOR = new Color(Color.ORANGE.getRed(), Color.ORANGE.getGreen(), Color.ORANGE.getBlue(), 0.5);

    public static final double LINE_STROKE_WIDTH = 7.0;


}
