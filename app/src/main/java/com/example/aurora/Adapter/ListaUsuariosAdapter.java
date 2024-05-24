package com.example.aurora.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Bean.Usuario;
import com.example.aurora.R;

import java.util.List;

public class ListaUsuariosAdapter extends RecyclerView.Adapter<ListaUsuariosAdapter.UsuarioViewHolder>{

    private List<Usuario> listaUsuarios;

    private Context context;

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_usuarios,parent,false);

        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {

        return 0;
    }

    public class UsuarioViewHolder extends RecyclerView.ViewHolder{

        Usuario usuario;

        public UsuarioViewHolder(@NonNull View itemView){

            super(itemView);
        }
    }

}
