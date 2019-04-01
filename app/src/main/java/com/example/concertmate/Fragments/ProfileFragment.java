package com.example.concertmate.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.concertmate.BaseActivity;
import com.example.concertmate.MainLoginActivity;
import com.example.concertmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class ProfileFragment extends Fragment {
     Dialog myDialog;
     Button changeUsername,changeEmail,changePassword,deleteProfile,sendReset,update;
     ImageButton avatar;
     TextView username,email,txtclose,title,currentSomething,oldchange;
     ProgressBar progressBar,dialogProgressBar;
     FirebaseAuth.AuthStateListener authListener;
     FirebaseAuth auth;
     EditText newChange,currentPassword;
     FirebaseUser user;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        //check if by user is still signed in.
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(getActivity(), MainLoginActivity.class));
                }
            }
        };
        username = view.findViewById(R.id.userNameTextView);
        email = view.findViewById(R.id.userEmailTextView);
        avatar=view.findViewById(R.id.userAvatarImageButton);
        progressBar = view.findViewById(R.id.profileProgressBar);
        changeUsername = view.findViewById(R.id.usernameChangeButton);
        changeEmail = view.findViewById(R.id.emailChangeButton);
        changePassword = view.findViewById(R.id.passwordChangeButton);
        deleteProfile = view.findViewById(R.id.deleteProfileButton);
        sendReset = view.findViewById(R.id.sendPasswordResetButton);
        username.setText(user.getDisplayName());
        email.setText(user.getEmail());
        Glide.with(this)
                .load(user.getPhotoUrl())
                .into(avatar);


        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                }
                else {
                    Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    cameraIntent.setType("image/*");
                    if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(cameraIntent, 1000);
                    }

                }
            }
        });
        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(3,"You are about to delete your account.Are you sure to continue?");
            }
        });

        sendReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                    auth.sendPasswordResetEmail(user.getEmail())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Reset password email is sent!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(getActivity(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
            }
        });
        myDialog = new Dialog(getContext());
        changeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(0,user.getDisplayName());
            }
        });
        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(1,user.getEmail());
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(2,"*****************");
            }
        });

        return view;
    }

    public void ShowPopup(final int buttonClicked,String hint) {
        myDialog.setContentView(R.layout.custom_dialog);
        dialogProgressBar=myDialog.findViewById(R.id.dialogProgressBar);
        title=myDialog.findViewById(R.id.dialogTitle);
        currentSomething=myDialog.findViewById(R.id.dialogCurrentTextview);
        oldchange=myDialog.findViewById(R.id.oldUsernameTextview);
        newChange=myDialog.findViewById(R.id.newValueEditView);
        update=myDialog.findViewById(R.id.updateButton);
        currentPassword=myDialog.findViewById(R.id.passwordEditView);
        switch (buttonClicked){
            case 0:
                currentPassword.setVisibility(View.GONE);
                title.setText(getString(R.string.dialog_username_title));
                currentSomething.setText(getString(R.string.dialog_username_current));
                oldchange.setText(hint);
                newChange.setHint(getString(R.string.dialog_username_new));
                break;

            case 1:
                title.setText(getString(R.string.dialog_email_title));
                currentSomething.setText(getString(R.string.dialog_email_current));
                oldchange.setText(hint);
                newChange.setHint(getString(R.string.dialog_email_new));
                break;
            case 2:
                title.setText(getString(R.string.dialog_password_title));
                currentSomething.setText(getString(R.string.dialog_password_current));
                oldchange.setText(hint);
                newChange.setHint(getString(R.string.dialog_password_new));
                break;
            case 3:
                title.setText(getString(R.string.delete_user));
                currentSomething.setVisibility(View.GONE);
                oldchange.setText(hint);
                newChange.setVisibility(View.GONE);
                update.setText(getString(R.string.title_delete));
                break;
        }
        txtclose = myDialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialogProgressBar.setVisibility(View.VISIBLE);
                String currentpassword = currentPassword.getText().toString();

                if (!StringUtils.isNotBlank(currentpassword) && buttonClicked !=0) {
                    Toast.makeText(getActivity(), "Current Password is Empty", Toast.LENGTH_SHORT).show();
                    dialogProgressBar.setVisibility(View.INVISIBLE);
                } else {

                    final String newChangeText = newChange.getText().toString();

                    if (StringUtils.isNotBlank(newChangeText) || buttonClicked ==3) {

                        switch (buttonClicked) {
                            case 0:
                                final UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(newChangeText).build();
                                                    auth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(getActivity(), "Username Changed Successfully", Toast.LENGTH_SHORT).show();
                                                                username.setText(newChangeText);
                                                                ((BaseActivity) getActivity()).setUsername(newChangeText);
                                                                dialogProgressBar.setVisibility(View.GONE);
                                                                myDialog.dismiss();
                                                            }else{
                                                                dialogProgressBar.setVisibility(View.GONE);
                                                                Toast.makeText(getActivity(), "Failed to update username!", Toast.LENGTH_LONG).show();
                                                                Log.e("ERROR",task.getException().toString());
                                                            }
                                                        }
                                                    });

                                break;

                            case 1:
                                if (StringUtils.contains(newChangeText, "@")) {
                                    AuthCredential credential1 = EmailAuthProvider
                                            .getCredential(user.getEmail(), currentpassword);
                                    user.reauthenticate(credential1)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        auth.getCurrentUser().updateEmail(newChangeText).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(getActivity(), "Email Changed Successfully!", Toast.LENGTH_SHORT).show();
                                                                    dialogProgressBar.setVisibility(View.GONE);
                                                                    email.setText(newChangeText);
                                                                    ((BaseActivity) getActivity()).setEmail(newChangeText);
                                                                    myDialog.dismiss();
                                                                } else {
                                                                    dialogProgressBar.setVisibility(View.GONE);
                                                                    Toast.makeText(getActivity(), "Failed to update email!", Toast.LENGTH_LONG).show();
                                                                    Log.e("ERROR",task.getException().toString());
                                                                }

                                                            }
                                                    });
                                                } else {
                                                        Log.d("INFO", "User not re-authenticated.");
                                                        dialogProgressBar.setVisibility(View.GONE);
                                                        Toast.makeText(getActivity(), "Current Password doesn't match our records", Toast.LENGTH_SHORT).show();
                                                    }
                                            }
                                    });
                                } else {
                                    Toast.makeText(getActivity(), "Invalid Email Format!", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 2:
                                if (newChangeText.length() < 6) {
                                    Toast.makeText(getActivity(), "Password to short!", Toast.LENGTH_SHORT).show();
                                } else {
                                    AuthCredential credential2 = EmailAuthProvider
                                            .getCredential(user.getEmail(), currentpassword);
                                    user.reauthenticate(credential2)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        auth.getCurrentUser().updatePassword(newChangeText).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(getActivity(), "Password Changed Successfully!", Toast.LENGTH_SHORT).show();
                                                                    dialogProgressBar.setVisibility(View.GONE);
                                                                    myDialog.dismiss();
                                                                }else{
                                                                    dialogProgressBar.setVisibility(View.GONE);
                                                                    Toast.makeText(getActivity(), "Failed to update password!", Toast.LENGTH_LONG).show();
                                                                    Log.e("ERROR",task.getException().toString());
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        Log.d("INFO", "User not re-authenticated.");
                                                        dialogProgressBar.setVisibility(View.GONE);
                                                        Toast.makeText(getActivity(), "Current Password doesn't match our records", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });


                                }
                                break;
                            case 3:
                                progressBar.setVisibility(View.VISIBLE);
                                AuthCredential credential3 = EmailAuthProvider
                                        .getCredential(user.getEmail(), currentpassword);
                                user.reauthenticate(credential3).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Log.d("INFO", "User re-authenticated.");
                                            user.delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getActivity(), "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(getActivity(), MainLoginActivity.class));
                                                                progressBar.setVisibility(View.GONE);
                                                            } else {
                                                                Log.e("ERROR",task.getException().toString());
                                                                Toast.makeText(getActivity(), "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                                                progressBar.setVisibility(View.GONE);
                                                            }
                                                        }
                                                    });
                                        }else{
                                            dialogProgressBar.setVisibility(View.GONE);
                                            Toast.makeText(getActivity(), "Failed to Delete Account", Toast.LENGTH_LONG).show();
                                            Log.e("ERROR",task.getException().toString());
                                        }
                                    }
                                });



                                break;
                        }
                    } else {
                        Toast.makeText(getActivity(), "Text field is empty!!!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            final Uri returnUri = data.getData();
            final UserProfileChangeRequest profileAvatarUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(returnUri).build();
            auth.getCurrentUser().updateProfile(profileAvatarUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Picasso.get().load(returnUri).fit().into(avatar);
                        ((BaseActivity) getActivity()).setImage(returnUri);
                        Toast.makeText(getActivity(), "Profile Picture Updated Succesfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed to update profile picture!", Toast.LENGTH_LONG).show();
                        Log.e("ERROR", task.getException().toString());
                    }
                }
            });

        }
    }
}
