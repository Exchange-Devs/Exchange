package com.example.exchange;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject
{
    public static final String KEY_USER = "user";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_IMAGE = "profileImage";


    public String getBody() {
        return getString(KEY_MESSAGE);
    }

    public void setBody(String body) { put(KEY_MESSAGE, body); }

    public ParseUser getUser()
    {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user)
    {
        put(KEY_USER, user);
    }

    public ParseFile getImage()
    {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile)
    {
        put(KEY_IMAGE, parseFile);
    }
}