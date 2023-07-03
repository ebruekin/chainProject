package com.blockchain.chain;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Peer {
    private String ipAddress;
    private int port;
    private Blockchain blockchain;
    private ExecutorService executorService;
    private List<PeerInfo> peerList;
    private PeerInfo peerInfo;


    public Peer(String ipAddress, int port, Blockchain blockchain) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.blockchain = blockchain;
        this.executorService = Executors.newFixedThreadPool(20);
        this.peerList = new ArrayList<>();
        this.peerInfo = new PeerInfo(ipAddress, port);
        // Peer adds itself to the blockchain
        this.blockchain.addPeer(this);
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("ServerSocket bound to: " + serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort());

            serverSocket.setSoTimeout(2500);
            ExecutorService handlerExecutorService = Executors.newFixedThreadPool(10);

            // Called while testing without an api endpoint
            //generateRandomTransaction();

            Thread incomingThread = new Thread(() -> {
                while (true) {
                    try {
                        System.out.println("Waiting for incoming connections on peer: " + this.peerInfo);

                        Socket socket = serverSocket.accept();
                        handlerExecutorService.execute(new PeerHandler(socket, this));

                        System.out.println("Executor service started running on: " + peerInfo);
                        System.out.println("Peer: " + this.peerInfo + "'s copy of the blockchain:");
                        blockchain.processPendingTransactions();
                        System.out.println("Copy of the blockchain on Peer: " + peerInfo);
                        blockchain.printChain();
                        System.out.println("-------------------------------");

                    } catch (SocketTimeoutException e) {
                        continue;
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            });
            incomingThread.start();

            Thread outgoingThread = new Thread(() -> {
                connectToPeers();
            });
            outgoingThread.start();

            incomingThread.join();
            outgoingThread.join();

            serverSocket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Used to connect to other peers found in the network.
    private void connectToPeers() {
        ExecutorService executorService = Executors.newFixedThreadPool(peerList.size());

        for (PeerInfo peerInfo : peerList) {
            if (this.peerInfo.equals(peerInfo)) {
                continue;
            } else {
                Thread outgoingConnectionThread = new Thread(() -> {
                    try (Socket socket = new Socket()) {
                        socket.connect(new InetSocketAddress(peerInfo.getIpAddress(), peerInfo.getPort()), 5000);
                        System.out.println("Outgoing connection established to peer: " + peerInfo.getIpAddress() + ":" + peerInfo.getPort());
                        executorService.execute(new PeerHandler(socket, this));
                    } catch (IOException e) {
                        System.out.println("Failed to connect to peer: " + peerInfo.getIpAddress() + ":" + peerInfo.getPort());
                        e.printStackTrace();
                    }
                });
                executorService.submit(outgoingConnectionThread);
            }
        }

        executorService.shutdown();
        try {
            boolean success = executorService.awaitTermination(5, TimeUnit.SECONDS);

            if (success) {
                System.out.println("All connections have been established within the timeout period.");
            } else {
                System.out.println("Not all connections were established within the timeout period.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Used while testing without an api endpoint.
    /*private void generateRandomTransaction() {
        TransactionGenerator transactionGenerator = new TransactionGenerator();
        if (!peerList.isEmpty()) {
            broadcastTransaction(transactionGenerator.generateTransaction());
        } else {
            System.out.println("No peers available to broadcast the transaction.");
        }
    }*/

    // Boradcasting
    public void broadcastTransaction(Transaction transaction) {
        for (PeerInfo peerInfo : peerList) {
            sendTransactionToPeer(transaction, peerInfo);
        }
    }
    public void broadcastBlock(Block block) {
        for (PeerInfo peerInfo : peerList) {
            sendBlockToPeer(block, peerInfo);
        }
    }

    public void broadCastVote(Vote vote) {
        for (PeerInfo peerInfo : peerList) {
            sendVoteToPeer(vote);
        }
    }

    // Sending blocks, transactions and votes to another peer
    public void sendBlockToPeer(Block block, PeerInfo peerInfo) {
        try (Socket peerSocket = new Socket(peerInfo.getIpAddress(), peerInfo.getPort())) {
            System.out.println("Creating output stream for block. From Peer: " + this.peerInfo + " , to Peer: " + peerInfo);
            ObjectOutputStream outputStream = new ObjectOutputStream(peerSocket.getOutputStream());
            outputStream.writeObject(block);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendTransactionToPeer(Transaction transaction, PeerInfo peerInfo) {
        try (Socket peerSocket = new Socket(peerInfo.getIpAddress(), peerInfo.getPort())) {
            System.out.println("Creating output stream for transaction. From Peer: " + this.peerInfo + " , to Peer: " + peerInfo);
            PendingTransaction pendingTransaction = new PendingTransaction(transaction, peerInfo.getPeerId());
            ObjectOutputStream outputStream = new ObjectOutputStream(peerSocket.getOutputStream());
            outputStream.writeObject(pendingTransaction);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendVoteToPeer(Vote vote) {
        try (Socket peerSocket = new Socket(peerInfo.getIpAddress(), peerInfo.getPort())) {
            System.out.println("Sending vote from Peer: " + this.peerInfo + " to Peer: " + peerInfo);
            // Create an ObjectOutputStream to write the Vote object to the socket
            ObjectOutputStream outputStream = new ObjectOutputStream(peerSocket.getOutputStream());
            outputStream.writeObject(vote);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Maintaining peers
    public synchronized void addToPeerList(String ipAddress, int port) {
        PeerInfo peerInfo = new PeerInfo(ipAddress, port);
        if (!peerList.contains(peerInfo)) {
            peerList.add(peerInfo);
            System.out.println("Added peer: " + ipAddress + ":" + port);
        }
    }
    public synchronized void removeFromPeerList(String ipAddress, int port) {
        PeerInfo peerInfo = new PeerInfo(ipAddress, port);
        if (peerList.contains(peerInfo)) {
            peerList.remove(peerInfo);
            System.out.println("Removed peer: " + ipAddress + ":" + port);
        }
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public int getPort() {
        return port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public List<PeerInfo> getPeerList() {
        return peerList;
    }

    public PeerInfo getPeerInfo() {
        return peerInfo;
    }

    public int getPeerCount() {
        return peerList.size();
    }

}
