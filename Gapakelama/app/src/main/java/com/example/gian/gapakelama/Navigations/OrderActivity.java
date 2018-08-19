package com.example.gian.gapakelama.Navigations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.gian.gapakelama.Helper.BottomNavigationViewHelper;
import com.example.gian.gapakelama.Helper.SharedPrefManager;
import com.example.gian.gapakelama.R;
import com.example.gian.gapakelama.Sign.SigninActivity;

public class OrderActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, SigninActivity.class));
            return;
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navbottom);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.taborder:

                        break;

                    case R.id.tabmenu:
                        Intent intent1 = new Intent(OrderActivity.this, MenuActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.tabchart:
                        Intent intent2 = new Intent(OrderActivity.this, ChartActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;

                }
                return false;
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
