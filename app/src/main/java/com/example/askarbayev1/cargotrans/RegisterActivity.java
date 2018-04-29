package com.example.askarbayev1.cargotrans;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    Database db;
    UserInput input;
    TextInputLayout fname_input, lname_input, email_input, phone_input, username_input, psw1_input, psw2_input;
    TextInputEditText fname_text, lname_text, email_text, phone_text, username_text, psw1_text, psw2_text;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        int status = intent.getIntExtra("status", -1);
        db = new Database(getApplicationContext());
        input = new UserInput();
        main(status);
    }
    public void main(int status1){
        final int status = status1;
        fname_input = findViewById(R.id.text_input_fname);
        lname_input = findViewById(R.id.text_input_lname);
        email_input = findViewById(R.id.text_input_email);
        phone_input = findViewById(R.id.text_input_phone);
        username_input = findViewById(R.id.text_input_username);
        psw1_input = findViewById(R.id.text_input_password1);
        psw2_input = findViewById(R.id.text_input_password2);

        fname_text = findViewById(R.id.firstname);
        lname_text = findViewById(R.id.lastname);
        email_text = findViewById(R.id.email);
        phone_text = findViewById(R.id.phone);
        username_text = findViewById(R.id.username);
        psw1_text = findViewById(R.id.password1);
        psw2_text = findViewById(R.id.password2);

        register = (Button)findViewById(R.id.register);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicked", "First");
                if (input.name(fname_input, fname_text) && input.name(lname_input, lname_text) && input.email(email_input, email_text)
                        && input.phone(phone_input, phone_text) && input.username(username_input, username_text)
                        && input.password(psw1_input, psw1_text) && input.passwordMatch(psw2_input, psw1_text, psw2_text)){
                    Log.d("Clicked", "Second");
                    String fname = fname_text.getText().toString();
                    String lname = lname_text.getText().toString();
                    String email = email_text.getText().toString();
                    String phone = phone_text.getText().toString();
                    String username = username_text.getText().toString();
                    String password1 = psw1_text.getText().toString();
                    boolean res = db.register(fname, lname, email, phone, username, password1, status, 0);
                    if (res){
                        Toast.makeText(getApplicationContext(), "you have successfully registered", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("username", username);
                        intent.putExtra("password", password1);
                        intent.putExtra("status", status);
                        startActivity(intent);
                    }
                }
            }
        });


    }
}
