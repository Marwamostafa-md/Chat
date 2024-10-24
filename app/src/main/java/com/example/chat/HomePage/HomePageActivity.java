package com.example.chat.HomePage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.R;
import com.example.chat.SignInPage.SignInPageActivity;
import com.example.chat.UserAdaptorActivity2;
import com.example.chat.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class HomePageActivity extends AppCompatActivity {
Toolbar toolbar;
RecyclerView recyclerView;
Context context;
String myName;
DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page);
      toolbar=findViewById(R.id.toolbar);
      recyclerView=findViewById(R.id.recycler);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");
        context=this;
        setSupportActionBar(toolbar);
        myName=getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(myName);
        UserAdaptorActivity2 useradaptoractivity2 = new UserAdaptorActivity2(context);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                useradaptoractivity2.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String userId = dataSnapshot.getKey();
                   UserModel user = dataSnapshot.getValue(UserModel.class);
                    if (user != null && user.getId() != null &&
                            !user.getId().equals(FirebaseAuth.getInstance().getUid())) {
                        useradaptoractivity2.add(user);
                    }else {
                    }
                }
               List<UserModel> list = useradaptoractivity2.getUserModellist();
                useradaptoractivity2.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("dfgdfgertert","200");
            }
        });

        recyclerView.setAdapter( useradaptoractivity2);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.mymenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout){

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(context, SignInPageActivity.class));
            finish();
            return true;
        }
        else
            return false;
    }


}