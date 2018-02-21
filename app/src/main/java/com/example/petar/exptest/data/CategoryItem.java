package com.example.petar.exptest.data;


import java.util.ArrayList;

public class CategoryItem {
    public int categoryId;
    public String categorName;
    public int categoryParentId;
    public int categorySort;
    public String categoryImage;
    public ArrayList<CategoryItem> subCategories = new ArrayList<>();

}
