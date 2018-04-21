package com.example.askarbayev1.cargotrans;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;

public class UserInput {
    public static boolean firstLastNameCheck(String name){
        return name.matches("[A-Z]?[a-z]+");
    }
    public static boolean emailCheck(String email){
        return email.matches("[a-zA-Z0-9]+@[a-zA-Z]+.[a-zA-Z]+");
    }
    public static boolean phoneCheck(String phone){
        return phone.matches("[1-9]\\d{2}-[1-9]\\d{2}-\\d{4}");
    }
    public static boolean usernameCheck(String username){
        return username.matches("[a-zA-Z]+");
    }
    public static boolean passwordCheck(String password){
        return password.matches(".+");
    }

    public static boolean name(TextInputLayout layout, TextInputEditText text){
        try{
            String name = text.getText().toString();
            if (!firstLastNameCheck(name)){
                throw new NumberFormatException();
            }
            layout.setErrorEnabled(false);
            return true;
        }catch (NumberFormatException ex){
            layout.setError("enter valid name");
            return false;
        }
    }

    public static boolean email(TextInputLayout layout, TextInputEditText text){
        try{
            String email = text.getText().toString();
            if (!emailCheck(email)){
                throw new NumberFormatException();
            }
            layout.setErrorEnabled(false);
            return true;
        }catch (NumberFormatException ex){
            layout.setError("enter valid email address");
            return false;
        }
    }

    public static boolean phone(TextInputLayout layout, TextInputEditText text){
        Log.d("Phone", text.getText().toString());
        try{
            String phone = text.getText().toString();
            if (!phoneCheck(phone)){
                throw new NumberFormatException();
            }
            layout.setErrorEnabled(false);
            return true;
        }catch (NumberFormatException ex){
            layout.setError("enter valid phone number");
            return false;
        }
    }

    public static boolean username(TextInputLayout layout, TextInputEditText text){
        try{
            String username = text.getText().toString();
            if (!usernameCheck(username)){
                throw new NumberFormatException();
            }
            layout.setErrorEnabled(false);
            return true;
        }catch (NumberFormatException ex){
            layout.setError("enter valid username");
            return false;
        }
    }
    public static boolean password(TextInputLayout layout, TextInputEditText text){
        try{
            String password = text.getText().toString();
            if (!passwordCheck(password)){
                throw new NumberFormatException();
            }
            layout.setErrorEnabled(false);
            return true;
        }catch (NumberFormatException ex){
            layout.setError("enter valid password");
            return false;
        }
    }
    public static boolean passwordMatch(TextInputLayout layout, TextInputEditText pass1, TextInputEditText pass2){
        try{
            String password1 = pass1.getText().toString();
            String password2 = pass2.getText().toString();
            if (!password1.equals(password2)){
                throw new NumberFormatException();
            }
            layout.setErrorEnabled(false);
            return true;
        }catch (NumberFormatException ex){
            layout.setError("passwords doesn't match");
            return false;
        }
    }


}
