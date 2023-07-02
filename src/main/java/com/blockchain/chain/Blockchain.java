package com.blockchain.chain;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Blockchain {
    private List<Block> chain;
    private List<Peer> peers;
    private List<Transaction> pendingTransactions;

    public Blockchain() {
        this.chain = new ArrayList<>();
        this.peers = new ArrayList<>();
        this.pendingTransactions = new ArrayList<>();
        createGenesisBlock();
    }

    private synchronized void createGenesisBlock() {
        Block genesisBlock = new Block(0, "0", new ArrayList<>());
        chain.add(genesisBlock);
    }

    public synchronized void addBlock(Block block) {
        chain.add(block);
    }

    public Block createBlock(List<Transaction> transactions) {
        int index = chain.size();
        String previousHash = getLatestBlock().getHash();
        Block block = new Block(index, previousHash, transactions);
        return block;
    }

    public void addTransaction(Transaction transaction) {
        pendingTransactions.add(transaction);
    }

    public synchronized void processPendingTransactions() {
        if (!pendingTransactions.isEmpty()) {
            List<Transaction> awaitingTransactions = new ArrayList<>(pendingTransactions);
            Set<Transaction> transactionsToProcess = new HashSet<>(awaitingTransactions);
            if (areTransactionsValid(transactionsToProcess)) {
                if (transactionsToProcess.size() >= 3) {
                    List<Transaction> processedTransactions = new ArrayList<>(transactionsToProcess);
                    Block newBlock = createBlock(processedTransactions);

                    // Check if any transaction in the new block has already been processed
                    if (isAnyTransactionAlreadyProcessed(newBlock)) {
                        System.out.println("Pending transactions contain duplicate transactions. Discarding them.");
                        pendingTransactions.removeAll(awaitingTransactions);
                    } else {
                        addBlock(newBlock);
                        pendingTransactions.removeAll(awaitingTransactions);
                    }
                } else {
                    System.out.println("Waiting for at least 3 transactions to process a block.");
                }
            } else {
                System.out.println("Pending transactions are not valid. Discarding them.");
                pendingTransactions.clear();
            }
        }
    }

    private synchronized boolean isAnyTransactionAlreadyProcessed(Block newBlock) {
        List<Transaction> newBlockTransactions = newBlock.getTransactions();
        for (Transaction newTransaction : newBlockTransactions) {
            if (isTransactionAlreadyProcessed(newTransaction)) {
                return true;
            }
        }
        return false;
    }

    private synchronized boolean isTransactionAlreadyProcessed(Transaction transaction) {
        for (Block block : chain) {
            List<Transaction> transactions = block.getTransactions();
            for (Transaction tx : transactions) {
                if (tx.equals(transaction)) {
                    return true;
                }
            }
        }
        return false;
    }

    private synchronized boolean areTransactionsValid(Set<Transaction> transactions) {
        if (transactions.size() == 0) {
            System.out.println("No transactions found to validate!");
            return false;
        }
        for (Transaction transaction : transactions) {
            if (!verifyTransaction(transaction)) {
                return false;
            }
        }
        return true;
    }

    public List<Block> getChain() {
        return chain;
    }

    public List<Peer> getPeers() {
        return peers;
    }

    public void addPeer(Peer peer) {
        peers.add(peer);
    }

    public void removePeer(Peer peer) {
        peers.remove(peer);
    }

    public synchronized List<Transaction> getPendingTransactions() {
        return pendingTransactions;
    }

    public void printChain() {
        List<Block> chainCopy = new ArrayList<>(chain);
        for (Block block : chainCopy) {
            System.out.println(block);
        }
    }

    public synchronized boolean verifyTransaction(Transaction transaction) {
        // Check if the transaction amount is positive
        if (transaction.getRequestedAmount() <= 0) {
            System.out.println("Transaction verification failed: Invalid transaction amount.");
            return false;
        }

        // Check if the sender has sufficient balance
        double senderBalance = transaction.getSender().getBalance();
        if (senderBalance < transaction.getRequestedAmount()) {
            System.out.println("Transaction verification failed: Insufficient balance for sender.");
            return false;
        }

        // Check if the transaction is signed correctly
        if (!transaction.verifySignature()) {
            System.out.println("Transaction verification failed: Invalid transaction signature.");
            return false;
        }

        // Check if the transaction is a duplicate
        if (isTransactionDuplicate(transaction)) {
            System.out.println("Transaction verification failed: Duplicate transaction.");
            return false;
        }

        System.out.println("Transaction verification successful: Transaction is valid.");
        return true;
    }

    private synchronized boolean isTransactionDuplicate(Transaction transaction) {
        Set<String> transactionIds = new HashSet<>();
        for (Block block : chain) {
            List<Transaction> transactions = block.getTransactions();
            for (Transaction tx : transactions) {
                String transactionId = tx.getTransactionId();
                if (transactionIds.contains(transactionId)) {
                    return true;
                }
                transactionIds.add(transactionId);
            }
        }
        return false;
    }

    public synchronized boolean verifyBlock(Block block) {
        // Verify the previous hash and hash of the block
        Block previousBlock = chain.get(block.getIndex() - 1);

        if (!block.getPreviousHash().equals(previousBlock.getHash())) {
            System.out.println("Block verification failed: Invalid previous hash for block " + block.getIndex());
            return false;
        }

        if (!block.calculateHash().equals(block.getHash())) {
            System.out.println("Block verification failed: Invalid hash for block " + block.getIndex());
            return false;
        }

        // Verify the transactions in the block
        List<Transaction> transactions = block.getTransactions();
        for (Transaction transaction : transactions) {
            if (!verifyTransaction(transaction)) {
                System.out.println("Block verification failed: Invalid transaction in block " + block.getIndex());
                return false;
            }
        }

        System.out.println("Block verification successful: Block " + block.getIndex() + " is valid.");
        return true;
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }
}
