package com.blockchain.chain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Student implements Serializable {
    private String id;
    private double balance;
    private List<Transaction> transactionHistory;
    private List<Student> friends;
    private List<ScholarshipRequest> scholarshipRequests;


    public Student(String id, double balance) {
        this.id = id;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.scholarshipRequests = new ArrayList<>();
    }

    public void createScholarshipRequest(double requestedAmount, String purpose) {
        if (requestedAmount > 0) {
            ScholarshipRequest request = new ScholarshipRequest(this, requestedAmount, purpose);
            scholarshipRequests.add(request);
        } else {
            System.out.println("Hatalı burs miktarı. Lütfen geçerli bir miktar girin.");
        }
    }

    public void receiveDonation(Transaction transaction) {
        double amount = transaction.getRequestedAmount();
        double previousBalance = balance;
        setBalance(previousBalance + amount);
        transactionHistory.add(transaction);
        //transaction.getDonater().updateDonationHistory(this, amount);
    }


    public String getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public List<Student> getFriends() {
        return friends;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setTransactionHistory(List<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public List<ScholarshipRequest> getScholarshipRequests() {
        return scholarshipRequests;
    }
    public void removeScholarshipRequest(ScholarshipRequest scholarshipRequest) {
        scholarshipRequests.remove(scholarshipRequest);
    }


    public List<Transaction> getTransactionsWithinDateRange(Date startDate, Date endDate) {
        List<Transaction> transactionsWithinRange = new ArrayList<>();

        for (Transaction transaction : transactionHistory) {
            Date timeStamp = transaction.getTimeStamp();

            if (timeStamp.after(startDate) && timeStamp.before(endDate)) {
                transactionsWithinRange.add(transaction);
            }
        }

        return transactionsWithinRange;
    }

    public List<Transaction> getTransactionsByRecipient(Student recipient) {
        List<Transaction> transactionsByRecipient = new ArrayList<>();

        for (Transaction transaction : transactionHistory) {
            if (transaction.getRecipient().equals(recipient)) {
                transactionsByRecipient.add(transaction);
            }
        }

        return transactionsByRecipient;
    }

    public double calculateTotalTransactionAmount() {
        double totalAmount = 0.0;

        for (Transaction transaction : transactionHistory) {
            totalAmount += transaction.getRequestedAmount();
        }

        return totalAmount;
    }

    public void printTransactionHistory() {
        for (Transaction transaction : transactionHistory) {
            System.out.println("Transaction: Sender: " + transaction.getDonater().getName() + ", Recipient: " +
                    transaction.getRecipient().getId() + ", Amount: " + transaction.getRequestedAmount() + ", TimeStamp: " +
                    transaction.getTimeStamp());
        }
    }
}
