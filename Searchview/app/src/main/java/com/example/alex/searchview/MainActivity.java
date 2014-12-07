package com.example.alex.searchview;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

/**
 * Main Activity used to load layout and start searches.
 *
 * This Activity instantiates the searchview widget.
 *
 * @author Alex Cordero
 */
public class MainActivity extends Activity {

    @Override
    /**
     * Creates the layout or reload it if savedInstanceState is not null
     *
     * @param savedInstanceState The bundle to reload any past data in the activity initially null
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    /**
     *
     * Creates a menu and associates the Searchview Object with a SearchManager
     *
     * @param menu The menu to be created at the top of the screen
     *
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        /// Creates the options menu based on the options_menu.xml specification
        inflater.inflate(R.menu.options_menu, menu);
        /// Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        /// Creates a SearchView instance and attach it to the SearchView object
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        /// create a submit button for searches
        searchView.setSubmitButtonEnabled(true);
        /// Set the searchable info in the SearchView to the searchManager instance
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}