package com.example.exchange;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "SignUp Activity";
    private EditText etSignUpUsername;
    private EditText etSignUpPassword;
    private EditText etSignUpEmail;
    private EditText etSignUpRPPassword;
    private TextView tvLogin;
    private Button btSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tvLogin = findViewById(R.id.tvLogin);
        tvLogin.setPaintFlags(tvLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.i(TAG, "onClick SignUp Link");
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        etSignUpEmail = findViewById(R.id.etSignUpEmail);
        etSignUpUsername = findViewById(R.id.etSignUpUsername);
        etSignUpPassword = findViewById(R.id.etSignUpPassword);
        etSignUpRPPassword = findViewById(R.id.etSignUpRPPassword);
        btSignUp = findViewById(R.id.btnSignUp);
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signup button");
                Toast.makeText(SignUpActivity.this, "Signing up", Toast.LENGTH_SHORT).show();
                validatePassword();
            }
        });
    }

    private boolean validatePassword()
    {
        String emailInput = etSignUpEmail.getText().toString().trim();
        String usernameInput = etSignUpUsername.getText().toString().trim();
        String passwordInput = etSignUpPassword.getText().toString().trim();
        String ConfitmpasswordInput = etSignUpRPPassword.getText().toString().trim();
        if (emailInput.isEmpty())
        {
            Toast.makeText(SignUpActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(usernameInput.isEmpty())
        {
            Toast.makeText(SignUpActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (passwordInput.isEmpty())
        {
            Toast.makeText(SignUpActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (passwordInput.length()<5)
        {
            Toast.makeText(SignUpActivity.this, "Password must be at least 5 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!passwordInput.equals(ConfitmpasswordInput))
        {
            Toast.makeText(SignUpActivity.this, "Password do not match", Toast.LENGTH_SHORT).show();;
            return false;
        }
        else
        {
            createUser();
            return true;
        }
    }

    private void createUser() {

        String  username = etSignUpUsername.getText().toString();
        String password = etSignUpPassword.getText().toString();
        String email = etSignUpEmail.getText().toString();

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        // Other fields can be set just like any other ParseObject,
        // using the "put" method, like this: user.put("attribute", "its value");
        // If this field does not exists, it will be automatically created
        user.signUpInBackground(e ->
        {
            if (e == null) {
                // Hooray! Let them use the app now.
                Intent intent = new Intent (SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                Log.e(TAG,"issues signing up",e);
                Toast.makeText(SignUpActivity.this, "failed to signUp", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
