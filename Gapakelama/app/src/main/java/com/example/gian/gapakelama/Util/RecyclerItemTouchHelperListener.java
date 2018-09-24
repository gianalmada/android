package com.example.gian.gapakelama.Util;

import android.support.v7.widget.RecyclerView;

/**
 * Created by gian on 23/09/2018.
 */

public interface RecyclerItemTouchHelperListener {

    void onSwiped(RecyclerView.ViewHolder viewHolder,int direction, int position);
}
