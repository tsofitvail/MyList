package com.myListApp.mylist.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.myListApp.mylist.BaseActivity;
import com.myListApp.mylist.BoardingActivity;
import com.myListApp.mylist.ComparePriceActivity;
import com.myListApp.mylist.Firebase.UserInfo;
import com.myListApp.mylist.Firebase.UserOperation;
import com.myListApp.mylist.Models.ArchiveItemModel;
import com.myListApp.mylist.Models.BoardingModel;
import com.myListApp.mylist.Models.ItemModel;
import com.myListApp.mylist.MyListActivity;
import com.myListApp.mylist.R;
import com.myListApp.mylist.SQLite.AppDatabase;
import com.myListApp.mylist.SQLite.ArchiveItemListDao;
import com.myListApp.mylist.SQLite.ItemListDao;
import com.myListApp.mylist.UpdateListActivity;

import java.util.ArrayList;
import java.util.List;

public class BoardingPresenter {
    private View view;
    private ItemListDao itemListDao;
    private ArchiveItemListDao archiveItemListDao;
    private UserInfo userInfo;
    private UserOperation userOperation;
    private List<BoardingModel> boardingModelList;

    public BoardingPresenter(View view,Context context) {
        this.view = view;
        this.itemListDao = AppDatabase.getInstance(context).itemListDao();
        this.archiveItemListDao = AppDatabase.getInstance(context).archiveItemListDao();
        this.userInfo=new UserInfo();
        this.userOperation=UserOperation.getInstance();
        this.boardingModelList= new ArrayList<>();
    }

    public void getDataForActivity(Bundle bundle){
        if(bundle!=null) {
            view.setLayoutVisibility(true);
            getBundleValue(bundle);
            checkIfAlreadySignup();
            if(!BoardingActivity.itemModelArray.isEmpty())
                BoardingActivity.itemModelArray.clear();
            if(!BoardingActivity.archiveItemArray.isEmpty())
                BoardingActivity.archiveItemArray.clear();
        }
        else{
            view.setLayoutVisibility(false);
            addListsToSQLite();
            setBoardingModelList();
            view.reloadAdapter(boardingModelList);
        }

    }
    public void getBundleValue(Bundle bundle){
        StringBuilder sb = new StringBuilder();
        String myName = bundle.getString("DISPLAY_NAME");
        sb.append(",").append(" ").append(myName).append(" ").append("היי");
        int isSignUp = bundle.getInt("IS_SIGN_UP");
        String email = bundle.getString("EMAIL");
        BoardingActivity.email=email;
        userInfo.setEmail(email);
        userInfo.setIsSignUp(isSignUp);
        userInfo.setName(myName);
        view.setUserName(userInfo);
    }

    public void checkIfAlreadySignup(){
       // setBoardingModelList();
        if(userInfo.getIsSignUp()==1){
            view.setWelcomeMessage();
            userOperation.insertNewUser(new UserInfo(userInfo.getEmail(),userInfo.getName(),new ArrayList<>(),new ArrayList<>()));
            addListsToSQLite();
            setBoardingModelList();
            view.reloadAdapter(boardingModelList);
        }
        else {
            setBoardingModelList();
            view.setReturnMessage();
            MyAsyncTask myAsyncTask=new MyAsyncTask();
            myAsyncTask.execute(userInfo.getEmail(),"true");
        }

    }

    public void setBoardingModelList() {
        if(boardingModelList.isEmpty()) {
            boardingModelList.add(new BoardingModel(R.drawable.ic_basket, "יצירת רשימה קניות", "הוסיפו פריטים לרשימה כך שלכל מקום שתלכו,לא תשכחו מה צריך לקנות", MyListActivity.class.getName()));
            boardingModelList.add(new BoardingModel(R.drawable.ic_update_details, "עדכון הפריטים ברשימה", "הוסיפו לכל פריט שקניתם את מחירו,מותג ומאפיינים נוספים שישמשו להשוואה עתידית", UpdateListActivity.class.getName()));
            boardingModelList.add(new BoardingModel(R.drawable.ic_compare_price, "השוו מחיר מוצר", "השוו מחיר של כל מוצר שקניתם וכך תוכלו לבצע קניה חכמה יותר", ComparePriceActivity.class.getName()));
        }
    }

    /*
    add to SQLite data base the updated value from Firebase
     */
    public void addListsToSQLite() {
        itemListDao.deleteAll();
        itemListDao.insertAll(BoardingActivity.itemModelArray);
        archiveItemListDao.deleteAll();
        archiveItemListDao.insertAll(BoardingActivity.archiveItemArray);

    }

    public interface View{
        void setUserName(UserInfo userInfo);
        void setWelcomeMessage();
        void setReturnMessage();
        void reloadAdapter(List<BoardingModel> boardingModelList);
        void setProgressBar();
        void setLayoutVisibility(Boolean visible);

    }

    public class MyAsyncTask extends AsyncTask<String,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            view.setProgressBar();
        }

        @Override
        protected synchronized Void doInBackground(String...params) {
            String email=params[0];
            if(params[1].equals("true")){
                DatabaseReference dbReference= FirebaseDatabase.getInstance().getReference().child("users");
                Query userQuery=dbReference.orderByChild("email").equalTo(email);
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
                                addListsToSQLite();
                                setBoardingModelList();
                                view.reloadAdapter(boardingModelList);
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
            return  null;
        }

        @Override
        //runs if you call publishProgress, usually from within doInBackground
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
