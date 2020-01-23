package com.myListApp.mylist.Adapter;

import android.app.Activity;
import android.content.Context;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.myListApp.mylist.Enum.EnumLayoutType;
import com.myListApp.mylist.Models.ArchiveItemModel;
import com.myListApp.mylist.Models.ItemModel;
import com.myListApp.mylist.R;

import java.util.List;

public class ItemViewAdapter<T extends ItemModel> extends RecyclerView.Adapter<ItemViewAdapter.ItemViewHolder>{

    private LayoutInflater inflater;
    private List<T> itemModels;
    private EnumLayoutType layoutType;
    private T mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private View view;
    private Context context;

    public ItemViewAdapter(Context context, List<T> itemModelArrayAdapter,EnumLayoutType layoutType) {
        this.itemModels=itemModelArrayAdapter;
        this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.layoutType=layoutType;
        this.context=context;
    }

    public Context getContext() {
        return context;
    }

    public EnumLayoutType getLayoutType() {
        return layoutType;
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    public void setData(List<T> items) {
        itemModels.clear();
        itemModels.addAll(items);
        notifyDataSetChanged();//Notify any registered observers that the data set has changed.
    }

    /*
     inflate the view with item_list layout (without data)
     */
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutType.equals(EnumLayoutType.MY_LIST))
            view=inflater.inflate(R.layout.item_list,parent,false);
        if(layoutType.equals(EnumLayoutType.UPDATE_LIST))
            view = inflater.inflate(R.layout.item_edit_list, parent, false);
        if(layoutType.equals(EnumLayoutType.COMPARE_PRICE_LIST))
            view = inflater.inflate(R.layout.item_compare_price, parent, false);
        return new ItemViewHolder(view);
    }

    /*
    insert data to the view
     */
    @Override
    public void onBindViewHolder(@NonNull ItemViewAdapter.ItemViewHolder holder,int position) {
        T itemModel=itemModels.get(position);
        holder.onBindViewHolder(itemModel);
        if(layoutType.equals(EnumLayoutType.MY_LIST)){
            TextView itemTextView=holder.itemView.findViewById(R.id.itemName);
            //ImageView itemImageView=holder.itemView.findViewById(R.id.v_image_view);
            itemTextView.setOnClickListener(v -> {
                boolean isMarked=itemModel.isMarked();
                itemModel.setMarked(!isMarked);
                // Notify the adapter that item has changed
                ItemViewAdapter.this.notifyItemChanged(position);
            });
        }
        if(layoutType.equals(EnumLayoutType.UPDATE_LIST)) {
            ImageButton viewMoreBtn=holder.itemView.findViewById(R.id.viewMoreBtn);
            viewMoreBtn.setOnClickListener(v -> {
                        // Get the current state of the item
                        boolean expanded = itemModel.isExpended();
                        // Change the state
                         itemModel.setExpended(!expanded);
                        // Notify the adapter that item has changed
                        ItemViewAdapter.this.notifyItemChanged(position);

            });
        }
        if(layoutType.equals(EnumLayoutType.COMPARE_PRICE_LIST)){
            TextView t=holder.itemView.findViewById(R.id.itemPrice1);
            if(itemModels.size()>1) {
                if (position == 0)
                    t.setBackgroundColor(Color.parseColor("#02C39A"));
                if (position == itemModels.size() - 1)
                    t.setBackgroundColor(Color.parseColor("#F44336"));
            }
        }
    }

    /*
    Deleting item from recycler view
    save the removed item into a member variable to be used in case the user wants to undo the delete
     */
    public void deleteItem(int position) {
        mRecentlyDeletedItem = itemModels.get(position);
        mRecentlyDeletedItemPosition = position;
        itemModels.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    /*
    show the snack bar when the item delete
     */
    private void showUndoSnackbar() {
        Snackbar snackbar = Snackbar.make( ((Activity) context).findViewById(R.id.myList), context.getResources().getString(R.string.toast_itemSuccessfullyDeleted), Snackbar.LENGTH_SHORT);
        snackbar.setAction("בטל", v -> ItemViewAdapter.this.undoDelete());
        snackbar.show();
    }

    /*
    undo delete item
     */
    private void undoDelete() {
        itemModels.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public final TextView itemName;
        public TextView price,amount,dateOfPurchase,store,brand,weight;
        public EditText editTextPrice,editTextAmount,editTextPriceforKilo,editTextBrand;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemName=itemView.findViewById(R.id.itemName);
            if(layoutType.equals(EnumLayoutType.COMPARE_PRICE_LIST)){
                price=itemView.findViewById(R.id.itemPrice1);
                amount=itemView.findViewById(R.id.amount);
                dateOfPurchase=itemView.findViewById(R.id.dateOfPurchase);
                store=itemView.findViewById(R.id.store);
                brand=itemView.findViewById(R.id.brand);
                weight=itemView.findViewById(R.id.priceforKilo);

            }
            if(layoutType.equals(EnumLayoutType.UPDATE_LIST)) {
                editTextPrice = itemView.findViewById(R.id.itemPrice);
                editTextPrice.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        T itModel = itemModels.get(getAdapterPosition());
                        itModel.setItemPrice(Double.parseDouble(editTextPrice.getText().toString()));
                    }
                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                editTextAmount = itemView.findViewById(R.id.amount);
                editTextAmount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        ItemModel itModel = itemModels.get(getAdapterPosition());
                        itModel.setAmount(Double.parseDouble(editTextAmount.getText().toString()));
                    }
                    @Override
                    public void afterTextChanged(Editable s) { }
                });
                editTextPriceforKilo = itemView.findViewById(R.id.priceforKilo);
                editTextPriceforKilo.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        T itModel = itemModels.get(getAdapterPosition());
                        itModel.setWeight(Double.parseDouble(editTextPriceforKilo.getText().toString()));
                    }
                    @Override
                    public void afterTextChanged(Editable s) { }
                });
                editTextBrand=itemView.findViewById(R.id.brand);
                editTextBrand.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        T itModel=itemModels.get(getAdapterPosition());
                        itModel.setBrand(editTextBrand.getText().toString());
                    }
                    @Override
                    public void afterTextChanged(Editable s) { }});
            }
        }

        public void onBindViewHolder(T itemModel) {
            if((layoutType.equals(EnumLayoutType.UPDATE_LIST))||(layoutType.equals(EnumLayoutType.MY_LIST)))
            {
                itemName.setText(itemModel.getItemName());

            }
            if(layoutType.equals(EnumLayoutType.MY_LIST)){
                boolean marked=itemModel.isMarked();
                if(marked) {
                    //itemName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_complete, 0, 0, 0);
                    itemName.setTextColor(context.getResources().getColor(R.color.gray));
                    itemName.setBackgroundColor(context.getResources().getColor(R.color.lightGray));

                }
                else {
                    itemName.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    itemName.setBackgroundColor(context.getResources().getColor(R.color.white));
                   // itemName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

            }

            if(layoutType.equals(EnumLayoutType.UPDATE_LIST)){
                boolean expanded=itemModel.isExpended();
                View expandedLayout=itemView.findViewById(R.id.layoutExpand);
                expandedLayout.setVisibility(expanded? View.VISIBLE : View.GONE);
            }
            if(layoutType.equals(EnumLayoutType.COMPARE_PRICE_LIST)){
                dateOfPurchase.setText(((ArchiveItemModel)itemModel).getDateOfPurchase());
                store.setText(((ArchiveItemModel)itemModel).getStore());
                brand.setText(itemModel.getBrand());
                checkIfDecimal(itemModel.getItemPrice(),price);
                checkIfDecimal(itemModel.getAmount(),amount);
                checkIfDecimal(itemModel.getWeight(),weight);
            }
        }

        public void checkIfDecimal(double number,TextView textView){
            if(number==0)
                textView.setText("-");
            else {
                if (number - Math.floor(number) == 0)
                    textView.setText(String.valueOf(Math.round(number)));
                else
                    textView.setText(String.valueOf(number));
            }
        }
    }

}
