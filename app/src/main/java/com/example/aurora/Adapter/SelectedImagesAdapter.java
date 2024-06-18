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

public class SelectedImagesAdapter extends RecyclerView.Adapter<SelectedImagesAdapter.ImageViewHolder> {

    private Context context;
    private List<Uri> imageUris;
    private List<Uri> selectedImageUris;

    public SelectedImagesAdapter(Context context, List<Uri> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
        this.selectedImageUris = new ArrayList<>();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_selected_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri imageUri = imageUris.get(position);
        //Picasso.get().load(imageUri).into(holder.imageView);
        Picasso.get()
                .load(imageUri)
                .placeholder(R.drawable.perfil_icono) // Reemplaza con tu imagen por defecto
                .transform(new CropCircleTransformation())
                .into(holder.imageView);

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
        return imageUris.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagenEquipo2);
        }
    }

    public List<Uri> getSelectedImageUris() {
        return selectedImageUris;
    }


    //basado en gpt
    public void removeSelectedImages() {
        imageUris.removeAll(selectedImageUris);
        selectedImageUris.clear();
        notifyDataSetChanged();
    }

    public void addImage(Uri imageUri) {
        imageUris.add(imageUri);
        notifyItemInserted(imageUris.size() - 1);
    }
}
