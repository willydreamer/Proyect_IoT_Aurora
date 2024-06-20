package com.example.aurora.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Bean.Usuario;
import com.example.aurora.Admin.InformacionSupervisorActivity;
import com.example.aurora.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ListaSupervisoresAdapter
        extends RecyclerView.Adapter<ListaSupervisoresAdapter.SupervisorViewHolder>{

    private Context context;

    private ArrayList<Usuario> listaSupervisores;

    public ListaSupervisoresAdapter(Context context, ArrayList<Usuario> supervisores) {
        this.context = context;
        this.listaSupervisores = supervisores;
    }

    /*public ListaSupervisoresAdapter(ArrayList<String> listSupervisores){
        this.listaSupervisores= listSupervisores;
    }*/

    public class SupervisorViewHolder extends RecyclerView.ViewHolder{
        //Supervisor supervisor;
        //TextView nombre;

        Usuario supervisor;
        ImageView fotoSupervisor;
        public SupervisorViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoSupervisor = itemView.findViewById(R.id.imageTitulo1);

        }

        /* public void setearDatos(String name) {
            nombre.setText(name);
        }*/
    }

    @NonNull
    @Override
    //Para inflar la vista
    public SupervisorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_super, null, false);
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_super, parent, false);
        return new SupervisorViewHolder(view);
        //return null;
    }
    @Override
    public void onBindViewHolder(@NonNull SupervisorViewHolder holder, int position) {

        Usuario s = listaSupervisores.get(position) ;
        holder.supervisor = s;
        TextView nombre = holder.itemView.findViewById(R.id.textTitle1);
        nombre.setText(s.getNombre()+" "+s.getApellido());
        TextView dni= holder.itemView.findViewById(R.id.textSubtitle1);
        dni.setText("DNI:"+s.getDni());
        TextView estado = holder.itemView.findViewById(R.id.textEstado);
        if(s.getEstado()!=null){
            if(s.getEstado().equals("activo")){
                estado.setText("Activo");
            }else if(s.getEstado().equals("inactivo")){
                estado.setText("Inactivo");
                estado.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
            }
        }
        //inicio foto
        // Cargar la imagen usando Picasso con transformación circular - basado en gpt
        if (s.getFotoURL() != null && !s.getFotoURL().isEmpty()) {
            Picasso.get()
                    .load(s.getFotoURL())
                    .placeholder(R.drawable.perfil_icono) // Reemplaza con tu imagen por defecto
                    .transform(new CropCircleTransformation())
                    .into(holder.fotoSupervisor);
        } else {
            Picasso.get()
                    .load(R.drawable.perfil_icono) // Imagen por defecto
                    .transform(new CropCircleTransformation())
                    .into(holder.fotoSupervisor);
        }
        //fin foto
        context = holder.itemView.getContext();
        ImageButton flecha1 = holder.itemView.findViewById(R.id.flecha1);
        flecha1.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), InformacionSupervisorActivity.class);
            intent.putExtra("supervisor",s);// Reemplaza "TuActivity" con el nombre de tu Activity
            context.startActivity(intent);
        });

        //holder.setearDatos(listaSupervisores.get(position));
        
    }


    @Override
    public int getItemCount() {
        //Este método debe indicar la cantidad total de elementos, en nuestro caso, del
        //arreglo “data”.
        return listaSupervisores.size();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Usuario> getListaSupervisores() {
        return listaSupervisores;
    }

    public void setListaSupervisores(ArrayList<Usuario> listaSupervisores) {
        this.listaSupervisores = listaSupervisores;
        notifyDataSetChanged();
    }
}
