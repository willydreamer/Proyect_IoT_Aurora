package com.example.aurora.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Bean.EquipoAdmin;
import com.example.aurora.CrearEquipoActivity;
import com.example.aurora.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ListaEquiposAdapterAdmin extends RecyclerView.Adapter<ListaEquiposAdapterAdmin.EquipoAdminViewHolder>{
    private Context context;
    FirebaseFirestore db;
    private ArrayList<EquipoAdmin> listaEquipos;


    public class EquipoAdminViewHolder extends RecyclerView.ViewHolder{
        EquipoAdmin equipo;

        FirebaseFirestore db;

        ImageView fotoEquipo;

        public EquipoAdminViewHolder(@NonNull View itemView) {

            super(itemView);
            fotoEquipo = itemView.findViewById(R.id.imagenEquipo2);

        }
    }


    @NonNull
    @Override
    //Para inflar la vista
    public ListaEquiposAdapterAdmin.EquipoAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        db = FirebaseFirestore.getInstance();
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_equipos_admin, parent, false);
        return new ListaEquiposAdapterAdmin.EquipoAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaEquiposAdapterAdmin.EquipoAdminViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();

        EquipoAdmin e = listaEquipos.get(position);
        holder.equipo = e;

        TextView nombreEquipo = holder.itemView.findViewById(R.id.textTituloEquipo2);
        nombreEquipo.setText(e.getMarca()+" - "+e.getModelo());

        TextView tipoEquipo = holder.itemView.findViewById(R.id.textTipoEquipo2);
        tipoEquipo.setText(e.getTipoDeEquipo());

        TextView numeroDeSerie = holder.itemView.findViewById(R.id.textNumeroSerie2);
        numeroDeSerie.setText(e.getTipoDeEquipo());

        if (e.getFotoEquipo() != null && !e.getFotoEquipo().isEmpty()) {
            Picasso.get()
                    .load(e.getFotoEquipo())
                    .placeholder(R.drawable.perfil_icono) // Reemplaza con tu imagen por defecto
                    .transform(new CropCircleTransformation())
                    .into(holder.fotoEquipo);
        } else {
            Picasso.get()
                    .load(R.drawable.perfil_icono) // Imagen por defecto
                    .transform(new CropCircleTransformation())
                    .into(holder.fotoEquipo);
        }
        context = holder.itemView.getContext();
        Button botonVer = holder.itemView.findViewById(R.id.buttonVer);
        botonVer.setOnClickListener(view -> {
            //if(s.getEncargado() == null || s.getEncargado().isEmpty()){
            Intent intent = new Intent(context, CrearEquipoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("equipo",e);
            context.startActivity(intent);
            /*}else if(s.getEncargado() != null) {
                Intent intent = new Intent(context, AdminInformacionSitioActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("sitio", s);
                context.startActivity(intent);
            }*/});

        //            if(s.getSupervisor()!=null) {
//                Intent intent = new Intent(context, AsignarSitioActivity.class); // Reemplaza "TuActivity" con el nombre de tu Activity
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("sitio", s);
//                intent.putExtra("supervisor",supervisor);
//                context.startActivity(intent);
//            }else if (s.getSupervisor() == null){
//                Intent intent = new Intent(context, AdminInformacionSitioActivity.class); // Reemplaza "TuActivity" con el nombre de tu Activity
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("sitio", s);
//                context.startActivity(intent);

    }



    @Override
    public int getItemCount() {
        //Este método debe indicar la cantidad total de elementos, en nuestro caso, del
        //arreglo “data”.
        return listaEquipos.size();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<EquipoAdmin> getListaEquipos() {
        return listaEquipos;
    }

    public void setListaEquipos(ArrayList<EquipoAdmin> listaEquipos) {
        this.listaEquipos = listaEquipos;
    }
}