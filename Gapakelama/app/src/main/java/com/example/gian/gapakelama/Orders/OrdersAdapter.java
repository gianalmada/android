package com.example.gian.gapakelama.Orders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gian.gapakelama.R;

import java.util.List;

/**
 * Created by gian on 16/09/2018.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ProductViewHolder> {
    private Context mCtx ;
    private List<Orders> ordersList;

    public OrdersAdapter(Context mCtx, List<Orders> ordersList) {
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

        Orders orders = ordersList.get(position);

        holder.textViewId.setText(orders.getId_order());
        holder.textViewNama.setText(orders.getNama_order());
        holder.textViewHarga.setText("Rp."+String.valueOf(orders.getHarga_order()));
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{

        TextView textViewId, textViewNama, textViewHarga;

        ImageButton deleteItem;



        public ProductViewHolder(View itemView){
            super(itemView);

            textViewId = itemView.findViewById(R.id.id_menu_order);
            textViewNama = itemView.findViewById(R.id.nama_menu_order);
            textViewHarga = itemView.findViewById(R.id.harga_makanan);
            deleteItem = itemView.findViewById(R.id.delete_item);
        }
    }
}
