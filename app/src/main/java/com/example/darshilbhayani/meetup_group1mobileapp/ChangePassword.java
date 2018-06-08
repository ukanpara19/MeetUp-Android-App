package com.example.darshilbhayani.meetup_group1mobileapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity{
    FirebaseUser user;
    protected void onCreate(Bundle savedInstanceState) {
        final EditText oldPassword, newPassword, reTypeNewPassword;
        final Button changePassword;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);

        oldPassword = findViewById(R.id.editText11);
        newPassword = findViewById(R.id.editText11);
        reTypeNewPassword = findViewById(R.id.editText11);
        changePassword = findViewById(R.id.button2);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(oldPassword.getText())){
                    Toast.makeText(ChangePassword.this,"Enter Old Password",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(newPassword.getText())){
                    Toast.makeText(ChangePassword.this,"Enter new Password",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(reTypeNewPassword.getText())){
                    Toast.makeText(ChangePassword.this,"Re-Enter new Password",Toast.LENGTH_LONG).show();
                    return;
                }

                SharedPreferences editor = getApplicationContext().getSharedPreferences(LoginDemo.MY_PREFS_NAME, MODE_PRIVATE);
                String loogedInUser = editor.getString("Email_ID","darshilbhayani1992@gmail.com");

                user = FirebaseAuth.getInstance().getCurrentUser();
                AuthCredential credential = EmailAuthProvider
                        .getCredential(loogedInUser,oldPassword.getText().toString().trim());

                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(newPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("Change Password", "Password updated");
                                                oldPassword.setText("");
                                                newPassword.setText("");
                                                reTypeNewPassword.setText("");
                                                Toast.makeText(ChangePassword.this,"Password Updated",Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.d("Change Password", "Error password not updated");
                                                Toast.makeText(ChangePassword.this,"Password Not Updated",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Log.d("Change Password", "Error auth failed");
                                    Toast.makeText(ChangePassword.this,"Error auth failed.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }
}
