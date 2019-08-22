package com.example.krisshopapp;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krisshopapp.utilities.CommonUtilManager;
import com.example.krisshopapp.utilities.EmailManager;
import com.example.krisshopapp.utilities.HttpRequest;
import com.example.krisshopapp.utilities.RegisterManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.example.krisshopapp.utilities.NicefoodConstants.HTTP_SUCCESS_RESULT;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_ACCOUNT_ADDED_SUCCESS;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_COMMA;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_CONNECTION_ERROR;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_CREATING_USER;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_EMAIL;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_EMAIL_SUBJECT;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_EMAIL_VERIFICATION_URL;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_EMPTY;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_FILL_UP_ALL_FIELDS;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_HASH;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_INTEREST;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_INTEREST_ENTERTAINMENT;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_INTEREST_FASHION;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_INTEREST_FOOD;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_INTEREST_GADGETS;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_INTEREST_MUSIC;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_INTEREST_TRAVEL;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_MESSAGE;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_N;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_PASSWORD;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_PASSWORDS_MISMATCH;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_PASSWORD_RESET_VALUE;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_REGISTER_URL;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_RESET;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_SALT;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_SERVER_EMAIL;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_SMTP_SERVER;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_SUCCESS;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_USER_ALREADY_REGISTERED;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_USER_NAME;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_UTF8;
import static com.example.krisshopapp.utilities.NicefoodConstants.TAG_VERIFIED;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameText,emailText, passwordText, passwordConfirmText;
    private CheckBox travelCheckBox, foodCheckBox, fashionCheckBox, entertainmentCheckBox, musicCheckBox, gadgetsCheckBox;

    private String username = "";
    private String email = "";
    private String password = "";
    private String confirmPassword = "";
    private String encodedName = "";
    private String declaredInterest = "";
    private int httpRequestResult;
    private boolean isInterestDeclared;

    private List<String> registrar = new ArrayList<>();

    private RegisterManager registerManager = new RegisterManager();
    private EmailManager emailManager = new EmailManager();

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CommonUtilManager.removeSupportActionBar(getSupportActionBar());
        initializeComponents();

        TextView completeNow = findViewById(R.id.completeNow);
        TextView doItLater = findViewById(R.id.doLater);
        completeNow.setPaintFlags(completeNow.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        doItLater.setPaintFlags(doItLater.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public void registerUser(View view) {
        username = usernameText.getText().toString();
        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        confirmPassword = passwordConfirmText.getText().toString();
        isInterestDeclared = checkInterestDeclaration();
        if(isInterestDeclared) {
            declaredInterest = retrieveDeclaredInterest();
        }

        if(username.equals(TAG_EMPTY)  ||  email.equals(TAG_EMPTY) ||  password.equals(TAG_EMPTY) || confirmPassword.equals(TAG_EMPTY) || !isInterestDeclared) {
            Toast.makeText(getApplicationContext(), TAG_FILL_UP_ALL_FIELDS, Toast.LENGTH_LONG).show();
        }else {
            if (!(password.equals(confirmPassword))) {
                Toast.makeText(getApplicationContext(), TAG_PASSWORDS_MISMATCH, Toast.LENGTH_LONG).show();
            } else {
                registrar = registerManager.register(username, email, password);
                registrar.add(emailManager.secureEmail((new Random().nextInt(50) + 1) +email));
                try {
                    //byte[] userEnteredEmail = registrar.get(1).getBytes("UTF-8");
                    //encodedEmail = Base64.encodeToString(userEnteredEmail, Base64.DEFAULT);
                    byte[] userEnteredName = registrar.get(0).getBytes(TAG_UTF8);
                    encodedName = Base64.encodeToString(userEnteredName, Base64.DEFAULT);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                new CreateUser().execute();
            }
        }
    }

    class CreateUser extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage(TAG_CREATING_USER);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                URL url = new URL(TAG_REGISTER_URL);
                HttpRequest req = new HttpRequest(url);
                try {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(TAG_USER_NAME, registrar.get(0));
                    params.put(TAG_EMAIL, registrar.get(1));
                    params.put(TAG_PASSWORD, registrar.get(2));
                    params.put(TAG_INTEREST, declaredInterest);
                    params.put(TAG_SALT, registrar.get(3));

                    Log.d("request!", "starting");

                    try {
                        JSONObject json = req.preparePost().withData(params).sendAndReadJSON();

                        Log.d("Login attempt", json.toString());

                        httpRequestResult = json.getInt(TAG_SUCCESS);
                        if (httpRequestResult == HTTP_SUCCESS_RESULT) {
                            Log.d("User Created!", json.toString());
                            return json.getString(TAG_MESSAGE);
                        } else {
                            Log.d("Registration Failure!", json.getString(TAG_MESSAGE));
                            return json.getString(TAG_MESSAGE);
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
            return null;
        }

        protected void onPostExecute(String file_url) {
            //pDialog.dismiss();
            if (file_url != null){
                if(httpRequestResult != HTTP_SUCCESS_RESULT) {
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(), TAG_USER_ALREADY_REGISTERED, Toast.LENGTH_LONG).show();
                } else {
                    Log.d("sending email", "leaving create user");
                    new SendEmail().execute();
                }
            }else{
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), TAG_CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
            }
        }
    }

    class SendEmail extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            emailManager.sendMail(registrar.get(1), TAG_SERVER_EMAIL, "Please Verify Your KrisShop Account by clicking on the following link:\n VERIFICATIONEMAIL"+registrar.get(1)+"&hash="+registrar.get(4), TAG_EMAIL_SUBJECT, TAG_SMTP_SERVER);
            Log.d("send email attempt", "Sent Email");
            return null;
        }

        protected void onPostExecute(String file_url) {
            Log.d("Email sent", "Email successfuly sent");
            new VerificationAccount().execute();
        }
    }

    class VerificationAccount extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... args) {
            try {
                URL url = new URL(TAG_EMAIL_VERIFICATION_URL);
                HttpRequest req = new HttpRequest(url);
                try {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(TAG_EMAIL, registrar.get(1));
                    params.put(TAG_HASH, registrar.get(4));
                    params.put(TAG_VERIFIED, TAG_N);

                    Log.d("request!", "starting");

                    try {
                        JSONObject json = req.preparePost().withData(params).sendAndReadJSON();
                        Log.d("verifying account", json.toString());

                        httpRequestResult = json.getInt(TAG_SUCCESS);
                        if (httpRequestResult == HTTP_SUCCESS_RESULT) {
                            Log.d("verification Created!", json.toString());
                            return json.getString(TAG_MESSAGE);
                        } else {
                            Log.d("Verification Failure!", json.getString(TAG_MESSAGE));
                            return json.getString(TAG_MESSAGE);
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
            return null;

        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), TAG_ACCOUNT_ADDED_SUCCESS, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initializeComponents() {
        usernameText = (EditText) findViewById(R.id.username);
        emailText = (EditText) findViewById(R.id.email);
        passwordText = (EditText) findViewById(R.id.password);
        passwordConfirmText = (EditText) findViewById(R.id.passwordConfirmation);

        travelCheckBox = (CheckBox) findViewById(R.id.travelCheckBox);
        foodCheckBox = (CheckBox) findViewById(R.id.foodCheckBox);
        fashionCheckBox = (CheckBox) findViewById(R.id.fashionCheckBox);
        entertainmentCheckBox = (CheckBox) findViewById(R.id.entertainmentCheckBox);
        musicCheckBox = (CheckBox) findViewById(R.id.musicCheckBox);
        gadgetsCheckBox = (CheckBox) findViewById(R.id.gadgetsCheckBox);
    }

    private boolean checkInterestDeclaration() {
        if(!travelCheckBox.isChecked() && !foodCheckBox.isChecked() && !fashionCheckBox.isChecked()
                && !entertainmentCheckBox.isChecked() && !musicCheckBox.isChecked() && !gadgetsCheckBox.isChecked()) {
            return false;
        } else {
            return true;
        }
    }

    private String retrieveDeclaredInterest() {
        String currentInterests = TAG_EMPTY;
        if (travelCheckBox.isChecked()) {
            currentInterests += TAG_INTEREST_TRAVEL;
        }
        if(foodCheckBox.isChecked()) {
            if(currentInterests.isEmpty()) {
                currentInterests += TAG_INTEREST_FOOD;
            } else {
                currentInterests += TAG_COMMA;
                currentInterests += TAG_INTEREST_FOOD;
            }
        }
        if(fashionCheckBox.isChecked()) {
            if(currentInterests.isEmpty()) {
                currentInterests += TAG_INTEREST_FASHION;
            } else {
                currentInterests += TAG_COMMA;
                currentInterests += TAG_INTEREST_FASHION;
            }
        }
        if(entertainmentCheckBox.isChecked()) {
            if(currentInterests.isEmpty()) {
                currentInterests += TAG_INTEREST_ENTERTAINMENT;
            } else {
                currentInterests += TAG_COMMA;
                currentInterests += TAG_INTEREST_ENTERTAINMENT;
            }
        }
        if(musicCheckBox.isChecked()) {
            if(currentInterests.isEmpty()) {
                currentInterests += TAG_INTEREST_MUSIC;
            } else {
                currentInterests += TAG_COMMA;
                currentInterests += TAG_INTEREST_MUSIC;
            }
        }
        if(gadgetsCheckBox.isChecked()) {
            if(currentInterests.isEmpty()) {
                currentInterests += TAG_INTEREST_GADGETS;
            } else {
                currentInterests += TAG_COMMA;
                currentInterests += TAG_INTEREST_GADGETS;
            }
        }

        return currentInterests;
    }
}
