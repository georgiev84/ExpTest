package com.example.petar.exptest;

import android.app.Fragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.petar.exptest.adapters.ProductListAdapter;
import com.example.petar.exptest.data.ProductList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ProductListFragment extends Fragment{

    String address = "http://192.168.2.108/opencart203/myapiproductlist.php?pass=kolega&";

    InputStream is = null;
    String line = null;
    String result = null;
    RecyclerView recyclerView;
    ArrayList<ProductList> productList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.product_list_fragment, container, false);

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        final ProductListAdapter adapter = new ProductListAdapter(getActivity(), productList);



        getData();

        recyclerView = (RecyclerView) v.findViewById(R.id.productListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);



        return v;
    }

    private void getData() {
        try {


                if(MainActivity.fillFragment=="category") {
                    URL url = new URL(address + "category_id=" + String.valueOf(MainActivity.catId));
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");

                    con.setConnectTimeout(30000);
                    con.setReadTimeout(30000);

                    is = new BufferedInputStream((con.getInputStream()));
                }
                if(MainActivity.fillFragment == "search") {
                     URL url = new URL(address + "search=" + URLEncoder.encode(String.valueOf(MainActivity.searchProduct), "UTF-8"));
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");

                    con.setConnectTimeout(30000);
                    con.setReadTimeout(30000);

                    is = new BufferedInputStream((con.getInputStream()));
                }




        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            //tyi e ftp-to

            while ((line = br.readLine()) != null) {
                //  sb.append(line + "\n");
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // PARSE JSON DATA
        try {
            productList.clear();

            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);

                ProductList p = new ProductList();
                p.productId = o.getInt("product_id");
                if(o.getString("product_image") != null) {
                    p.image = o.getString("product_image");
                }
                p.name = o.getString("name");
                p.categoryId = o.getInt("category_id");
                p.price = Float.parseFloat(String.valueOf(o.get("price")));


                productList.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
