package com.example.gian.gapakelama.Navigations;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gian.gapakelama.Helper.BottomNavigationViewHelper;
import com.example.gian.gapakelama.Helper.SharedPrefManager;
import com.example.gian.gapakelama.Menus.Makanan;
import com.example.gian.gapakelama.Menus.MakananAdapter;
import com.example.gian.gapakelama.Menus.Minuman;
import com.example.gian.gapakelama.Menus.MinumanAdapter;
import com.example.gian.gapakelama.R;
import com.example.gian.gapakelama.Sign.SigninActivity;
import com.example.gian.gapakelama.animation.AnimationTabsListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import customfonts.MyTextView;

import static android.content.ContentValues.TAG;

public class MenuActivity extends Activity {

    private static final String MAKANAN_URL = "http://gapakelama.net/JSON/GetMakanan.php";
    private static final String MINUMAN_URL = "http://gapakelama.net/JSON/GetMinuman.php";

    TabHost tabHost;

    MyTextView tab1, tab2;

    TextView no_meja;

    SharedPrefManager sharedPrefManager;

    NotificationBadge mBadge;

    private RecyclerView recyclerView1,recyclerView2;
    MakananAdapter adapter;
    MinumanAdapter adapter2;

    List<Makanan> productList;
    List<Minuman> productList2;

    int listOrders = 0;

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

        ButterKnife.bind(this);

        productList = new ArrayList<>();
        productList2 = new ArrayList<>();

        recyclerView1 = (RecyclerView) findViewById(R.id.recyclerView1);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));

        recyclerView2 = (RecyclerView) findViewById(R.id.recyclerView2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        loadMakanan();
        loadMinuman();


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
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        String nomeja = SharedPrefManager.getInstance(this).getScan();

        no_meja = (TextView) findViewById(R.id.nomeja);

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

    private void loadMakanan(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MAKANAN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray listMakanan  = new JSONArray(response);

                            for(int i = 0; i<listMakanan.length();i++){
                                JSONObject menuMakanan  = listMakanan.getJSONObject(i);

                                String id = menuMakanan.getString("id");
                                String nama = menuMakanan.getString("nama");
                                Double harga = menuMakanan.getDouble("harga");
                                String image = menuMakanan.getString("image");

                                Makanan product = new Makanan(id,nama,harga,image);
                                productList.add(product);
                            }

                            adapter = new MakananAdapter(MenuActivity.this, productList);
                            recyclerView1.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("myTag", error.toString());
//                        Toast.makeText(DashboardActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadMinuman(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MINUMAN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray listMinuman  = new JSONArray(response);

                            for(int i = 0; i<listMinuman.length();i++){
                                JSONObject menuMinuman  = listMinuman.getJSONObject(i);

                                String id = menuMinuman.getString("id");
                                String nama = menuMinuman.getString("nama");
                                Double harga = menuMinuman.getDouble("harga");
                                String image = menuMinuman.getString("image");

                                Minuman product = new Minuman(id,nama,harga,image);
                                Log.d(TAG, "onResponse: "+product);
                                productList2.add(product);
                            }

                            adapter2 = new MinumanAdapter(MenuActivity.this, productList2);

                            recyclerView2.setAdapter(adapter2);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("myTag", error.toString());
//                        Toast.makeText(DashboardActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
