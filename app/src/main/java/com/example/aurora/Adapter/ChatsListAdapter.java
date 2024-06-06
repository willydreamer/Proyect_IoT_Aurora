package com.example.aurora.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Bean.Chat;
import com.example.aurora.R;

import java.util.List;

public class ChatsListAdapter extends RecyclerView.Adapter<ChatsListAdapter.ChatViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Chat chat);
    }

    private List<Chat> chatsList;
    private OnItemClickListener listener;

    public ChatsListAdapter(List<Chat> chatsList, OnItemClickListener listener) {
        this.chatsList = chatsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatsList.get(position);
        holder.bind(chat, listener);
    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView textViewChatName;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewChatName = itemView.findViewById(R.id.text_view_chat_name);
        }

        public void bind(final Chat chat, final OnItemClickListener listener) {
            textViewChatName.setText(chat.getChatName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(chat);
                }
            });
        }
    }
}
