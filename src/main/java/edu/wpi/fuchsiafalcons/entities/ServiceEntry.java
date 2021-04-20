package edu.wpi.fuchsiafalcons.entities;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class ServiceEntry extends RecursiveTreeObject<ServiceEntry> {
    final String uuid;
    String requestType;
    String assignedTo;
    String completeStatus;

    public ServiceEntry(String uuid, String requestType, String assignedTo, String completeStatus) {
        this.uuid = uuid;
        this.requestType = requestType;
        this.assignedTo = assignedTo;
        this.completeStatus = completeStatus;
    }
}


