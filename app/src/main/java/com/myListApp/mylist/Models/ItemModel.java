package com.myListApp.mylist.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity
public class ItemModel {

    @PrimaryKey
    @NonNull
    private String Id;

    private String itemName;

    private double itemPrice;
    private double amount;
    private double weight;
    private String brand;

    private boolean isExpended;

    public ItemModel(){this.Id= UUID.randomUUID().toString();}

    public ItemModel(String name){
        this.Id= UUID.randomUUID().toString();
        this.itemName=name;
        this.isExpended=false;
    }

    public ItemModel(String name,double itemPrice,double amount,double weight,String brand){
        this.Id= UUID.randomUUID().toString();
        this.itemName=name;
        this.itemPrice=itemPrice;
        this.amount=amount;
        this.weight=weight;
        this.brand=brand;
    }

    @NonNull
    public String getId() {
        return Id;
    }

    public void setId(@NonNull String id) {
        Id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
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

    public boolean isExpended() {
        return isExpended;
    }

    public void setExpended(boolean expended) {
        isExpended = expended;
    }
}
