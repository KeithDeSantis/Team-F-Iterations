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

    public String getAdditionalInstructions() {
        return additionalInstructions;
    }

    public void setAdditionalInstructions(String additionalInstructions) {
        this.additionalInstructions = additionalInstructions;
    }

    public SimpleStringProperty getAdditionalInstructionsProperty() {
        return additionalInstructionsProperty;
    }

    public SimpleStringProperty additionalInstructionsPropertyProperty() {
        return additionalInstructionsProperty;
    }

    public void setAdditionalInstructionsProperty(String additionalInstructionsProperty) {
        this.additionalInstructionsProperty.set(additionalInstructionsProperty);
    }

    SimpleStringProperty additionalInstructionsProperty;

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


