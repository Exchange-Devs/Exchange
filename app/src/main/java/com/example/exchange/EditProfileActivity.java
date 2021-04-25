package com.example.exchange;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.exchange.fragments.MyProfileFragment;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class EditProfileActivity extends AppCompatActivity
{
    public static final String TAG = "EditProfileActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 40;
    private File photoFile;
    private String photoFileName = "profile.jpg";
    private ConstraintLayout clCapture;
    private ImageView ivProfileImage2, ivBackButton;
    private ParseUser user;
    private Button btnSubmit, btnSignOut;
    private EditText etEditUsername, etEdiPassword, etRPPassword;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        user = ParseUser.getCurrentUser();
        clCapture = findViewById(R.id.clCapture);
        ivProfileImage2 = findViewById(R.id.ivProfileImage2);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSignOut = findViewById(R.id.btnSignOut);
        ivBackButton =findViewById(R.id.ivBackButton);
        etEdiPassword = findViewById(R.id.etEdiPassword);
        etEditUsername = findViewById(R.id.etEditUsername);
        etRPPassword = findViewById(R.id.etRPPassword);

        etEditUsername.setText(user.getUsername());
        ParseFile file = user.getParseFile("profileImage");
        if(file != null)
        {
            String path = file.getUrl();
            Glide.with(this).load(path).transform(new CircleCrop()).into(ivProfileImage2);
        }
        else
        {
            Glide.with(this).load(R.drawable.profile_pic).transform(new CircleCrop()).into(ivProfileImage2);
        }

        btnSubmit.setEnabled(true);

        clCapture.setOnClickListener(v -> {
            launchCamera();
        });


        btnSignOut.setOnClickListener(v -> {
            ParseUser.logOutInBackground();
            Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (photoFile != null) {
                    ParseFile pic = new ParseFile(photoFile);
                    user.put("profileImage", pic);
                }

                if(!etEditUsername.getText().toString().equals(""))
                {
                    Log.e(TAG, "Issue");
                    user.setUsername(etEditUsername.getText().toString());
                }

                if (validate() && !etEdiPassword.getText().toString().isEmpty())
                {
                    Log.e(TAG, "Issue2");
                    user.setPassword(etEdiPassword.getText().toString());
                }
               if (etEdiPassword.getText().toString().isEmpty() || (!etEdiPassword.getText().toString().isEmpty() && validate())) {
                   user.saveInBackground(new SaveCallback() {
                       @Override
                       public void done(ParseException e) {
                           if (e != null) {
                               Log.e(TAG, "Issue saving profile image", e);
                           }
                           finish();
                       }
                   });
               }
            }
        });
        etEditUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
            {
                noFocus(v);
            }
        });
        etEdiPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
            {
                noFocus(v);
            }
        });
        etRPPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
            {
                noFocus(v);
            }
        });
        ivBackButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    private void launchCamera()
    {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(this, "com.exchange.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            // Start the image capture intent to take photo
            Log.i(TAG, "Lauching Camera");
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                Glide.with(this).load(takenImage).transform(new CircleCrop()).into(ivProfileImage2);
               Log.i(TAG, "Loaded Image");
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
           Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    public void noFocus(View view)
    {
        InputMethodManager ip = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        ip.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    private boolean validate()
    {
        String passwordInput = etEdiPassword.getText().toString();
        String ConfitmpasswordInput = etRPPassword.getText().toString();

            if (passwordInput.length()> 0 && passwordInput.length()<5)
            {
                Toast.makeText(EditProfileActivity.this, "Password must be at least 5 characters", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!passwordInput.equals(ConfitmpasswordInput))
            {
                Toast.makeText(EditProfileActivity.this, "Password do not match", Toast.LENGTH_SHORT).show();;
                return false;
            }
            return true;
    }
}