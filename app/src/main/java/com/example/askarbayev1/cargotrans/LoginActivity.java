package com.example.askarbayev1.cargotrans;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout text_input_username, text_input_password;
    TextInputEditText username, password;
    Button signIn;
    TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        int status = intent.getIntExtra("status", -1);
        if (status != -1){
            main();
        }

    }

    public void main(){
        text_input_username = findViewById(R.id.text_input_username);
        text_input_password = findViewById(R.id.text_input_password);
        signIn = findViewById(R.id.signin);
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

}

