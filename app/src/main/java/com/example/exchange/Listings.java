package com.example.exchange;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Listings")
public class Listings extends ParseObject
{
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE= "productImage";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_KEY = "createdAt";
    public static final String KEY_UPDATED_KEY = "updatedAt";
    public static final String KEY_TITLE = "title";
    public static final String KEY_PRICE = "price";
    public static final String KEY_CONDITION = "condition";
    public static final String KEY_CATEGORY = "category";


    public String getDescription()
    {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description)
    {
        put(KEY_DESCRIPTION, description);
    }

    public String getTitle() {return getString(KEY_TITLE);}

    public void setTitle(String title) {put(KEY_TITLE, title);}

    public ParseFile getImage()
    {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile)
    {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser()
    {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user)
    {
        put(KEY_USER, user);
    }

    public int getPrice() {return getInt(KEY_PRICE); }

    public void setPrice(int price) {put(KEY_PRICE, price);}

    public String getCondition() {return getString(KEY_CONDITION);}

    public void setConditioon(String conditioon) {put(KEY_CONDITION,conditioon);}

   public String getCategory() {return getString(KEY_CATEGORY);}

   public void setCategory(String category) {put(KEY_CATEGORY,category);}

    public Date getCreatedKeyAt() {return getCreatedAt();}

    public Date getUpdatedKeyAt() {return getUpdatedAt();}
    
}
