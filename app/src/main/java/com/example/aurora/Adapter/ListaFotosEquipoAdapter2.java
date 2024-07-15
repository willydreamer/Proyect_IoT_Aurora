package com.example.aurora.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ListaFotosEquipoAdapter2 extends RecyclerView.Adapter<ListaFotosEquipoAdapter2.FotoEquipoViewHolder> {

    private Context context;
    private List<Uri> fotosEquipo;
    public boolean isEditMode = false;

    public ListaFotosEquipoAdapter2(Context context, List<Uri> fotosEquipo) {
        this.context = context;
        this.fotosEquipo =fotosEquipo;
    }


    @NonNull
    @Override
    public ListaFotosEquipoAdapter2.FotoEquipoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_foto_equipo_edit, parent, false);
        return new ListaFotosEquipoAdapter2.FotoEquipoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaFotosEquipoAdapter2.FotoEquipoViewHolder holder, int position) {
        /*Object photo = fotosEquipo.get(position);
        if (photo instanceof Uri) {
            Picasso.get()
                    .load((Uri) photo)
                    .placeholder(R.drawable.perfil_icono) // Reemplaza con tu imagen por defecto
                    .transform(new CropCircleTransformation())
                    .into(holder.imageView);
        } else if (photo instanceof String) {
            Picasso.get()
                    .load((String) photo)
                    .placeholder(R.drawable.perfil_icono) // Reemplaza con tu imagen por defecto
                    .transform(new CropCircleTransformation())
                    .into(holder.imageView);
        }*/
        Uri imageUri = fotosEquipo.get(position);
        //Picasso.get().load(imageUri).into(holder.imageView);
        Picasso.get()
                .load(imageUri)
                .placeholder(R.drawable.perfil_icono) // Reemplaza con tu imagen por defecto
                .transform(new CropCircleTransformation())
                .into(holder.imageView);



        //basado de gpt
        holder.buttonDelete.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
        holder.buttonDelete.setOnClickListener(v -> {
            fotosEquipo.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, fotosEquipo.size());
        });
    }

    @Override
    public int getItemCount() {
        return fotosEquipo.size();
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        notifyDataSetChanged();
    }

    public static class FotoEquipoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton buttonDelete;

        public FotoEquipoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagenEquipo2);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }

    public List<Uri> getFotosEquipo() {
        return fotosEquipo;
    }

    public void setFotosEquipo(List<Uri> fotosEquipo) {
        this.fotosEquipo = fotosEquipo;
    }

    public void addPhoto(Uri photoUri) {
        fotosEquipo.add(photoUri);
        notifyItemInserted(fotosEquipo.size() - 1);
    }
}

