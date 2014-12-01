package com.example.alex.searchview;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.Normalizer;


public class SearchableActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.w("myApp", "4");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.w("myApp", "9");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.w("myApp", "5");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            NearbySearchRequest search = new NearbySearchRequest();
            JSONObject results = search.doInBackground(query);
            int count = 0;
            try {
                count = results.getInt("count");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.w("myApp", Integer.toString(count));
        }


    }
}