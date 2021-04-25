package com.example.exchange;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class ComposeActivity extends AppCompatActivity
{
    public static final String TAG = "ComposeActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private EditText etDescription, etPrice, etTitle;
    private ImageView ivBackButtonB;
    private ConstraintLayout clCapture2;
    private Button btnSubmit;
    private ImageView ivPostImage;
    private ProgressBar progressBar;
    private RadioButton rbSell, rbExchange;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    private AutoCompleteTextView actvCondition, actvCategory;
    private TextInputLayout tilPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        etDescription = findViewById(R.id.etDescription);
        ivBackButtonB = findViewById(R.id.ivBackButtonB);
        clCapture2 = findViewById(R.id.clCapture2);
        ivPostImage = findViewById(R.id.ivPostImage);
        actvCondition = findViewById(R.id.actvCondition);
        actvCategory = findViewById(R.id.actvCategory);
        etPrice = findViewById(R.id.etPrice);
        rbSell = findViewById(R.id.rbSell);
        rbExchange = findViewById(R.id.rbExchange);
        tilPrice = findViewById(R.id.tilPrice);
        btnSubmit = findViewById(R.id.btnSubmit);
        etTitle = findViewById(R.id.etTitle);
        //progressBar = findViewById(R.id.progressBar);

        tilPrice.setVisibility(View.GONE);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, getResources().getStringArray(R.array.condition));

        ArrayAdapter<String> adapt = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, getResources().getStringArray(R.array.category));

        actvCondition.setThreshold(0);
        actvCondition.setAdapter(adapter);
        actvCondition.setInputType(0);

        actvCategory.setThreshold(0);
        actvCategory.setAdapter(adapt);
        actvCategory.setInputType(0);

        actvCondition.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    actvCondition.showDropDown();
            }
        });

        actvCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    actvCategory.showDropDown();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String description = etDescription.getText().toString();
                String title = etTitle.getText().toString();
                int price = 0;
                if(!etPrice.getText().toString().equals("") && etPrice.getVisibility() == View.VISIBLE)
                {
                    price = Integer.parseInt(etPrice.getText().toString().substring(0,0) + etPrice.getText().toString().substring(1));
                }

                String condition = actvCondition.getText().toString();
                String category = actvCategory.getText().toString();
                if (title.isEmpty())
                {
                    etTitle.setError("Title cannot be empty");
                    return;
                }

                if(etPrice.getText().toString().isEmpty())
                {
                    etPrice.setError("Price cannot be empty");
                    return;
                }

                if (category.isEmpty())
                {
                    actvCondition.setError("Category cannot be empty");
                    return;
                }
                if (condition.isEmpty())
                {
                    actvCondition.setError("Condition cannot be empty");
                    return;
                }
                if (description.isEmpty())
                {
                    etDescription.setError("Description cannot be empty");
                    return;
                }

                if (photoFile == null || ivPostImage.getDrawable() == null)
                {
                    Toast.makeText(ComposeActivity.this, "There is no Image!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description,title,price,condition,category,currentUser,photoFile);
            }
        });

        rbSell.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tilPrice.setVisibility(View.VISIBLE);
            }
        });

        rbExchange.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tilPrice.setVisibility(View.GONE);
                etPrice.setText("0");
            }
        });

        etPrice.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (etPrice.getText().toString().startsWith("$"))
                    return;
                etPrice.setTextKeepState("$" + etPrice.getText().toString());
                etPrice.setSelection(etPrice.getText().length());
            }
        });


        clCapture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        ivBackButtonB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ComposeActivity.this, MainActivity.class);
                startActivity(intent);
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
        Uri fileProvider = FileProvider.getUriForFile(ComposeActivity.this, "com.exchange.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(ComposeActivity.this.getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPostImage.setImageBitmap(takenImage);
            }
            else { // Result was a failure
                Toast.makeText(ComposeActivity.this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(ComposeActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }
    private void savePost(String description, String title, int price, String condition, String category, ParseUser currentUser, File photoFile)
    {
        Listings listings = new Listings();
        listings.setDescription(description);
        listings.setTitle(title);
        listings.setPrice(price);
        listings.setConditioon(condition);
        listings.setCategory(category);
        listings.setImage(new ParseFile(photoFile));
        listings.setUser(currentUser);
        listings.saveInBackground(new SaveCallback()
        {

            @Override
            public void done(ParseException e)
            {
                if (e != null)
                {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(ComposeActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful!!!");
                //progressBar.setVisibility(View.GONE);
                etDescription.setText("");
                etTitle.setText("");
                etPrice.setText("");
                actvCategory.setText("");
                actvCondition.setText("");
                ivPostImage.setImageResource(0);
            }
        });
    }

}
