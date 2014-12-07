package com.example.alex.searchview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Alex on 12/2/2014.
 */
public class SingleRecipeActivity extends Activity{
    private static final String TAG_TITLE = "title";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recipe);

        // getting intent data
        Intent in = getIntent();

        // Get JSON values from previous intent
        String title = in.getStringExtra(TAG_TITLE);
        //String source = in.getStringExtra(TAG_SOURCE_URL);
        //String image = in.getStringExtra(TAG_IMAGE_URL);

        // Displaying all values on the screen
        TextView lblTitle = (TextView) findViewById(R.id.title);
        //TextView lblSource = (TextView) findViewById(R.id.source_url);
        //TextView lblImage = (TextView) findViewById(R.id.image_url);

        lblTitle.setText(title);
        //lblSource.setText(source);
        //lblImage.setText(image);
    }
}
