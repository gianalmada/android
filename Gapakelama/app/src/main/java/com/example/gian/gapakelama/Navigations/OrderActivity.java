package com.example.gian.gapakelama.Navigations;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gian.gapakelama.ConfirmActivity;
import com.example.gian.gapakelama.Helper.BottomNavigationViewHelper;
import com.example.gian.gapakelama.Helper.SharedPrefManager;
import com.example.gian.gapakelama.ModelDB.RequestHandler;
import com.example.gian.gapakelama.R;
import com.example.gian.gapakelama.Sign.SigninActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderActivity extends Activity {

    @BindView(R.id.paymentButton)
    Button confirm2;

    RequestQueue requestQueue;

    private static final String RECENT = "http://gapakelama.net/JSON/GetAktifTrans.php";
    private static final String HISTORY = "http://gapakelama.net/JSON/GetHistoryTrans.php";

    @BindView(R.id.date_order)
    TextView dates;

    @BindView(R.id.transaksi_id)
    TextView no_struk;

    @BindView(R.id.no_meja_order)
    TextView no_meja;

    @BindView(R.id.total_bayar)
    TextView price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, SigninActivity.class));
            return;
        }

        ButterKnife.bind(this);

        requestQueue = Volley.newRequestQueue(getBaseContext());

        getRecent();
//        getHystory();


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

                        break;

                    case R.id.tabmenu:
                        Intent intent1 = new Intent(OrderActivity.this, MenuActivity.class);
                        finish();
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.tabchart:
                        Intent intent2 = new Intent(OrderActivity.this, ChartActivity.class);
                        finish();
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;

                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getRecent();
    }

    //    private void getHystory() {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, HISTORY,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//                            JSONArray listMakanan  = new JSONArray(response);
//
//                            for(int i = 0; i<listMakanan.length();i++){
//                                JSONObject menuMakanan  = listMakanan.getJSONObject(i);
//
//                                String id = menuMakanan.getString("id");
//                                String nama = menuMakanan.getString("nama");
//                                Double harga = menuMakanan.getDouble("harga");
//                                String image = menuMakanan.getString("image");
//
//                                Makanan product = new Makanan(id,nama,harga,image);
//                                productList.add(product);
//                            }
//
//                            adapter = new MakananAdapter(MenuActivity.this, productList);
//                            recyclerView1.setAdapter(adapter);
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("myTag", error.toString());
////                        Toast.makeText(DashboardActivity.this, error.toString(), Toast.LENGTH_LONG).show();
//                    }
//                });
//        Volley.newRequestQueue(this).add(stringRequest);
//
//
//    }

    private void getRecent() {

        class getRecent extends AsyncTask<Void, Void, String>{
            @Override
            protected void onPostExecute(String s) {

                super.onPostExecute(s);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
//                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();


                        //getting the user from the response
                        JSONObject recent = obj.getJSONObject("scan");

                        String id = recent.getString("order_id");
                        String meja = recent.getString("table_no");
                        String date = recent.getString("date");
                        String bayar = recent.getString("total_bayar");

                        no_struk.setText(id);
                        no_meja.setText(meja);
                        dates.setText(date);
                        price.setText("Rp. "+bayar);

                    } else {
                        Toast.makeText(getApplicationContext(), "Tidak ada traksaksi aktif !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                RequestHandler requestHandler = new RequestHandler();

                String no_meja = SharedPrefManager.getInstance(OrderActivity.this).getScan();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("nomeja",no_meja);

                final String url = "http://gapakelama.net/JSON/GetAktifTrans.php";

                //returing the response
                return requestHandler.sendPostRequest(url, params);
            }
        }

        getRecent sc = new getRecent();
        sc.execute();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, RECENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray recentTrans  = new JSONArray(response);

                            for(int i = 0; i<recentTrans.length();i++){
                                JSONObject recent  = recentTrans.getJSONObject(i);

                                String id = recent.getString("order_id");
                                String meja = recent.getString("table_no");
                                String date = recent.getString("date");
                                String bayar = recent.getString("total_bayar");

                                no_struk.setText(id);
                                no_meja.setText(meja);
                                dates.setText(date);
                                price.setText("Rp. "+bayar);

                            }

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

    @OnClick(R.id.paymentButton)
    public void setConfirm2(View view){

        updateStatus();

        Intent intent0 = new Intent(OrderActivity.this, ConfirmActivity.class);
        startActivity(intent0);
        finish();
        overridePendingTransition(0, 0);
        Toast.makeText(this, "Konfirmasikan pembayaran anda kepada pelayan yang akan datang !",
                Toast.LENGTH_LONG).show();


    }

    private void updateStatus() {

        final String no_mejas = SharedPrefManager.getInstance(this).getScan();
        final String status = "true";
        final String user_id = SharedPrefManager.getInstance(this).getUsername();

        final String url = "http://gapakelama.net/JSON/setStatus.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("myTag", response);
//                        Toast.makeText(DashboardActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("myTag", error.toString());
//                        Toast.makeText(DashboardActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("no_meja", no_mejas);
                params.put("statusNow", status);
                params.put("user_id", user_id);
                params.put("progress","9");
                return params;
            }

        };
        requestQueue.add(stringRequest);

    }


    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRecent();
    }
}
