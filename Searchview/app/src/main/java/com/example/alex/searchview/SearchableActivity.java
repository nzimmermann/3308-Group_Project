package com.example.alex.searchview;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Searchable Activity used to load list layout and start searches.
 *
 * This Activity is run when a Search Intent is created from the searchview widget.
 * This Activity handles searches and produces a listview with results.
 *
 * @author Alex Cordero
 */
public class SearchableActivity extends ListActivity {

    private ProgressDialog pDialog;
    private Context context;
    private ListView lv;
    private ArrayList<String> recipeList;
    private ArrayList<String> imgList;
    private ArrayList<String> urlList;
    private Boolean no_results = true;
    private String query;

    @Override
    /**
     * Creates the layout or reload it if savedInstanceState is not null
     * This method also handles the Search Intent that initiated it.
     *
     * @param savedInstanceState The bundle to reload any past data in the activity initially null
     *
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        context=this;
        handleIntent(getIntent());
    }

    @Override
    /**
     *
     * This method handles the Search Intent that initiated it.
     *
     * @param intent The intent that called Searchable Activity containing the necessary query
     *
     */
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    /**
     *
     * This method handles the Search Intent that initiated it.  And calls the API to produce a results of the query
     *
     * @param intent The intent that called Searchable Activity containing the necessary query
     *
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            /// recipeList, imgList, and urlList are recreated here so that when a new page is requested the old page is replaced
            recipeList = new ArrayList<String>();
            imgList = new ArrayList<String>();
            urlList = new ArrayList<String>();
            query = intent.getStringExtra(SearchManager.QUERY);
            //Call the API
            new APICall().execute(query);
        }
    }
    /**
     *
     * APICall to call the api, gather results of the search and produce a listview
     *
     * This class APICall extends AsyncTask so it can operate on a seperate thread and not crash the GUI
     *
     *
     */
    public class APICall extends AsyncTask<String, Void, Void>
    {
        Exception mException = null;

        @Override
        /**
         *
         * This method creates a ProgressDialog so to alert the user that a search is occurring
         *
         */
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(SearchableActivity.this);
            pDialog.setMessage("Loading Recipes. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
            this.mException = null;
        }

        /**
         *
         * This method executes the api call to food2fork with the search query
         *
         * It then produces a JSON object with the returned String and parses through the object
         * to find each recipe title, image URL, and source URL
         *
         * @param params This is an array of strings that only contains the search query
         *
         */
        protected Void doInBackground(String... params)
        {
            String urlString = "http://food2fork.com/api/search?key=aaabb2f003cb88fe8e1a8858fc168c27&q=" + params[0];
            HttpURLConnection urlConnection;
            URL url;
            JSONObject object = null;

            try
            {
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.connect();
                InputStream inStream;
                inStream = urlConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                String temp, response = "";
                temp = bReader.readLine();
                while (temp != null){
                    response += temp;
                    temp = bReader.readLine();
                }
                bReader.close();
                inStream.close();
                urlConnection.disconnect();
                object = (JSONObject) new JSONTokener(response).nextValue();
                if(object.getInt("count") != 0)
                    no_results = false;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                this.mException=e;
            }
            /// Only parse through the JSON Array if there are recipes in it
            if(!no_results) {
                try {
                    JSONArray recipes = object.getJSONArray("recipes");
                    for (int i = 1; i <= object.getInt("count"); ++i) {
                        JSONObject r = recipes.getJSONObject(i);
                        String image_url = r.getString("image_url");
                        String source_url = r.getString("source_url");
                        String title = r.getString("title");
                        recipeList.add(title);
                        imgList.add(image_url);
                        urlList.add(source_url);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        /**
         *
         * This method dismisses the Progress Dialog.
         * It also will return a ListView with results if there are in fact results.
         * If there are no results the user will be alerted with a message on screen.
         *
         */
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if(!no_results) {
                lv = getListView();
                lv.setAdapter(new CustomAdapter(SearchableActivity.this, recipeList, imgList, urlList, query));
            }
            else{
                Toast.makeText(context, "No Search Results Found", Toast.LENGTH_LONG).show();
            }
        }
    }
}