package com.example.petar.exptest.adapters;


import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.petar.exptest.R;
import com.example.petar.exptest.data.CategoryItem;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FillAdapter extends BaseExpandableListAdapter {
    private Context context;
    private final ArrayList<CategoryItem> categoryLists;

    public FillAdapter(Context context, ArrayList<CategoryItem> categoryLists) {
        this.context = context;
        this.categoryLists = categoryLists;
    }

    //private final ArrayList<CategoryList> categoryLists;

    @Override
    public int getGroupCount() {
        return categoryLists.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return categoryLists.get(groupPosition).subCategories.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categoryLists.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categoryLists.get((groupPosition)).subCategories.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }
        CategoryItem group = (CategoryItem) getGroup(groupPosition);
        TextView listheader =(TextView) convertView.findViewById(R.id.listHeader);
        //listheader.setTypeface(null, Typeface.BOLD);
        listheader.setText(group.categorName);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        CategoryItem child = (CategoryItem) getChild(groupPosition, childPosition);
        TextView txtListChild = (TextView) convertView.findViewById(R.id.listItem);
        txtListChild.setText("\u2022 "+child.categorName);

        ImageView setImage = (ImageView) convertView.findViewById(R.id.image);
//        String fullUrl = "http://192.168.2.108/opencart203/image/"+child.categoryImage;
//        Picasso.with(context).load(fullUrl).into(setImage);



        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
