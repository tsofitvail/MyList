package com.myListApp.mylist;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.myListApp.mylist.Adapter.BoardingAdapter;
import com.myListApp.mylist.Firebase.UserInfo;
import com.myListApp.mylist.Models.ArchiveItemModel;
import com.myListApp.mylist.Models.BoardingModel;
import com.myListApp.mylist.Models.ItemModel;
import com.myListApp.mylist.presenter.BoardingPresenter;

import java.util.ArrayList;
import java.util.List;

public class BoardingActivity extends BaseActivity implements BoardingPresenter.View {

    public static List<ItemModel> itemModelArray=new ArrayList<ItemModel>();
    public static List<ArchiveItemModel> archiveItemArray=new ArrayList<ArchiveItemModel>();
    public static String email;

    private RecyclerView boardingRecView;
    private BoardingAdapter boardingAdapter;
    private LinearLayoutManager layoutManager;
    private TextView textViewDisplayName,textViewWelcomUser;
    private LinearLayout headlineLayout;
    private ProgressBar progressBar;
    private BoardingPresenter boardingPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textViewWelcomUser=findViewById(R.id.welcomeMessage);
        textViewDisplayName=findViewById(R.id.displayName);
        progressBar=findViewById(R.id.progressBar);
        boardingPresenter=new BoardingPresenter(this,getApplicationContext());
        headlineLayout=findViewById(R.id.headlineLayout);
        Bundle bundle=getIntent().getExtras();
        boardingPresenter.getDataForActivity(bundle);
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_welcome_boarding;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public void setUserName(UserInfo userInfo) {
        textViewDisplayName.setText(userInfo.getName());//present the user name
    }

    @Override
    public void setWelcomeMessage() {
        textViewWelcomUser.setText("אני כל כך שמחה לראותך כאן!");
    }

    @Override
    public void setReturnMessage() {
        textViewWelcomUser.setText("איזה כיף שחזרת!");
    }

    @Override
    public void reloadAdapter(List<BoardingModel> boardingModelList) {
        boardingRecView = findViewById(R.id.boardingRecycleView);
        layoutManager = new LinearLayoutManager(this);
        boardingRecView.setLayoutManager(layoutManager);
        boardingRecView.setHasFixedSize(true);
        boardingAdapter = new BoardingAdapter(this, boardingModelList);
        boardingRecView.setAdapter(boardingAdapter);
        boardingRecView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setProgressBar() {
        progressBar.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void setLayoutVisibility(Boolean visible) {
        if(visible)
            headlineLayout.setVisibility(View.VISIBLE);
        else
            headlineLayout.setVisibility(View.GONE);

    }
}