package edu.wpi.cs3733.D21.teamF.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.IntegerValidator;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

/**
 * Used to display info about the covid vaccine
 * @author Alex Friedman
 */
public class CovidVaccineDialogController extends AbsController {

    @FXML
    private JFXTextField zipCodeTxt;

    @FXML
    private JFXButton searchBtn;

    @FXML
    private void initialize(){

        final IntegerValidator validator = new IntegerValidator();

        //FIXME: prevent negatives!

        zipCodeTxt.getValidators().add(validator);

        //Do not ask questions... especially about infinite recursion... please...
        zipCodeTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty() || newValue.matches("\\d*"))
                zipCodeTxt.setText(newValue.replaceAll("[^\\d]", ""));
        });

        searchBtn.disableProperty().bind(Bindings.createBooleanBinding(() -> zipCodeTxt.getText().isEmpty(), zipCodeTxt.textProperty()));
    }

    public void handleSearchClicked() throws IOException {
        Desktop.getDesktop().browse(URI.create("https://vaxfinder.mass.gov/?zip_or_city=" + zipCodeTxt.getText() + "&q="));
    }

    /**
     * Used to view the CDC 's website.
     * @throws IOException If an error somehow occurred.
     */
    public void onVaccineInfoClicked() throws IOException {
        Desktop.getDesktop().browse(URI.create("https://www.cdc.gov/vaccines/covid-19/index.html"));
    }
}
