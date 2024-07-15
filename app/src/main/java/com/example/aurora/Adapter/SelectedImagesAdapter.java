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

public class SelectedImagesAdapter extends RecyclerView.Adapter<SelectedImagesAdapter.ImageViewHolder> {

    private Context context;
    private List<Uri> selectedImageUris;
    //private List<Uri> imageUris;

    public SelectedImagesAdapter(Context context, List<Uri> selectedImageUris) {
        this.context = context;
        this.selectedImageUris = selectedImageUris;
        //this.imageUris = imageUris;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_selected_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri imageUri = selectedImageUris.get(position);
        //Picasso.get().load(imageUri).into(holder.imageView);
        Picasso.get()
                .load(imageUri)
                .placeholder(R.drawable.perfil_icono) // Reemplaza con tu imagen por defecto
                .transform(new CropCircleTransformation())
                .into(holder.imageView);

        //basado de gpt
        /*holder.imageView.setOnClickListener(v -> {
            if (selectedImageUris.contains(imageUri)) {
                selectedImageUris.remove(imageUri);
                holder.imageView.setAlpha(1.0f); // Quitar el efecto de selección
            } else {
                selectedImageUris.add(imageUri);
                holder.imageView.setAlpha(0.5f); // Añadir un efecto visual para indicar selección
            }
        });*/
        holder.buttonDelete.setOnClickListener(v -> {
            selectedImageUris.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, selectedImageUris.size());
        });
    }

    @Override
    public int getItemCount() {
        return selectedImageUris.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton buttonDelete;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagenEquipo2);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }

    public List<Uri> getSelectedImageUris() {
        return selectedImageUris;
    }

    public void addPhoto(Uri photoUri) {
        selectedImageUris.add(photoUri);
        notifyItemInserted(selectedImageUris.size() - 1);
    }


    //basado en gpt
    /*public void removeSelectedImages() {
        imageUris.removeAll(selectedImageUris);
        selectedImageUris.clear();
        notifyDataSetChanged();
    }

    public void addImage(Uri imageUri) {
        imageUris.add(imageUri);
        notifyItemInserted(imageUris.size() - 1);
    }*/
}
