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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.myListApp.mylist.Adapter.ItemViewAdapter;
import com.myListApp.mylist.Enum.EnumLayoutType;
import com.myListApp.mylist.Models.ItemModel;
import com.myListApp.mylist.SQLite.AppDatabase;
import com.myListApp.mylist.SQLite.ArchiveItemListDao;
import com.myListApp.mylist.SQLite.ItemListDao;

import java.util.ArrayList;
import java.util.List;


public class MyListActivity extends BaseActivity {

   // private Dialog mAddItemDialog;

    private RecyclerView mrecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ItemViewAdapter itemViewAdapter;
    public static List<ItemModel> itemModelArray=new ArrayList<ItemModel>();
    private ItemListDao itemListDao;
    private ArchiveItemListDao archiveItemModel;
    DatePicker datePicker;
    AutoCompleteTextView place;
    LinearLayout recycleViewEmpty;
    private AutoCompleteTextView autoItemsTextView;
    //private List<ItemModel> cachedItemList=new ArrayList<ItemModel>();//Room gets only List or array



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_my_list);
      //  mAddItemDialog=new Dialog(this);
        recycleViewEmpty=findViewById(R.id.recycleViewEmpty);
       // itemModelArray=new ArrayList<ItemModel>();
        itemListDao=AppDatabase.getInstance(this).itemListDao();
        archiveItemModel=AppDatabase.getInstance(this).archiveItemListDao();
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            String myName=bundle.getString("DISPLAY_NAME");
            Toast.makeText(this,"ברוך הבא "+myName,Toast.LENGTH_LONG).show();
        }
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
        //divide the item in the list
        mrecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        itemModelArray=itemListDao.getAll();
        if(itemModelArray.isEmpty()){
            recycleViewEmpty.setVisibility(View.VISIBLE);
            mrecyclerView.setVisibility(View.GONE);
        }

        itemViewAdapter = new ItemViewAdapter(MyListActivity.this,itemModelArray, EnumLayoutType.MY_LIST);
        mrecyclerView.setAdapter(itemViewAdapter);
        //Attach ItemTouchHelper to the Recyclerview
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteItem(itemViewAdapter));
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
                    for (int i = 0; i < itemModelArray.size(); i++) {
                        if (itemModelArray.get(i).getItemName().equals(item)) {
                            Toast.makeText(getApplicationContext(), "הפריט שרשמת כבר נמצא ברשימה", Toast.LENGTH_SHORT).show();
                            itemExist = true;
                            break;
                        }
                    }
                    if (!itemExist) {
                        recycleViewEmpty.setVisibility(View.GONE);
                        mrecyclerView.setVisibility(View.VISIBLE);
                        itemModelArray.add(new ItemModel(item));
                        itemViewAdapter.notifyDataSetChanged();
                        mAddItem.setText("");
                        //create snack bar with the new item
                        // createSnackbar(v,item);
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
       // autoCompleteTextView=d.findViewById(R.id.item);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.select_dialog_item,list);
        autoCompleteTextView.setThreshold(1);//will start working from first character
        autoCompleteTextView.setAdapter(adapter);
    }

    public void ShowPurchaseDialog(View v){
        if(MyListActivity.itemModelArray.isEmpty())
            Toast.makeText(getApplicationContext(),"הרשימה שלך ריקה,"+"\n"+"אין לך מה לעדכן כרגע",Toast.LENGTH_LONG).show();
        else{
            Intent intent=new Intent(v.getContext(), UpdateListActivity.class);
            startActivity(intent);
        }

      /*  View viewDialog=getLayoutInflater().inflate(R.layout.dialog_purchase,null);
        Button addPriceBtn=viewDialog.findViewById(R.id.addPriceBtn);
        place=viewDialog.findViewById(R.id.place);

        AddAutocompleteTextView(place, archiveItemModel.getDistinctPlaces());

        datePicker=viewDialog.findViewById(R.id.datePicker);
        addPriceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

                GregorianCalendar d = new GregorianCalendar(year, month, day);
                String strDate = dateFormatter.format(d.getTime());
                Intent intent=new Intent(v.getContext(), UpdateListActivity.class);
                intent.putExtra("PLACE",place.getText().toString());
                intent.putExtra("DATE_OF_PURCHASE",strDate);
                startActivity(intent);

            }
        });
        AlertDialog purchaseDialog = new AlertDialog.Builder(this)
                .setView(viewDialog)
                .create();

        purchaseDialog.show();*/

    }

    private void createSnackbar(View v,String item) {
        // create instance
        item=item.concat(" נוסף בהצלחה!");
        Snackbar snackbar = Snackbar.make(v, item, Snackbar.LENGTH_LONG);

        // set text color
        snackbar.setActionTextColor(getResources().getColor(R.color.white));
        //set background color
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        //set text direction color
        snackbar.getView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        snackbar.show();

    }


    @Override
    protected void onPause() {
        itemListDao.deleteAll();
        itemListDao.insertAll(itemModelArray);
        Toast.makeText(getApplicationContext(), "onPauseCall", Toast.LENGTH_SHORT).show();
        super.onPause();
    }

    @Override
    protected void onResume() {
        Toast.makeText(getApplicationContext(), "onResumeCall", Toast.LENGTH_SHORT).show();
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
