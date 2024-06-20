package com.example.aurora.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Bean.Message2;
import com.example.aurora.R;

import android.text.format.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MessageAdapter2 extends RecyclerView.Adapter<MessageAdapter2.Message2ViewHolder> {

    private ArrayList<Message2> listaMensajes;
    private Context context;

    public MessageAdapter2(Context context,ArrayList<Message2> listaMensajes) {
        this.context=context;
        this.listaMensajes = listaMensajes;
    }

    @NonNull
    @Override
    public Message2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new Message2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Message2ViewHolder holder, int position) {
        Message2 message = listaMensajes.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    public class Message2ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewMessage;
        TextView textViewTimestamp;

        public Message2ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.text_view_message);
            textViewTimestamp = itemView.findViewById(R.id.text_view_timestamp);
        }

        public void bind(Message2 message) {
            textViewMessage.setText(message.getMensaje());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getDefault());
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            cal.setTime(message.getFecha());
            textViewTimestamp.setText(sdf.format(message.getFecha()));
            //textViewTimestamp.setText(DateFormat.format("HH:mm",message.getFecha()).toString());

        }
    }

    public ArrayList<Message2> getListaMensajes() {
        return listaMensajes;
    }

    public void setListaMensajes(ArrayList<Message2> listaMensajes) {
        this.listaMensajes = listaMensajes;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

