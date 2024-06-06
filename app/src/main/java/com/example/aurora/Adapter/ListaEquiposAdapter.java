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
import com.example.aurora.AsignarSitioActivity;
import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.R;

import java.util.ArrayList;


public class ListaEquiposAdapter
        extends RecyclerView.Adapter<ListaEquiposAdapter.SitioViewHolder>{
    private Context context;

    private ArrayList<Sitio> listaEquipos;

    private Usuario supervisor;

    @NonNull
    @Override
    public SitioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SitioViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class SitioViewHolder extends RecyclerView.ViewHolder{
        Sitio sitio;
        Usuario supervisor;

        public SitioViewHolder(@NonNull View itemView) {

            super(itemView);

        }
    }

}

