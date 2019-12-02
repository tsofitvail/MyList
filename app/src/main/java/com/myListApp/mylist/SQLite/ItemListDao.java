package com.myListApp.mylist.SQLite;

import android.widget.ArrayAdapter;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.myListApp.mylist.Models.ItemModel;

import java.util.Collection;
import java.util.List;

@Dao
public interface ItemListDao {

    @Query("SELECT * FROM ItemModel ORDER BY itemName DESC")
    List<ItemModel> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ItemModel> items);

    @Query("DELETE FROM ItemModel")
    void deleteAll();

    @Query("SELECT itemname FROM ItemModel")
    List<String> getItemNames();



}
