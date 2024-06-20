package com.example.aurora.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


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
        ImageView fotoSitio;


        public SitioViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoSitio = itemView.findViewById(R.id.imageTitulo1);
        }
    }

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

            if (s.getFotoURL() != null && !s.getFotoURL().isEmpty()) {
                Picasso.get()
                        .load(s.getFotoURL())
                        .placeholder(R.drawable.perfil_icono) // Reemplaza con tu imagen por defecto
                        .transform(new CropCircleTransformation())
                        .into(holder.fotoSitio);
            }

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
                if (s.getEncargado() == null || s.getEncargado().isEmpty()) {
                    Intent intent = new Intent(context, AdminInformacionSitioActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("sitio", s);
                    intent.putExtra("supervisor", s.getEncargado());
                    context.startActivity(intent);
                } else if (s.getEncargado() != null) {
                    Intent intent = new Intent(context, AdminInformacionSitioActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("sitio", s);
                    context.startActivity(intent);
                }

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

