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

import com.example.aurora.Bean.Equipo;
import com.example.aurora.CrearEquipoActivity;
import com.example.aurora.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ListaEquiposAdapter extends RecyclerView.Adapter<ListaEquiposAdapter.EquipoViewHolder>{
    private Context context;
    FirebaseFirestore db;
    private ArrayList<Equipo> listaEquipos;


    public class EquipoViewHolder extends RecyclerView.ViewHolder{
        Equipo equipo;

        FirebaseFirestore db;

        ImageView fotoEquipo;

        public EquipoViewHolder(@NonNull View itemView) {

            super(itemView);
            fotoEquipo = itemView.findViewById(R.id.imagenEquipo2);

        }
    }


    @NonNull
    @Override
    //Para inflar la vista
    public ListaEquiposAdapter.EquipoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        db = FirebaseFirestore.getInstance();
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_equipos, parent, false);
        return new ListaEquiposAdapter.EquipoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaEquiposAdapter.EquipoViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();

        Equipo e = listaEquipos.get(position);
        holder.equipo = e;

        TextView nombreEquipo = holder.itemView.findViewById(R.id.textTituloEquipo2);
        nombreEquipo.setText(e.getMarca()+" - "+e.getModelo());

        TextView tipoEquipo = holder.itemView.findViewById(R.id.textTipoEquipo2);
        tipoEquipo.setText(e.getTipoDeEquipo());

        TextView numeroDeSerie = holder.itemView.findViewById(R.id.textNumeroSerie2);
        numeroDeSerie.setText(e.getNumeroDeSerie());

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
                intent.putExtra("equipo", e);
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

    public ArrayList<Equipo> getListaEquipos() {
        return listaEquipos;
    }

    public void setListaEquipos(ArrayList<Equipo> listaEquipos) {
        this.listaEquipos = listaEquipos;
    }
}
