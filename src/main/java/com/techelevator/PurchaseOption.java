package com.techelevator;

import java.io.*;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class PurchaseOption extends Option {
    private double currentBalance = 0.00;
    private double amountFed = 0.00;
    private String purchaseAction = "";
    private Change customerChange;

    File transactions = new File("Log.txt");
    NumberFormat dollarAmount = NumberFormat.getCurrencyInstance();
    DateTimeFormatter todaysDateTime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime todayNow = LocalDateTime.now();

    public PurchaseOption(String name, int number) {
        super(name, number);
        this.currentBalance = currentBalance;

    }

    public double getAmountFed() {
        return amountFed;
    }

    public void setAmountFed(double amountFed) {
        this.amountFed = amountFed;
    }

    public String displayPurchaseMenu() {
        System.out.println("Please select from one of the following options:");
        System.out.println("1: Feed Money");
        System.out.println("2: Select Product");
        System.out.println("3: Finish Transaction");
        Scanner purchaseChoice = new Scanner(System.in);
        purchaseAction = purchaseChoice.nextLine();
        return purchaseAction;

    }

    public double getBalance() {
        return currentBalance;
    }

    public void printBalance() {
        NumberFormat dollarAmount = NumberFormat.getCurrencyInstance();
        System.out.println("Current funds provided: " + dollarAmount.format(currentBalance));
    }

    public double askForMoney(){
        //needs to check for null or STRINGS.
        System.out.println("Please enter your money, in whole dollars.");
        Scanner userInput = new Scanner(System.in);
        amountFed = Double.parseDouble(userInput.nextLine());
        return amountFed;
    }

    public double feedMoney() {
        //write to text file with amount fed
        currentBalance += amountFed;
        try{
            BufferedWriter buffer = new BufferedWriter(new FileWriter(transactions, true));
            //today's date, current date, FEED MONEY: amountEntered currentBalance
            buffer.write(todaysDateTime.format(todayNow) + "  FEED MONEY: " + dollarAmount.format(amountFed) + " " + dollarAmount.format(currentBalance));
            buffer.newLine();
            buffer.flush();
        }
         catch (IOException e){
            System.out.println(e.getMessage());
        }
        return currentBalance;
    }

    public double selectProduct(VendingMachineItem itemToPurchase) {
        if (itemToPurchase.getPrice() > currentBalance) {
            System.out.println("You do not have enough funds available for this purchase.");
            System.out.println("Enter more funds to proceed.");
            displayPurchaseMenu();
        }
        //write to text file with item purchased
        try{
            BufferedWriter buffer = new BufferedWriter(new FileWriter(transactions, true));
            buffer.write(todaysDateTime.format(todayNow) + "  " + itemToPurchase.getName() + "  " + dollarAmount.format(currentBalance) + " " + dollarAmount.format(currentBalance-itemToPurchase.getPrice()));
            buffer.newLine();
            buffer.flush();

        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        System.out.println(itemToPurchase.getSound());
        System.out.println("Item purchased: " + itemToPurchase.getName());
        System.out.println("Purchase price: " + dollarAmount.format(itemToPurchase.getPrice()));
        currentBalance -= itemToPurchase.getPrice();
        return currentBalance;
    }

    public double completeTransaction(){
        //write to File with change returned
        try {
            BufferedWriter buffer = new BufferedWriter(new FileWriter(transactions, true));
            buffer.write(todaysDateTime.format(todayNow) + "  GIVE CHANGE: " + dollarAmount.format(currentBalance) + " " + dollarAmount.format(0.0));
            buffer.newLine();
            buffer.flush();

        } catch (IOException e){
            System.out.println(e.getMessage());
        }

        return currentBalance;
    }

    public String getPurchaseAction() {
        return purchaseAction;
    }

    public void setPurchaseAction(String purchaseAction) {
        this.purchaseAction = purchaseAction;
    }

    public Change calculateChange() {
        int quarters = 0;
        int dimes = 0;
        int nickels = 0;
        quarters = (int) Math.floor(currentBalance / 0.25);
        currentBalance -= quarters * 0.25;

        dimes = (int) Math.floor(currentBalance / 0.10);
        currentBalance -= dimes * 0.10;

        nickels = (int) Math.floor(currentBalance / 0.05);
        currentBalance -= nickels * 0.05;

        Change customerChange = new Change(quarters, dimes, nickels);
        System.out.println("Here's your change: " + customerChange.getQuarter() + " quarters, " + customerChange.getDime() + " dimes, " + customerChange.getNickel() + " nickels.");
        return customerChange;
    }

}
