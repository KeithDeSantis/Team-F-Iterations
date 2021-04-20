package edu.wpi.fuchsiafalcons.entities;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;


public class ServiceEntry extends RecursiveTreeObject<ServiceEntry> {
    final String uuid;
    String requestType;
    String assignedTo;
    String completeStatus;
    SimpleStringProperty requestTypeProperty;
    SimpleStringProperty assignedToProperty;
    SimpleStringProperty completeStatusProperty;

    public ServiceEntry(String uuid, String requestType, String assignedTo, String completeStatus) {
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


