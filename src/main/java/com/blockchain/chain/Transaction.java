package com.blockchain.chain;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Date;

public class Transaction implements Serializable {
    private String transactionId;
    private Donater sender;
    private Student recipient;
    private double requestedAmount;
    private Date timeStamp;
    private byte[] signature;

public Transaction(Donater sender, Student recipient, double requestedAmount, Date timeStamp) {
    this.sender = sender;
    this.recipient = recipient;
    this.requestedAmount = requestedAmount;
    this.timeStamp = timeStamp;
    this.transactionId = calculateTransactionId();
    generateSignature(sender.getPrivateKey());
}


    public String getTransactionId() {
        return transactionId;
    }

    public Donater getSender() {
        return sender;
    }

    public Student getRecipient() {
        return recipient;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public byte[] getSignature() {
        return signature;
    }

    public synchronized void signTransaction(PrivateKey privateKey) {
        String data = sender.getId() + recipient.getId() + requestedAmount + timeStamp.toString();
        signature = SecurityUtil.applyRSASig(privateKey, data);
    }



   
    public synchronized boolean verifySignature() {
        if (signature == null) {
            return false;
        }
        String data = getDataToSign();
        return SecurityUtil.verifyRSASig(sender.getPublicKey(), data, signature);
    }




    private String calculateTransactionId() {
        String data = getDataToSign();
        return SecurityUtil.applySha256(data);
    }


    @Override
    public String toString() {
        return "TransactionId: " + transactionId + ", Sender: " + sender.getName() + ", StudentId: " + recipient.getId() + ", Amount transferred:" + Double.toString(requestedAmount) + ", TimeStamp: " + timeStamp.toString(); 
    }



    public String getDataToSign() {
        return String.format("%s:%s:%.2f:%s", sender.getId(), recipient.getId(), requestedAmount, timeStamp.toString());
    }

    public Donater getDonater() {
        return sender;
    }

    public synchronized void generateSignature(PrivateKey privateKey) {
        String data = getDataToSign();
        try {
            Signature rsaSign = Signature.getInstance("SHA256withRSA");
            rsaSign.initSign(privateKey);
            rsaSign.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] signature = rsaSign.sign();
            this.signature = signature;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Transaction transaction = (Transaction) obj;
        return transactionId.equals(transaction.transactionId);
    }



}

