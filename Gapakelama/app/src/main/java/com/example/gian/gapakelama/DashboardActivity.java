package com.example.gian.gapakelama;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gian.gapakelama.Helper.NoTransaksi;
import com.example.gian.gapakelama.Helper.SharedPrefManager;
import com.example.gian.gapakelama.Navigations.MenuActivity;
import com.example.gian.gapakelama.Sign.ProfileActivity;
import com.example.gian.gapakelama.Sign.SigninActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;

public class DashboardActivity extends Activity {

    @BindView(R.id.nomeja)
    TextView nomeja;

    String getNo_meja;

    SharedPrefManager sharedPrefManager;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, SigninActivity.class));
            return;
        }
        ButterKnife.bind(this);

        requestQueue = Volley.newRequestQueue(getBaseContext());

        String no_meja = SharedPrefManager.getInstance(this).getScan();
        nomeja.setText(no_meja);
        getNo_meja = no_meja;
    }

    @OnClick({R.id.orderButton, R.id.aboutButton, R.id.profileButton, R.id.exitButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.orderButton:

                DateFormat df = new SimpleDateFormat("yyMMddHHmmss");
                String no_meja = SharedPrefManager.getInstance(this).getScan();

                String date = df.format(Calendar.getInstance().getTime());

                String noStruck = no_meja+date;

                Log.d(TAG, "onViewClicked: "+noStruck);

                NoTransaksi noTransaksi = new NoTransaksi(noStruck);

                SharedPrefManager.getInstance(getApplicationContext()).setNoStruk(noTransaksi);

                setProgres();

                Intent it = new Intent(DashboardActivity.this, MenuActivity.class);
                startActivity(it);
                finish();
                break;
            case R.id.aboutButton:
                Intent it2 = new Intent(DashboardActivity.this, LocationActivity.class);
                startActivity(it2);
                break;
            case R.id.profileButton:
                Intent it3 = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(it3);
                break;
            case R.id.exitButton:
                stopTransaction();
                SharedPrefManager.getInstance(this).endOrders();
                break;
        }
    }

    private void setProgres() {
        final String no_mejas = getNo_meja;
        final String status = "true";
        final String user_id = SharedPrefManager.getInstance(this).getUsername();
        final String progress = "1";

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
                params.put("progress",progress);
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }

    private void stopTransaction() {

        final String no_mejas = getNo_meja;
        final String status = "false";
        final String user_id = "-";

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
                params.put("progress","8");
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }

}
