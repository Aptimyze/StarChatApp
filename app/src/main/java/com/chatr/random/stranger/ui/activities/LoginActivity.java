package com.chatr.random.stranger.ui.activities;

import android.Manifest;
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
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.chatr.random.stranger.AppPreferences;
import com.chatr.random.stranger.R;
import com.chatr.random.stranger.network.Firebase;
import com.vistrav.ask.Ask;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.chatr.random.stranger.Constants.RC_SIGN_IN;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public TextView username_et, password_et;
    Firebase firebase;
    RelativeLayout rootlayout;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        mAuth = FirebaseAuth.getInstance();
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        Ask.on(this)
                .id(1) // in case you are invoking multiple time Ask from same activity or fragment
                .forPermissions(Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withRationales("In order to Share and save Media Files you will need to grant storage permission") //optional
                .go();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, MainBaseActivity.class));
        }
    }

    private void initViews() {
        username_et = findViewById(R.id.username_et);
        password_et = findViewById(R.id.password_et);
        rootlayout = findViewById(R.id.rootLayout);
        progressBar = (ProgressBar)findViewById(R.id.spin_kit);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Firebase firebase = new Firebase(getApplication());
                FirebaseUserMetadata metadata = user.getMetadata();
                if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                    firebase.registerNewUserInDatabase(user.getUid(), user.getEmail(), user.getDisplayName());
                }
                Log.d("dsds", "onActivityResult: getLastSignInTimestamp" + metadata.getLastSignInTimestamp());
                Log.d("dsds", "onActivityResult: getCreationTimestamp()" + metadata.getCreationTimestamp());

//                Toast.makeText(this, user.getDisplayName(), Toast.LENGTH_SHORT).show();
                AppPreferences appPreferences = new AppPreferences(this);
                appPreferences.writeUid(user.getUid());
                appPreferences.writeUName(user.getDisplayName());

                startActivity(new Intent(this, MainBaseActivity.class));
                // ...
            } else {
                if (response == null) {

                } else {
                    Toast.makeText(this, Objects.requireNonNull(response.getError()).getErrorCode(), Toast.LENGTH_SHORT).show();
                }
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    public void signUp(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
        overridePendingTransition(R.anim.slide_up, R.anim.no_anim);
    }

    public void logInUser(View view) {
        final String email = username_et.getText().toString() + "@xyz.com";
        String password = password_et.getText().toString();
        try {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("dsd", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
//                                Toast.makeText(LoginActivity.this, email, Toast.LENGTH_SHORT).show();
                                AppPreferences appPreferences = new AppPreferences(getApplication());
                                appPreferences.writeUid(user.getUid());
                                appPreferences.writeUName(username_et.getText().toString());
                                startActivity(new Intent(LoginActivity.this, MainBaseActivity.class));
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
                                Log.w("dsd", "signInWithEmail:failure", task.getException());
                            }

                            progressBar.setVisibility(View.GONE);

                            // ...
                        }
                    });
        }catch (Exception e){
            Snackbar.make(rootlayout, getString(R.string.fields_empty), Snackbar.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
