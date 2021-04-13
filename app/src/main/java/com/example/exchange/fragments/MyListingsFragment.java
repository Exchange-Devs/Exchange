package com.example.exchange.fragments;

import android.util.Log;

import com.example.exchange.Listings;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MyListingsFragment extends ListingsFragment
{
    @Override
    protected void queryPosts(int limit, int skip)
    {
        ParseQuery<Listings> query = ParseQuery.getQuery(Listings.class);
        query.include(Listings.KEY_USER);
        query.setLimit(limit);
        query.whereEqualTo(Listings.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Listings.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Listings>()
        {
            @Override
            public void done(List<Listings> listings, ParseException e)
            {
                if (e != null)
                {
                    Log.e(TAG, "Issue with getting listings", e);
                    return;
                }
                for (Listings list : listings)
                {
                    Log.i(TAG, "Post: " + list.getDescription() + ", username: " + list.getUser().getUsername());
                }
                allListings.addAll(listings);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
