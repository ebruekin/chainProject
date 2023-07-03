package com.blockchain.chain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

public class PeerHandler implements Runnable {
    private Socket socket;
    private Peer peer; // Reference to the parent Peer instance

    public PeerHandler(Socket socket, Peer peer) {
        this.socket = socket;
        this.peer = peer;
    }

    @Override
    public void run() {
        try {
            handleIncomingConnection();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleIncomingConnection() throws IOException, ClassNotFoundException {
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Waiting for input from peer: " + peer.getPeerInfo());

            // Receive message from the peer
            Object receivedMessage = inputStream.readObject();
            System.out.println("Received: " + receivedMessage);

            // Process the received message based on its type
            if (receivedMessage instanceof Block) {
                System.out.println("Received a block");
                handleReceivedBlock((Block) receivedMessage);
                System.out.println("Processed new block");
            }else if (receivedMessage instanceof Transaction) {
                System.out.println("Received a new transaction");
                handleReceivedTransaction((Transaction) receivedMessage);
                System.out.println("Handling new transaction new transaction");
            }else if (receivedMessage instanceof Vote) {
                System.out.println("Received a vote");
                handleReceivedVote((Vote) receivedMessage);
                System.out.println("Handling received vote");
            }
            else if (receivedMessage instanceof PendingTransaction) {
                System.out.println("Received a transaction");
                handleReceivedTransaction((PendingTransaction) receivedMessage);
                System.out.println("Processed new transaction");
            }
            else if (receivedMessage instanceof PeerInfo) {
                System.out.println("Received a peer info");
                handleReceivedPeerInfo((PeerInfo) receivedMessage);
                System.out.println("Processed new peer info");
            } else {
                System.out.println("Received unknown message: " + receivedMessage);
            }

            // Send blockchain to the peer
            outputStream.writeObject(peer.getBlockchain().getChain());
            outputStream.flush();
        } catch (SocketException e) {
            System.out.println("SocketException occurred: " + e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }
    }}


    // Adds the vote to the blockchain
    private void handleReceivedVote(Vote receivedMessage) {
        Set<PendingTransaction> transactionList =
                peer.getBlockchain().getPendingTransactions()
                        .getOrDefault(receivedMessage.getTransaction().getTransactionId(), new HashSet<>());

        PendingTransaction votedTransaction = new PendingTransaction(receivedMessage.getTransaction(),peer.getPeerInfo().getPeerId());
        transactionList.add(votedTransaction);
        peer.getBlockchain().getPendingTransactions()
                .put(receivedMessage.getTransaction().getTransactionId(),transactionList);
    }


    private void handleReceivedBlock(Block receivedBlock) {
        // Verify the received block
        if (peer.getBlockchain().verifyBlock(receivedBlock)) {
            peer.getBlockchain().addBlock(receivedBlock);
            System.out.println("Received a valid block. Added to the blockchain.");

            // Broadcast the block to other peers in the peer list
            peer.broadcastBlock(receivedBlock);
        } else {
            System.out.println("Received an invalid block. Discarded.");
        }
    }

    // For transactions that were broadcast
    private void handleReceivedTransaction(PendingTransaction receivedTransaction) {

        // Verify and process the received transaction
        if (peer.getBlockchain().verifyPendingTransaction(receivedTransaction)) {
                System.out.println("Received a valid transaction. Added to the pending transactions.");
                peer.getBlockchain().addTransaction(receivedTransaction);
                peer.broadcastTransaction(receivedTransaction.getTransaction());
            peer.broadCastVote(new Vote(true,receivedTransaction.getTransaction(),peer.getPeerInfo()));

        } else {
            System.out.println("Received an invalid transaction. Discarded.");
        }
    }

    // For fresh transactons
    private void handleReceivedTransaction(Transaction receivedTransaction) {
        PendingTransaction pendingTransaction = new PendingTransaction(receivedTransaction, peer.getPeerInfo().getPeerId());
        // Verify and process the received transaction
        if (peer.getBlockchain().verifyPendingTransaction(pendingTransaction)) {
            System.out.println("Received a valid transaction. Added to the pending transactions.");
            peer.getBlockchain().addTransaction(pendingTransaction);
            peer.broadcastTransaction(pendingTransaction.getTransaction());
        } else {
            System.out.println("Received an invalid transaction. Discarded.");
        }
    }

    private void handleReceivedPeerInfo(PeerInfo receivedPeerInfo) {
        // Add the received peer to the peer list
        peer.addToPeerList(receivedPeerInfo.getIpAddress(), receivedPeerInfo.getPort());
    }
}
