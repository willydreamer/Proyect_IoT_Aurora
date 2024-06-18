package com.example.aurora.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ListaFotosEquipoAdapter extends RecyclerView.Adapter<ListaFotosEquipoAdapter.FotoEquipoViewHolder> {

    private Context context;
    private List<String> fotosEquipo;

    private List<Uri> imageUris;

    private List<Uri> selectedImageUris;

    public ListaFotosEquipoAdapter(Context context, List<String> fotosEquipo, List<Uri> imageUris ) {
        this.context = context;
        this.fotosEquipo = fotosEquipo;
        this.imageUris = imageUris;
        this.selectedImageUris = new ArrayList<>();
    }

    @NonNull
    @Override
    public ListaFotosEquipoAdapter.FotoEquipoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_foto_equipo, parent, false);
        return new ListaFotosEquipoAdapter.FotoEquipoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaFotosEquipoAdapter.FotoEquipoViewHolder holder, int position) {
        String foto = fotosEquipo.get(position);
        //Picasso.get().load(imageUri).into(holder.imageView);
        Picasso.get()
                .load(foto)
                .placeholder(R.drawable.perfil_icono) // Reemplaza con tu imagen por defecto
                .transform(new CropCircleTransformation())
                .into(holder.imageView);


        Uri imageUri = imageUris.get(position);
        //basado de gpt
        holder.imageView.setOnClickListener(v -> {
            if (selectedImageUris.contains(imageUri)) {
                selectedImageUris.remove(imageUri);
                holder.imageView.setAlpha(1.0f); // Quitar el efecto de selección
            } else {
                selectedImageUris.add(imageUri);
                holder.imageView.setAlpha(0.5f); // Añadir un efecto visual para indicar selección
            }
        });
    }

    @Override
    public int getItemCount() {
        return fotosEquipo.size();
    }

    public static class FotoEquipoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public FotoEquipoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagenEquipo2);
        }
    }

    public List<Uri> getSelectedImageUris() {
        return selectedImageUris;
    }

    public void removeSelectedImages() {
        imageUris.removeAll(selectedImageUris);
        selectedImageUris.clear();
        notifyDataSetChanged();
    }
}

