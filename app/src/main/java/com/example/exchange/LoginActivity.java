package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity
{

    public static final String TAG = "LoginActivity";
    private EditText etUsername, etPassword;
    private Button btnLogin, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser() != null)
        {
            goMainActivity();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.i(TAG, "onClick signup button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                signUpUser(username, password);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });
    }

        private void loginUser(String username, String password)
        {
            Log.i(TAG, "Attempting to login user " + username);

            ParseUser.logInInBackground(username, password, new LogInCallback()
            {
                @Override
                public void done(ParseUser user, ParseException e)
                {
                    if (e != null)
                    {
                        Log.i(TAG, "Issue with login", e);
                        Toast.makeText(com.example.exchange.LoginActivity.this, "Issue With Login", Toast.LENGTH_SHORT).show();

                        return;
                    }
                    goMainActivity();
                    Log.i(TAG, "Success", e);
                    Toast.makeText(com.example.exchange.LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void signUpUser(String username, String password)
        {
            ParseUser newUser = new ParseUser();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.signUpInBackground(new signUpCallback()
            {
                @Override
                public void done(java.text.ParseException e)
                {
                    if (e != null)
                   {
                        Log.i(TAG, "Issue with signup", e);
                        Toast.makeText(com.example.exchange.LoginActivity.this, "Issue With Signup", Toast.LENGTH_SHORT).show();

                        return;
                    }
                    goMainActivity();
                    Log.i(TAG, "Success", e);
                    Toast.makeText(com.example.exchange.LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                }
            });
      }

    private void goMainActivity()
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

}
