package com.myListApp.mylist.Firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.myListApp.mylist.BoardingActivity;
import com.myListApp.mylist.Models.ArchiveItemModel;
import com.myListApp.mylist.Models.ItemModel;
import com.myListApp.mylist.MyListActivity;

import java.util.List;

public class UserOperation {

    private String userId;
    public static DatabaseReference dbReference;
    private static String email;
    private static Query userQuery;


    private static UserOperation INSTANCE;

    public static UserOperation getInstance(){
        if(INSTANCE==null){
            INSTANCE=new UserOperation();

        }
        dbReference= FirebaseDatabase.getInstance().getReference().child("users");
        email=BoardingActivity.email;
        userQuery=dbReference.orderByChild("email").equalTo(email);

        return INSTANCE;
    }

    /*
    insert new user to data base
     */
    public void insertNewUser(UserInfo user){
        userId = dbReference.push().getKey().toString();
        dbReference.child(userId).setValue(user);
    }

    /*
    get user data from data base
     */
    public void getUserDetailsByEmail(String email){
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> data=dataSnapshot.getChildren();
                for(DataSnapshot user:data){
                    UserInfo item=user.getValue(UserInfo.class);
                    if(item.getEmail().equals(email)){
                        GenericTypeIndicator<List<ItemModel>> genericTypeIndicatorList=new GenericTypeIndicator<List<ItemModel>>() {};
                        List<ItemModel> myList=user.child("MyList").getValue(genericTypeIndicatorList);
                        if(myList!=null) {
                            BoardingActivity.itemModelArray.addAll(myList);
                        }
                        GenericTypeIndicator<List<ArchiveItemModel>> genericTypeIndicatorArchive=new GenericTypeIndicator<List<ArchiveItemModel>>() {};
                        List<ArchiveItemModel> archiveList=user.child("archiveList").getValue(genericTypeIndicatorArchive);
                        if(archiveList!=null)
                            BoardingActivity.archiveItemArray.addAll(archiveList);
                    }
                }
            }

            @Override
            /*
            will be call if the application is unable to read data from the database
             */
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*
    insert user list to firebase
     */
    public void setUserListToFirebase(List<ItemModel> list){
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> data=dataSnapshot.getChildren();
                for(DataSnapshot user:data){
                    UserInfo item=user.getValue(UserInfo.class);
                    if(item.getEmail().equals(email)){
                        user.getRef().child("MyList").setValue(list);
                    }
                }
            }

            @Override
            /*
            will be call if the application is unable to read data from the database
             */
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("in concelled");
            }
        });
    }

    public void addToArchiveFirebase(List<ArchiveItemModel> archiveList){
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> data=dataSnapshot.getChildren();
                for(DataSnapshot user:data){
                    UserInfo item=user.getValue(UserInfo.class);
                    if(item.getEmail().equals(email)){
                        GenericTypeIndicator<List<ArchiveItemModel>> genericTypeIndicatorArchive=new GenericTypeIndicator<List<ArchiveItemModel>>() {};
                        List<ArchiveItemModel> archiveListFirebase=user.child("archiveList").getValue(genericTypeIndicatorArchive);
                        if(archiveListFirebase!=null)
                            archiveList.addAll(archiveListFirebase);
                        user.getRef().child("archiveList").setValue(archiveList);
                    }
                }
            }

            @Override
            /*
            will be call if the application is unable to read data from the database
             */
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
