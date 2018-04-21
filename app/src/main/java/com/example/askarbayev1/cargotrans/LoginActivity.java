package com.example.askarbayev1.cargotrans;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout text_input_username, text_input_password;
    TextInputEditText username, password;
    Button signIn;
    TextView register;
    Database db;
    UserInput input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new Database(getApplicationContext());
        input = new UserInput();
        Intent intent = getIntent();
        int status = intent.getIntExtra("status", -1);
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        if (username!=null){
            Log.d("Username", username);
            Log.d("password", password);
        }
        if (status != -1){
            main(status, username, password);
        }

    }

    public void main(final int status, String usern, String pass){
        text_input_username = findViewById(R.id.text_input_username);
        text_input_password = findViewById(R.id.text_input_password);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.signin);

        if (usern!=null && pass!=null){
            Log.d("usern", usern);
            Log.d("pass", pass
            );
            username.setText(usern);
            password.setText(pass);
        }


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input.name(text_input_username, username) && input.password(text_input_password, password)){
                    final String fusername = username.getText().toString();
                    final String fpassword = password.getText().toString();
                    int user_id = db.signIn(fusername, fpassword, status);
                    Log.d("USER_ID: ", user_id+"");
                    if (user_id != -1){
                        Intent intent = new Intent(getApplicationContext(), CentralActivity.class);
                        intent.putExtra("status", status);
                        intent.putExtra("user_id", user_id);
                        startActivity(intent);
                    }
                    else {
                        text_input_username.setError("username or password doesn't exist");
                        text_input_password.setError("username or password doesn't exist");
                    }
                }
                else {
                    text_input_username.setError("enter valid username");
                    text_input_password.setError("enter valid password");
                }

            }
        });

        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.putExtra("status", status);
                startActivity(intent);
            }
        });
    }

}

