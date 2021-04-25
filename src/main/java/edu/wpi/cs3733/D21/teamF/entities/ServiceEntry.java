package edu.wpi.cs3733.D21.teamF.entities;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.LocalTime;


public class ServiceEntry extends RecursiveTreeObject<ServiceEntry> {
    final String uuid;
    String requestType;
    String assignedTo;
    String completeStatus;
    String date;
    String time;
    String name;
    String appointment;
    String language;
    SimpleStringProperty requestTypeProperty;
    SimpleStringProperty assignedToProperty;
    SimpleStringProperty completeStatusProperty;
    SimpleStringProperty dateProperty;
    SimpleStringProperty timeProperty;
    SimpleStringProperty nameProperty;
    SimpleStringProperty appointmentProperty;
    SimpleStringProperty languageProperty;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public SimpleStringProperty requestTypeProperty() {
        return requestTypeProperty;
    }

    public SimpleStringProperty assignedToProperty() {
        return assignedToProperty;
    }

    public SimpleStringProperty completeStatusProperty() {
        return completeStatusProperty;
    }

    public SimpleStringProperty getDateProperty() {
        return dateProperty;
    }

    public SimpleStringProperty dateProperty() {
        return dateProperty;
    }

    public void setDateProperty(String dateProperty) {
        this.dateProperty.set(dateProperty);
    }

    public SimpleStringProperty getTimeProperty() {
        return timeProperty;
    }

    public SimpleStringProperty timeProperty() {
        return timeProperty;
    }

    public void setTimeProperty(String timeProperty) {
        this.timeProperty.set(timeProperty);
    }

    public SimpleStringProperty getNameProperty() {
        return nameProperty;
    }

    public SimpleStringProperty nameProperty() {
        return nameProperty;
    }

    public void setNameProperty(String nameProperty) {
        this.nameProperty.set(nameProperty);
    }

    public SimpleStringProperty getAppointmentProperty() {
        return appointmentProperty;
    }

    public SimpleStringProperty appointmentProperty() {
        return appointmentProperty;
    }

    public void setAppointmentProperty(String appointmentProperty) {
        this.appointmentProperty.set(appointmentProperty);
    }

    public SimpleStringProperty getLanguageProperty() {
        return languageProperty;
    }

    public SimpleStringProperty languageProperty() {
        return languageProperty;
    }

    public void setLanguageProperty(String languageProperty) {
        this.languageProperty.set(languageProperty);
    }


    public ServiceEntry(String uuid, String requestType, String assignedTo, String completeStatus, String date, String time, String name, String appointment, String language) {
        this.uuid = uuid;
        this.requestType = requestType;
        this.assignedTo = assignedTo;
        this.completeStatus = completeStatus;
        this.date = date;
        this.time = time;
        this.name = name;
        this.appointment = appointment;
        this.language = language;
        requestTypeProperty = new SimpleStringProperty(requestType);
        assignedToProperty = new SimpleStringProperty(assignedTo);
        completeStatusProperty = new SimpleStringProperty(completeStatus);
        dateProperty = new SimpleStringProperty(date);
        timeProperty = new SimpleStringProperty(time);
        nameProperty = new SimpleStringProperty(name);
        appointmentProperty = new SimpleStringProperty(appointment);
        languageProperty = new SimpleStringProperty(language);
    }

    public ServiceEntry(String uuid1, String language_interpretation_request, String s, String uuid, LocalDate dateValue, LocalTime value, String requestType, String assignedTo, String completeStatus) {
        this.uuid = uuid;
        this.requestType = requestType;
        this.assignedTo = assignedTo;
        this.completeStatus = completeStatus;
        requestTypeProperty = new SimpleStringProperty(requestType);
        assignedToProperty = new SimpleStringProperty(assignedTo);
        completeStatusProperty = new SimpleStringProperty(completeStatus);
    }

    public String getRequestType() {
        return requestType;
    }

    public SimpleStringProperty requestTypeProperty() {
        return requestTypeProperty;
    }

    public void setRequestTypeProperty(String requestType) {
        this.requestTypeProperty.set(requestType);
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public SimpleStringProperty assignedToProperty() {
        return assignedToProperty;
    }

    public void setAssignedToProperty(String assignedToProperty) {
        this.assignedToProperty.set(assignedToProperty);
    }

    public String getCompleteStatus() {
        return completeStatus;
    }

    public SimpleStringProperty completeStatusProperty() {
        return completeStatusProperty;
    }

    public void setCompleteStatusProperty(String completeStatusProperty) {
        this.completeStatusProperty.set(completeStatusProperty);
    }

    public String getUuid() {
        return uuid;
    }

    public SimpleStringProperty getRequestTypeProperty() {
        return requestTypeProperty;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public SimpleStringProperty getAssignedToProperty() {
        return assignedToProperty;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public SimpleStringProperty getCompleteStatusProperty() {
        return completeStatusProperty;
    }

    public void setCompleteStatus(String completeStatus) {
        this.completeStatus = completeStatus;
    }
}


