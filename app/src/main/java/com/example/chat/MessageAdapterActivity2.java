package com.example.chat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapterActivity2 extends RecyclerView.Adapter<MessageAdapterActivity2.MyViewHolder> {
    private static final int messagesend = 1;
    private static final int messagereceive = 2;
    public Object clear;
    private Context context;
    private List<Message> messageList;

    public MessageAdapterActivity2(Context context) {
        this.context = context;
        this.messageList = new ArrayList<>();
    }

    public void add(Message message) {

        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);

    }

    public void clear() {
        messageList.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        Log.d("asdasdasdasdasdasd", String.valueOf(viewType));
        if (viewType == messagesend) {
            View view = LayoutInflater.from(context).inflate(R.layout.messagesend, parent,false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.messagereceive, parent,false);
            return new MyViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message MessageModel = messageList.get(position);

        Log.d("erwersdfcxvxcv", messageList.get(position).messageid);
        Log.d("erwersdfcxvxcv", messageList.get(position).senderid);

        if (MessageModel.getSenderid().equals(FirebaseAuth.getInstance().getUid())) {
            holder.TextSendMessage.setText(MessageModel.getMessage());
        } else {
            holder.TextReceiveMessage.setText(MessageModel.getMessage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getSenderid().equals(FirebaseAuth.getInstance().getUid())) {
            return messagesend;

        } else {
            return messagereceive;
        }

    }

    public List<Message> getMessageList() {
        return messageList;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView TextSendMessage, TextReceiveMessage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TextSendMessage = itemView.findViewById(R.id.textviewsendmesssage);
            TextReceiveMessage = itemView.findViewById(R.id.textviewreceivedmesssage);
        }

    }

}