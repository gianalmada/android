package com.example.gian.gapakelama.Sign;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gian.gapakelama.Helper.SharedPrefManager;
import com.example.gian.gapakelama.Helper.URLs;
import com.example.gian.gapakelama.Helper.User;
import com.example.gian.gapakelama.MainActivity;
import com.example.gian.gapakelama.ModelDB.RequestHandler;
import com.example.gian.gapakelama.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SigninActivity extends Activity {

    TextView create;

    Typeface fonts1;

    TextView signin;

    TextInputEditText email,password;

    String getpass, getmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        create = (TextView)findViewById(R.id.create);
        signin = (TextView) findViewById(R.id.signin1);

        password = (TextInputEditText) findViewById(R.id.password);
        email = (TextInputEditText) findViewById(R.id.email);

        signin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getpass = password.getText().toString();
                getmail = email.getText().toString().trim();
                userLogin();

            }
        });

        //SIGN UP
        create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(SigninActivity.this, SignupActivity.class);
                        startActivity(it);
                        finish();
                    }
                });

                fonts1 =  Typeface.createFromAsset(SigninActivity.this.getAssets(),
                        "fonts/Lato-Regular.ttf");


                TextView t4 =(TextView)findViewById(R.id.create);
                t4.setTypeface(fonts1);
    }

    private void userLogin() {

        final String email =  getmail;
        final String password = getpass;

        //validating inputs
        if (TextUtils.isEmpty(email)) {
            this.email.setError("Please enter your email address");
            this.email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            this.password.setError("Please enter your password");
            this.password.requestFocus();
            return;
        }

        //if everything is fine

        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();


                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        User user = new User(
                                userJson.getString("username"),
                                userJson.getString("email"),
                                userJson.getString("name")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);


                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
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
                params.put("email", email);
                params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }

}

