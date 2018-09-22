package com.example.gian.gapakelama.Navigations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gian.gapakelama.Database.Model.DataSource.CartRepository;
import com.example.gian.gapakelama.Database.Model.Local.CartDataSource;
import com.example.gian.gapakelama.Database.Model.Local.CartDatabase;
import com.example.gian.gapakelama.Helper.BottomNavigationViewHelper;
import com.example.gian.gapakelama.Helper.SharedPrefManager;
import com.example.gian.gapakelama.Orders.OrderArray;
import com.example.gian.gapakelama.Orders.OrdersAdapter;
import com.example.gian.gapakelama.R;
import com.example.gian.gapakelama.Sign.SigninActivity;
import com.example.gian.gapakelama.Util.Server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class ChartActivity extends Activity {

    private RecyclerView recyclerView;

    OrdersAdapter adapter;

    OrderArray orders;

    @BindView(R.id.title_struck)
    TextView nostruck;

    @BindView(R.id.date_trans)
    TextView date_trans;

//    List<Orders> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, SigninActivity.class));
            return;
        }

        Log.d(TAG, "onCreate: "+Server.cartRepository.getCartList());

        ButterKnife.bind(this);

        String no_struck = SharedPrefManager.getInstance(this).getNoStruk();
        nostruck.setText("No. Transaksi : "+no_struck);

        DateFormat df = new SimpleDateFormat("YYYY-MM-dd");

        String date = df.format(Calendar.getInstance().getTime());
        date_trans.setText("Date :"+date);


        recyclerView = (RecyclerView) findViewById(R.id.cartRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initDB();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navbottom);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.taborder:
                        Intent intent0 = new Intent(ChartActivity.this, OrderActivity.class);
                        startActivity(intent0);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.tabmenu:
                        Intent intent1 = new Intent(ChartActivity.this, MenuActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.tabchart:

                        break;
            }

                return false;

            }
        });

    }

    private void initDB() {
        Server.cartDatabase = CartDatabase.getInstance(this);
        Server.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Server.cartDatabase.cartDAO()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
