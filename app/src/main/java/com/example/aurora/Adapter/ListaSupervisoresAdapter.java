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

import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.InformacionSupervisorActivity;
import com.example.aurora.R;

import java.util.ArrayList;

public class ListaSupervisoresAdapter
        extends RecyclerView.Adapter<ListaSupervisoresAdapter.SupervisorViewHolder>{

    private Context context;

    private ArrayList<Usuario> listaSupervisores;

    /*public ListaSupervisoresAdapter(ArrayList<String> listSupervisores){
        this.listaSupervisores= listSupervisores;
    }*/

    public class SupervisorViewHolder extends RecyclerView.ViewHolder{
        //Supervisor supervisor;
        //TextView nombre;

        Usuario supervisor;
        public SupervisorViewHolder(@NonNull View itemView) {
            super(itemView);


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
    }
}
