package com.example.alex.searchview;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.HashMap;


public class SearchableActivity extends ListActivity {

    private static final String TAG_TITLE = "title";
    private static final String TAG_SOURCE_URL = "Source URL";
    private static final String TAG_IMAGE_URL = "Image URL";
    private ProgressDialog pDialog;

    ArrayList<HashMap<String, String>> recipeList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.w("myApp", "4");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        recipeList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();

        // Listview on item click listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
                // getting values from selected ListItem
                String title = ((TextView) view.findViewById(R.id.title))
                .getText().toString();
                String source_url = ((TextView) view.findViewById(R.id.source_url))
                .getText().toString();
                String image_url = ((TextView) view.findViewById(R.id.image_url))
                .getText().toString();

                // Starting single contact activity
                Intent in = new Intent(getApplicationContext(),
                SingleRecipeActivity.class);
                in.putExtra(TAG_TITLE, title);
                in.putExtra(TAG_SOURCE_URL, source_url);
                in.putExtra(TAG_IMAGE_URL, image_url);
                startActivity(in);
                }
             });
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
            new APICall().execute(query);
            //JSONObject results = ;
            //JavaHttpUrlConnectionReader search = new JavaHttpUrlConnectionReader();
            Log.w("myApp", "6");
            int count = 0;
            /*try {
                count = results.getInt("count");
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }


    }
    public class APICall extends AsyncTask<String, Void, Void>
    {
        Exception mException = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(SearchableActivity.this);
            pDialog.setMessage("Loading Recipes. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            Log.w("myApp", "PreExecute");
            super.onPreExecute();
            this.mException = null;
        }

        protected Void doInBackground(String... params)
        {
            String urlString = "http://food2fork.com/api/search?key=aaabb2f003cb88fe8e1a8858fc168c27&q=" + params[0];
            HttpURLConnection urlConnection = null;
            URL url = null;
            JSONObject object = null;

            try
            {
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.w("myApp", urlConnection.toString());
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.connect();
                InputStream inStream = null;
                Log.w("myApp", "Pre-Connection Check");
                inStream = urlConnection.getInputStream();
                if(inStream == null){
                    Log.w("myApp", "inStream isNull");
                }
                Log.w("myApp", "Post-Connection Check");
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                Log.w("myApp", "bReader Check");
                String temp, response = "";
                temp = bReader.readLine();
                Log.w("myApp", temp);
                while (temp != null){
                    response += temp;
                    temp = bReader.readLine();
                    Log.w("myApp", "Responding...");
                }
                Log.w("myApp", "Finished Reading");
                bReader.close();
                inStream.close();
                urlConnection.disconnect();
                object = (JSONObject) new JSONTokener(response).nextValue();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                this.mException=e;
            }
            //This is true
            if(object == null){
                Log.w("myApp", "Object isNull");
            }
            try {
                JSONArray recipes = object.getJSONArray("recipes");
                for(int i=1; i <= object.getInt("count");++i){
                    JSONObject r = recipes.getJSONObject(i);
                    String image_url = r.getString("image_url");
                    String source_url = r.getString("source_url");
                    String title = r.getString("title");
                    HashMap<String, String> recipe = new HashMap<String, String>();
                    recipe.put("image", image_url);
                    recipe.put("source", source_url);
                    recipe.put("title", title);
                    recipeList.add(recipe);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            Log.w("myApp", "Post Execute");
            ListAdapter adapter = new SimpleAdapter(
                    SearchableActivity.this, recipeList,
                    R.layout.list_item, new String[] { "title", "source_url",
                    "image_url" }, new int[] { R.id.title,
                    R.id.source_url, R.id.image_url });
            setListAdapter(adapter);
        }
    }
}