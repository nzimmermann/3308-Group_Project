package com.example.alex.searchview;

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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter{
    ArrayList<String> result;
    ArrayList<String> imgresult;
    ArrayList<String> urlresult;
    Context context;
    private static LayoutInflater inflater=null;
    public CustomAdapter(SearchableActivity searchableActivity, ArrayList<String> recipeList, ArrayList<String> imgList, ArrayList<String> urlList) {
        // TODO Auto-generated constructor stub
        result=recipeList;
        imgresult = imgList;
        urlresult = urlList;
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

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
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
                .resize(40, 40)
                .centerCrop()
                .into(holder.img);
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
        return rowView;
    }

}