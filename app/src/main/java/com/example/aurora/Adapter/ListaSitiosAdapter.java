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
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Bean.Sitio;
import com.example.aurora.InformacionSupervisorActivity;
import com.example.aurora.R;

import java.util.ArrayList;
import java.util.List;


public class ListaSitiosAdapter
        extends RecyclerView.Adapter<ListaSitiosAdapter.SitioViewHolder>{
    private Context context;

    private ArrayList<Sitio> listaSitios;

    public class SitioViewHolder extends RecyclerView.ViewHolder{
        Sitio sitio;

        public SitioViewHolder(@NonNull View itemView) {

            super(itemView);
        }
    }

    /*
    public ListaSitiosAdapter(ArrayList<Sitio> listaSitios){
        this.listaSitios = listaSitios;
    }

    public class SitioViewHolder extends RecyclerView.ViewHolder{
        //Supervisor supervisor;
        TextView codigoSitio;
        TextView ubicacionSitio;
        public SitioViewHolder(@NonNull View itemView) {
            super(itemView);
            codigoSitio = itemView.findViewById(R.id.textTitle1);
            ubicacionSitio = itemView.findViewById(R.id.textSubtitle1);
        }

        public void setearDatosSitio(Sitio sitio) {
                codigoSitio.setText("Código: " + sitio.getIdSitio());
                Log.d("msg-test",sitio.getDistrito());
                ubicacionSitio.setText(sitio.getDepartamento() + "," + sitio.getProvincia() + "," + sitio.getDistrito());

        }
    }

    @NonNull
    @Override
    public ListaSitiosAdapter.SitioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_sitios, null, false);
        return new ListaSitiosAdapter.SitioViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ListaSitiosAdapter.SitioViewHolder holder, int position) {

        context = holder.itemView.getContext();
        ImageButton flecha1 = holder.itemView.findViewById(R.id.flecha1);
        flecha1.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), InformacionSupervisorActivity.class); // Reemplaza "TuActivity" con el nombre de tu Activity
            context.startActivity(intent);
        });

        holder.setearDatosSitio(listaSitios.get(position));



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

    public ListaSitiosAdapter(ArrayList<Sitio> listaSitios){
        this.listaSitios = listaSitios;
    }

    public class SitioViewHolder extends RecyclerView.ViewHolder{
        //Supervisor supervisor;
        TextView codigoSitio;
        TextView ubicacionSitio;
        public SitioViewHolder(@NonNull View itemView) {
            super(itemView);
            codigoSitio = itemView.findViewById(R.id.textTitle1);
            ubicacionSitio = itemView.findViewById(R.id.textSubtitle1);
        }

        public void setearDatosSitio(Sitio sitio) {
            codigoSitio.setText("Código: " + sitio.getIdSitio());
            Log.d("msg-test",sitio.getDistrito());
            ubicacionSitio.setText(sitio.getDepartamento() + "," + sitio.getProvincia() + "," + sitio.getDistrito());

        }
    } */

    @NonNull
    @Override
    //Para inflar la vista
    public SitioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_sitios, parent, false);
        return new SitioViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull SitioViewHolder holder, int position) {

        /*ImageButton flecha1 = holder.itemView.findViewById(R.id.flecha1);
        flecha1.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), InformacionSupervisorActivity.class); // Reemplaza "TuActivity" con el nombre de tu Activity
            context.startActivity(intent);
        });*/

        //holder.setearDatosSitio(listaSitios.get(position));

        Sitio sitio = listaSitios.get(position) ;
        holder.sitio = sitio;
        TextView codigoSitio = holder.itemView.findViewById(R.id.textTitle1);
        codigoSitio.setText("Código: " + sitio.getIdSitio());
        TextView ubicacionSitio= holder.itemView.findViewById(R.id.textSubtitle1);
        ubicacionSitio.setText(sitio.getDepartamento() + "," + sitio.getProvincia() + "," + sitio.getDistrito());

    }

    @Override
    public int getItemCount() {
        //Este método debe indicar la cantidad total de elementos, en nuestro caso, del
        //arreglo “data”.
        return listaSitios.size();
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
}

