package com.blockchain.chain;

public class PendingTransaction {

    private Transaction transaction;
    private String peerId;

    public PendingTransaction(Transaction transaction, String peerId) {
        this.transaction = transaction;
        this.peerId = peerId;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public String getPeerId() {
        return peerId;
    }
}
