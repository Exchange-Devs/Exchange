package com.example.exchange.fragments;

import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exchange.ListAdapter;
import com.example.exchange.Listings;
import com.example.exchange.R;

import java.util.ArrayList;
import java.util.List;

public class ListingSearch extends Fragment {


    public static final String TAG = "ListingsSearch";
    private RecyclerView rvListings;
    protected ListAdapter adapter;
    protected List<Listings> allListings;




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

        allListings = new ArrayList<>();
        adapter = new ListAdapter(getContext(), allListings);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        rvListings.setAdapter(adapter);
        rvListings.setLayoutManager(gridLayoutManager);

    }

    public boolean OnCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
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
return  true;
//        return super.onCreateOptionsMenu(menu);
    }

    private MenuInflater getMenuInflater() {
        return getMenuInflater();
    }


}
