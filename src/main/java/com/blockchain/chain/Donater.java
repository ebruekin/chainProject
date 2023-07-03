package com.blockchain.chain;

import java.io.Serializable;
import java.security.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Donater implements Serializable {
    private String id;
    private String name;
    private double balance;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private ArrayList<Transaction> transactionHistory;
    private ArrayList<Student> donatedStudents;

    public Donater(String id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
        this.donatedStudents = new ArrayList<>();

        // Generate public and private keys
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            this.publicKey = keyPair.getPublic();
            this.privateKey = keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    public void donate(Student student, double requestedAmount) {
        if (requestedAmount > 0 && balance >= requestedAmount) {
            Transaction transaction = new Transaction(this, student, requestedAmount, new Date());
            transaction.signTransaction(privateKey);
            transactionHistory.add(transaction);
            student.receiveDonation(transaction);

            balance -= requestedAmount;
            donatedStudents.add(student);
        } else {
            System.out.println("Yetersiz bakiye veya hatalı burs miktarı.");
        }
    }

    public void selectAndDonateScholarship(Student student, double requestedAmount) {
        List<ScholarshipRequest> scholarshipRequests = student.getScholarshipRequests();
        if (!scholarshipRequests.isEmpty()) {
            ScholarshipRequest selectedRequest = null;
            double highestAmount = 0.0;

            for (ScholarshipRequest request : scholarshipRequests) {
                if (request.getRequestedAmount() > highestAmount && request.getRequestedAmount() <= requestedAmount) {
                    selectedRequest = request;
                    highestAmount = request.getRequestedAmount();
                }
            }

            if (selectedRequest != null) {
                donate(student, selectedRequest.getRequestedAmount());
                student.removeScholarshipRequest(selectedRequest);
            } else {
                System.out.println("Talep edilen burs miktarı yetersiz.");
            }
        } else {
            System.out.println("Öğrencinin burs talebi bulunmamaktadır.");
        }
    }

    public void printTransactionHistory() {
        for (Transaction transaction : transactionHistory) {
            System.out.println("Transaction: Sender: " + transaction.getDonater().getName() + ", Recipient: " +
                    transaction.getRecipient().getId() + ", Amount: " + transaction.getRequestedAmount() + ", TimeStamp: " +
                    transaction.getTimeStamp());
        }
    }

    /*public void updateDonationHistory(Student student, double amount) {
    }*/


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public List<Student> getDonatedStudents() {
        return donatedStudents;
    }
    public int getTotalDonatedStudents() {
        return donatedStudents.size();
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
