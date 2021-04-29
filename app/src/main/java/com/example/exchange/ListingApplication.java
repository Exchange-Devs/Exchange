package com.example.exchange;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ListingApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        // Register your parse models
        ParseObject.registerSubclass(Listings.class);
        ParseObject.registerSubclass(Message.class);

        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // any network interceptors must be added with the Configuration Builder given this syntax
        builder.networkInterceptors().add(httpLoggingInterceptor);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("WkbscYbPLEnL5ujla6PkeWuFxkDFTUVmxXFWGiFz")
                .clientKey("RIsx8T1KG0nrCVrnxB3b1jK253r759R8zAdO3sh7")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
