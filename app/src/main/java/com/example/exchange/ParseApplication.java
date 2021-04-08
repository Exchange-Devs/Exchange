package com.example.exchange;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        // Register your parse models
        ParseObject.registerSubclass(Listings.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("3LbOEv7af2DuB6uS9tnyySUrVk52wHWZ1iwb1rtV")
                .clientKey("meB25a5WId1iV2CApXbxikxk6q8vGTVcsWcfp14L")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
