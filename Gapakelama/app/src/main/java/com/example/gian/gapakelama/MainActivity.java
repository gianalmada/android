package com.example.gian.gapakelama;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gian.gapakelama.Helper.ScanQR;
import com.example.gian.gapakelama.Helper.SharedPrefManager;
import com.example.gian.gapakelama.Helper.URLs;
import com.example.gian.gapakelama.ModelDB.RequestHandler;
import com.example.gian.gapakelama.Navigations.MenuActivity;
import com.example.gian.gapakelama.Sign.SigninActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    String getNo_meja;
    final Context context = this;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, SigninActivity.class));
            return;
        }

        requestQueue = Volley.newRequestQueue(getBaseContext());

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

        Toast toast = Toast.makeText(getApplicationContext(), "Silahkan scan QR Code yang tertera dimeja anda!",
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }


    @Override
    public void handleResult(com.google.zxing.Result rawResult) {
        Log.v("TAG", rawResult.getText()); // Prints scan results
        Log.v("TAG", rawResult.getBarcodeFormat().toString());
        getNo_meja = rawResult.getText();

        cekScan();

        mScannerView.resumeCameraPreview(this);
    }

    private void cekScan() {
        mScannerView.stopCamera();
        final String no_meja = getNo_meja;

        class cekScan extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();


                        //getting the user from the response
                        JSONObject scanJson = obj.getJSONObject("scan");

                        //creating a new user object
                        ScanQR scanQR = new ScanQR(
                                scanJson.getString("no_meja"),
                                scanJson.getString("digunakan")
                        );

                        String status = scanQR.getStatus().toString();

                        SharedPrefManager.getInstance(getApplicationContext()).userScan(scanQR);

                        Button btnPindah, btnOrder, btnCancel;

                        final Dialog myDialog = new Dialog(context);

                        //sedang digunakan
                        if (status.equals("true")) {
                            Toast.makeText(getBaseContext(), "Meja ini sedang digunakan oleh pelanggan lain, silahkan pindah kemeja lain dan scan kembali QR code yang tertera",
                                    Toast.LENGTH_LONG).show();
                            myDialog.setContentView(R.layout.popup_inuse);
                            btnPindah = (Button) myDialog.findViewById(R.id.btnPindah);
                            btnPindah.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    myDialog.dismiss();
                                    mScannerView.startCamera();
                                }
                            });
                            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            myDialog.show();

                        //sedang kosong
                        } else if (status.equals("false")) {
                            Toast.makeText(getBaseContext(), "Meja ini siap untuk digunakan, silahkan pesan sekarang!",
                                    Toast.LENGTH_LONG).show();
                            myDialog.setContentView(R.layout.popup_available);
                            btnOrder = (Button) myDialog.findViewById(R.id.btnOrder);
                            btnCancel = (Button) myDialog.findViewById(R.id.btnCancel);

                            btnOrder.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    gantiStatus();

                                    myDialog.dismiss();

                                    finish();
                                    startActivity(new Intent(getApplicationContext(), MenuActivity.class));

                                }
                            });
                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    myDialog.dismiss();
                                    Toast.makeText(getBaseContext(), "Anda membatalkan penggunaan meja ini, silahkan scan kembali QR code yang tertera!",
                                            Toast.LENGTH_LONG).show();
                                    mScannerView.startCamera();
                                }
                            });
                            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            myDialog.show();

                        } else {
                            Toast.makeText(getBaseContext(), "Maaf, ini bukan QR Code yang tertera di meja Restorant/Cafe kami",
                                    Toast.LENGTH_LONG).show();
                            Toast.makeText(getBaseContext(), "Silahkan Scan QR Code yang tertera dimeja",
                                    Toast.LENGTH_LONG).show();
                            mScannerView.startCamera();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid QR Code", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("no_meja", no_meja);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_SCAN, params);
            }
        }

        cekScan sc = new cekScan();
        sc.execute();
    }

    private void gantiStatus(){

        final String no_meja = getNo_meja;
        final String status = "true";
        final String user_id = SharedPrefManager.getInstance(this).getUsername();

        final String url = "http://gapakelama.net/JSON/setStatus.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("no_meja",no_meja);
                params.put("statusNow",status);
                params.put("user_id", user_id);
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }
}