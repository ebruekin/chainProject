package com.blockchain.chain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Block{
    private int index;
    private String previousHash;
    private List<Transaction> transactions;
    private long timeStamp;
    private String hash;
    private String merkleRoot;

    public Block(int index, String previousHash, List<Transaction> transactions) {
        this.index = index;
        this.previousHash = previousHash;
        this.transactions = transactions;
        this.timeStamp = System.currentTimeMillis();
        this.merkleRoot = calculateMerkleRoot();
        this.hash = calculateHash();
    }

    // Calculates hash for the block
    public String calculateHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String dataToHash = index + previousHash + merkleRoot + timeStamp;
            byte[] hashBytes = digest.digest(dataToHash.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = String.format("%02x", hashByte);
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return ""; // Return an empty string in case of hash calculation error
        }
    }

    String calculateMerkleRoot() {
        if (transactions.isEmpty()) {
            return ""; // Return an empty string if there are no transactions
        }

        List<String> transactionIds = new ArrayList<>();

        // Stores transaction ids
        for (Transaction transaction : transactions) {
            String transactionId = transaction.getTransactionId();
            transactionIds.add(transactionId);
        }

        // Construct the Merkle tree
        List<String> levelHashes = new ArrayList<>(transactionIds);

        while (levelHashes.size() > 1) {
            List<String> newLevelHashes = new ArrayList<>();

            for (int i = 0; i < levelHashes.size(); i += 2) {
                String leftHash = levelHashes.get(i);
                String rightHash = (i + 1 < levelHashes.size()) ? levelHashes.get(i + 1) : leftHash;

                String combinedHash = hashPair(leftHash, rightHash);
                newLevelHashes.add(combinedHash);
            }

            levelHashes = newLevelHashes;
        }

        return levelHashes.get(0); // The root hash is the only remaining hash in the final level
    }

    // Combining two hashes to create a hash pair. Used for merkle root calculation
    private String hashPair(String leftHash, String rightHash) {
        String concatenatedHash = leftHash + rightHash;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(concatenatedHash.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = String.format("%02x", hashByte);
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return ""; // Return an empty string in case of hash calculation error
        }
    }


    // Getters and Setters

    public int getIndex() {
        return index;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getHash() {
        return hash;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }


    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }



    @Override
    public String toString() {
        return "Block{" +
                "index=" + index +
                ", previousHash='" + previousHash + '\'' +
                ", transactions=" + transactions +
                ", timeStamp=" + timeStamp +
                ", hash='" + hash + '\'' +
                ", merkleRoot='" + merkleRoot + '\'' +
                '}';
    }
}