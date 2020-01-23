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

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myListApp.mylist.Adapter.ItemViewAdapter;
import com.myListApp.mylist.Enum.EnumLayoutType;
import com.myListApp.mylist.presenter.MyListActivityPresenter;

import java.util.List;


public class MyListActivity extends BaseActivity implements MyListActivityPresenter.View {
    private RecyclerView mrecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ItemViewAdapter itemViewAdapter;
    LinearLayout recycleViewEmpty;
    private EditText mAddItem;
    private MyListActivityPresenter myListPresenter;
    private AutoCompleteTextView autoCompleteTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recycleViewEmpty = findViewById(R.id.recycleViewEmpty);
        myListPresenter=new MyListActivityPresenter(this,getApplicationContext());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_my_list;
    }


    private void initRecyclerView() {
        mrecyclerView = findViewById(R.id.recycleView);
        layoutManager = new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(layoutManager);
        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));//divide the item in the list
        if (BoardingActivity.itemModelArray.isEmpty()) {
            recycleViewEmpty.setVisibility(View.VISIBLE);
            mrecyclerView.setVisibility(View.GONE);
        }
        itemViewAdapter = new ItemViewAdapter(MyListActivity.this, BoardingActivity.itemModelArray, EnumLayoutType.MY_LIST);
        mrecyclerView.setAdapter(itemViewAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteItem(itemViewAdapter));//Attach ItemTouchHelper to the Recyclerview
        itemTouchHelper.attachToRecyclerView(mrecyclerView);
    }


    public void ShowAddItemPopup(View v) {
        View viewDialog = getLayoutInflater().inflate(R.layout.dialog_add_popup, null);
        AlertDialog mAddItemDialog = new AlertDialog.Builder(this).setView(viewDialog).create();
        TextView txtclose;
        Button btnAddItem;

        //Dismiss the dialog
        txtclose = (TextView) viewDialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(v1 -> mAddItemDialog.dismiss());
        autoCompleteTextView=viewDialog.findViewById(R.id.item);
        myListPresenter.autocompleteItemsValue();
        //add item to recycler view
        btnAddItem = (Button) viewDialog.findViewById(R.id.addItem);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddItem = viewDialog.findViewById(R.id.item);
                String item = mAddItem.getText().toString();
                myListPresenter.checkItem(item);
            }
        });

        mAddItemDialog.show();
    }

    public void ShowPurchaseDialog(View v) {
        if (BoardingActivity.itemModelArray.isEmpty())
            Toast.makeText(getApplicationContext(), "הרשימה שלך ריקה," + "\n" + "אין לך מה לעדכן כרגע", Toast.LENGTH_LONG).show();
        else {
            Intent intent = new Intent(v.getContext(), UpdateListActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        myListPresenter.updateDataBase();
        super.onPause();
    }

    @Override
    protected void onResume() {
        initRecyclerView();
        super.onResume();
    }

    public void comparePrice(View view) {
        Intent intent = new Intent(view.getContext(), ComparePriceActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();//go for the previous activity in the stack
        startActivity(new Intent(this, BoardingActivity.class));
    }

    @Override
    public void setToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addItemToList() {
        recycleViewEmpty.setVisibility(View.GONE);
        mrecyclerView.setVisibility(View.VISIBLE);
        itemViewAdapter.notifyDataSetChanged();
        mAddItem.setText("");
        setToastMessage("הפריט נוסף בהצלחה!");
    }


     /*
    add autocomplete to edit text
     */
     @Override
    public void addAutocompleteTextView(List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, list);
        autoCompleteTextView.setThreshold(1);//will start working from first character
        autoCompleteTextView.setAdapter(adapter);
    }

}