package com.example.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.ChatPage.ChatPageActivity;

import java.util.ArrayList;
import java.util.List;

public class UserAdaptorActivity2  extends RecyclerView.Adapter<UserAdaptorActivity2.MyViewHolder> {

    private Context context;

    private List<UserModel> userModellist;

    public UserAdaptorActivity2(Context context) {
        this.context = context;
        this.userModellist = new ArrayList<>();
    }


    public void add(UserModel userModel) {
        userModellist.add(userModel);
        notifyItemInserted(userModellist.size() - 1);
    }

    public void clear() {
        userModellist.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserAdaptorActivity2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_user_adaptor2, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder parent, int position) {

        UserModel userModel = userModellist.get(position);
        parent.Name.setText(userModel.getName());
        parent.Email.setText(userModel.getEmail());

        parent.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent = new Intent(context, ChatPageActivity.class);
                intent.putExtra("id", userModel.getId());
                intent.putExtra("name", userModel.getName());
                context.startActivity(intent);
            }
        });

    }

    public List<UserModel> getUserModellist() {
        return userModellist;
    }

    @Override
    public int getItemCount() {
        return userModellist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

     TextView Name, Email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.name);
            Email = itemView.findViewById(R.id.email);
        }
    }
} 