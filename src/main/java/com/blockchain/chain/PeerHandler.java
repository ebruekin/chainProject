package com.blockchain.chain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

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
            } else if (receivedMessage instanceof Transaction) {
                System.out.println("Received a transaction");
                handleReceivedTransaction((Transaction) receivedMessage);
                System.out.println("Processed new transaction");
            } else if (receivedMessage instanceof PeerInfo) {
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
            // Handle the exception appropriately based on your specific use case
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

    private void handleReceivedTransaction(Transaction receivedTransaction) {
        peer.getBlockchain().addTransaction(receivedTransaction);
        System.out.println("Received a valid transaction. Added to the pending transactions.");
        peer.broadcastTransaction(receivedTransaction);

        // Verify and process the received transaction
        if (peer.getBlockchain().verifyTransaction(receivedTransaction)) {
            int numVotes = peer.castVotesForTransaction(receivedTransaction);
            // Basic consensus logic. Needs %50 + 1 of the votes only.
            if (numVotes >= (peer.getPeerCount() / 2) + 1) {
                System.out.println("Received a valid transaction. Added to the pending transactions.");
                peer.broadcastTransaction(receivedTransaction);
            } else {
                System.out.println("Transaction did not receive enough votes for consensus.");
            }
        } else {
            System.out.println("Received an invalid transaction. Discarded.");
        }
    }

    private void handleReceivedPeerInfo(PeerInfo receivedPeerInfo) {
        // Add the received peer to the peer list
        peer.addToPeerList(receivedPeerInfo.getIpAddress(), receivedPeerInfo.getPort());
    }
}
