package com.example.aurora.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Bean.Usuario;
import com.example.aurora.R;

import java.util.List;

public class ListaUsuariosAdapter extends RecyclerView.Adapter<ListaUsuariosAdapter.UsuarioViewHolder>{

    private List<Usuario> listaUsuarios;

    private Context context;

    public ListaUsuariosAdapter(List<Usuario> listaUsuarios, Context context) {
        this.listaUsuarios = listaUsuarios;
        this.context = context;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_usuarios,parent,false);

        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario = listaUsuarios.get(position);
        holder.bind(usuario);
    }

    @Override
    public int getItemCount() {

        return listaUsuarios.size();
    }

    public class UsuarioViewHolder extends RecyclerView.ViewHolder{

        TextView nombreUsuario;
        TextView rolUsuario;

        Usuario usuario;

        public UsuarioViewHolder(@NonNull View itemView){

            super(itemView);
            nombreUsuario = itemView.findViewById(R.id.textView25);
            rolUsuario = itemView.findViewById(R.id.textView26);
        }
        public void bind(Usuario usuario) {
            nombreUsuario.setText(usuario.getNombre());
            rolUsuario.setText(usuario.getRol());
        }
    }

}
