package com.example.gian.gapakelama;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.gian.gapakelama.Helper.SharedPrefManager;
import com.example.gian.gapakelama.ModelDB.RequestHandler;
import com.example.gian.gapakelama.Sign.SigninActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import customfonts.MyTextView;
import de.hdodenhof.circleimageview.CircleImageView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ConfirmActivity extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    String getId_Pelayan;
    final Context context = this;

    RequestQueue requestQueue;


    // Camera Permissions
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static String TAG = "QRCodeActivity";
    private static String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA
    };

    public static void verifyCameraPermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_CAMERA,
                    REQUEST_CAMERA_PERMISSION
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, SigninActivity.class));
            return;
        }


        verifyCameraPermissions(this);


        requestQueue = Volley.newRequestQueue(getBaseContext());

        initScannerView();

        Toast toast = Toast.makeText(getApplicationContext(), "Silahkan scan QR Code yang tertera ID CARD Pelayan!",
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void initScannerView() {
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.frame_layout_camera2);
        mScannerView = new ZXingScannerView(this);
        mScannerView.setAutoFocus(true);
        mScannerView.setResultHandler(this);
        frameLayout.addView(mScannerView);
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
        getId_Pelayan = rawResult.getText();

        cekScan();

        mScannerView.resumeCameraPreview(this);
    }


    private void cekScan() {
        mScannerView.stopCamera();
        final String id_pelayan = getId_Pelayan;

        class cekScan extends AsyncTask<Void, Void, String> {

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
                        JSONObject scanJson = obj.getJSONObject("scan");

                        String status = scanJson.getString("status");
                        String id = scanJson.getString("id");
                        String name = scanJson.getString("nama");
                        String img = scanJson.getString("img");

                        //sedang digunakan
                        if (status.equals("false")) {
                            Toast.makeText(getBaseContext(), "Barcode Salah, id pelayan tidak ditemukan !",
                                    Toast.LENGTH_LONG).show();


                        //sedang kosong
                        } else if (status.equals("true")) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmActivity.this);
                            View itemView = LayoutInflater.from(ConfirmActivity.this)
                                    .inflate(R.layout.popup_payment, null);

                            CircleImageView img_pelayan = (CircleImageView)itemView.findViewById(R.id.profile_image);
                            MyTextView id_pelayan = (MyTextView)itemView.findViewById(R.id.id_pelayan);
                            MyTextView nama_pelayan = (MyTextView)itemView.findViewById(R.id.nama_pelayan);

                            id_pelayan.setText(id);
                            nama_pelayan.setText(name);

                            String url = "http://gapakelama.net/assets/images/pelayan/"+img;

                            Glide.with(ConfirmActivity.this).load(url).into(img_pelayan);

                            builder.setView(itemView);
                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getBaseContext(), "Pembayaran telah dikonfirmasi, silahkan menungu pesanan anda!",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                                }
                            });

                            builder.show();

                        } else {
                            Toast.makeText(getBaseContext(), "Maaf, ini bukan QR Code pelayan terdaftar Restorant/Cafe kami",
                                    Toast.LENGTH_LONG).show();
                            Toast.makeText(getBaseContext(), "Silahkan Scan QR Code yang tertera dimeja",
                                    Toast.LENGTH_LONG).show();
                            mScannerView.startCamera();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid QR Code", Toast.LENGTH_SHORT).show();
                        mScannerView.startCamera();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                String no_struck = SharedPrefManager.getInstance(ConfirmActivity.this).getNoStruk();
                String no_meja = SharedPrefManager.getInstance(ConfirmActivity.this).getScan();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("id_pelayan", id_pelayan);
                params.put("struck",no_struck);
                params.put("nomeja",no_meja);

                final String url = "http://gapakelama.net/JSON/cekStatus.php";

                //returing the response
                return requestHandler.sendPostRequest(url, params);
            }
        }

        cekScan sc = new cekScan();
        sc.execute();
    }
}