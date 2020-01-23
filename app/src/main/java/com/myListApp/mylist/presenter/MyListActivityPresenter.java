package com.myListApp.mylist.presenter;

import android.content.Context;

import com.myListApp.mylist.BoardingActivity;
import com.myListApp.mylist.Firebase.UserOperation;
import com.myListApp.mylist.Models.ItemModel;
import com.myListApp.mylist.SQLite.AppDatabase;
import com.myListApp.mylist.SQLite.ArchiveItemListDao;
import com.myListApp.mylist.SQLite.ItemListDao;

import java.util.List;

public class MyListActivityPresenter {

    private View view;
    private ItemListDao itemListDao;
    private ArchiveItemListDao archiveItemModel;


    public MyListActivityPresenter(View view, Context context){
        this.view=view;
        this.itemListDao= AppDatabase.getInstance(context).itemListDao();
        this.archiveItemModel=AppDatabase.getInstance(context).archiveItemListDao();
    }

    public void checkItem(String item){
        if (!item.equals("")) {
            Boolean itemExist = false;
            for (int i = 0; i < BoardingActivity.itemModelArray.size(); i++) {
                if (BoardingActivity.itemModelArray.get(i).getItemName().equals(item)) {
                    view.setToastMessage("הפריט שרשמת כבר נמצא ברשימה");
                    itemExist = true;
                    break;
                }
            }
            if (!itemExist) {
                BoardingActivity.itemModelArray.add(new ItemModel(item));
                view.addItemToList();
            }
        }
        else{view.setToastMessage("אינך יכול להוסיף פריט ריק"); }

    }

    public void updateDataBase(){
        itemListDao.deleteAll();
        itemListDao.insertAll(BoardingActivity.itemModelArray);
        if (!BoardingActivity.itemModelArray.isEmpty()) {
            UserOperation userOperation = UserOperation.getInstance();
            userOperation.setUserListToFirebase(BoardingActivity.itemModelArray);
        }
    }

    public void autocompleteItemsValue(){
        view.addAutocompleteTextView(archiveItemModel.getDistinctNames());
    }





    public interface View{

        void setToastMessage(String message);
        void addItemToList();
        void addAutocompleteTextView(List<String> list);


    }

}

