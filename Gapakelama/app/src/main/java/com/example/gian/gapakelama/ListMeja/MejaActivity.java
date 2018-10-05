package com.example.gian.gapakelama.ListMeja;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gian.gapakelama.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gian on 27/09/2018.
 */

public class MejaActivity extends Activity {

    RecyclerView recyclerView1;

    MejaAdapter mejaAdapter;

    List<Meja> mejaList;

    private static final String MEJA_URL = "http://gapakelama.net/JSON/GetMeja.php";

    @Override
    protected void onCreate(@android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meja);

        mejaList = new ArrayList();

        recyclerView1 = (RecyclerView) findViewById(R.id.mejaRecycler);
        recyclerView1.setLayoutManager(new GridLayoutManager(this,2));

        loadMeja();
    }

    private void loadMeja() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, MEJA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray listMakanan  = new JSONArray(response);

                            for(int i = 0; i<listMakanan.length();i++){
                                JSONObject menuMakanan  = listMakanan.getJSONObject(i);

                                String no_meja = menuMakanan.getString("nomeja");
                                String status = menuMakanan.getString("status");
                                String id_user = menuMakanan.getString("user");
                                String time = menuMakanan.getString("time");

                                Meja meja = new Meja(no_meja,status,id_user,time);
                                mejaList.add(meja);
                            }

                            mejaAdapter = new MejaAdapter(MejaActivity.this, mejaList);
                            recyclerView1.setAdapter(mejaAdapter);

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
