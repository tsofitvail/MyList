package com.myListApp.mylist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myListApp.mylist.Adapter.BoardingAdapter;
import com.myListApp.mylist.Adapter.ItemViewAdapter;
import com.myListApp.mylist.Enum.EnumLayoutType;
import com.myListApp.mylist.Models.BoardingModel;
import com.myListApp.mylist.SQLite.AppDatabase;
import com.myListApp.mylist.SQLite.ItemListDao;

import java.util.ArrayList;
import java.util.List;

public class BoardingActivity extends BaseActivity {

    private RecyclerView boardingRecView;
    private BoardingAdapter boardingAdapter;
    private List<BoardingModel>  boardingModelList= new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private TextView textViewDisplayName,textViewWelcomUser;
    private LinearLayout headlineLayout;
    private ItemListDao itemListDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_welcome_boarding);
        itemListDao= AppDatabase.getInstance(this).itemListDao();
        if(MyListActivity.itemModelArray.isEmpty())
            MyListActivity.itemModelArray=itemListDao.getAll();//get item of the list

        boardingRecView=findViewById(R.id.boardingRecycleView);
        headlineLayout=findViewById(R.id.headlineLayout);
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
            if(isSignUp==1)//present welcom message depend if the user is already log in
                textViewWelcomUser.setText("אני כל כך שמחה לראותך כאן!");
             else
                 textViewWelcomUser.setText("איזה כיף שחזרת!");
        }
        else{headlineLayout.setVisibility(View.GONE);}



        layoutManager = new LinearLayoutManager(this);
        boardingRecView.setLayoutManager(layoutManager);
        boardingRecView.setHasFixedSize(true);
        boardingModelList.add(new BoardingModel(R.drawable.ic_basket,"יצירת רשימה קניות","הוסיפו פריטים לרשימה כך שלכל מקום שתלכו,לא תשכחו מה צריך לקנות",MyListActivity.class.getName()));
        boardingModelList.add(new BoardingModel(R.drawable.ic_update_details,"עדכון הפריטים ברשימה","הוסיפו לכל פריט שקניתם את מחירו,מותג ומאפיינים נוספים שישמשו להשוואה עתידית",UpdateListActivity.class.getName()));
        boardingModelList.add(new BoardingModel(R.drawable.ic_compare_price,"השוו מחיר מוצר","השוו מחיר של כל מוצר שקניתם וכך תוכלו לבצע קניה חכמה יותר",ComparePriceActivity.class.getName()));
        boardingAdapter=new BoardingAdapter(this,boardingModelList);
        boardingRecView.setAdapter(boardingAdapter);
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
}
