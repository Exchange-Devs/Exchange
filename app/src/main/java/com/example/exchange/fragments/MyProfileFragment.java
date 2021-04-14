package com.example.exchange.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.exchange.EndlessRecyclerViewScrollListener;
import com.example.exchange.ListAdapter;
import com.example.exchange.Listings;
import com.example.exchange.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MyProfileFragment extends Fragment
{
    public static final String TAG = "MyListingsFragment";
    private RecyclerView rvListings;
    protected ListAdapter adapter;
    protected List<Listings> allListings;
    private TextView tvVisibility;
    private ImageView ivProfileImage;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    int counter = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvListings = view.findViewById(R.id.rvListings);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        tvVisibility = view.findViewById(R.id.tvVisibility);
        allListings = new ArrayList<>();
        adapter = new ListAdapter(getContext(), allListings);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryPosts(10, 10 * counter++);
            }
        };
        rvListings.setAdapter(adapter);
        rvListings.setLayoutManager(gridLayoutManager);
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

        ParseFile file =  ParseUser.getCurrentUser().getParseFile("profileImage");
        if(file != null)
        {
            String path = file.getUrl();
            Glide.with(this).load(path).transform(new CircleCrop()).into(ivProfileImage);
        }
        else
        {
            Glide.with(this).load(R.drawable.ic_baseline_tag_faces_24).transform(new CircleCrop()).into(ivProfileImage);
        }
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
                allListings.clear();
                allListings.addAll(listings);
                if(listings.size() == 0)
                {
                    tvVisibility.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
