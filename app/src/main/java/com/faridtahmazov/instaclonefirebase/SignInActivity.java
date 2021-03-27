package com.faridtahmazov.instaclonefirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    EditText emailText, passwordText;
    ImageView instagramView;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        instagramView = findViewById(R.id.instagramView);
        firebaseAuth = FirebaseAuth.getInstance();




        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser(); // Əgər əvvəldən açılmış hesab varsa, həmin hesabı bu dəyişənə mənimsəd.

        if (firebaseUser != null){                          //Əgər dəyişən boş deyilsə həmin profilə apar
            Intent intent = new Intent(SignInActivity.this, FeedActivity.class);
            startActivity(intent);
            finish();
        }

    }

    //signUp click
    public void signUpWayClicked(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    //signIn click
    public void signInClicked(View view){
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if( email.matches("") || password.matches("")){
            Toast.makeText(SignInActivity.this, "Do not enter blank", Toast.LENGTH_LONG).show();

        }else{
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intentSignIn = new Intent(SignInActivity.this, FeedActivity.class);
                    startActivity(intentSignIn);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignInActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();

                }
            });
        }
    }
}