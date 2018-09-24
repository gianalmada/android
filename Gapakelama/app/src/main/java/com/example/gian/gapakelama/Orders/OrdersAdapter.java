package com.example.gian.gapakelama.Orders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.gian.gapakelama.Database.Model.ModelDB.Cart;
import com.example.gian.gapakelama.R;

import java.util.List;

/**
 * Created by gian on 16/09/2018.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ProductViewHolder> {

    private Context mCtx ;
    private List<Cart> ordersList;
    private int position;

    public OrdersAdapter(Context mCtx, List<Cart> ordersList) {
        this.mCtx = mCtx;
        this.ordersList = ordersList;
    }

    @Override
    public OrdersAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.orders_layout, null);
        OrdersAdapter.ProductViewHolder holder = new OrdersAdapter.ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final OrdersAdapter.ProductViewHolder holder, final int position) {

        final Cart orders = ordersList.get(position);


        holder.textViewNama.setText(orders.nama_menu);

        holder.textViewHarga.setText(new StringBuilder("Rp").append(orders.harga_menu));

        holder.qty_order.setNumber(String.valueOf(orders.qty_menu));

        final double[] subtotal = {orders.qty_menu * orders.harga_menu};

        holder.qty_order.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                holder.qty_order.setNumber(String.valueOf(newValue));
                subtotal[0] = newValue * orders.harga_menu;
                holder.textViewSubTotal.setText(new StringBuilder("Rp").append(subtotal[0]));
            }
        });

        holder.textViewSubTotal.setText(new StringBuilder("Rp").append(subtotal[0]));

        holder.catatan_menu.setText(orders.catatan_menu);

    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        TextView textViewSubTotal, textViewNama, textViewHarga;

        ImageButton deleteItem;

        ElegantNumberButton qty_order;

        public RelativeLayout view_background;

        public LinearLayout view_foreground;

        EditText catatan_menu;

        public ProductViewHolder(View itemView){
            super(itemView);

            textViewSubTotal = itemView.findViewById(R.id.subtotal_menu_order);
            textViewNama = itemView.findViewById(R.id.nama_menu_order);
            textViewHarga = itemView.findViewById(R.id.harga_menu_order);
            qty_order = itemView.findViewById(R.id.qty_menu_order);

            view_background = (RelativeLayout)itemView.findViewById(R.id.view_background);
            view_foreground = (LinearLayout)itemView.findViewById(R.id.view_foreground);

            catatan_menu = itemView.findViewById(R.id.catatan_menu);
        }
    }

    public void removeItem(int position){
        ordersList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Cart cart,int position){
        ordersList.add(position,cart);
        notifyItemInserted(position);
    }
}
