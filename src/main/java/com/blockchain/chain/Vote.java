package com.blockchain.chain;

public class Vote {
    private boolean vote;
    Transaction transaction;
    PeerInfo peerInfo;

    public Vote(boolean vote, Transaction transaction, PeerInfo peerInfo) {
        this.vote = vote;
        this.transaction = transaction;
        this.peerInfo = peerInfo;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public boolean isVote() {
        return vote;
    }

    public void setVote(boolean vote) {
        this.vote = vote;
    }


    public PeerInfo getPeerInfo() {
        return peerInfo;
    }

    public void setPeerInfo(PeerInfo peerInfo) {
        this.peerInfo = peerInfo;
    }
}
