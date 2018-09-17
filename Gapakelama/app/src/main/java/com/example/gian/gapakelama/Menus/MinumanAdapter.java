package com.example.gian.gapakelama.Menus;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gian.gapakelama.R;

import java.util.List;

/**
 * Created by gian on 04/09/2018.
 */

public class MinumanAdapter extends RecyclerView.Adapter<MinumanAdapter.ProductViewHolder> {

    private Context mCtx ;
    private List<Minuman> productList2;

    public MinumanAdapter(Context mCtx, List<Minuman> productList2) {
        this.mCtx = mCtx;
        this.productList2 = productList2;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.tab_minuman_layout, null);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Minuman product = productList2.get(position);

        holder.textViewNama.setText(product.getNama());
        holder.textViewHarga.setText("Rp."+String.valueOf(product.getHarga()));

        Glide.with(mCtx).load(product.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return productList2.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textViewNama, textViewHarga;

        public ProductViewHolder(View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.image_minuman);
            textViewNama = itemView.findViewById(R.id.nama_minuman);
            textViewHarga = itemView.findViewById(R.id.harga_minuman);

        }

    }
}
