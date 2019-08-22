package com.example.krisshopapp.utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;

//import com.han.nicefood.com.han.nicefood.activity.RecommendFoodActivity;

//import info.androidhive.listview.app.MainActivity;

//import static utilities.NicefoodConstants.TAG_MAIN_URL_FEED;

/**
 * Created by Han on 9/3/18.
 */

public class CommonUtilManager {

    public static void removeSupportActionBar(ActionBar actionBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.hide();
        }
    }

    public static ProgressDialog displayProgressDialog(Activity targetActivity, String message, boolean indeterminate, boolean cancelable, ProgressDialog progressDialog) {
        progressDialog = new ProgressDialog(targetActivity);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(indeterminate);
        progressDialog.setCancelable(cancelable);
        progressDialog.show();

        return progressDialog;
    }

    public static void dismissProgressDialog(ProgressDialog progressDialog) {
        progressDialog.dismiss();
    }

//    public static void advanceToCorrectActivity(Context context, String loginId, String loginName, String userVotes, String chosenActivity, String URL) {
//        if(chosenActivity.equals("retrieveRecommend")) {
//            Intent i = new Intent(context, RecommendFoodActivity.class);
//            i.putExtra("loginId", loginId);
//            i.putExtra("loginName", loginName);
//            i.putExtra("userVotes", userVotes);
//            context.startActivity(i);
//        } else if(chosenActivity.equals("retrieveHalal")) {
//            Intent i = new Intent(context, MainActivity.class);
//            i.putExtra("loginId", loginId);
//            i.putExtra("loginName", loginName);
//            i.putExtra("task", chosenActivity);
//            i.putExtra("userVotes", userVotes);
//            context.startActivity(i);
//        } else if(chosenActivity.equals("retrieveFried")) {
//            Intent i = new Intent(context, MainActivity.class);
//            i.putExtra("loginId", loginId);
//            i.putExtra("loginName", loginName);
//            i.putExtra("urlFeed", TAG_MAIN_URL_FEED + "retrieveFried.php");
//            i.putExtra("task", "retrieveFried");
//            i.putExtra("userVotes", userVotes);
//            context.startActivity(i);
//        } else if(chosenActivity.equals("retrieveBoiled")) {
//            Intent i = new Intent(context, MainActivity.class);
//            i.putExtra("loginId", loginId);
//            i.putExtra("loginName", loginName);
//            i.putExtra("urlFeed", TAG_MAIN_URL_FEED + "retrieveBoiled.php");
//            i.putExtra("task", "retrieveBoiled");
//            i.putExtra("userVotes", userVotes);
//            context.startActivity(i);
//        } else if(chosenActivity.equals("retrieveSteamed")) {
//            Intent i = new Intent(context, MainActivity.class);
//            i.putExtra("loginId", loginId);
//            i.putExtra("loginName", loginName);
//            i.putExtra("urlFeed", TAG_MAIN_URL_FEED + "retrieveSteamed.php");
//            i.putExtra("task", "retrieveSteamed");
//            i.putExtra("userVotes", userVotes);
//            context.startActivity(i);
//        } else if(chosenActivity.equals("retrieveGrilled")) {
//            Intent i = new Intent(context, MainActivity.class);
//            i.putExtra("loginId", loginId);
//            i.putExtra("loginName", loginName);
//            i.putExtra("urlFeed", TAG_MAIN_URL_FEED + "retrieveGrilled.php");
//            i.putExtra("task", "retrieveGrilled");
//            i.putExtra("userVotes", userVotes);
//            context.startActivity(i);
//        } else if(chosenActivity.equals("retrieveBaked")) {
//            Intent i = new Intent(context, MainActivity.class);
//            i.putExtra("loginId", loginId);
//            i.putExtra("loginName", loginName);
//            i.putExtra("urlFeed", TAG_MAIN_URL_FEED + "retrieveBaked.php");
//            i.putExtra("task", "retrieveBaked");
//            i.putExtra("userVotes", userVotes);
//            context.startActivity(i);
//        }
//    }
}
