package edu.wpi.cs3733.D21.teamF.entities;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;


public class ServiceEntry extends RecursiveTreeObject<ServiceEntry> {
    final String uuid;
    String requestType;
    String assignedTo;
    String completeStatus;
    String additionalInstructions;
    SimpleStringProperty requestTypeProperty;
    SimpleStringProperty assignedToProperty;
    SimpleStringProperty completeStatusProperty;
    SimpleStringProperty additionalInstructionsProperty;
    SimpleStringProperty uuidProperty;

    public ServiceEntry(String uuid, String requestType, String assignedTo, String completeStatus, String additionalInstructions) {
        this.uuid = uuid;
        this.requestType = requestType;
        this.assignedTo = assignedTo;
        this.completeStatus = completeStatus;
        this.additionalInstructions = additionalInstructions;
        requestTypeProperty = new SimpleStringProperty(requestType);
        assignedToProperty = new SimpleStringProperty(assignedTo);
        completeStatusProperty = new SimpleStringProperty(completeStatus);
        additionalInstructionsProperty = new SimpleStringProperty(additionalInstructions);
        uuidProperty = new SimpleStringProperty(uuid);
    }

    public SimpleStringProperty getUuidProperty() {
        return uuidProperty;
    }

    public SimpleStringProperty uuidProperty() {
        return uuidProperty;
    }

    public void setUuidProperty(SimpleStringProperty uuidProperty) {
        this.uuidProperty = uuidProperty;
    }

    public String getAdditionalInstructions() {
        return additionalInstructions;
    }

    public void setAdditionalInstructions(String additionalInstructions) {
        this.additionalInstructions = additionalInstructions;
    }

    public SimpleStringProperty getAdditionalInstructionsProperty() {
        return additionalInstructionsProperty;
    }

    public void setAdditionalInstructionsProperty(SimpleStringProperty additionalInstructionsProperty) {
        this.additionalInstructionsProperty = additionalInstructionsProperty;
    }

    public String getRequestType() {
        return requestType;
    }

    public SimpleStringProperty requestTypeProperty() {
        return requestTypeProperty;
    }

    public void setRequestTypeProperty(SimpleStringProperty requestTypeProperty) {
        this.requestTypeProperty = requestTypeProperty;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public SimpleStringProperty assignedToProperty() {
        return assignedToProperty;
    }

    public void setAssignedToProperty(SimpleStringProperty assignedToProperty) {
        this.assignedToProperty = assignedToProperty;
    }

    public String getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatusProperty(SimpleStringProperty completeStatusProperty) {
        this.completeStatusProperty = completeStatusProperty;
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


