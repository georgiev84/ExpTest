package com.example.petar.exptest.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.petar.exptest.R;
import com.example.petar.exptest.data.CategoryItem;
import com.example.petar.exptest.data.ProductList;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter{
    private final Context context;
    private final ArrayList<ProductList> productList;

    public ProductListAdapter(Context context, ArrayList<ProductList> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View row = layoutInflater.inflate(R.layout.product_list_row, parent, false);

        return new ItemHolder(row);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ProductList m = productList.get(position);

        ((ItemHolder)holder).name.setText(m.name);
        ((ItemHolder)holder).price.setText(String.valueOf(m.price));
        ((ItemHolder)holder).image.findViewById(R.id.productImageList);





        String fullUrl = "http://192.168.2.108/opencart203/image/"+m.image;
        Picasso.with(context).load(fullUrl).into(((ItemHolder) holder).image);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private class ItemHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView price;
        ImageView image;

        public ItemHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.productNameList);
            price = (TextView) itemView.findViewById(R.id.productPriceList);
            image = (ImageView) itemView.findViewById(R.id.productImageList);

//            String fullUrl = "http://192.168.2.108/opencart203/image/"+child.categoryImage;
//            Picasso.with(context).load(fullUrl).into(setImage);

        }
    }


}
