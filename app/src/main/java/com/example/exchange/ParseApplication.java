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
                .applicationId("WkbscYbPLEnL5ujla6PkeWuFxkDFTUVmxXFWGiFz")
                .clientKey("RIsx8T1KG0nrCVrnxB3b1jK253r759R8zAdO3sh7")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
