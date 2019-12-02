package com.myListApp.mylist.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity
public class ArchiveItemModel extends ItemModel {


   // private String itemName;
 //   private int itemPrice;
 //   private double amount;
 //   private double weight;
 //   private String brand;
    private String store;
    private String dateOfPurchase;


    public ArchiveItemModel(){

    }
    public ArchiveItemModel(String name,double price,String store,String dateOfPurchase,double amount,double weight,String brand){
        super(name,price,amount,weight,brand);
       // Id= UUID.randomUUID().toString();
        this.store=store;
        this.dateOfPurchase=dateOfPurchase;

     /*   this.itemName=name;
        this.itemPrice=price;

        this.amount=amount;
        this.weight=weight;
        this.brand=brand;*/
    }


/*
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
*/
    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }
}
