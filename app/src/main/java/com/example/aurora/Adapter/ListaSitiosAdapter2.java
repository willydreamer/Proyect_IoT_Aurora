package com.example.aurora.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Admin.AdminInformacionSitioActivity;
import com.example.aurora.Admin.AsignarSitioActivity;
import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.R;
import com.example.aurora.Supervisor.SupervisorInfoSitio;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class ListaSitiosAdapter2
        extends RecyclerView.Adapter<ListaSitiosAdapter2.SitioViewHolder>{
    private Context context;
    FirebaseFirestore db;
    private ArrayList<Sitio> listaSitios;

    private Usuario supervisor;
    public class SitioViewHolder extends RecyclerView.ViewHolder{
        Sitio sitio;
        //TextView codigoSitio;
        Usuario supervisor;

        FirebaseFirestore db;

        public SitioViewHolder(@NonNull View itemView) {

            super(itemView);

        }
    }

    @NonNull
    @Override
    //Para inflar la vista
    public SitioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        db = FirebaseFirestore.getInstance();
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_sitios_supervisor, parent, false);
        return new SitioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SitioViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();

        Sitio s = listaSitios.get(position);
        holder.sitio = s;

        TextView codigoSitio = holder.itemView.findViewById(R.id.textTitle1);
        codigoSitio.setText(s.getIdSitio());

        TextView zonaSitio = holder.itemView.findViewById(R.id.zona);
        zonaSitio.setText(s.getTipoDeZona());

        TextView sitioSitio = holder.itemView.findViewById(R.id.sitio);
        sitioSitio.setText("Movil");

        TextView ubicacionSitio = holder.itemView.findViewById(R.id.textSubtitle1);
        ubicacionSitio.setText(s.getDepartamento() + ", " + s.getProvincia() + ", " + s.getDistrito());


        context = holder.itemView.getContext();
        ImageButton flecha1 = holder.itemView.findViewById(R.id.flecha1);
        flecha1.setOnClickListener(view -> {
            Intent intent = new Intent(context, SupervisorInfoSitio.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("sitio", s);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return listaSitios.size();
    }

    public void updateList(ArrayList<Sitio> newList) {
        listaSitios = newList;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Sitio> getListaSitios() {
        return listaSitios;
    }

    public void setListaSitios(ArrayList<Sitio> listaSitios) {
        this.listaSitios = listaSitios;
    }

    public Usuario getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Usuario supervisor) {
        this.supervisor = supervisor;
    }
}

