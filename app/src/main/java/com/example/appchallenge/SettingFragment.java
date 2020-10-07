package com.example.appchallenge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingFragment extends Fragment implements View.OnClickListener {
    boolean delete;
    View view;
    LinearLayout nameLayout;
    LinearLayout gradeLayout;
    LinearLayout hsLayout;
    LinearLayout teleLayout;
    LinearLayout emailLayout;
    LinearLayout passLayout;
    LinearLayout cityLayout;
    LayoutInflater inflate;

    TextView nameView;
    String name;
    TextView hsView;
    String hs;
    TextView gradeView;
    String grade;
    TextView phoneView;
    String phone;
    TextView emailView;
    String email;
    TextView cityView;
    String city;
    TextView passView;

    ViewGroup containerView;

    FirebaseDatabase database;

    EditText firstInput;
    TextView firstView;
    EditText confirmPassword;
    TextView enterPass;

    Button signout;
    Button deleteAccount;
    Button done;

    private FirebaseAuth auth;

    LoadingDialog loadingDialog;

    /*@Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof LoginActivity){
            activity = (Activity) context;
        }
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //activity = (LoginActivity) savedInstanceState.get("ContextVal");
        delete = false;
        inflate = inflater;
        view = inflate.inflate(R.layout.settingsscreen, container, false);
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoadingDialog();
        database = FirebaseDatabase.getInstance();
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference dataPull = database.getReference("Users").child(id);
        nameView = view.findViewById(R.id.id_changeNameInput);
        hsView = view.findViewById(R.id.id_changeHighSchoolInput);
        gradeView = view.findViewById(R.id.id_gradeChangeInput);
        phoneView = view.findViewById(R.id.id_changePhoneNumInput);
        emailView = view.findViewById(R.id.id_changeEmailInput);
        cityView = view.findViewById(R.id.id_changeCityInput);
        passView = view.findViewById(R.id.id_passInputChange);
        deleteAccount = view.findViewById(R.id.id_deleteAccount);



        auth = FirebaseAuth.getInstance();

        dataPull.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("name").getValue(String.class);
                hs = snapshot.child("hs").getValue(String.class);
                grade = snapshot.child("grade").getValue(String.class);
                phone = snapshot.child("telephone").getValue(String.class);
                email = snapshot.child("email").getValue(String.class);
                city = snapshot.child("city").getValue(String.class);

                Log.d("TAG", name + "");

                nameView.setText(name);
                hsView.setText(hs);
                gradeView.setText(grade);
                phoneView.setText(phone);
                emailView.setText(email);
                cityView.setText(city);

                loadingDialog.dismissDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", error.getDetails());
            }
        });

        containerView = container;
        Log.d("TAG", "Starting SettingFragment...");

        nameLayout = view.findViewById(R.id.id_changeNameInfo);
        nameLayout.setOnClickListener(this);

        gradeLayout = view.findViewById(R.id.id_gradeChangeInfo);
        gradeLayout.setOnClickListener(this);

        hsLayout = view.findViewById(R.id.id_hsChangeInfo);
        hsLayout.setOnClickListener(this);

        teleLayout = view.findViewById(R.id.id_phoneChangeInfo);
        teleLayout.setOnClickListener(this);

        emailLayout = view.findViewById(R.id.id_emailChangeInfo);
        emailLayout.setOnClickListener(this);

        passLayout = view.findViewById(R.id.id_passwordCreationChange);
        passLayout.setOnClickListener(this);

        cityLayout = view.findViewById(R.id.id_cityChangeInfo);
        cityLayout.setOnClickListener(this);

        signout = view.findViewById(R.id.id_signoutButton);
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "Starting OnClick Method");
                // inflate the layout of the popup window
                View popUpView = inflate.inflate(R.layout.editsettings, null);
                final PopupWindow pw = new PopupWindow(popUpView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                Log.d("TAG", "OnClick1");

                firstView = popUpView.findViewById(R.id.id_changeLabel);
                firstInput = popUpView.findViewById(R.id.id_changeFirstInput);
                confirmPassword = popUpView.findViewById(R.id.id_changePassInput);
                done = popUpView.findViewById(R.id.id_modifyButton);

                firstInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                firstView.setText("Verify email");
                done.setBackgroundResource(R.drawable.rounded3);
                done.setText("Delete Permanently");

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("TAG", "onClick");
                        delete = true;
                        String email = firstInput.getText().toString();
                        String password = confirmPassword.getText().toString();
                        firstInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                        if (email.isEmpty()) {
                            firstInput.setError("Email is required");
                            firstInput.requestFocus();
                            Log.d("TAG", "email is empty");
                            //Toast.makeText(this, "Make sure you entered all fields!", Toast.LENGTH_SHORT).show();
                            delete = false;
                        }
                        if (password.isEmpty()) {
                            confirmPassword.setError("Password is required");
                            confirmPassword.requestFocus();
                            Log.d("TAG", "password is empty");
                            //Toast.makeText(this, "Make sure you entered all fields!", Toast.LENGTH_SHORT).show();
                            delete = false;
                        }

                        if (!delete) {
                            return;
                        }
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        // Get auth credentials from the user for re-authentication. The example below shows
                        // email and password credentials but there are multiple possible providers,
                        // such as GoogleAuthProvider or FacebookAuthProvider.
                        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                        Log.d("TAG", credential.toString());

                        // Prompt the user to re-provide their sign-in credentials
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TAG", "onComplete: authentication complete");
                                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    myRef.child("Status").setValue("Deleted");
                                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("TAG", "User account deleted.");
                                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                                startActivity(intent);
                                                getActivity().finish();
                                            } else {
                                                Log.d("TAG", "User account deletion unsuccessful.");
                                            }
                                        }
                                    });
                                } else
                                    Log.d("TAG", "onComplete: authentication failed");
                            }
                        });
                    }
                });

                Button returnButton = popUpView.findViewById(R.id.backButton);

                returnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pw.dismiss();
                    }
                });
                Log.d("TAG", "OnClick2");

                pw.setFocusable(true);
                // create the popup window
                Log.d("TAG", "OnClick3");

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window token
                pw.showAtLocation(view, Gravity.BOTTOM, 10, 10);
                pw.setAnimationStyle(R.style.Animation);
                pw.update();
                Log.d("TAG", "Finished...");
            }
        });

        Log.d("TAG", "Finished...");
        return view;
    }

    @Override
    public void onClick(View v) {
        Log.d("TAG", "Starting OnClick Method");
        // inflate the layout of the popup window
        View popUpView = inflate.inflate(R.layout.editsettings, null);
        final PopupWindow pw = new PopupWindow(popUpView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Log.d("TAG", "OnClick1");

        firstView = popUpView.findViewById(R.id.id_changeLabel);
        firstInput = popUpView.findViewById(R.id.id_changeFirstInput);
        enterPass = popUpView.findViewById(R.id.id_enterPass);
        confirmPassword = popUpView.findViewById(R.id.id_changePassInput);

        enterPass.setVisibility(View.GONE);
        confirmPassword.setVisibility(View.INVISIBLE);
        done = popUpView.findViewById(R.id.id_modifyButton);

        int viewId = v.getId();

        if (viewId == nameLayout.getId()) {
            firstView.setText("Change name ([First Name] [Last Name])");
            firstInput.setText(nameView.getText());
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = firstInput.getText().toString();
                    if (name.isEmpty()) {
                        firstInput.setError("Enter new name");
                        return;
                    }
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ref.child("name").setValue(name + "");
                    pw.dismiss();
                }
            });
        }
        if (viewId == hsLayout.getId()) {
            firstView.setText("Change high school");
            firstInput.setText(hsView.getText());
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String hs = firstInput.getText().toString();
                    if (hs.isEmpty()) {
                        firstInput.setError("Enter new high school");
                        return;
                    }
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ref.child("hs").setValue(hs + "");
                    pw.dismiss();
                }
            });
        }

        if (viewId == teleLayout.getId()) {
            firstView.setText("Change phone number");
            firstInput.setText(phoneView.getText());
            firstInput.setInputType(InputType.TYPE_CLASS_NUMBER);

            int maxLength = 10;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            firstInput.setFilters(fArray);

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tele = firstInput.getText().toString();
                    if (tele.length() < 10 && tele.length() >= 1) {
                        firstInput.setError("Phone number must be 10 digits");
                        return;
                    }
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ref.child("telephone").setValue(tele + "");
                    pw.dismiss();
                }
            });
        }
        if (viewId == gradeLayout.getId()) {
            firstView.setText("Change grade");
            firstView.setGravity(Gravity.CENTER);
            firstInput.setText(gradeView.getText());
            firstInput.setEms(3);
            int maxLength = 2;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            firstInput.setFilters(fArray);
            firstInput.setGravity(Gravity.CENTER);
            firstInput.setInputType(InputType.TYPE_CLASS_NUMBER);

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String grade = firstInput.getText().toString();
                    if (grade.isEmpty()) {
                        firstInput.setError("Enter new grade");
                        return;
                    }
                    int gradeD = Integer.parseInt(grade);
                    if(6 > gradeD || gradeD > 12){
                        firstInput.setError("Grade must be between 6-12");
                        return;
                    }
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ref.child("grade").setValue(grade + "");
                    pw.dismiss();
                }
            });
        }
        if (viewId == cityLayout.getId()) {
            firstView.setText("Change city");
            firstInput.setText(cityView.getText());
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String city = firstInput.getText().toString();
                    if (city.isEmpty()) {
                        firstInput.setError("Enter new city");
                        return;
                    }
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ref.child("city").setValue(city + "");
                    pw.dismiss();
                }
            });
        }
        if (viewId == emailLayout.getId()) {
            firstView.setText("Change email");
            firstInput.setText(emailView.getText());
            firstInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = firstInput.getText().toString();
                    if (email.isEmpty()) {
                        firstInput.setError("Enter new email");
                        return;
                    }
                    if (!(isEmailValid(email))) {
                        firstInput.setError("Email not valid");
                        return;
                    }
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast toast = Toast.makeText(getContext(),"Email has been updated",Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    });

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ref.child("email").setValue(email + "");
                    pw.dismiss();
                }
            });
        }
        if (viewId == passLayout.getId()) {
            firstView.setText("Enter email to change password");
            firstInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String emailAddress = firstInput.getText().toString();
                    if(emailAddress.isEmpty()){
                        firstInput.setError("Enter email");
                        return;
                    }
                    if(!isEmailValid(emailAddress)){
                        firstInput.setError("Not a valid email");
                        return;
                    }
                    if(emailAddress.isEmpty()) {
                        firstInput.setError("Please enter an email");
                        return;
                    }
                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast toast = Toast.makeText(getContext(), "Password reset email sent!", Toast.LENGTH_LONG);
                                        toast.show();
                                        pw.dismiss();
                                    }
                                }
                            });
                }
            });
        }

        Button returnButton = popUpView.findViewById(R.id.backButton);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
        Log.d("TAG", "OnClick2");

        pw.setFocusable(true);
        // create the popup window
        Log.d("TAG", "OnClick3");

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        pw.showAtLocation(view, Gravity.CENTER, 10, 10);
        pw.setAnimationStyle(R.style.Animation);
        pw.update();
        Log.d("TAG", "Finished...");
    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

