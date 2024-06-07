package com.example.aurora.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Bean.Log;
import com.example.aurora.R;

import java.util.Date;
import java.util.List;

public class ListaLogAdapter extends RecyclerView.Adapter<ListaLogAdapter.LogViewHolder>{

    private List<Log> listaLog;

    private Context context;

    public ListaLogAdapter(List<Log> listaLog, Context context) {
        this.listaLog = listaLog;
        this.context = context;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_log,parent,false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        Log log = listaLog.get(position);
        Date hola = log.getTimestamp();
        String fecha = String.valueOf(hola);
        holder.textViewFecha.setText(fecha);
        holder.textViewActividad.setText(log.getActividad());
        holder.textViewDescripcion.setText(log.getDescription());

    }

    @Override
    public int getItemCount() {
        return listaLog.size();
    }

    public class LogViewHolder extends RecyclerView.ViewHolder {

        TextView textViewFecha;
        TextView textViewActividad;
        TextView textViewDescripcion;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFecha = itemView.findViewById(R.id.textView27);
            textViewActividad = itemView.findViewById(R.id.textView28);
            textViewDescripcion = itemView.findViewById(R.id.textView29);
        }
    }
}
