package com.example.alex.searchview;
import android.os.AsyncTask;
import android.provider.SyncStateContract;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NearbySearchRequest extends AsyncTask<String, Void, JSONObject>
{
    Exception mException = null;

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        this.mException = null;
    }

    @Override
    protected JSONObject doInBackground(String... params)
    {
        String urlString = "http://food2fork.com/api/search?key=aaabb2f003cb88fe8e1a8858fc168c27&q=" + params[0];
        HttpURLConnection urlConnection = null;
        URL url = null;
        JSONObject object = null;

        try
        {
            url = new URL(urlString.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            InputStream inStream = null;
            inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
            String temp, response = "";
            while ((temp = bReader.readLine()) != null)
                response += temp;
            bReader.close();
            inStream.close();
            urlConnection.disconnect();
            object = (JSONObject) new JSONTokener(response).nextValue();
        }
        catch (Exception e)
        {
            this.mException = e;
        }

        return (object);
    }

    /*@Override
    protected void onPostExecute(JSONObject result)
    {
        super.onPostExecute(result);

        if (this.mException != null)
            ErrorHelper.report(this.mException, "Error # NearbySearchRequest");
    }*/
}

