package com.example.gian.gapakelama.ListMeja;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gian.gapakelama.R;

import java.util.List;

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
    public MejaAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.meja_layout, null);
        MejaAdapter.ProductViewHolder holder = new MejaAdapter().ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MejaAdapter.ProductViewHolder holder, int position) {
            final Meja meja = mejaList.get(position);

            holder.nomeja.setText(meja.getNomeja());
            if(meja.getStatus() == "true"){
                holder.status.setText("[Aktif]");
                holder.cardView.setCardBackgroundColor(R.color.endblue);
            } else {
                holder.status.setText("[Kosong]");
                holder.cardView.setCardBackgroundColor(R.color.colorhint);
            }

            holder.user.setText(meja.getId_user());
            holder.time.setText(meja.getStarttime());

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

            nomeja = itemView.findViewById(R.id.nomeja);
            status = itemView.findViewById(R.id.statusmeja);
            user = itemView.findViewById(R.id.usermeja);
            time = itemView.findViewById(R.id.startmeja);

            cardView = itemView.findViewById(R.id.cardMeja);

        }
    }
}
