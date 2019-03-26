package com.example.concertmate.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.concertmate.BaseActivity;
import com.example.concertmate.Models.User;
import com.example.concertmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.lang3.StringUtils;

public class SignupFragment extends BaseFragment {

    DatabaseReference mDatabase;
    EditText inputEmail, inputPassword,inputUsername;
    Button btnSignIn, btnSignUp;
    ProgressBar progressBar;
    FirebaseAuth auth;

    public SignupFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.signup_fragment, container, false);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnSignIn = view.findViewById(R.id.sign_in_button);
        btnSignUp = view.findViewById(R.id.sign_up_button);
        inputUsername = view.findViewById(R.id.username);
        inputEmail = view.findViewById(R.id.email);
        inputPassword = view.findViewById(R.id.password);

        progressBar = view.findViewById(R.id.progressBar);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFragment(getActivity());
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                final String username = inputUsername.getText().toString().trim();

                if (!StringUtils.isNotEmpty(email)){
                    Toast.makeText(getContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!StringUtils.isNotEmpty(password)){
                    Toast.makeText(getContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!StringUtils.isNotEmpty(username)){
                    Toast.makeText(getContext(), "Enter username!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(getContext(), "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                    User user = new User(auth.getCurrentUser().getUid(),
                                            username,email,
                                            "https://firebasestorage.googleapis.com/v0/b/concertmate-e87ec.appspot.com/o/avatar_tiny.jpg?alt=media&token=0a9802ea-f8de-4650-a241-8c529f322c44");
                                    mDatabase.child("Users").child(user.getUID()).setValue(user.toMap());
                                    startActivity(new Intent(getActivity(), BaseActivity.class));
                                }
                            }
                        });

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
