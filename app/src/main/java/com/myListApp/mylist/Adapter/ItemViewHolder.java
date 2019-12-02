package com.myListApp.mylist.Adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myListApp.mylist.Models.ItemModel;
import com.myListApp.mylist.R;


public class ItemViewHolder extends RecyclerView.ViewHolder {

    public final TextView itemName;
  //  public EditText editTextPrice;
  //  public CustomEditTextListener customEditTextListener;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemName=itemView.findViewById(R.id.itemName);

      //  this.editTextPrice=itemView.findViewById(R.id.itemPrice);
     //   this.customEditTextListener=customEditTextListener;
      //  this.editTextPrice.addTextChangedListener(customEditTextListener);


    }

    public void onBindViewHolder(final ItemModel itemModel) {
        itemName.setText(itemModel.getItemName());


    }

}
