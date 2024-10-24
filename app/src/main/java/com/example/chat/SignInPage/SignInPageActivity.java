package com.example.chat.SignInPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chat.HomePage.HomePageActivity;
import com.example.chat.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;

public class SignInPageActivity extends AppCompatActivity {
    Button creat,login;
    EditText editacount,editpassowrd;
    String email,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in_page);
        creat=findViewById(R.id.LoginButton);
        login=findViewById(R.id.LoginButton);
        editacount=findViewById(R.id.EditAcount);
        editpassowrd=findViewById(R.id.EditPassowrd);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=editacount.getText().toString();
                pass=editpassowrd.getText().toString();
                if(email.isEmpty()||pass.isEmpty()){
                    Toast.makeText(SignInPageActivity.this, "empity ", Toast.LENGTH_SHORT).show();
                }
                else{
                signin(email,pass);}
            }
        });



    }


    private void signin(String email, String pass) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.trim(), pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String User_name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                        Intent intent = new Intent(SignInPageActivity.this, HomePageActivity.class);
                        intent.putExtra("name", User_name);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthEmailException) {
                            Toast.makeText(SignInPageActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignInPageActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}






