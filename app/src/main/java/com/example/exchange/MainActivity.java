package com.example.exchange;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.exchange.fragments.ListingsFragment;
//import com.example.exchange.fragments.MapFragment;
import com.example.exchange.fragments.MapFragment;
import com.example.exchange.fragments.MessageFragment;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        ivProfileImage = findViewById(R.id.ivProfileImage);


        loadImage();

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId())
                {
                    case R.id.action_home:
                        fragment = new ListingsFragment();
                        break;
                    case R.id.action_message:
                        fragment = new MessageFragment();
                        break;
                    case R.id.action_map:
                        fragment = new MapFragment();
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

        Fragment fragment = new MapFragment();
        getSupportFragmentManager()
                .beginTransaction()
                //.replace(R.id.frame_layout, fragment)
                .commit();
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