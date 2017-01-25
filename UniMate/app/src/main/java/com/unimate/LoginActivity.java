package com.unimate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends BaseActivity {

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login_button = (Button)findViewById(R.id.login_button);
        Button switch_to_register_button = (Button)findViewById(R.id.switch_to_register_button);
        final TextView username_text = (TextView)findViewById(R.id.username_text);
        final TextView password_text = (TextView)findViewById(R.id.password_text);
        password_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    String email = username_text.getText().toString();
                    String password = password_text.getText().toString();
                    signIn(email, password);
                    handled = true;
                }
                return handled;
            }
        });

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Toast.makeText(LoginActivity.this, "signed in", Toast.LENGTH_LONG).show();

                } else {
                    // User is signed out
                    Toast.makeText(LoginActivity.this, "signed out", Toast.LENGTH_LONG).show();

                }
            }
        };
        // [END auth_state_listener]

        // Initialize onclick listeners
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = username_text.getText().toString();
                String password = password_text.getText().toString();

                signIn(email, password);
            }
        });

        switch_to_register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

    }

    private void signIn(String email, String password) {

        //validate form here
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(LoginActivity.this,"Please fill in username and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Toast.makeText(LoginActivity.this, "login state: "+ task.isSuccessful(), Toast.LENGTH_LONG).show();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "signin failed", Toast.LENGTH_LONG).show();
                        }
                        else{
                            //check if this is the first startup
                            SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
                            int defaultValue = 0;
                            int value = sharedPref.getInt("firstStart", defaultValue);

                            //Toast.makeText(LoginActivity.this, "firstStart: " + value ,Toast.LENGTH_SHORT).show();

                            if(value == 0) {
                                //uncomment this part before production !!!
                                SharedPreferences sharedPref2 = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putInt("firstStart", 1);
                                editor.commit();

                                Intent  i = new Intent(LoginActivity.this, ChooseTagsActivity.class);
                                startActivity(i);

                            }
                            else{
                                Intent  i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                        }

                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        //updateUI(null);
    }


}
