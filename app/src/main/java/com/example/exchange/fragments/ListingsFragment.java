package com.example.exchange.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.exchange.ListAdapter;
import com.example.exchange.Listings;
import com.example.exchange.EndlessRecyclerViewScrollListener;
import com.example.exchange.R;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ListingsFragment extends Fragment
{
    public static final String TAG = "ListingsFragment";
    private RecyclerView rvListings;
    protected ListAdapter adapter;
    protected List<Listings> allListings;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    int counter = 1;


    public ListingsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        rvListings = view.findViewById(R.id.rvListings);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        allListings = new ArrayList<>();
        adapter = new ListAdapter(getContext(), allListings);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager)
        {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view)
            {
                queryPosts(10,10 * counter++);
            }
        };
        rvListings.setAdapter(adapter);
        rvListings.setLayoutManager(linearLayoutManager);
        rvListings.addOnScrollListener(endlessRecyclerViewScrollListener);
        queryPosts(10,0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            swipeContainer.setProgressBackgroundColorSchemeColor(getResources().getColor(android.R.color.black, null));
        }
        else
        {
            swipeContainer.setProgressBackgroundColorSchemeColor(getResources().getColor(android.R.color.black));
        }

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG,"fetching new data!");
                queryPosts(10,0);
                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        queryPosts(10, 0);
        adapter.notifyDataSetChanged();
    }

    protected void queryPosts(int limit, int skip)
    {
        ParseQuery<Listings> query = ParseQuery.getQuery(Listings.class);
        query.include(Listings.KEY_USER);
        query.setLimit(limit);
        query.setSkip(skip);
        query.addDescendingOrder(Listings.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Listings>()
        {
            @Override
            public void done(List<Listings> listings, ParseException e)
            {
                if (e != null)
                {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Listings list : listings)
                {
                    Log.i(TAG, "Post: " + list.getDescription() + ", username: " + list.getUser().getUsername());
                }
                if (skip <= 0)
                {
                    allListings.clear();
                }
                allListings.addAll(listings);
                adapter.notifyDataSetChanged();
            }
        });
    }
}