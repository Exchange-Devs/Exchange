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
    public static final String KEY_IMAGE= "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_KEY = "createdAt";

    public String getDescription()
    {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description)
    {
        put(KEY_DESCRIPTION, description);
    }

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

    public Date getCreatedKeyAt() {return getCreatedAt();}
    
}