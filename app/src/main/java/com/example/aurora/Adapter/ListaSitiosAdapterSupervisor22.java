package com.example.aurora.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Bean.EquipoAdmin;
import com.example.aurora.Bean.Sitio;
import com.example.aurora.R;
import com.example.aurora.Supervisor.SupervisorAsignarSitioActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ListaSitiosAdapterSupervisor22 extends RecyclerView.Adapter<ListaSitiosAdapterSupervisor22.EquipoAdminViewHolder> {
    Context context;
    FirebaseFirestore db;
    ArrayList<Sitio> listaSitios;

    public class EquipoAdminViewHolder extends RecyclerView.ViewHolder {
        Sitio sitio;
        public EquipoAdminViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private WeakReference<SupervisorAsignarSitioActivity> weakReference;

    public ListaSitiosAdapterSupervisor22(ArrayList<Sitio> listaSitios, SupervisorAsignarSitioActivity activity) {
        this.listaSitios = listaSitios;
        this.weakReference = new WeakReference<>(activity);
    }

    @NonNull
    @Override
    public ListaSitiosAdapterSupervisor22.EquipoAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        db = FirebaseFirestore.getInstance();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_sitios_supervisor_elegir, parent, false);
        return new ListaSitiosAdapterSupervisor22.EquipoAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaSitiosAdapterSupervisor22.EquipoAdminViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();

        Sitio e = listaSitios.get(position);
        holder.sitio = e;

        TextView idEquipo = holder.itemView.findViewById(R.id.textTituloSitio2);
        idEquipo.setText(e.getIdSitio());

        TextView estado = holder.itemView.findViewById(R.id.textTipoSitio2);
        estado.setText(e.getDepartamento());

        TextView numeroDeSerie = holder.itemView.findViewById(R.id.textDistrito2Sitio);
        numeroDeSerie.setText(e.getDistrito());


        context = holder.itemView.getContext();
        Button buttonElegir = holder.itemView.findViewById(R.id.buttonElegirSitio);
        buttonElegir.setOnClickListener(view -> {
            SupervisorAsignarSitioActivity fragment = weakReference.get();
            if (fragment != null) {
                fragment.updateTextView(e.getIdSitio());
            }
        });
    }

    public void updateList(ArrayList<Sitio> newList) {
        listaSitios = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listaSitios.size();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Sitio> getListaEquipos() {
        return listaSitios;
    }

    public void setListaEquipos(ArrayList<Sitio> listaSitios) {
        this.listaSitios = listaSitios;
    }
}
