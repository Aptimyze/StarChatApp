package com.chatr.random.stranger.ui.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.chatr.random.stranger.AppPreferences;
import com.chatr.random.stranger.R;
import com.chatr.random.stranger.network.Firebase;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public TextView username_et, password_et;
    Firebase firebase;
    RelativeLayout rootlayout;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();
        mAuth = FirebaseAuth.getInstance();
        firebase = new Firebase(getApplication());
    }

    private void initViews() {
        username_et = findViewById(R.id.username_et);
        password_et = findViewById(R.id.password_et);
        rootlayout = findViewById(R.id.rootLayout);
        progressBar = (ProgressBar)findViewById(R.id.spin_kit);

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, MainBaseActivity.class));
        }
    }

    public void signUpUser(View view) {
        String email = username_et.getText().toString() + "@xyz.com";
        final String userName = username_et.getText().toString();
        final String password = password_et.getText().toString();

        try {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                    Log.d("jhjh", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    firebase.registerNewUserInDatabase(user.getUid(), user.getEmail(), username_et.getText().toString());
                                    String displayName = username_et.getText().toString();
                                    AppPreferences appPreferences = new AppPreferences(getApplication());
                                    appPreferences.writeUid(user.getUid());
                                    appPreferences.writeUName(displayName);
                                    startActivity(new Intent(getApplicationContext(), MainBaseActivity.class));
                                    finish();
                            } else {
                                if (!task.isSuccessful()) {
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthWeakPasswordException e) {
                                        Snackbar.make(rootlayout, getString(R.string.weak_password), Snackbar.LENGTH_LONG).show();
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        Snackbar.make(rootlayout, getString(R.string.invalid_credentials), Snackbar.LENGTH_LONG).show();
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        Snackbar.make(rootlayout, getString(R.string.username_exist), Snackbar.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        Snackbar.make(rootlayout, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                                    }
                                }

                                // If sign in fails, display a message to the user.
                                Log.w("jkj", "createUserWithEmail:failure" + task.getException());

//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                            }
                            progressBar.setVisibility(View.GONE);

                            // ...
                        }
                    });
        } catch (Exception e) {
            Snackbar.make(rootlayout, getString(R.string.fields_empty), Snackbar.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);

        }

    }

    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_down, R.anim.no_anim);
    }

}
