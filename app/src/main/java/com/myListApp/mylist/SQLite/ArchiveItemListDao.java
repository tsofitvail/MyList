package com.myListApp.mylist.SQLite;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.myListApp.mylist.Models.ArchiveItemModel;
import com.myListApp.mylist.Models.ItemModel;

import java.util.List;

@Dao
public interface ArchiveItemListDao {

    @Query("SELECT * FROM ArchiveItemModel ORDER BY itemName DESC")
    List<ArchiveItemModel> getAll();

    @Query(("SELECT * FROM ArchiveItemModel WHERE itemName=:name ORDER BY itemPrice ASC"))
    List<ArchiveItemModel> getByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ArchiveItemModel> items);

    @Query("DELETE FROM ArchiveItemModel")
    void deleteAll();

    @Query("SELECT DISTINCT itemname FROM ArchiveItemModel;")
    List<String> getDistinctNames();

    @Query("SELECT DISTINCT store FROM ArchiveItemModel;")
    List<String> getDistinctPlaces();


}
