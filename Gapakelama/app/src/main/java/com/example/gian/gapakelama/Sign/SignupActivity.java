package com.example.gian.gapakelama.Sign;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
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

public class SignupActivity extends Activity {

    TextView signup;

    CheckBox checkBox;

    boolean ischecked = false;

    boolean fieldkosong;

    TextInputEditText nama, username, email, password;
    String getnama, getusername, getemail, getpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        signup = (TextView) findViewById(R.id.signup1);

        checkBox = (CheckBox) findViewById(R.id.checkbox);

        nama = (TextInputEditText) findViewById(R.id.newnama);
        username = (TextInputEditText) findViewById(R.id.newusername);
        password = (TextInputEditText) findViewById(R.id.newpassword);
        email = (TextInputEditText) findViewById(R.id.newemail);

        getnama = nama.getText().toString();
        getusername = username.getText().toString().trim();
        getemail = email.getText().toString().trim();
        getpassword = password.getText().toString();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getnama = nama.getText().toString();
                getusername = username.getText().toString().trim();
                getemail = email.getText().toString().trim();
                getpassword = password.getText().toString();

                registerUser();

            }
        });
    }

        private void registerUser () {
            final String username = getusername;
            final String email = getemail;
            final String password = getpassword;
            final String nama = getnama;


            //first we will do the validations

            if (TextUtils.isEmpty(username)) {
                this.username.setError("Please enter username");
                this.username.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(email)) {
                this.email.setError("Please enter your email");
                this.email.requestFocus();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                this.email.setError("Enter a valid email");
                this.email.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                this.password.setError("Enter a password");
                this.password.requestFocus();
                return;
            }

            if (!checkBox.isChecked()) {
                this.checkBox.setError("Check this!");
                this.checkBox.requestFocus();
                return;
            }

                //if it passes all the validations

                class RegisterUser extends AsyncTask<Void, Void, String> {

                    private ProgressBar progressBar;

                    @Override
                    protected String doInBackground(Void... voids) {
                        //creating request handler object
                        RequestHandler requestHandler = new RequestHandler();

                        //creating request parameters
                        HashMap<String, String> params = new HashMap<>();
                        params.put("username", username);
                        params.put("email", email);
                        params.put("password", password);
                        params.put("name", nama);

                        //returing the response
                        return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        //displaying the progress bar while user registers on the server
                        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        //hiding the progressbar after completion
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
                                startActivity(new Intent(getApplicationContext(), SigninActivity.class));


                            } else {
                                Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            //executing the async task
            RegisterUser ru = new RegisterUser();
            ru.execute();

    }
}
