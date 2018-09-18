package com.example.gian.gapakelama.Menus;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.gian.gapakelama.R;

import java.util.List;

import customfonts.MyTextView;

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
    public void onBindViewHolder(final ProductViewHolder holder,final int position) {
        final Minuman product = productList2.get(position);

        holder.textViewNama.setText(product.getNama());
        holder.textViewHarga.setText("Rp."+String.valueOf(product.getHarga()));

        Glide.with(mCtx).load(product.getImage()).into(holder.imageView);

        holder.cardView.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                showAddToCartDialog(position);

                holder.cardView.setClickable(false);
            }
        });
    }

    private void showAddToCartDialog(int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        View itemView = LayoutInflater.from(mCtx)
                .inflate(R.layout.popup_add_cart, null);

        ImageView img_selected_menu = (ImageView)itemView.findViewById(R.id.img_menu_selected);
        MyTextView name_selected_menu = (MyTextView)itemView.findViewById(R.id.nama_menu_cart);
        MyTextView harga_selected_menu = (MyTextView)itemView.findViewById(R.id.harga_menu_cart);
        EditText catatan_menu = (EditText)itemView.findViewById(R.id.catatan);
        ElegantNumberButton set_qty = (ElegantNumberButton)itemView.findViewById(R.id.qty_menu_cart);

        Glide.with(mCtx).load(productList2.get(position).getImage()).into(img_selected_menu);

        name_selected_menu.setText(productList2.get(position).getNama());
        harga_selected_menu.setText("Rp."+String.valueOf(productList2.get(position).getHarga()));

        builder.setView(itemView);
        builder.setNegativeButton("Add to Cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Toast.makeText(mCtx, "Item Berhasil di Tambahkan",
                        Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
    }


    @Override
    public int getItemCount() {
        return productList2.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textViewNama, textViewHarga;
        CardView cardView;

        public ProductViewHolder(View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.image_minuman);
            textViewNama = itemView.findViewById(R.id.nama_minuman);
            textViewHarga = itemView.findViewById(R.id.harga_minuman);

            cardView = itemView.findViewById(R.id.card_view_minuman);

        }

    }
}
