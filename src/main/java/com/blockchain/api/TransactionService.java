package com.blockchain.api;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.blockchain.chain.Donater;
import com.blockchain.chain.Peer;
import org.springframework.stereotype.Service;

import com.blockchain.api.dto.TransactionDto;
import com.blockchain.chain.Transaction;

@Service
public class TransactionService {

    public Transaction createTransaction(TransactionDto transactionDto){
        Date timeStamp = new Date();

        if(transactionDto.getSender().getPrivateKey()==null){
            Donater donater = new Donater(
                    transactionDto.getSender().getId(),
                    transactionDto.getSender().getName(),
                    transactionDto.getSender().getBalance());

            Transaction transaction = new Transaction(
                    donater, transactionDto.getRecipient(),
                    transactionDto.getRequestedAmount(), timeStamp);
            return transaction;

        }else {
            Transaction transaction = new Transaction(
                    transactionDto.getSender(), transactionDto.getRecipient(),
                    transactionDto.getRequestedAmount(), timeStamp);
            return transaction;


        }

    }

    public void sendTransactionToPeers(TransactionDto transactionDto) {
        Transaction transaction = createTransaction(transactionDto);
        Peer peer = findAvailablePeer();
        if (peer == null) {
            System.out.println("No available peers found to process the received transaction.");
            return;
        }

        System.out.println("Received a transaction request. Creating an output stream for the transaction to the randomly selected peer on the network: " + peer.getPeerInfo());

        try (Socket peerSocket = new Socket(peer.getIpAddress(), peer.getPort())) {
            ObjectOutputStream outputStream = new ObjectOutputStream(peerSocket.getOutputStream());
            outputStream.writeObject(transaction);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Peer findAvailablePeer() {
        List<Peer> peers = com.blockchain.api.DemoApplication.getPeers();
        if (peers.isEmpty()) {
            System.out.println("No available peers found.");
            return null;
        } else {
            Random random = new Random();
            int index = random.nextInt(peers.size());
            return peers.get(index);
        }
    }
}

