package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.exchange.fragments.ListingsFragment;
import com.example.exchange.fragments.MyProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity
{
    public static final String TAG = "MainActivity";
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigation;
    private Toolbar toolbar;
    private ImageView ivProfileImage;
    private ImageButton btnComposeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        btnComposeBtn = findViewById(R.id.btnComposeBtn);

        btnComposeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ComposeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loadImage();

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        fragment = new ListingsFragment();
                        break;
                    case R.id.action_message:
                        fragment = new ListingsFragment();
                        break;
                    case R.id.action_search:
                        fragment = new ListingsFragment();
                        break;
                    case R.id.action_profile:
                    default:
                        fragment = new MyProfileFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigation.setSelectedItemId(R.id.action_home);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean OnCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bottom_navigation, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
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
                ListAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onResume()
    {
        super.onResume();

        loadImage();
    }

    private void loadImage()
    {
        ivProfileImage.setImageDrawable(null);
        ParseFile file = ParseUser.getCurrentUser().getParseFile("profileImage");
        if (file != null) {
            String path = file.getUrl();
            Glide.with(this).load(path).transform(new CircleCrop()).into(ivProfileImage);
        } else {
            Glide.with(this).load(R.drawable.profile_pic).transform(new CircleCrop()).into(ivProfileImage);
        }
    }
}