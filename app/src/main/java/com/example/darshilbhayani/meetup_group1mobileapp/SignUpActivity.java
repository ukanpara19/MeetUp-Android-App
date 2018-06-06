package com.example.darshilbhayani.meetup_group1mobileapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity{
    Pattern pattern;
    Matcher matcher;
    Button signup;

    EditText name_edittext_signup,email_edittext_signup,password_edittext_signup,mobileNumber_edittext_signup;
    String name_signup,email_signup,password_signup,mobilenumber_signup;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private HashMap<String, String> contactHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup );

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        signup = findViewById(R.id.button5);
        name_edittext_signup =  findViewById(R.id.editText6);
        email_edittext_signup = findViewById(R.id.editText7);
        password_edittext_signup = findViewById(R.id.editText8);
        mobileNumber_edittext_signup =  findViewById(R.id.editText9);
        final ProgressDialog pd = new ProgressDialog(SignUpActivity.this);

        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                name_signup =  name_edittext_signup.getText().toString();
                email_signup = email_edittext_signup.getText().toString();
                password_signup = password_edittext_signup.getText().toString();
                mobilenumber_signup = mobileNumber_edittext_signup.getText().toString();
                showProgressBar(pd);
                if(!validateInputData(name_signup,"Name")){
                    if(pd.isShowing()){
                        pd.dismiss();
                        pd.hide();
                        Log.d("progress bar","progress bar");
                    }return;
                }
                if(!validateInputData(email_signup,"Email")){
                    if(pd.isShowing()){
                        pd.dismiss();
                        pd.hide();
                        Log.d("progress bar","progress bar");
                    }return;
                }
                if(!validateEmailFormat(email_signup)){
                    if(pd.isShowing()){
                        pd.dismiss();
                        pd.hide();
                        Log.d("progress bar","progress bar");
                    }Toast.makeText(getApplicationContext(),"Invalid Email Address Format" ,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!validateInputData(password_signup,"Password")){
                    if(pd.isShowing()){
                        pd.dismiss();
                        pd.hide();
                        Log.d("progress bar","progress bar");
                    }
                    return;
                }
                if(!validateInputData(mobilenumber_signup,"Mobile number")){
                    pd.dismiss();
                    pd.hide();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email_signup,password_signup).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            onAuthSuccess(task.getResult().getUser(),email_signup,name_signup,mobilenumber_signup);
                            Toast.makeText(getApplicationContext(),"Signed Up Successfully.",Toast.LENGTH_LONG).show();
                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("logged_in", "yes");
                            editor.apply();
                            pd.dismiss();
                            pd.dismiss();
                            Intent i = new Intent(getApplicationContext(),MapsActivity.class);
                            startActivity(i);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Email ID already in use.",Toast.LENGTH_LONG).show();

                            return;
                        }
                    }
                });
            }
        });
    }
    private void showProgressBar(ProgressDialog pd) {
        pd.setMessage("Signing Up");
        pd.show();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    private void onAuthSuccess(FirebaseUser user, String email_signup, String name_signup, String mobilenumber_signup) {
        String username = user.getEmail().replace(".",",");
        writeNewUser(username,name_signup,email_signup,mobilenumber_signup);
    }

    private void writeNewUser(String username, String name_signup, String email_signup, String mobilenumber_signup) {
        User user = new User(name_signup,email_signup,mobilenumber_signup);
        mDatabase.child("users").child(username).setValue(user);
        signup.setEnabled(true);
    }

    public HashMap<String, String> getContactHash() {
        return contactHash;
    }

    private boolean isValidPassword(String password) {
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean validateEmailFormat(String email_signup) {
        return Patterns.EMAIL_ADDRESS.matcher(email_signup).matches();
    }

    private boolean validateInputData(String str, String str2) {
        Log.d(str2,str);
        if(TextUtils.isEmpty(str)){
            Toast.makeText(getApplicationContext(),"Enter "+str2 ,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
