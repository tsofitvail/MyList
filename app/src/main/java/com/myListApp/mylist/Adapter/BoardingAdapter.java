package com.myListApp.mylist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.myListApp.mylist.BoardingActivity;
import com.myListApp.mylist.Models.ArchiveItemModel;
import com.myListApp.mylist.Models.BoardingModel;
import com.myListApp.mylist.R;
import com.myListApp.mylist.SQLite.AppDatabase;

import java.util.List;

public class BoardingAdapter extends RecyclerView.Adapter<BoardingAdapter.BoardingViewHolder> {

    private LayoutInflater inflater;
    private List<BoardingModel> boardingModels;
    private View view;
    private Context context;

    public BoardingAdapter(Context context, List<BoardingModel> boardingModels) {
        this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.boardingModels = boardingModels;
        this.context=context;
    }

    @NonNull
    @Override
    public BoardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.item_welcome_boarding,parent,false);

        return new BoardingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardingViewHolder holder, int position) {
        holder.onBindViewHolder(boardingModels.get(position));
        LinearLayout itemLayout=holder.itemView.findViewById(R.id.itemLayout);

        itemLayout.setOnClickListener(v -> {
            Intent intent=new Intent();
            String  activity=boardingModels.get(position).getActivity();
            intent.setClassName(v.getContext(),activity);
            String message;
            if(activity.contains("UpdateListActivity")&& BoardingActivity.itemModelArray.isEmpty()) {
                message = context.getResources().getString(R.string.toast_message_yourListIsEmpty);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
            else {
                if (BoardingActivity.archiveItemArray.isEmpty() && activity.contains("ComparePriceActivity")) {
                    message = context.getResources().getString(R.string.toast_message_noItemToCompare);
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
                else{
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return boardingModels.size();
    }

    public class BoardingViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView,detailsTextView;
        private ImageView iconImageView;

        public BoardingViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView=itemView.findViewById(R.id.title);
            detailsTextView=itemView.findViewById(R.id.detail);
            iconImageView=itemView.findViewById(R.id.icon);
        }


        public void onBindViewHolder(BoardingModel boardingModel) {
            titleTextView.setText(boardingModel.getTitle());
            detailsTextView.setText(boardingModel.getDetails());
            iconImageView.setImageResource(boardingModel.getIcon());
        }
    }
}
