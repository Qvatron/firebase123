package com.example.firebasetest2; //поменять название пакета на свой

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    ArrayList<Message> messages;

    public MessageAdapter(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message m = messages.get(position);
        holder.content.setText(m.getContent());
        holder.date.setText(m.getDate());
        holder.user.setText(m.getUser().getUsername());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        TextView content;
        TextView date;
        TextView user;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.textViewContent);
            date = itemView.findViewById(R.id.textViewDate);
            user = itemView.findViewById(R.id.textViewUser);
        }
    }
}
