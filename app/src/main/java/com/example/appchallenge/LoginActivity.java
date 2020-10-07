package com.example.appchallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button signInButton;
    Button signUpButton;
    Button forgotPasswordButton;
    DatabaseReference myRef;
    boolean passed;
    private FirebaseAuth mAuth;
    PopupWindow popupWindowForgotPass;
    LinearLayout passLayout;
    LinearLayout mainLayout;
    Button done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentlogin);

        final LayoutInflater inflater = getLayoutInflater();

        getWindow().setStatusBarColor(Color.parseColor("#20111111"));
        getWindow().setNavigationBarColor(Color.parseColor("#20111111"));

        email = findViewById(R.id.id_usernameInput);
        password = findViewById(R.id.id_passwordInput);
        signInButton = findViewById(R.id.id_signin);
        signUpButton = findViewById(R.id.id_signup);
        forgotPasswordButton = findViewById(R.id.id_forgotpass);
        popupWindowForgotPass = new PopupWindow(this);
        passLayout = new LinearLayout(this);
        mainLayout = new LinearLayout(this);
        done = new Button(this);

        passed = true;
        mAuth = FirebaseAuth.getInstance();

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popUpView = inflater.inflate(R.layout.forgetpass, null);
                final PopupWindow popupWindowForgotPass = new PopupWindow(popUpView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                popupWindowForgotPass.setFocusable(true);
                // create the popup window
                Log.d("TAG", "OnClick3");

                final EditText emailInput = popUpView.findViewById(R.id.id_forgetPassInput);
                Button verify = popUpView.findViewById(R.id.id_sendverify);
                Button backButton = popUpView.findViewById(R.id.id_backtoLoginButton);

                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindowForgotPass.dismiss();
                    }
                });

                verify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        String email = emailInput.getText().toString();
                        if(email.isEmpty()){
                            emailInput.setError("Enter email");
                            return;
                        }
                        if(!isEmailValid(email)){
                            emailInput.setError("Not a valid email");
                            return;
                        }
                        auth.sendPasswordResetEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast toast = Toast.makeText(LoginActivity.this, "Password reset email sent!", Toast.LENGTH_LONG);
                                            toast.show();
                                        }
                                    }
                                });
                        popupWindowForgotPass.dismiss();
                    }
                });

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window token

                popupWindowForgotPass.showAtLocation(popUpView, Gravity.CENTER, 10, 10);
                popupWindowForgotPass.setAnimationStyle(R.style.Animation);
                popupWindowForgotPass.update();
            }
        });

        final LoadingDialog loadingDialog = new LoadingDialog(this);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                passed = true;
                if(email.getText().toString().isEmpty()){
                    email.setError("Please enter email");
                    email.requestFocus();
                    passed = false;
                }
                if(password.getText().toString().isEmpty()){
                    password.setError("Please enter password");
                    password.requestFocus();
                    passed = false;
                }
                if(!passed) {
                    loadingDialog.dismissDialog();
                    return;
                }
                final String userString = email.getText().toString();
                final String passString = password.getText().toString();

                mAuth.signInWithEmailAndPassword(userString, passString)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if(user.isEmailVerified() == false) {
                                        user.sendEmailVerification();
                                        Toast.makeText(LoginActivity.this, "Please check email to verify account and try again", Toast.LENGTH_LONG).show();
                                        loadingDialog.dismissDialog();
                                    }
                                    else {
                                        //Toast.makeText(LoginActivity.this, "Email already verified!", Toast.LENGTH_LONG).show();
                                        updateUI(user);
                                        loadingDialog.dismissDialog();
                                    }
                                }
                                else {
                                    Log.d("TAG", "fail");
                                    // If sign in fails, display a message to the user.
                                    Log.w("TAG", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                    loadingDialog.dismissDialog();
                                }
                            }
                        });
                /*
                myRef = FirebaseDatabase.getInstance().getReference().child(userString);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String passStringData = "";
                        if(dataSnapshot.exists()) {
                            passStringData = dataSnapshot.child("Password").getValue().toString();
                            if(passString.equals(passStringData)) {
                                Toast toast = Toast.makeText(LoginActivity.this, "Accepted User", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 0);
                                toast.show();

                                Intent signUpIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(signUpIntent);
                                finish();
                            }
                            else{
                                Toast toast = Toast.makeText(LoginActivity.this, "Password or email incorrect. Please check again", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 0);
                                toast.show();
                            }
                        }
                        else {
                            Toast toast = Toast.makeText(LoginActivity.this, "Password or email incorrect. Please check again", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();
                        }
                        Log.d("TAG", userString + " " + passString);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
             */
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null && currentUser.isEmailVerified())
            updateUI(currentUser);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            hideSystemUI();
        }
    }

    public void updateUI(FirebaseUser currentUser) {
        if(currentUser != null){
            Intent transfer = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(transfer);
            finish();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        // Hide the nav bar and status bar
        //| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        //| View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}