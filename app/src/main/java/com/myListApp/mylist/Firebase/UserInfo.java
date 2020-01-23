package com.myListApp.mylist.Firebase;

import com.myListApp.mylist.Models.ArchiveItemModel;
import com.myListApp.mylist.Models.ItemModel;

import java.util.List;

public class UserInfo {

    private String email;
    private String name;
    private List<ItemModel> myList;
    private List<ArchiveItemModel> archiveList;
    private int isSignUp;

    public UserInfo(){};

    public UserInfo(String email, String name, List<ItemModel> myList, List<ArchiveItemModel> archiveList) {
        this.email = email;
        this.name = name;
        this.myList = myList;
        this.archiveList = archiveList;
        this.isSignUp=0;
    }

    public int getIsSignUp() {
        return isSignUp;
    }

    public void setIsSignUp(int isSignUp) {
        this.isSignUp = isSignUp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItemModel> getMyList() {
        return myList;
    }

    public void setMyList(List<ItemModel> myList) {
        this.myList = myList;
    }

    public List<ArchiveItemModel> getArchiveList() {
        return archiveList;
    }

    public void setArchiveList(List<ArchiveItemModel> archiveList) {
        this.archiveList = archiveList;
    }
}
