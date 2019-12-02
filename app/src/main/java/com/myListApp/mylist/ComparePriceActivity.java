package com.myListApp.mylist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myListApp.mylist.Adapter.ItemViewAdapter;
import com.myListApp.mylist.Enum.EnumLayoutType;
import com.myListApp.mylist.Models.ArchiveItemModel;
import com.myListApp.mylist.SQLite.AppDatabase;
import com.myListApp.mylist.SQLite.ArchiveItemListDao;

import java.util.List;

public class ComparePriceActivity extends BaseActivity {

    private RecyclerView mrecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ItemViewAdapter itemViewAdapter;
    private List<ArchiveItemModel> detailsItemList;
    private ArchiveItemListDao archiveItemListDao;
    private Spinner itemSpinner;
    private String chosenItemSpinner;
    private List<String> items;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_compare_price);
        final HorizontalScrollView scrollView=(HorizontalScrollView)findViewById(R.id.horizontalScrollView);
        scrollView.postDelayed(new Runnable() {
            public void run() {
                scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 100L);

        archiveItemListDao=AppDatabase.getInstance(this).archiveItemListDao();
        fillSpinner();
        initRecyclerView();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_compare_price;
    }

    private void fillSpinner() {
        itemSpinner=findViewById(R.id.itemSpinner);
        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                      chosenItemSpinner = (String) parent.getItemAtPosition(position);
                                                      addDetailsToAdapter();
                                                  }
          @Override
           public void onNothingSelected(AdapterView<?> parent) {

                                                  }});
        //get a list of items for the spinner.
        items=archiveItemListDao.getDistinctNames();
        chosenItemSpinner=items.get(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        itemSpinner.setAdapter(adapter);
    }


    private void initRecyclerView() {
        mrecyclerView=findViewById(R.id.compareRecycleView);
        layoutManager=new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(layoutManager);
        mrecyclerView.setHasFixedSize(true);
        //divide the item in the list
        mrecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        addDetailsToAdapter();

    }

    /*
    get details of specific item from db and present then in adapter
     */
    private void addDetailsToAdapter() {
        detailsItemList=archiveItemListDao.getByName(chosenItemSpinner);
        itemViewAdapter = new ItemViewAdapter(ComparePriceActivity.this,detailsItemList, EnumLayoutType.COMPARE_PRICE_LIST);
        mrecyclerView.setAdapter(itemViewAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,BoardingActivity.class));
    }
}
