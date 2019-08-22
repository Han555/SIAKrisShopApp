package com.example.krisshopapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krisshopapp.utilities.CommonUtilManager;
import com.example.krisshopapp.utilities.HttpRequest;
import com.example.krisshopapp.utilities.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import static com.example.krisshopapp.utilities.NicefoodConstants.HTTP_SUCCESS_RESULT;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_ACCOUNT_DOES_NOT_EXIST;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_ACCOUNT_NOT_VERIFIED;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_CHECK_VERIFICATION_URL;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_CONNECTION;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_CONNECTION_ERROR;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_EMAIL;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_FAIL;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_INCORRECT_USERNAME_PASSWORD;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_LOGGING_IN;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_LOGIN_ID;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_LOGIN_NAME;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_MESSAGE;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_NAME;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_PASSWORD;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_PASSWORDRESET;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_PLEASE_WAIT;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_PROFILE_PIC;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_RETRIEVE_LOGIN_CREDENTIALS_URL;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_SALT;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_SUCCESS;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_VERIFIED;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_Y;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_YES;


public class MainActivity extends AppCompatActivity {

    private String loginId = "";
    private int success;
    private String verificationStatus = "";
    private boolean isVerified;

    private ProgressDialog pDialog;

    private String retrievedSalt;
    private String retrievedPassword;
    private String retrievedName;
    private String loginPassword;

    private LoginManager loginManager = new LoginManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CommonUtilManager.removeSupportActionBar(getSupportActionBar());

        TextView registerText = findViewById(R.id.register);
        TextView forgotPwText = findViewById(R.id.forgotPW);
        registerText.setPaintFlags(registerText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        forgotPwText.setPaintFlags(forgotPwText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public void register(View view) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    public void login(View view) {
        EditText username = findViewById(R.id.loginID);
        EditText passwordText = findViewById(R.id.loginPassword);
        loginId = username.getText().toString();
        loginPassword = passwordText.getText().toString();
        new checkVerification().execute();
    }

    class checkVerification extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... args) {
            try {
                URL url = new URL(TAG_CHECK_VERIFICATION_URL);
                HttpRequest req = new HttpRequest(url);
                try {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(TAG_EMAIL, loginId);

                    Log.d("request!", "starting");

                    try {
                        JSONObject json = req.preparePost().withData(params).sendAndReadJSON();

                        Log.d("Login attempt", json.toString());

                        success = json.getInt(TAG_SUCCESS);
                        if (success == HTTP_SUCCESS_RESULT) {
                            Log.d("Login Successful!", json.toString());

                            verificationStatus = json.getString(TAG_VERIFIED);
                            if(verificationStatus.equals(TAG_Y)) {
                                isVerified = true;
                            }

                            return TAG_SUCCESS;
                        } else {
                            Log.d("Verification Failure!", json.getString(TAG_MESSAGE));
                            return TAG_FAIL;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch(MalformedURLException e) {
                e.printStackTrace();
            }
            return TAG_CONNECTION;
        }

        protected void onPostExecute(String status) {
            if(!status.equals(TAG_SUCCESS)) {
                if (status.equals(TAG_CONNECTION)) {
                    Toast.makeText(getApplicationContext(), TAG_CONNECTION_ERROR, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), TAG_ACCOUNT_DOES_NOT_EXIST, Toast.LENGTH_LONG).show();
                }
            } else {
                if(isVerified) {
                    new AttemptLogin().execute();
                } else {
                    Toast.makeText(getApplicationContext(), TAG_ACCOUNT_NOT_VERIFIED, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    class AttemptLogin extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getApplicationContext());
            pDialog = ProgressDialog.show(MainActivity.this, TAG_LOGGING_IN, TAG_PLEASE_WAIT, false, false);

        }

        @Override
        protected String doInBackground(String... args) {
            try {
                URL url = new URL(TAG_RETRIEVE_LOGIN_CREDENTIALS_URL);
                HttpRequest req = new HttpRequest(url);
                try {
                    // Building Parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put(TAG_EMAIL, loginId);

                    Log.d("request!", "starting");

                    try {
                        JSONObject json = req.preparePost().withData(params).sendAndReadJSON();
                        Log.d("Login attempt", json.toString());

                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            Log.d("Login Successful!", json.toString());

                            retrievedSalt = json.getString(TAG_SALT);
                            retrievedPassword = json.getString(TAG_PASSWORD);
                            retrievedName = json.getString(TAG_NAME);
                            //passwordResetStatus = json.getString(TAG_PASSWORDRESET);
                            //profilePic = json.getString(TAG_PROFILE_PIC);

                            return TAG_SUCCESS;
                        } else {
                            Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                            return TAG_FAIL;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch(MalformedURLException e) {
                e.printStackTrace();
            }
            return TAG_CONNECTION;
        }

        protected void onPostExecute(String status) {
            pDialog.dismiss();
            if(!status.equals(TAG_SUCCESS)) {
                if (status.equals(TAG_CONNECTION)) {
                    Toast.makeText(getApplicationContext(), TAG_CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), TAG_INCORRECT_USERNAME_PASSWORD, Toast.LENGTH_SHORT).show();
                }
            } else {
                boolean accessPermission = loginManager.checkPassword(loginPassword, retrievedPassword, retrievedSalt);

                if(accessPermission) {
//                    if(passwordResetStatus.equals(TAG_YES)) {
//                        Intent i = new Intent(getApplicationContext(), GiveNewPasswordActivity.class);
//                        i.putExtra(TAG_LOGIN_ID, loginId);
//                        startActivity(i);
//                    } else {
                        Intent i = new Intent(getApplicationContext(), PromotionActivity.class);
                        i.putExtra(TAG_LOGIN_ID, loginId);
                        i.putExtra(TAG_LOGIN_NAME, retrievedName);
                       // i.putExtra(TAG_PROFILE_PIC, profilePic);
                        startActivity(i);
                    //}
                } else {
                    Toast.makeText(getApplicationContext(), TAG_INCORRECT_USERNAME_PASSWORD, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
