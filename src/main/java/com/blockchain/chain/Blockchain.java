package com.blockchain.chain;

import java.util.*;


public class Blockchain {
    private List<Block> chain;
    private List<Peer> peers;
    private Set<Transaction> processedTransactions;

    // Could be used to simplify existing logic later.
    private Set<Transaction> finalizedTransactions;

    // Hash map of transactionId, PendingTransaction object. Essential to the engine as transactions may easily duplicate without this!
    private HashMap<String, Set<PendingTransaction>> pendingTransactions;

    public Blockchain() {
        this.chain = new ArrayList<>();
        this.peers = new ArrayList<>();
        this.pendingTransactions = new HashMap<>();
        this.processedTransactions =  new HashSet<>();
        this.finalizedTransactions = new HashSet<>();
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

    // Adds a transaction to the waiting list.
    public synchronized void addTransaction(PendingTransaction pendingTransaction) {
        // Get each entry for a transaction id.
        Set<PendingTransaction> transactionList =
                pendingTransactions.get(pendingTransaction.getTransaction().getTransactionId());
        if(transactionList == null) {
            transactionList = new HashSet<>();
        }
        if(transactionList.contains(pendingTransaction)){
            System.out.println("Same transaction can not be processed by the same peer twice!");
            return;
        }else {
            if(isTransactionAlreadyProcessed(pendingTransaction.getTransaction())){
                System.out.println("Transaction already in blockchain!");
                return;
            }
            transactionList.add(pendingTransaction);
            pendingTransactions.put(pendingTransaction.getTransaction().getTransactionId(), transactionList);
        }
        if(countVotes(transactionList)) {
            // Checks the current vote on the transaction. If vote is passed, proceeds with adding it to the block.
            processedTransactions.add(pendingTransaction.getTransaction());
        }
    }

    // Implementation of this blockchain engine creates multiple transaction entries. By restricting these to one per peer, we can actually call for a vote.
    // This way, the problem of having to handle double-spending turns into a consensus algortih.
    private synchronized boolean countVotes(Set<PendingTransaction> transactionList) {
        int peerCount = this.getPeers().size();
        double consensusRate = transactionList.size() % peerCount;
        if(consensusRate > 0.08){
            System.out.println("Votes counted for transaction.");
            return true;
        }
        System.out.println("Votes counted for transaction.FAILED");
        return false;

    }

    // Goes through several steps for processing a transaction. Called periodically by each peer independently.
    public synchronized void processPendingTransactions() {
        // Set a block size for demonstration purposes. This is so that transactions are processed when there are 2 or more waiting.
        int blockSize = 2;
        if (!processedTransactions.isEmpty()) {
                if (processedTransactions.size() >= blockSize) {
                    List<Transaction> processedTransactionsList = new ArrayList<>(processedTransactions);
                    Block newBlock = createBlock(processedTransactionsList);
                    addBlock(newBlock);
                    finalizedTransactions.addAll(processedTransactions);
                    processedTransactions.clear();
                } else {
                    System.out.println("Waiting for at least " + blockSize + " transactions to process a block.");
                }
            printChain();
        }
    }

    /*private synchronized boolean isAnyTransactionAlreadyProcessed(Block newBlock) {
        List<Transaction> newBlockTransactions = newBlock.getTransactions();
        for (Transaction newTransaction : newBlockTransactions) {
            if (isTransactionAlreadyProcessed(newTransaction)) {
                return true;
            }
        }
        return false;
    }*/

    // Checks if the transaction was processed before
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


    // Transaction verification. Overloaded for two different scenaros.
    // One is for fresh transactions and the other for transactions which havbe been broadcast from another peer.
    public synchronized boolean verifyPendingTransaction(PendingTransaction pendingTransaction) {

        Transaction transaction = pendingTransaction.getTransaction();
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

    // Checks if there are any other transactions with the same id
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

        // Verify the transactions in a block
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

    public Set<Transaction> getProcessedTransactions() {
        return processedTransactions;
    }

    public Set<Transaction> getFinalizedTransactions() {
        return finalizedTransactions;
    }

    public HashMap<String, Set<PendingTransaction>> getPendingTransactions() {
        return pendingTransactions;
    }

    public void printChain() {
        List<Block> chainCopy = new ArrayList<>(chain);
        for (Block block : chainCopy) {
            System.out.println(block);
        }
    }
}
