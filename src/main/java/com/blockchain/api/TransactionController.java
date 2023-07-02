package com.blockchain.api;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.bind.annotation.*;

import com.blockchain.api.dto.TransactionDto;
import com.blockchain.chain.Donater;
import com.blockchain.chain.Student;

@RestController
@RequestMapping
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions")
    public void createTransaction(@RequestBody TransactionDto transactionDto){
        transactionService.sendTransactionToPeers(transactionDto);
    }

    @GetMapping
    public void testci(){
        System.out.println("TEEEEEEEEEEEEEEEEST NEEEEEEEEEEEXT");
    }
}

