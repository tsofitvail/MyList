package com.myListApp.mylist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myListApp.mylist.Adapter.ItemViewAdapter;
import com.myListApp.mylist.Enum.EnumLayoutType;
import com.myListApp.mylist.Firebase.UserInfo;
import com.myListApp.mylist.Firebase.UserOperation;
import com.myListApp.mylist.Models.ItemModel;
import com.myListApp.mylist.SQLite.AppDatabase;
import com.myListApp.mylist.SQLite.ArchiveItemListDao;
import com.myListApp.mylist.SQLite.ItemListDao;

import java.util.ArrayList;
import java.util.List;


public class MyListActivity extends BaseActivity {
    private RecyclerView mrecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ItemViewAdapter itemViewAdapter;
    private ItemListDao itemListDao;
    private ArchiveItemListDao archiveItemModel;
    LinearLayout recycleViewEmpty;
    private DatabaseReference dbReference;
    private UserOperation userOperation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recycleViewEmpty=findViewById(R.id.recycleViewEmpty);
        itemListDao=AppDatabase.getInstance(this).itemListDao();
        archiveItemModel=AppDatabase.getInstance(this).archiveItemListDao();
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            String myName=bundle.getString("DISPLAY_NAME");
            Toast.makeText(this,"ברוך הבא "+myName,Toast.LENGTH_LONG).show();
        }
        userOperation=UserOperation.getInstance();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_my_list;
    }


    private void initRecyclerView() {
        mrecyclerView=findViewById(R.id.recycleView);
        layoutManager=new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(layoutManager);
        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));//divide the item in the list
        if(BoardingActivity.itemModelArray.isEmpty()){
            recycleViewEmpty.setVisibility(View.VISIBLE);
            mrecyclerView.setVisibility(View.GONE);
        }
        itemViewAdapter = new ItemViewAdapter(MyListActivity.this,BoardingActivity.itemModelArray, EnumLayoutType.MY_LIST);
        mrecyclerView.setAdapter(itemViewAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteItem(itemViewAdapter));//Attach ItemTouchHelper to the Recyclerview
        itemTouchHelper.attachToRecyclerView(mrecyclerView);
    }


    public void ShowAddItemPopup(View v) {
        View viewDialog=getLayoutInflater().inflate(R.layout.dialog_add_popup,null);
        AlertDialog mAddItemDialog = new AlertDialog.Builder(this)
                .setView(viewDialog)
                .create();
        TextView txtclose;
        Button btnAddItem;

        //Dismiss the dialog
        txtclose =(TextView) viewDialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(v1 -> mAddItemDialog.dismiss());

        AddAutocompleteTextView(viewDialog.findViewById(R.id.item),archiveItemModel.getDistinctNames());
        //add item to recycler view
        btnAddItem = (Button) viewDialog.findViewById(R.id.addItem);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mAddItem = viewDialog.findViewById(R.id.item);
                String item = mAddItem.getText().toString();
                if (!item.equals("")) {
                    Boolean itemExist = false;
                    for (int i = 0; i < BoardingActivity.itemModelArray.size(); i++) {
                        if (BoardingActivity.itemModelArray.get(i).getItemName().equals(item)) {
                            Toast.makeText(getApplicationContext(), "הפריט שרשמת כבר נמצא ברשימה", Toast.LENGTH_SHORT).show();
                            itemExist = true;
                            break;
                        }
                    }
                    if (!itemExist) {
                        recycleViewEmpty.setVisibility(View.GONE);
                        mrecyclerView.setVisibility(View.VISIBLE);
                        BoardingActivity.itemModelArray.add(new ItemModel(item));
                        itemViewAdapter.notifyDataSetChanged();
                        mAddItem.setText("");
                        Toast.makeText(getApplicationContext(), "הפריט נוסף בהצלחה!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{Toast.makeText(getApplicationContext(), "אינך יכול להוסיף פריט ריק", Toast.LENGTH_SHORT).show(); }

            }
        });

        mAddItemDialog.show();
    }

    /*
    add autocomplete to edit text
     */
    private void AddAutocompleteTextView(AutoCompleteTextView autoCompleteTextView,List<String> list) {
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.select_dialog_item,list);
        autoCompleteTextView.setThreshold(1);//will start working from first character
        autoCompleteTextView.setAdapter(adapter);
    }

    public void ShowPurchaseDialog(View v){
        if(BoardingActivity.itemModelArray.isEmpty())
            Toast.makeText(getApplicationContext(),"הרשימה שלך ריקה,"+"\n"+"אין לך מה לעדכן כרגע",Toast.LENGTH_LONG).show();
        else{
            Intent intent=new Intent(v.getContext(), UpdateListActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onPause() {
        itemListDao.deleteAll();
        itemListDao.insertAll(BoardingActivity.itemModelArray);
        if(!BoardingActivity.itemModelArray.isEmpty())
            userOperation.setUserListToFirebase(BoardingActivity.itemModelArray);
        super.onPause();
    }

    @Override
    protected void onResume() {
        initRecyclerView();
        super.onResume();
    }

    public void comparePrice(View view) {
        Intent intent=new Intent(view.getContext(),ComparePriceActivity.class);
        startActivity(intent);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();//go for the previous activity in the stack
        startActivity(new Intent(this,BoardingActivity.class));
    }
}
