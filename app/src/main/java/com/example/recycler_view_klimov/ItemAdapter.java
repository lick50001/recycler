package com.example.recycler_view_klimov;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.VerifiedInputEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    public iOnClickInterface AddBasket;
    private LayoutInflater Inflater;
    private List<Item> Items;

    ItemAdapter(Context context, List<Item> items, iOnClickInterface addBasket) {
        this.Inflater = LayoutInflater.from(context);
        this.Items = items;
        this.AddBasket = addBasket;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = Inflater.inflate(R.layout.item_card, parent, false);
        return new ItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        Item Item = Items.get(position);
        holder.TvName.setText(Item.Name);
        holder.TvModel.setText(Item.Model);
        holder.TvPrice.setText("₽" + String.valueOf(Item.Price));

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddBasket.setClick(view, Item.Id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView TvName, TvModel, TvPrice;
        public LinearLayout btnAdd;

        ViewHolder(View view) {
            super(view);
            TvName = view.findViewById(R.id.tv_name);
            TvModel = view.findViewById(R.id.tv_model);
            TvPrice = view.findViewById(R.id.tv_price);

            btnAdd = view.findViewById(R.id.btnAdd);
        }
    }
}
