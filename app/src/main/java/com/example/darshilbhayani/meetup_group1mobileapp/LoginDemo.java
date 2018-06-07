package com.example.darshilbhayani.meetup_group1mobileapp;

import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.HashMap;



public class LoginDemo extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button signinButton,registerButton;
    TextView forgot_password, login_with_facebook;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    ImageView forgot_password_image;
    EditText usernameEdittext,passwordEdittext;
    private CallbackManager mCallbackManager;
    private DatabaseReference mDatabase;
    String loginUsername,loginPassword;
    ProgressDialog pd;
    String fbEmailId;
    private HashMap<String, String> contactHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        signinButton = findViewById(R.id.button4);
        registerButton =  findViewById(R.id.button);
        usernameEdittext =  findViewById(R.id.editText4);
        passwordEdittext = findViewById(R.id.editText5);
        forgot_password = findViewById(R.id.planName);
        login_with_facebook = findViewById(R.id.sourceLoc);
        forgot_password_image =  findViewById(R.id.imageView4);

        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        System.out.println("onSuccess");
                        pd = new ProgressDialog(LoginDemo.this);
                        String accessToken = loginResult.getAccessToken().getToken();
                        Log.i("accessToken", accessToken);
                        pd.setMessage("Signing In..");
                        pd.show();
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.i("LoginActivity", response.toString());
                                // Get facebook data from login
                                Bundle bFacebookData = getFacebookData(object);
                                try {
                                    fbEmailId = object.getString("email");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try{
                                    writeNewUser(bFacebookData.getString("email"),bFacebookData.getString("first_name"),bFacebookData.getString("last_name"));
                                }
                                catch (Exception e){
                                    Toast.makeText(getApplicationContext(),"Error while fetching data from facebook",Toast.LENGTH_LONG).show();
                                    Log.d("Error",e+"");
                                }

                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "first_name, last_name, email"); // Parámetros que pedimos a facebook
                        request.setParameters(parameters);
                        Log.d("facebook data",parameters+"");
                        request.executeAsync();
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d("onCancel","facebook");
                        Toast.makeText(LoginDemo.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("onError",exception+"");
                        if (exception instanceof FacebookAuthorizationException) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                        }
                        Toast.makeText(LoginDemo.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        login_with_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginDemo.this, Arrays.asList("public_profile", "email"));
            }
        });


        forgot_password_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUsername = usernameEdittext.getText().toString();
                if(TextUtils.isEmpty(loginUsername)){
                    Toast.makeText(LoginDemo.this,"Enter Email ID to reset password",Toast.LENGTH_SHORT).show();
                    return;
                }
                resetPassword(loginUsername);
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUsername = usernameEdittext.getText().toString();
                if(TextUtils.isEmpty(loginUsername)){
                    Toast.makeText(LoginDemo.this,"Enter Email ID to reset password",Toast.LENGTH_SHORT).show();
                    return;
                }
                resetPassword(loginUsername);
            }
        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sign In","Signing In");
                loginUsername = usernameEdittext.getText().toString();
                loginPassword = passwordEdittext.getText().toString();

                if(TextUtils.isEmpty(loginUsername)){
                    Toast.makeText(LoginDemo.this,"Enter Email ID",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!validateEmailFormat(loginUsername)){
                    Toast.makeText(LoginDemo.this,"Invalid Email Address Format" ,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(loginPassword)){
                    Toast.makeText(LoginDemo.this,"Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }

                pd = new ProgressDialog(LoginDemo.this);
                pd.setMessage("Signing In..");
                pd.show();
                validateLogin(loginUsername,loginPassword);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginDemo.this,SignUpActivity.class);
                startActivity(i);
            }
        });
    }
    private void writeNewUser(String email, String first_name, String last_name) {
        Log.d("checking for perm","got permission");
        String username = email.trim().replace(".",",");
        User user = new User(first_name + " " + last_name,email,"");
        Log.d("checking for perm",user+"");
        mDatabase.child("users").child(username).setValue(user);
    }
    private Bundle getFacebookData(JSONObject object) {
        try {
            Bundle bundle = new Bundle();
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            return bundle;
        }
        catch(JSONException e) {
            Log.d("Error fetching data","Error parsing JSON");
        }
        return null;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("Facebook Token", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Firebase Successful", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("user",user+"");
                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("logged_in", "yes");
                            editor.putString("Email_ID",fbEmailId);
                            editor.apply();
                            Intent i = new Intent(LoginDemo.this,MapsActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            pd.hide();
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Firebase Failed", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginDemo.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void resetPassword(final String loginUsername) {
        mAuth.sendPasswordResetEmail(loginUsername).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Reset password link sent.",Toast.LENGTH_SHORT).show();
                    usernameEdittext.setText("");
                    passwordEdittext.setText("");
                }
                else {
                    Toast.makeText(getApplicationContext(),"Invalid Email ID.",Toast.LENGTH_SHORT).show();
                    passwordEdittext.setText("");
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private boolean validateEmailFormat(String loginUsername) {
        return Patterns.EMAIL_ADDRESS.matcher(loginUsername).matches();
    }

    private void validateLogin(final String loginUsername, String loginPassword) {
        mAuth.signInWithEmailAndPassword(loginUsername,loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                pd.hide();
                if(task.isSuccessful()){
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("logged_in", "yes");
                    editor.putString("Email_ID",loginUsername);
                    editor.apply();
                    Intent i = new Intent(LoginDemo.this,MapsActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(LoginDemo.this,"Invalid Username or Password.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
