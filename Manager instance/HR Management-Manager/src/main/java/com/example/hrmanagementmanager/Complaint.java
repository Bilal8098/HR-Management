package com.example.hrmanagementmanager;

public class Complaint {
    private String title;
    private String shortDescription;
    private String fullDetails;

    public Complaint(String title, String shortDescription, String fullDetails) {
        this.title = title;
        this.shortDescription = shortDescription;
        this.fullDetails = fullDetails;
    }

    public String getTitle() {
        return title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getFullDetails() {
        return fullDetails;
    }
}
