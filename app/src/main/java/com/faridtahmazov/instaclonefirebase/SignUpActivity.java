package com.faridtahmazov.instaclonefirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    EditText signUpFirstName, signUpSurname, signUpEmail, signUpPassword, signUpConfirmPassword;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpFirstName = findViewById(R.id.signUpFirstName);
        signUpSurname = findViewById(R.id.signUpSurname);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);
        signUpConfirmPassword = findViewById(R.id.signUpConfirmPassword);

        firebaseAuth = FirebaseAuth.getInstance();

    //    Intent intent = getIntent();


    }

    public void signUpClicked(View view) {

        String firstname = signUpFirstName.getText().toString();
        String surname = signUpSurname.getText().toString();
        String username = signUpEmail.getText().toString();
        String password = signUpPassword.getText().toString();
        String confirmPassword = signUpConfirmPassword.getText().toString();

        if( firstname.matches("") || surname.matches("") || username.matches("") || password.matches("") || confirmPassword.matches("")){
            Toast.makeText(SignUpActivity.this, "Please, enter is form!", Toast.LENGTH_LONG).show();

        }else if(password.length() < 6){
            Toast.makeText(SignUpActivity.this, "Password cannot be less than 6!", Toast.LENGTH_LONG).show();

        }else if(!password.equals(confirmPassword)){
            Toast.makeText(SignUpActivity.this, "Passwords are not the same!", Toast.LENGTH_SHORT).show();

        }else{
            firebaseAuth.createUserWithEmailAndPassword(username, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(SignUpActivity.this, "Sign up is Successful!", Toast.LENGTH_LONG).show();
                    Intent intentSignUp = new Intent(SignUpActivity.this, FeedActivity.class);
                    startActivity(intentSignUp);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();

                }
            });

        }






    }
}