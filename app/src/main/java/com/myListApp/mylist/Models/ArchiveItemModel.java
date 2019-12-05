package com.myListApp.mylist.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity
public class ArchiveItemModel extends ItemModel {

    private String store;
    private String dateOfPurchase;


    public ArchiveItemModel(){

    }
    public ArchiveItemModel(String name,double price,String store,String dateOfPurchase,double amount,double weight,String brand){
        super(name,price,amount,weight,brand);
        this.store=store;
        this.dateOfPurchase=dateOfPurchase;
    }

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
