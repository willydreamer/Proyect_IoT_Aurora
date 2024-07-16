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
import com.example.aurora.R;
import com.example.aurora.Supervisor.SupervisorAsignarSitioActivity;
import com.example.aurora.Supervisor.SupervisorEstadoEquipoFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ListaEquiposAdapterSupervisor22 extends RecyclerView.Adapter<ListaEquiposAdapterSupervisor22.EquipoAdminViewHolder> {
    private Context context;
    FirebaseFirestore db;
    private ArrayList<EquipoAdmin> listaEquipos;

    public class EquipoAdminViewHolder extends RecyclerView.ViewHolder {
        EquipoAdmin equipo;
        FirebaseFirestore db;
        ImageView fotoEquipo;

        public EquipoAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoEquipo = itemView.findViewById(R.id.imagenEquipo2);
        }
    }

    private WeakReference<SupervisorAsignarSitioActivity> weakReference;

    public ListaEquiposAdapterSupervisor22(ArrayList<EquipoAdmin> listaEquipos, SupervisorAsignarSitioActivity activity) {
        this.listaEquipos = listaEquipos;
        this.weakReference = new WeakReference<>(activity);
    }

    @NonNull
    @Override
    public ListaEquiposAdapterSupervisor22.EquipoAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        db = FirebaseFirestore.getInstance();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_equipos_supervisor, parent, false);
        return new ListaEquiposAdapterSupervisor22.EquipoAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaEquiposAdapterSupervisor22.EquipoAdminViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();

        EquipoAdmin e = listaEquipos.get(position);
        holder.equipo = e;

        TextView idEquipo = holder.itemView.findViewById(R.id.textTituloEquipo2);
        idEquipo.setText(e.getIdEquipo());

        TextView estado = holder.itemView.findViewById(R.id.textNumeroSerie2);
        estado.setText(e.getEstado());

        TextView numeroDeSerie = holder.itemView.findViewById(R.id.textTipoEquipo2);
        numeroDeSerie.setText(e.getNumeroDeSerie());

        if (e.getFotosEquipo().get(0) != null && !e.getFotosEquipo().get(0).isEmpty()) {
            Picasso.get()
                    .load(e.getFotosEquipo().get(0))
                    .placeholder(R.drawable.perfil_icono)
                    .transform(new CropCircleTransformation())
                    .into(holder.fotoEquipo);
        } else {
            Picasso.get()
                    .load(R.drawable.perfil_icono)
                    .transform(new CropCircleTransformation())
                    .into(holder.fotoEquipo);
        }

        context = holder.itemView.getContext();
        Button buttonElegir = holder.itemView.findViewById(R.id.buttonElegir);
        buttonElegir.setOnClickListener(view -> {
            SupervisorAsignarSitioActivity fragment = weakReference.get();
            if (fragment != null) {
                fragment.updateTextView(e.getIdEquipo());
            }
        });
    }

    public void updateList(ArrayList<EquipoAdmin> newList) {
        listaEquipos = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
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
