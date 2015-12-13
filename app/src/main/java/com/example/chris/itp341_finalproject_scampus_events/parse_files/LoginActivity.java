package com.example.chris.itp341_finalproject_scampus_events.parse_files;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chris.itp341_finalproject_scampus_events.HomeScreenActivity;
import com.example.chris.itp341_finalproject_scampus_events.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    Button login, signUp;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //TODO: check if user already logged in
        ParseUser user = ParseUser.getCurrentUser();

        if (user != null) {
            Intent intent = new Intent(this, HomeScreenActivity.class);
            startActivity(intent);
            finish();
        } else {

            login = (Button) findViewById(R.id.login);
            signUp = (Button) findViewById(R.id.signup);
            email = (EditText) findViewById(R.id.email);
            password = (EditText) findViewById(R.id.password);

            //TODO: set login listner
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseUser user = new ParseUser();
                    user.setUsername(email.getText().toString());
                    user.setPassword(password.getText().toString());

                    ParseUser.logInInBackground(email.getText().toString(), password.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (e == null) {
                                Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), Signup.class);
                    startActivity(intent);
                }
            });

        }

    }
}
