package com.example.aurora.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.AdminInformacionSitioActivity;
import com.example.aurora.Bean.Sitio;
import com.example.aurora.InformacionSupervisorActivity;
import com.example.aurora.R;

import java.util.ArrayList;

public class ListaMensajesAdapter extends RecyclerView.Adapter<ListaMensajesAdapter.MensajeViewHolder>{
    private Context context;

    private ArrayList<String> listaMensajes;

    public ListaMensajesAdapter(ArrayList<String> listaMensajes){
        this.listaMensajes= listaMensajes;
    }

    public class MensajeViewHolder extends RecyclerView.ViewHolder{
        //Sitio sitio;

        TextView mensaje;
        public MensajeViewHolder(@NonNull View itemView) {

            super(itemView);
            mensaje = itemView.findViewById(R.id.textView1);

        }

        public void setearDatos(String msg) {
            mensaje.setText(msg);
        }
    }

    @NonNull
    @Override
    //Para inflar la vista
    public ListaMensajesAdapter.MensajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_mensaje, parent, false);
        return new ListaMensajesAdapter.MensajeViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ListaMensajesAdapter.MensajeViewHolder holder, int position) {


        /*Sitio s = listaSitios.get(position) ;
        holder.sitio = s;
        TextView codigoSitio = holder.itemView.findViewById(R.id.textTitle1);
        codigoSitio.setText("Código: " + s.getIdSitio());
        TextView ubicacionSitio= holder.itemView.findViewById(R.id.textSubtitle1);
        ubicacionSitio.setText(s.getDepartamento() + "," + s.getProvincia() + "," + s.getDistrito());

        context = holder.itemView.getContext();
        ImageButton flecha1 = holder.itemView.findViewById(R.id.flecha1);
        flecha1.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AdminInformacionSitioActivity.class); // Reemplaza "TuActivity" con el nombre de tu Activity
            context.startActivity(intent);
        });*/

        context = holder.itemView.getContext();
        ImageButton flecha1 = holder.itemView.findViewById(R.id.flecha1);
        flecha1.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), InformacionSupervisorActivity.class); // Reemplaza "TuActivity" con el nombre de tu Activity
            context.startActivity(intent);
        });

        holder.setearDatos(listaMensajes.get(position));

    }

    @Override
    public int getItemCount() {
        //Este método debe indicar la cantidad total de elementos, en nuestro caso, del
        //arreglo “data”.
        return listaMensajes.size();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<String> getListaMensajes() {
        return listaMensajes;
    }

    public void setListaMensajes(ArrayList<String> listaMensajes) {
        this.listaMensajes = listaMensajes;
    }
}


