package com.example.chat.CreatAccount;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.chat.SignInPage.SignInPageActivity;
import com.example.chat.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreatAccountActivity extends AppCompatActivity {
    EditText email,pass,confirm,name;
    Button regist,haveAccount;
    String userEmail,userPassowrd,userName,userconfirm;
    boolean hasuppercase,haslowercase,hasdigit,validEmail,validPass,hasspecialChar,confirme;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_account);
        haveAccount=findViewById(R.id.haveaccount);

        name=findViewById(R.id.name);
        email=findViewById(R.id.editemail);
        pass=findViewById(R.id.editpassowrd);
        confirm=findViewById(R.id.confirm);
        regist=findViewById(R.id.register);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail=email.getText().toString();
                userPassowrd=pass.getText().toString();
                userconfirm=confirm.getText().toString();
                userName=name.getText().toString();
                validEmail=validMail(userEmail);
                validPass=validPassowrd(userPassowrd);
                confirme=validConfirm(userPassowrd,userconfirm);
                if(validPass&&validEmail&&confirme){
                    signUp(userName,userPassowrd,userEmail);

                }

            }
        });

      haveAccount.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent=new Intent(CreatAccountActivity.this,SignInPageActivity.class);
              startActivity(intent);
              finish();
          }
      });
    }
     @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){

            startActivity( new Intent(CreatAccountActivity.this, HomePageActivity.class));
            finish();
        }
    }
    private void signUp(String userName,String userPassowrd,String userEmail) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail.trim(), userPassowrd)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(userName).build();
                        FirebaseAuth.getInstance().getCurrentUser().updateProfile(userProfileChangeRequest);
                   UserModel userModel = new UserModel(FirebaseAuth.getInstance().getUid(), userName,userPassowrd , userEmail);
                        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userModel);
                        Intent intent = new Intent(CreatAccountActivity.this, SignInPageActivity.class);
                        intent.putExtra("name", userName);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("fddfgdfgdfgfd", e.getMessage());
                        Toast.makeText(CreatAccountActivity.this, "SignUp faild", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean validPassowrd(String passowr) {
        if (passowr.isEmpty()) {



            Toast.makeText(CreatAccountActivity.this, "empty passowrd", Toast.LENGTH_LONG).show();
            return false;

        } else if (passowr.length() >= 8) {
            for (char c : passowr.toCharArray()) {
                if (Character.isUpperCase(c))
                    hasuppercase = true;
                if (Character.isLowerCase(c))
                    haslowercase = true;
                if (Character.isDigit(c))
                    hasdigit = true;
            }
            // hasspecialchar= Pattern.compile("[!@#$%^&*()\\-_=+\\[\\]{}|;'\".,<>/?]").matcher(passowr).find();
            final String special = "[!@#$%^&*()\\-_=+\\[\\]{}|;:'\"\\,.<>/?]";
            Pattern pattern = Pattern.compile(special);


            Matcher matcher = pattern.matcher(passowr);


            hasspecialChar = matcher.find();
            if (hasuppercase && haslowercase && hasdigit && hasspecialChar)
                return true;
            else {


                Toast.makeText(this, "weak password it should contain number and lower anupper case and special char", Toast.LENGTH_LONG).show();
                return false;

            }
        } else {

            Toast.makeText(this, "short password", Toast.LENGTH_LONG).show();
            return false;
        }
    }


    public boolean validConfirm(String pas, String con) {
        if (con.isEmpty()) {

            Toast.makeText(this, "empty confirm password", Toast.LENGTH_LONG).show();
            return false;
        }
        if (con.equals(pas))
            return true;
        else {

            Toast.makeText(this, "confirm password is not indetical with passowrd", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean validMail(String email) {

        if (email.isEmpty()) {

            Toast.makeText(this, "empty email", Toast.LENGTH_LONG).show();
            return false;
        }
        if (email.length() >= 10) {
            if (email.contains(" ")) {

                Toast.makeText(this, "invalid email it shouldn't contain spaces", Toast.LENGTH_LONG).show();
                return false;
            }
            if (email.contains(".com") && email.contains("@"))
                return true;
            else {

                Toast.makeText(this, "invalid email make sure you write .com and @", Toast.LENGTH_LONG).show();
                return false;
            }


        } else {

            Toast.makeText(this, "email is short it must be more than 10 charecter", Toast.LENGTH_LONG).show();
            return false;


        }}
}
