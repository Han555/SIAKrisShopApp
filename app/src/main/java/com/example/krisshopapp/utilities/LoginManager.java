package com.example.krisshopapp.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by Han on 7/12/17.
 */

public class LoginManager {

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;


    public boolean checkPassword(String loginPassword, String retrievedPassword, String salt) {
        SecurityManager secure = new SecurityManager();
        String toBeHashed = salt+loginPassword;
        String hashedPassword = secure.doMD5Hashing(toBeHashed);

        Log.d("loginpassword", loginPassword);
        Log.d("retrievedPassword", retrievedPassword);
        Log.d("salt", salt);
        Log.d("hashedPassword", hashedPassword);

        return hashedPassword.equals(retrievedPassword);
    }

    public void saveLoginCredentials(String username, String password) {
        loginPrefsEditor.putBoolean("saveLogin", true);
        loginPrefsEditor.putString("username", username);
        loginPrefsEditor.putString("password", password);
        loginPrefsEditor.commit();
    }

    public void clearSavedLoginCredentials() {
        loginPrefsEditor.clear();
        loginPrefsEditor.commit();
    }

    public void setUpRememberMeFunction(Activity loginActivity) {
        loginPreferences = loginActivity.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
    }

    public void clearLoginFields(EditText username, EditText password, CheckBox rememberMe) {
        if(loginPreferences.getBoolean("saveLogin", false)) {
            username.setText(loginPreferences.getString("username", ""));
            password.setText(loginPreferences.getString("password", ""));
            rememberMe.setChecked(true);
        }
    }
}
