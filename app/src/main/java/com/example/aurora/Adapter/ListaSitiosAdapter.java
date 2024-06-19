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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class ListaSitiosAdapter
        extends RecyclerView.Adapter<ListaSitiosAdapter.SitioViewHolder>{
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
        db = FirebaseFirestore.getInstance();
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_sitios, parent, false);
        return new SitioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SitioViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();

        Sitio s = listaSitios.get(position);
        holder.sitio = s;

        TextView codigoSitio = holder.itemView.findViewById(R.id.textTitle1);
        codigoSitio.setText(s.getIdSitio());

        TextView ubicacionSitio = holder.itemView.findViewById(R.id.textSubtitle1);
        ubicacionSitio.setText(s.getDepartamento() + ", " + s.getProvincia() + ", " + s.getDistrito());

        TextView nombreEncargado = holder.itemView.findViewById(R.id.textNombre);

        // Por si existe un supervisor
        if (s.getEncargado() == null || s.getEncargado().isEmpty()) {
            nombreEncargado.setText("Sin asignar");
            nombreEncargado.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.rounded_background_red));
        } else {
            //nombreEncargado.setText("Sin asignar");
            db.collection("usuarios")
                    .document(s.getEncargado())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Usuario supervisor = document.toObject(Usuario.class);
                            if (supervisor != null) {
                                nombreEncargado.setText(supervisor.getNombre() + " " + supervisor.getApellido());
                            }
                        } else {
                            Log.d("msg-test", "get failed with ", task.getException());
                        }
                    });
        }

        context = holder.itemView.getContext();
        ImageButton flecha1 = holder.itemView.findViewById(R.id.flecha1);
        flecha1.setOnClickListener(view -> {
            if(s.getEncargado() == null || s.getEncargado().isEmpty()){
                Intent intent = new Intent(context, AdminInformacionSitioActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("sitio", s);
                intent.putExtra("supervisor", getSupervisor());
                context.startActivity(intent);
            }else if(s.getEncargado() != null) {
                Intent intent = new Intent(context, AdminInformacionSitioActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("sitio", s);
                context.startActivity(intent);
            }

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
        });
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

    public Usuario getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Usuario supervisor) {
        this.supervisor = supervisor;
    }
}

