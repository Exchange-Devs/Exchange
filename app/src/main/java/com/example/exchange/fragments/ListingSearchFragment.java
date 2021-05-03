package com.example.exchange.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exchange.ListAdapter;
import com.example.exchange.Listings;
import com.example.exchange.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ListingSearchFragment extends Fragment
{
    public static final String TAG = "ListingsSearchFragment";
    private RecyclerView rvListings;
    protected ListAdapter adapter;
    protected List<Listings> allListings;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        rvListings = view.findViewById(R.id.rvListings);
        toolbar = view.findViewById(R.id.toolbar4);

        toolbar.inflateMenu(R.menu.menu_search);
        
        allListings = new ArrayList<>();
        adapter = new ListAdapter(getContext(), allListings);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvListings.setAdapter(adapter);
        rvListings.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        setHasOptionsMenu(true);
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            // https://courses.codepath.org/courses/android_university/unit/6#!exercises
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

//    public boolean onCreateOptionsMenu(Menu menu){
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_search, menu);
//
//        MenuItem searchItem = menu.findItem(R.id.item_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//
//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            // https://courses.codepath.org/courses/android_university/unit/6#!exercises
//            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//            return  true;
////        return super.onCreateOptionsMenu(menu);
//    }

//    private MenuInflater getMenuInflater() {
//        return getMenuInflater();
//    }

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