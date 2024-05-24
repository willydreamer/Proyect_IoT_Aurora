package com.example.aurora.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Bean.Log;
import com.example.aurora.R;

import java.util.List;

public class ListaLogAdapter extends RecyclerView.Adapter<ListaLogAdapter.LogViewHolder>{

    private List<Log> listaLog;

    private Context context;

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_log,parent,false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class LogViewHolder extends RecyclerView.ViewHolder{

        Log log;

        public  LogViewHolder(@NonNull View itemView){
            super(itemView);
        }

    }
}
