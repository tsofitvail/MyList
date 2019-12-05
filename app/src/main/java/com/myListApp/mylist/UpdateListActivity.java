package com.myListApp.mylist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.DatePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.myListApp.mylist.Adapter.BoardingAdapter;
import com.myListApp.mylist.Adapter.ItemViewAdapter;
import com.myListApp.mylist.Enum.EnumLayoutType;
import com.myListApp.mylist.Firebase.UserOperation;
import com.myListApp.mylist.Models.ArchiveItemModel;
import com.myListApp.mylist.Models.ItemModel;
import com.myListApp.mylist.SQLite.AppDatabase;
import com.myListApp.mylist.SQLite.ArchiveItemListDao;
import com.myListApp.mylist.SQLite.ItemListDao;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class UpdateListActivity extends BaseActivity {

    private TextView txtplace,txtdate;
    private RecyclerView mrecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ItemViewAdapter itemViewAdapter;
    //public static List<ArchiveItemModel> =new ArrayList<ArchiveItemModel>();
    private ItemListDao itemListDao;
    private ArchiveItemListDao archiveItemListDao;
    private String place,date;
    private AlertDialog purchaseDialog;
    private AutoCompleteTextView editTextPlace;
    private DatePicker datePicker;
    private UserOperation userOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_update_list);
        archiveItemListDao= AppDatabase.getInstance(this).archiveItemListDao();
        itemListDao=AppDatabase.getInstance(this).itemListDao();
        txtplace=findViewById(R.id.place);
        txtdate=findViewById(R.id.date);
        userOperation=UserOperation.getInstance();
        ShowPurchaseDialog();

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_update_list;
    }


    private void initRecyclerView() {
        mrecyclerView=findViewById(R.id.recycleView);
        layoutManager=new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(layoutManager);
        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));//divide the item in the list

        ((SimpleItemAnimator) mrecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);// Removes blinks
        itemViewAdapter = new ItemViewAdapter(UpdateListActivity.this, BoardingActivity.itemModelArray, EnumLayoutType.UPDATE_LIST);
        mrecyclerView.setAdapter(itemViewAdapter);
    }

    /* insert the items with price to the archive.
     *remove from list the items with price.
    * open pop up dialog that the list save successfully. */
    public void saveAndUpdateList(View v) throws IOException {
        Iterator<ItemModel> iterator=BoardingActivity.itemModelArray.iterator();
        ItemModel itmodel;
        while (iterator.hasNext()){
            itmodel=iterator.next();
            if(itmodel.getItemPrice()!=0){
                BoardingActivity.archiveItemArray.add(new ArchiveItemModel(itmodel.getItemName(),itmodel.getItemPrice(),place,date,itmodel.getAmount(),itmodel.getWeight(),itmodel.getBrand()));
               // MyListActivity.itemModelArray.remove(itmodel);
                //Removes from the itemModelArray element returned by this iterator
                iterator.remove();
            }
        }
        //insert to archive
        archiveItemListDao.insertAll( BoardingActivity.archiveItemArray);
        userOperation.addToArchiveFirebase( BoardingActivity.archiveItemArray);
        //update the db with the new list
        itemListDao.deleteAll();
        itemListDao.insertAll(BoardingActivity.itemModelArray);
        userOperation.setUserListToFirebase(BoardingActivity.itemModelArray);
        ShowSuccessDialog(v);
    }

    /*
    show success dialog after insert details to data base
     */
    private void ShowSuccessDialog(View v) {
        View viewDialog=getLayoutInflater().inflate(R.layout.dialog_list_update,null);
        Button backtoList=viewDialog.findViewById(R.id.backtoList);
        backtoList.setOnClickListener(v1 -> {
            Intent intent=new Intent(v1.getContext(), BoardingActivity.class);
            startActivity(intent);
        });
        AlertDialog successDialog = new AlertDialog.Builder(this)
                .setView(viewDialog)
                .create();
        successDialog.show();
    }

    public void ShowPurchaseDialog(){
        View viewDialog=getLayoutInflater().inflate(R.layout.dialog_purchase,null);
        Button addPriceBtn=viewDialog.findViewById(R.id.addPriceBtn);
        editTextPlace=(AutoCompleteTextView)viewDialog.findViewById(R.id.place);
        AddAutocompleteTextView(editTextPlace, archiveItemListDao.getDistinctPlaces());
        datePicker=viewDialog.findViewById(R.id.datePicker);
        datePicker.setMaxDate(System.currentTimeMillis());//disable future date
        addPriceBtn.setOnClickListener(v1 -> {
        //    hideKeyboard();
            GetPlaceAndDateValue();
        });

        purchaseDialog = new AlertDialog.Builder(this)
                .setView(viewDialog)
                .create();

        purchaseDialog.setOnCancelListener(dialog -> {
            UpdateListActivity.this.finish();
        });
        purchaseDialog.show();
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /*
    get place and date of purchase from user
     */
    private void GetPlaceAndDateValue() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        GregorianCalendar d = new GregorianCalendar(year, month, day);

        date = dateFormatter.format(d.getTime());
        txtdate.setText("תאריך הקניה: " + date);

        place = editTextPlace.getText().toString();
        txtplace.setText(place + ":מקום קניה ");

        purchaseDialog.dismiss();

        initRecyclerView();//add recycler view
    }

    /*add autocomplete to edit text*/
    private void AddAutocompleteTextView(AutoCompleteTextView autoCompleteTextView, List<String> list) {
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.select_dialog_item,list);
        autoCompleteTextView.setThreshold(1);//will start working from first character
        autoCompleteTextView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,BoardingActivity.class));
    }

}
