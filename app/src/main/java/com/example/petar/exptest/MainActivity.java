package com.example.petar.exptest;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petar.exptest.adapters.FillAdapter;
import com.example.petar.exptest.data.CategoryItem;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private String address = "http://192.168.2.108/opencart203/myapi2.php?pass=kolega";
    InputStream is = null;
    String line = null;
    String result = null;
    //stoi 6e go opraim tuka
    FillAdapter listAdapter;
    ExpandableListView listView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listHash;
    ArrayList<CategoryItem> categoryData = new ArrayList<>();
    private ProductListFragment productListFragment;
    public static int catId=0;
    public static String searchProduct;
    public static String fillFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#B40404")));

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        listView = (ExpandableListView) findViewById(R.id.listview1);

        //todo json data
        getData();

        //todo manual data
        //initData();

        listAdapter = new FillAdapter(this, categoryData);
        listView.setAdapter(listAdapter);

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                CategoryItem cat = (CategoryItem) listAdapter.getChild(groupPosition, childPosition);
                String selected = String.valueOf(cat.categoryId);
//                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG).show();

                catId = cat.categoryId;
                fillFragment = "category";

                FragmentManager FM = getFragmentManager();
                FragmentTransaction FT = FM.beginTransaction();
                ProductListFragment fragment = new ProductListFragment();
                FT.replace(R.id.load, fragment);
                FT.addToBackStack("product_fragment");

                FT.commit();
                mDrawerLayout.closeDrawers();


                return true;
            }
        });


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);


        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void getData() {
        try {
            URL url = new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            is = new BufferedInputStream((con.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {

                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // PARSE JSON DATA
        try {

// dai da testvame ok do tuka razbrah
            JSONArray array = new JSONArray(result);

            listDataHeader = new ArrayList<>();
            listHash = new HashMap<>();

            ArrayList<CategoryItem> allCategories = new ArrayList<>(); //tuk pazim si4ki zana4alo
            HashMap<Integer, CategoryItem> parentCategories = new HashMap<>(); //tok she pazim ve4e parentite i posle 6e im pylnil chilodeve

            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);

                CategoryItem cat = new CategoryItem();
                String categoryId = o.getString("category_id");
                cat.categoryId = Integer.parseInt(categoryId);
                cat.categorName = o.getString("category_name");
                String categoryParentId = o.getString("category_parent_id");
                cat.categoryParentId = Integer.parseInt(categoryParentId);
                cat.categorySort = o.getInt("category_sort");
                if(o.getString("category_image") != null) {
                    cat.categoryImage = o.getString("category_image");
                }
                allCategories.add(cat);
            }

            //tok poplanihme parentite parvo samo
            for (CategoryItem item : allCategories) {
                if (item.categoryParentId == 0 ) {
                    parentCategories.put(item.categoryId, item);
                }
            }

//sa my4i6 li ili da ti go pravq ako imash vreme da pokajesh posle she gi tormozq dosta
            for (CategoryItem item : allCategories) {
                if (item.categoryParentId != 0) {
                    CategoryItem parent = parentCategories.get(item.categoryParentId);
                    parent.subCategories.add(item);
                }
            }

// super deiba :)
            categoryData.clear();
            categoryData.addAll(parentCategories.values());
            listAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("LAN");
        listDataHeader.add("CATV");


        List<String> lan = new ArrayList<>();
        lan.add("This is Lan");

        List<String> catv = new ArrayList<>();
        catv.add("This is Catv");
        catv.add("This is sss");
        catv.add("This is Catfasdv");


        listHash.put(listDataHeader.get(0), lan);
        listHash.put(listDataHeader.get(1), catv);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //TODO write your code what you want to perform on search

                searchProduct = query;
                fillFragment = "search";

                Toast.makeText(getBaseContext(), "KOR", Toast.LENGTH_LONG).show();

                FragmentManager FM = getFragmentManager();
                FragmentTransaction FT = FM.beginTransaction();
                ProductListFragment fragment = new ProductListFragment();
                FT.replace(R.id.load, fragment);
                FT.addToBackStack("product_fragment");

                FT.commit();
                mDrawerLayout.closeDrawers();

                return true;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                //TODO write your code what you want to perform on search text change
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


}
