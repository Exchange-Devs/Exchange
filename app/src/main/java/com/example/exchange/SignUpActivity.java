package com.example.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "SignUp Activity";
    private EditText etSignUpUsername;
    private EditText etSignUpPassword;
    private Button btSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etSignUpUsername = findViewById(R.id.etSignUpUsername);
        etSignUpPassword = findViewById(R.id.etSignUpPassword);
        btSignUp = findViewById(R.id.btnSignUp);
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signup button");
                Toast.makeText(SignUpActivity.this, "Signing up", Toast.LENGTH_SHORT).show();
                createUser();
            }
        });
    }

    private void createUser() {

        String  username = etSignUpUsername.getText().toString();
        String password = etSignUpPassword.getText().toString();

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        // Other fields can be set just like any other ParseObject,
        // using the "put" method, like this: user.put("attribute", "its value");
        // If this field does not exists, it will be automatically created

        user.signUpInBackground(e ->
        {
            if (e == null) {
                // Hooray! Let them use the app now.
                Intent intent = new Intent (this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                Log.e(TAG,"issues signing up",e);
                Toast.makeText(this, "failed to signUp", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
