package com.blockchain.chain;

public class ScholarshipRequest {
    private Student student;
    private double requestedAmount;
    private String purpose;

    public ScholarshipRequest(Student student, double requestedAmount, String purpose) {
        this.student = student;
        this.requestedAmount = requestedAmount;
        this.purpose = purpose;
    }

    public Student getStudent() {
        return student;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public String getPurpose() {
        return purpose;
    }
}
