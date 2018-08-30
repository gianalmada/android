package com.example.gian.gapakelama.Sign;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gian.gapakelama.Helper.SharedPrefManager;
import com.example.gian.gapakelama.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import customfonts.MyTextView;

public class ProfileActivity extends Activity {

    @BindView(R.id.name)
    MyTextView name;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.phone)
    TextView phone;

    SharedPrefManager sharedPrefManager;
    RequestQueue requestQueue;

    String getNo_meja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        String names = SharedPrefManager.getInstance(this).getName();
        String emails = SharedPrefManager.getInstance(this).getEmail();
        name.setText(names);
        email.setText(emails);
//        phone.setText();

        requestQueue = Volley.newRequestQueue(getBaseContext());

        String no_meja = SharedPrefManager.getInstance(this).getScan();
        getNo_meja = no_meja;
    }

    @OnClick(R.id.signoutButton)
    public void onViewClicked() {
        gantiStatus();
        SharedPrefManager.getInstance(this).logout();
    }

    private void gantiStatus() {

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
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }

}
