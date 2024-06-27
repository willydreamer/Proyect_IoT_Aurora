package com.example.aurora.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Bean.Log;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.R;
import com.example.aurora.Superadmin.SuperAdmin;
import com.example.aurora.Superadmin.SuperAdminEditarAdministradorFragment;
import com.example.aurora.Superadmin.SuperAdminLogDetalles;

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
        holder.bind(log);
    }

    @Override
    public int getItemCount() {
        return listaLog.size();
    }

    public class LogViewHolder extends RecyclerView.ViewHolder {

        TextView textViewFecha;
        TextView textViewActividad;
        TextView textViewDescripcion;

        ImageView imageView13;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFecha = itemView.findViewById(R.id.textView27);
            textViewActividad = itemView.findViewById(R.id.textView28);
            textViewDescripcion = itemView.findViewById(R.id.textView29);
            imageView13 = itemView.findViewById(R.id.imageView13);
        }

        public void bind(Log log) {
            Date hola = log.getTimestamp();
            String fecha = String.valueOf(hola);
            textViewFecha.setText(fecha);
            textViewActividad.setText(log.getActividad());

            String olausuario;

            if(log.getUsuario()!= null) {
                olausuario = log.getUsuario().getNombre() + " " +log.getUsuario().getApellido();
            }else {
                olausuario = "Sin Usuario";
            }

            textViewDescripcion.setText(olausuario);
            imageView13.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Crear el fragmento de detalle y pasar los datos del usuario seleccionado
                    SuperAdminLogDetalles fragment = SuperAdminLogDetalles.newInstance(log);

                    // Navegar al nuevo fragmento
                    ((SuperAdmin) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container1, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }
}
