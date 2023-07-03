package com.blockchain.chain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TransactionGenerator {

    // Helper class written for testing purposes
    private List<Donater> donaterList;
    private List<Student> studentList;

    public TransactionGenerator() {
        donaterList = new ArrayList<>();
        studentList = new ArrayList<>();


        Donater donater1 = new Donater("D123", "John", 1000);
        Donater donater2 = new Donater("D456", "Emily", 500);
        Donater donater3 = new Donater("D789", "David", 2000);
        Donater donater4 = new Donater("D234", "Sarah", 1500);
        Donater donater5 = new Donater("D567", "Michael", 800);
        Donater donater6 = new Donater("D890", "Olivia", 1200);
        Donater donater7 = new Donater("D345", "Daniel", 2500);
        Donater donater8 = new Donater("D678", "Sophia", 300);
        Donater donater9 = new Donater("D901", "Matthew", 100);
        Donater donater10 = new Donater("D456", "Emma", 700);

        donaterList.add(donater1);
        donaterList.add(donater2);
        donaterList.add(donater3);
        donaterList.add(donater4);
        donaterList.add(donater5);
        donaterList.add(donater6);
        donaterList.add(donater7);
        donaterList.add(donater8);
        donaterList.add(donater9);
        donaterList.add(donater10);

        Student student1 = new Student("S123", 2000);
        Student student2 = new Student("S456", 1500);
        Student student3 = new Student("S789", 1000);
        Student student4 = new Student("S234", 500);
        Student student5 = new Student("S567", 800);
        Student student6 = new Student("S890", 1200);
        Student student7 = new Student("S345", 300);
        Student student8 = new Student("S678", 100);
        Student student9 = new Student("S901", 700);
        Student student10 = new Student("S456", 2500);

        studentList.add(student1);
        studentList.add(student2);
        studentList.add(student3);
        studentList.add(student4);
        studentList.add(student5);
        studentList.add(student6);
        studentList.add(student7);
        studentList.add(student8);
        studentList.add(student9);
        studentList.add(student10);
    }

    public synchronized Transaction generateTransaction() {
        Random random = new Random();

        // Choose random donator
        Donater sender = donaterList.get(random.nextInt(donaterList.size()));

        // Choose random student
        Student recipient = studentList.get(random.nextInt(studentList.size()));

        double requestedAmount = 50;

        Date timeStamp = new Date();

        Transaction transaction = new Transaction(sender, recipient, requestedAmount, timeStamp);

        sender.setBalance(sender.getBalance() - requestedAmount);

        return transaction;
    }
}
