package com.example.gian.gapakelama.Sign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.gian.gapakelama.R;

public class splash_activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread thread = new Thread(){
            public void run() {
                try{
                    sleep(2000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(splash_activity.this, SigninActivity.class));
                    finish();
                }
            }
        }; thread.start();
    }
}
