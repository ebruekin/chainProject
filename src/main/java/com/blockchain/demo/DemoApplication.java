package com.blockchain.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.blockchain.chain.Blockchain;
import com.blockchain.chain.Peer;

@SpringBootApplication
public class DemoApplication {

        private static List<Peer> peers;

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

                // Add peers to the global peer list
                peers = new ArrayList<>();
                peers.add(peer1);
                peers.add(peer2);
                peers.add(peer3);

                // Start the peers
                Thread thread1 = new Thread(peer1::start);
                Thread thread2 = new Thread(peer2::start);
                Thread thread3 = new Thread(peer3::start);
                thread3.start();
                thread1.start();
                thread2.start();

                // Sleep for a moment to allow the peers to start
                try {
                        Thread.sleep(10000);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }

                // Run the Spring Boot application
                SpringApplication.run(DemoApplication.class, args);
        }

        public static List<Peer> getPeers() {
                return peers;
        }
}
