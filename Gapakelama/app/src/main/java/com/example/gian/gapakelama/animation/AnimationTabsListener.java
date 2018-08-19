package com.example.gian.gapakelama.animation;

import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TabHost;

/**
 * Created by gian on 24/02/2018.
 */

public class AnimationTabsListener implements TabHost.OnTabChangeListener {

    private Context context;
    private TabHost tabHost;
    private View previousView,currentView;
    private int currentTab;

    public AnimationTabsListener(TabHost tabHost){
        this.tabHost = tabHost;
        this.previousView = tabHost.getCurrentView();
    }

    @Override
    public void onTabChanged(String s) {
            currentView = tabHost.getCurrentView();
            if(tabHost.getCurrentTab() > currentTab){
                previousView.setAnimation(outToLeftAnimation());
                currentView.setAnimation(inFromRightAnimation());
            }
            else {
                previousView.setAnimation(outToRightAnimation());
                currentView.setAnimation(inFromLeftAnimation());
        }

    }

    private Animation outToLeftAnimation() {
        Animation outToLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                -1.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f);
        return  setProperties(outToLeft);
    }

    private Animation outToRightAnimation() {
        Animation OutToRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                1.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f);
        return  setProperties(OutToRight);
    }

    private Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                -1.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f);
        return  setProperties(inFromLeft);
    }

    private Animation inFromRightAnimation() {
        Animation inFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                1.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f,
                Animation.RELATIVE_TO_PARENT,
                0.0f);
        return  setProperties(inFromRight);
    }

    private Animation setProperties(Animation animation){
        animation.setDuration((long) 5.0f);
        animation.setInterpolator(new AccelerateInterpolator());
        return animation;
    }
}
