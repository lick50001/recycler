package com.example.recycler_view_klimov;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {

    public iOnClickInterface Delete, Cost;
    public LayoutInflater Inflater;
    public ArrayList<Basket> BasketItems;

    public BasketAdapter(Context context, ArrayList<Basket> basketItems,
                         iOnClickInterface delete, iOnClickInterface cost) {
        this.Inflater = LayoutInflater.from(context);
        this.BasketItems = basketItems;
        this.Delete = delete;
        this.Cost = cost;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = Inflater.inflate(R.layout.item_basket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Basket item = BasketItems.get(position);

        holder.tvName.setText(item.Product.Name);
        holder.tvPrice.setText("₽" + item.Product.Price);
        holder.tvCount.setText(String.valueOf(item.Count));

        holder.btnPlus.setOnClickListener(view -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_ID) return;
            BasketItems.get(pos).Count++;
            holder.tvCount.setText(String.valueOf(BasketItems.get(pos).Count));
            Cost.setClick(view, pos);
        });

        holder.btnMinus.setOnClickListener(view -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_ID) return;
            Basket current = BasketItems.get(pos);
            if (current.Count > 1) {
                current.Count--;
                holder.tvCount.setText(String.valueOf(current.Count));
                Cost.setClick(view, pos);
            }
        });

        holder.btnDelete.setOnClickListener(view -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_ID) return;
            Delete.setClick(view, pos);
            Cost.setClick(view, pos);
        });
    }

    @Override
    public int getItemCount() {
        return BasketItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvPrice, tvCount;
        public ImageView btnPlus, btnMinus;
        public LinearLayout btnDelete, llCount;

        ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvPrice = view.findViewById(R.id.tv_price);
            tvCount = view.findViewById(R.id.tv_count);
            btnPlus = view.findViewById(R.id.btnPlus);
            btnMinus = view.findViewById(R.id.btnMinus);
            btnDelete = view.findViewById(R.id.ll_delete);
            llCount = view.findViewById(R.id.ll_count);
        }
    }
}