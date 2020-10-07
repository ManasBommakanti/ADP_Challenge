package com.example.appchallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class SignUpActivity extends AppCompatActivity {
    EditText firstnameInput;
    EditText lastnameInput;
    EditText emailInput;
    EditText teleInput;
    EditText cityInput;
    EditText passInput;
    EditText gradeInput;
    EditText passConfirm;
    Button signUp;
    Button login;
    AutoCompleteTextView highschoolSearcher;
    TextView firstNameLabel;
    boolean passed;
    LoadingDialog loadingDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentsignup);

        mAuth = FirebaseAuth.getInstance();
        loadingDialog = new LoadingDialog(this);

        highschoolSearcher = findViewById(R.id.id_highschoolSearcher);
        firstnameInput = findViewById(R.id.id_firstNameInput);
        lastnameInput = findViewById(R.id.id_lastNameInput);
        emailInput = findViewById(R.id.id_emailInput);
        teleInput = findViewById(R.id.id_phoneNumInput);
        cityInput = findViewById(R.id.id_cityInput);
        passInput = findViewById(R.id.id_passInput);
        login = findViewById(R.id.id_backtoLoginButton);
        firstNameLabel = findViewById(R.id.id_firstnameLabel);
        signUp = findViewById(R.id.id_signupUser);
        gradeInput = findViewById(R.id.id_gradeInput);
        passConfirm = findViewById(R.id.id_passConfirmInput);
        passed = true;

        getWindow().setStatusBarColor(Color.parseColor("#20111111"));
        getWindow().setNavigationBarColor(Color.parseColor("#20111111"));


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

        public void registerUser() {
            Log.d("TAG", "Checking if method opens");
            passed = true;
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            final String first = firstnameInput.getText().toString();
            final String last = lastnameInput.getText().toString();
            final String email = emailInput.getText().toString();
            final String tele = teleInput.getText().toString();
            final String city = cityInput.getText().toString();
            final String password = passInput.getText().toString();
            final String highschool = highschoolSearcher.getText().toString();
            final String grade = gradeInput.getText().toString();
            final String confirm = passConfirm.getText().toString();

            teleInput.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

            Log.d("TAG", "Checking each Edittext");

            boolean passwordC = false;

            if (first.isEmpty()) {
                firstnameInput.setError("First name is required");
                firstnameInput.requestFocus();
                Log.d("TAG", "first is empty");
                //Toast.makeText(this, "Make sure you entered all fields!", Toast.LENGTH_SHORT).show();
                passed = false;
            }
            if (last.isEmpty()) {
                lastnameInput.setError("Last name is required");
                lastnameInput.requestFocus();
                Log.d("TAG", "last is empty");
                //Toast.makeText(this, "Make sure you entered all fields!", Toast.LENGTH_SHORT).show();
                passed = false;
            }
            if (grade.isEmpty()) {
                gradeInput.setError("Grade is required");
                gradeInput.requestFocus();
                Log.d("TAG", "grade is empty");
                //Toast.makeText(this, "Make sure you entered all fields!", Toast.LENGTH_SHORT).show();
                passed = false;
            }
            else {
                int gradeD = Integer.parseInt(grade);

                if (6 > gradeD || gradeD > 12) {
                    gradeInput.setError("Grade must be between 6-12");
                    gradeInput.requestFocus();
                    passed = false;
                }
            }
            if (city.isEmpty()) {
                cityInput.setError("City is required");
                cityInput.requestFocus();
                Log.d("TAG", "city is empty");
                //Toast.makeText(this, "Make sure you entered all fields!", Toast.LENGTH_SHORT).show();
                passed = false;
            }
            if((tele.length() != 10) && (tele.length() != 0))
            {
                teleInput.setError("Phone number should be 10 digits");
                teleInput.requestFocus();
                Log.d("TAG", "phone number length is wrong");
                //Toast.makeText(this, "Make sure you entered all fields!", Toast.LENGTH_SHORT).show();
                passed = false;
            }

            if (password.isEmpty()) {
                passInput.setError("Password is required");
                passInput.requestFocus();
                Log.d("TAG", "password is empty");
                //Toast.makeText(this, "Make sure you entered all fields!", Toast.LENGTH_SHORT).show();
                passed = false;
            }
            else
                passwordC = true;

            if (password.length() < 6) {
                passInput.setError("Password should be at least six characters");
                passInput.requestFocus();
                Log.d("TAG", "password is empty");
                //Toast.makeText(this, "Make sure you entered all fields!", Toast.LENGTH_SHORT).show();
                passed = false;
                passwordC = false;
            }
            if (confirm.isEmpty()) {
                if(passwordC)
                    passConfirm.setError("Confirm password");
                else
                    passConfirm.setError("Create a valid password");
                passConfirm.requestFocus();
                Log.d("TAG", "confirmation is empty");
                //Toast.makeText(this, "Make sure you entered all fields!", Toast.LENGTH_SHORT).show();
                passed = false;
            }
            else if (!confirm.equals(password)) {
                passConfirm.setError("Passwords do not match");
                passConfirm.requestFocus();
                Log.d("TAG", "password is not empty, wrong confirmation");
                //Toast.makeText(this, "Make sure you entered all fields!", Toast.LENGTH_SHORT).show();
                passed = false;
            }

            if (highschool.isEmpty()) {
                highschoolSearcher.setError("High school is required");
                highschoolSearcher.requestFocus();
                Log.d("TAG", "highschool is empty");
                //Toast.makeText(this, "Make sure you entered all fields!", Toast.LENGTH_SHORT).show();
                passed = false;
            }
            if(email.isEmpty()){
                emailInput.setError("Email is required");
                emailInput.requestFocus();
                Log.d("TAG", "email is empty");
                //Toast.makeText(this, "Make sure you entered all fields!", Toast.LENGTH_SHORT).show();
                passed = false;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.setError("Please Enter a valid email address");
                emailInput.requestFocus();
                passed = false;
            }

            if(!passed)
                return;
            else {
                mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG", "Starting onComplete...");
                        loadingDialog.startLoadingDialog();
                        if(task.isSuccessful())
                        {
                            Log.d("TAG", "Task successful");
                            User user;
                            if(tele.equals(""))
                                user = new User(first+" "+last, highschool, city, grade, email);
                            else
                                user = new User(first+" "+last, highschool, city, grade, tele, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(SignUpActivity.this, "Please verify your email", Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                        loadingDialog.dismissDialog();
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(SignUpActivity.this, "Registration with parameters failed.", Toast.LENGTH_LONG).show();
                                        loadingDialog.dismissDialog();
                                    }
                                }
                            });

                        }else
                        {
                           Log.d("Task Error", task.getException().toString());
                            loadingDialog.dismissDialog();
                        }
                    }
                });
/*
                DatabaseReference myRef = database.getReference(email);
                myRef.child("Email").setValue(email);
                myRef.child("First Name").setValue(first);
                myRef.child("Last Name").setValue(last);
                myRef.child("High School").setValue(highschool);
                myRef.child("City").setValue(city);
                myRef.child("Phone Number").setValue(tele);
                myRef.child("Password").setValue(password);
                Toast.makeText(SignUpActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
           */
            }
        }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            hideSystemUI();
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
}
