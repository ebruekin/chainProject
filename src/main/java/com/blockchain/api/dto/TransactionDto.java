package com.blockchain.api.dto;



import java.util.Date;

import com.blockchain.chain.Donater;
import com.blockchain.chain.Student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    @NonNull
    Donater sender;
    
    @NonNull
    Student recipient;
        
    double requestedAmount;

}
