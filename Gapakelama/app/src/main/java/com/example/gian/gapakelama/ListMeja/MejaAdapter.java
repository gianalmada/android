package com.example.gian.gapakelama.ListMeja;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gian.gapakelama.R;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by gian on 27/09/2018.
 */

public class MejaAdapter extends RecyclerView.Adapter<MejaAdapter.ProductViewHolder> {

    private Context mCtx ;
    private List<Meja> mejaList;

    public MejaAdapter(Context mCtx, List<Meja> mejaList) {
        this.mCtx = mCtx;
        this.mejaList = mejaList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.meja_layout, parent, false);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MejaAdapter.ProductViewHolder holder, int position) {
            final Meja meja = mejaList.get(position);

            holder.nomeja.setText(meja.getNomeja());
            Log.d(TAG, "onBindViewHolder: "+meja.getStatus());
            if(meja.getStatus().equals("true")){
                holder.status.setText("[Aktif]");
                holder.cardView.setCardBackgroundColor(holder.cardView.getContext().getResources().getColor(R.color.colorPrimary));
                holder.user.setText("User : "+meja.getId_user());
                holder.time.setText("Start : "+meja.getStarttime());
            } else {
                holder.status.setText("[Kosong]");
                holder.cardView.setCardBackgroundColor(holder.cardView.getContext().getResources().getColor(R.color.colorhint));
                holder.user.setText("-");
                holder.time.setText("-");
            }



    }

    @Override
    public int getItemCount() {
        return mejaList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView nomeja,status,user,time;
        CardView cardView;
        public ProductViewHolder(View itemView) {
            super(itemView);

            nomeja = (TextView) itemView.findViewById(R.id.no_mejas);
            status = (TextView) itemView.findViewById(R.id.statusmeja);
            user = (TextView) itemView.findViewById(R.id.usermeja);
            time = (TextView) itemView.findViewById(R.id.startmeja);

            cardView = (CardView) itemView.findViewById(R.id.cardMeja);

        }
    }
}
