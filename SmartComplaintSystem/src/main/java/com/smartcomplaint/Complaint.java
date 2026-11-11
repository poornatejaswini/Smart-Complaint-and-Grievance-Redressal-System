package com.smartcomplaint;

public class Complaint {

    private int id;
    private String complaintId;
    private String student;
    private String category;
    private String description;
    private String department;
    private String worker;
    private String status;
    private String resolution;

    public Complaint(int id, String complaintId, String student,
                     String category, String description,
                     String department, String worker,
                     String status, String resolution) {

        this.id = id;
        this.complaintId = complaintId;
        this.student = student;
        this.category = category;
        this.description = description;
        this.department = department;
        this.worker = worker;
        this.status = status;
        this.resolution = resolution;
    }

    public int getId() {
        return id;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public String getStudent() {
        return student;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getDepartment() {
        return department;
    }

    public String getWorker() {
        return worker;
    }

    public String getStatus() {
        return status;
    }

    public String getResolution() {
        return resolution;
    }
}