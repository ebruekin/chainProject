package com.blockchain.demo;

import com.blockchain.chain.Blockchain;
import com.blockchain.chain.Peer;

public class Test2 {
    public static void main(String[] args) {
        // Create the blockchain
        Blockchain blockchain = new Blockchain();


        // Create the peers
        Peer peer1 = new Peer("127.0.0.1", 8003, blockchain);
        Peer peer2 = new Peer("127.0.0.2", 8001, blockchain);
        Peer peer3 = new Peer("127.0.0.3", 8004, blockchain);

        // Add peers to each other's peer lists
        peer1.addToPeerList("127.0.0.2", 8001);
        peer1.addToPeerList("127.0.0.3", 8004);

        peer2.addToPeerList("127.0.0.1", 8003);
        peer2.addToPeerList("127.0.0.3", 8004);

        peer3.addToPeerList("127.0.0.1", 8003);
        peer3.addToPeerList("127.0.0.2", 8001);

        System.out.println("-------------------------------");

        // Start the peers
        Thread thread1 = new Thread(peer1::start);
        Thread thread2 = new Thread(peer2::start);
        Thread thread3 = new Thread(peer3::start);
        thread3.start();
        thread1.start();
        thread2.start();

        // Sleep for a moment to allow the peers to start
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("-------------------------------");



        // Sleep for a moment to allow the peers to process the transactions
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Print the blockchain of each peer
        System.out.println("Peer 1's copy of the blockchain:");
        peer1.getBlockchain().printChain();

        System.out.println("Peer 2's copy of the blockchain:");
        peer2.getBlockchain().printChain();

        System.out.println("Peer 3's copy of the blockchain:");
        peer3.getBlockchain().printChain();
    }
}
