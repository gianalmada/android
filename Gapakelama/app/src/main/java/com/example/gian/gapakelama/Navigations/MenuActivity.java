package com.example.gian.gapakelama.Navigations;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.gian.gapakelama.Helper.BottomNavigationViewHelper;
import com.example.gian.gapakelama.Helper.SharedPrefManager;
import com.example.gian.gapakelama.R;
import com.example.gian.gapakelama.Sign.SigninActivity;
import com.example.gian.gapakelama.animation.AnimationTabsListener;

import customfonts.MyTextView;

public class MenuActivity extends Activity {

    TabHost tabHost;

    MyTextView tab1, tab2;

    TextView no_meja;

    SharedPrefManager sharedPrefManager;

    private RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, SigninActivity.class));
            return;
        }


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navbottom);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        String nomeja = SharedPrefManager.getInstance(this).getScan();

        no_meja = (TextView) findViewById(R.id.nomeja);
        no_meja.setText(nomeja);

        //TAB
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();


        //tab1
        TabHost.TabSpec spec = tabHost.newTabSpec("Makanan");
        spec.setContent(R.id.tab1);
        spec.setIndicator("",getResources().getDrawable(R.drawable.ic_food));
        tabHost.addTab(spec);

        //tab2
        spec = tabHost.newTabSpec("Minuman");
        spec.setContent(R.id.tab2);
        spec.setIndicator("", getResources().getDrawable(R.drawable.ic_drink));
        tabHost.addTab(spec);

        tabHost.setOnTabChangedListener(new AnimationTabsListener(this.tabHost));

        //BotNav
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.taborder:
                        Intent intent0 = new Intent(MenuActivity.this, OrderActivity.class);
                        startActivity(intent0);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.tabmenu:

                        break;

                    case R.id.tabchart:
                        Intent intent2 = new Intent(MenuActivity.this, ChartActivity.class);
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
