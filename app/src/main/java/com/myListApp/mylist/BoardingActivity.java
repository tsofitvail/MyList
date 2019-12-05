package com.myListApp.mylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.myListApp.mylist.Adapter.BoardingAdapter;
import com.myListApp.mylist.Adapter.ItemViewAdapter;
import com.myListApp.mylist.Enum.EnumLayoutType;
import com.myListApp.mylist.Firebase.UserInfo;
import com.myListApp.mylist.Firebase.UserOperation;
import com.myListApp.mylist.Models.ArchiveItemModel;
import com.myListApp.mylist.Models.BoardingModel;
import com.myListApp.mylist.Models.ItemModel;
import com.myListApp.mylist.SQLite.AppDatabase;
import com.myListApp.mylist.SQLite.ArchiveItemListDao;
import com.myListApp.mylist.SQLite.ItemListDao;

import java.util.ArrayList;
import java.util.List;

public class BoardingActivity extends BaseActivity {

    public static List<ItemModel> itemModelArray=new ArrayList<ItemModel>();
    public static List<ArchiveItemModel> archiveItemArray=new ArrayList<ArchiveItemModel>();
    public static String email;

    private RecyclerView boardingRecView;
    private BoardingAdapter boardingAdapter;
    private List<BoardingModel>  boardingModelList= new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private TextView textViewDisplayName,textViewWelcomUser;
    private LinearLayout headlineLayout;
    private ItemListDao itemListDao;
    private ArchiveItemListDao archiveItemListDao;
    private UserOperation userOperation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_welcome_boarding);

        itemListDao= AppDatabase.getInstance(this).itemListDao();
        archiveItemListDao=AppDatabase.getInstance(this).archiveItemListDao();
       //if(MyListActivity.itemModelArray.isEmpty())
          //  MyListActivity.itemModelArray=itemListDao.getAll();//get item of the list
      //  MyListActivity.itemModelArray=new ArrayList<>();

        headlineLayout=findViewById(R.id.headlineLayout);
        MyAsyncTask myAsyncTask=new MyAsyncTask();
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            headlineLayout.setVisibility(View.VISIBLE);
            StringBuilder sb=new StringBuilder();
            String myName=bundle.getString("DISPLAY_NAME");
            sb.append(",").append(" ").append(myName).append(" ").append("היי");
            textViewWelcomUser=findViewById(R.id.welcomeMessage);
            textViewDisplayName=findViewById(R.id.displayName);
            textViewDisplayName.setText(sb);//present the user name
            int isSignUp=bundle.getInt("IS_SIGN_UP");
            email=bundle.getString("EMAIL");
            userOperation=UserOperation.getInstance();

            if(isSignUp==1) {//present welcom message depend if the user is already log in
                textViewWelcomUser.setText("אני כל כך שמחה לראותך כאן!");
                userOperation.insertNewUser(new UserInfo(email,myName,new ArrayList<>(),new ArrayList<>()));
                reloadAdapter();
            }
             else {
                if(!itemModelArray.isEmpty())
                    itemModelArray.clear();
                if(!archiveItemArray.isEmpty())
                    archiveItemArray.clear();
                textViewWelcomUser.setText("איזה כיף שחזרת!");
                myAsyncTask.execute(email,"true");
               // userOperation.getUserDetailsByEmail(email);
             //   addListsToSQLite();
            }

        }
        else{
            headlineLayout.setVisibility(View.GONE);
            reloadAdapter();
        }



    }


    public void reloadAdapter(){
        addListsToSQLite();
        boardingRecView=findViewById(R.id.boardingRecycleView);
        layoutManager = new LinearLayoutManager(this);
        boardingRecView.setLayoutManager(layoutManager);
        boardingRecView.setHasFixedSize(true);
        boardingModelList.add(new BoardingModel(R.drawable.ic_basket,"יצירת רשימה קניות","הוסיפו פריטים לרשימה כך שלכל מקום שתלכו,לא תשכחו מה צריך לקנות",MyListActivity.class.getName()));
        boardingModelList.add(new BoardingModel(R.drawable.ic_update_details,"עדכון הפריטים ברשימה","הוסיפו לכל פריט שקניתם את מחירו,מותג ומאפיינים נוספים שישמשו להשוואה עתידית",UpdateListActivity.class.getName()));
        boardingModelList.add(new BoardingModel(R.drawable.ic_compare_price,"השוו מחיר מוצר","השוו מחיר של כל מוצר שקניתם וכך תוכלו לבצע קניה חכמה יותר",ComparePriceActivity.class.getName()));
        boardingAdapter=new BoardingAdapter(this,boardingModelList);
        boardingRecView.setAdapter(boardingAdapter);
    }

    /*
        add to SQLite data base the updated value from Firebase
         */
    private void addListsToSQLite() {
        itemListDao.deleteAll();
        itemListDao.insertAll(itemModelArray);
        archiveItemListDao.deleteAll();
        archiveItemListDao.insertAll(archiveItemArray);

    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_welcome_boarding;
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //this.finish();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public class MyAsyncTask extends AsyncTask<String,Void,Void> {
        private UserOperation userOperation;
        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showing progress bar
            // progressBar = new ProgressBar(BoardingActivity.this, null, android.R.attr.progressBarStyleLarge);
            //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
           // params.addRule(RelativeLayout.CENTER_IN_PARENT);
           // layoutManager.addView(progressBar);
         //   progressBar.setVisibility(View.VISIBLE);


            userOperation=UserOperation.getInstance();

        }
        @Override
        protected synchronized Void doInBackground(String...params) {
            //firebase
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
                                user.getRef().child("name").setValue("tsuf");
                                GenericTypeIndicator<List<ItemModel>> genericTypeIndicatorList=new GenericTypeIndicator<List<ItemModel>>() {};
                                List<ItemModel> myList=user.child("MyList").getValue(genericTypeIndicatorList);
                                if(myList!=null) {
                                    //  MyListActivity.itemModelArray.addAll(myList);
                                    BoardingActivity.itemModelArray.addAll(myList);
                                    //  BoardingActivity.itemModelArray.addAll(myList);
                                }
                                GenericTypeIndicator<List<ArchiveItemModel>> genericTypeIndicatorArchive=new GenericTypeIndicator<List<ArchiveItemModel>>() {};
                                List<ArchiveItemModel> archiveList=user.child("archiveList").getValue(genericTypeIndicatorArchive);
                                if(archiveList!=null)
                                    BoardingActivity.archiveItemArray.addAll(archiveList);
                                reloadAdapter();

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
            //   userOperation.getUserDetailsByEmail(email);
            return null;
        }

        //runs if you call publishProgress, usually from within doInBackground
        @Override
        protected void onProgressUpdate(Void... values) {
          //  super.onProgressUpdate();
            //after firebase ens

        }

        @Override
        protected synchronized void onPostExecute(Void v) {
        //    progressBar.setVisibility(View.GONE);

            // super.onPreExecute();
            //set adapter
       /*     addListsToSQLite();
            layoutManager = new LinearLayoutManager(getApplicationContext());
            boardingRecView.setLayoutManager(layoutManager);
            boardingRecView.setHasFixedSize(true);
            boardingModelList.add(new BoardingModel(R.drawable.ic_basket,"יצירת רשימה קניות","הוסיפו פריטים לרשימה כך שלכל מקום שתלכו,לא תשכחו מה צריך לקנות", MyListActivity.class.getName()));
            boardingModelList.add(new BoardingModel(R.drawable.ic_update_details,"עדכון הפריטים ברשימה","הוסיפו לכל פריט שקניתם את מחירו,מותג ומאפיינים נוספים שישמשו להשוואה עתידית", UpdateListActivity.class.getName()));
            boardingModelList.add(new BoardingModel(R.drawable.ic_compare_price,"השוו מחיר מוצר","השוו מחיר של כל מוצר שקניתם וכך תוכלו לבצע קניה חכמה יותר", ComparePriceActivity.class.getName()));
            boardingAdapter=new BoardingAdapter(getApplicationContext(),boardingModelList);
            boardingRecView.setAdapter(boardingAdapter);*/

            //dissmiss progress bar
        }

    }









}

