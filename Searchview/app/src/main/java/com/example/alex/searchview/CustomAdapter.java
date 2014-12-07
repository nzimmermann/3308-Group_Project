package com.example.alex.searchview;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 *
 * CustomAdapter used to create the ListView.
 *
 * @author Alex Cordero
 */
public class CustomAdapter extends BaseAdapter{
    private ArrayList<String> result;
    private ArrayList<String> imgresult;
    private ArrayList<String> urlresult;
    private String nextquery;
    private Context context;
    private static LayoutInflater inflater=null;
    /**
     *
     * Stages the Listview to be made and also creates an item for the next page and intantiates
     * the search for the next page.
     *
     * @param searchableActivity This is a link to the searchable Activity where the ListView will be hosted
     * @param recipeList This is a list of all of the names of the recipes
     * @param imgList This is the list of all of the image urls so they can be produced
     * @param urlList This is the list of each url for the recipe directions
     * @param query This is the search query it is used incase the user requests more results
     */
    public CustomAdapter(SearchableActivity searchableActivity, ArrayList<String> recipeList,
                         ArrayList<String> imgList, ArrayList<String> urlList, String query) {
        // TODO Auto-generated constructor stub
        result=recipeList;
        imgresult = imgList;
        urlresult = urlList;
        result.add("Next Page");
        imgresult.add("https://cdn4.iconfinder.com/data/icons/miu/22/circle_next_arrow_disclosure-128.png");
        Integer pagenum;
        Character last = query.charAt(query.length()-1);
        if(Character.isDigit(last)) {
            pagenum = (int) last;
            ++pagenum;
            nextquery = query.substring(0, query.length() - 2) + pagenum;
        }
        else{
            nextquery = query + "&page=2";
        }
        context=searchableActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /**
     *
     * This method is the base for the listview object it contains a textview for the recipe name
     * and an ImageView for the image to be shown.
     *
     */
    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    /**
     *
     * This method creates the view for each listview object.  The picasso Library is used to
     * easily produce the Image to be shown.  For each recipe there is a link to the recipe
     * instructions and all of the ingredients if the user clicks on the recipe.
     * If a user clicks on the NextPage item then a search is performed for the next page of results
     *
     * @param position is the position for the listview object and makes it easy to access the Title and URLs
     *
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_item, null);
        holder.tv=(TextView) rowView.findViewById(R.id.title);
        holder.img=(ImageView) rowView.findViewById(R.id.img);
        holder.tv.setText(result.get(position));
        Picasso.with(context)
                .load(imgresult.get(position))
                .resize(50, 50)
                .centerCrop()
                .into(holder.img);
        if(!result.get(position).equals("Next Page")) {
            rowView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserintent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(urlresult.get(position)));
                    context.startActivity(browserintent);
                    // TODO Auto-generated method stub
                    //Toast.makeText(context, "You Clicked "+result.get(position), Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            rowView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent searchintent = new Intent(Intent.ACTION_SEARCH);
                            searchintent.putExtra(SearchManager.QUERY, nextquery);
                    context.startActivity(searchintent);
                    // TODO Auto-generated method stub
                    //Toast.makeText(context, "You Clicked "+result.get(position), Toast.LENGTH_LONG).show();
                }
            });
        }
        return rowView;
    }

}