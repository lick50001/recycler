package com.example.recycler_view_kazakov;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycler_view_kazakov.R;
import com.example.recycler_view_kazakov.BasketAdapter;
import com.example.recycler_view_kazakov.MainActivity;
import com.example.recycler_view_kazakov.iOnClickInterface;

public class BasketActivity extends AppCompatActivity {

    public RecyclerView BasketRV;
    public TextView tvSum, tvAllSum;
    BasketAdapter BasketAdapter;
    Context Context;

    public iOnClickInterface Delete = new iOnClickInterface() {
        @Override
        public void setClick(View view, int position) {
            MainActivity.init.BasketList.remove(position);
            BasketAdapter.notifyItemRemoved(position);
            BasketAdapter.notifyItemRangeChanged(position, BasketAdapter.getItemCount());
            CostCalculator();
        }
    };

    public iOnClickInterface EventCost = new com.example.recycler_view_kazakov.iOnClickInterface() {
        @Override
        public void setClick(View view, int position) {
            CostCalculator();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_basket);

        Context = this;
        BasketRV = findViewById(R.id.basket_list);
        tvSum = findViewById(R.id.tv_sum);
        tvAllSum = findViewById(R.id.tv_all_sum);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(SwipeAdapter);
        itemTouchHelper.attachToRecyclerView(BasketRV);

        BasketAdapter = new BasketAdapter(this, MainActivity.init.BasketList, Delete, EventCost);
        BasketRV.setAdapter(BasketAdapter);

        CostCalculator();
    }

    ItemTouchHelper.SimpleCallback SwipeAdapter = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int position = viewHolder.getAdapterPosition();
            if (swipeDir == ItemTouchHelper.LEFT) {
                // свайп влево — удалить товар
                MainActivity.init.BasketList.remove(position);
                BasketAdapter.notifyItemRemoved(position);
                BasketAdapter.notifyItemRangeChanged(position, BasketAdapter.getItemCount());
                CostCalculator();
            } else {
                // свайп вправо — просто вернуть карточку на место
                BasketAdapter.notifyItemChanged(position);
            }
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder) {
            // сбросить трансляцию карточки когда свайп завершён
            View cardView = viewHolder.itemView.findViewById(R.id.parent);
            if (cardView != null) cardView.setTranslationX(0);
            super.clearView(recyclerView, viewHolder);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {

            View cardView = viewHolder.itemView.findViewById(R.id.parent);
            LinearLayout btnDelete = viewHolder.itemView.findViewById(R.id.ll_delete);
            LinearLayout btnCount = viewHolder.itemView.findViewById(R.id.ll_count);

            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                if (dX < 0) {
                    // свайп влево — показать удаление справа
                    btnDelete.setVisibility(View.VISIBLE);
                    btnCount.setVisibility(View.GONE);
                } else if (dX > 0) {
                    // свайп вправо — показать счётчик слева
                    btnDelete.setVisibility(View.GONE);
                    btnCount.setVisibility(View.VISIBLE);
                } else {
                    btnDelete.setVisibility(View.GONE);
                    btnCount.setVisibility(View.GONE);
                }
                // двигаем только саму карточку, панели остаются на месте
                if (cardView != null) cardView.setTranslationX(dX);
                // super НЕ вызываем, чтобы не двигался весь itemView
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
    };

    public void CostCalculator() {
        float ItemPrice = 0;
        for (Basket Item : MainActivity.init.BasketList) {
            ItemPrice += Item.Product.Price * Item.Count;
        }
        tvSum.setText("₽" + ItemPrice);
        ItemPrice += 60.20f;
        tvAllSum.setText("₽" + ItemPrice);
    }

    public void ClosePopularActivity(View view) {
        finish();
    }
}