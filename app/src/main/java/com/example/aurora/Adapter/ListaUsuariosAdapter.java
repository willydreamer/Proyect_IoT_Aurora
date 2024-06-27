package com.example.aurora.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Bean.Usuario;
import com.example.aurora.R;
import com.example.aurora.Superadmin.SuperAdmin;
import com.example.aurora.Superadmin.SuperAdminEditarAdministradorFragment;
import com.example.aurora.Superadmin.SuperadminLogUsuario;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

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

        ImageView editImageView;
        ImageView foto;

        ImageView button;

        Usuario usuario;

        public UsuarioViewHolder(@NonNull View itemView){

            super(itemView);
            nombreUsuario = itemView.findViewById(R.id.textView25);
            rolUsuario = itemView.findViewById(R.id.textView26);
            editImageView = itemView.findViewById(R.id.imageView16);
            foto = itemView.findViewById(R.id.imageView10);
            button = itemView.findViewById(R.id.imageView4);
        }
        public void bind(Usuario usuario) {
            nombreUsuario.setText(usuario.getNombre());
            rolUsuario.setText(usuario.getRol());
            if ("administrador".equals(usuario.getRol())) {
                rolUsuario.setTextColor(context.getResources().getColor(R.color.blue));
                editImageView.setVisibility(View.VISIBLE);
            } else if ("supervisor".equals(usuario.getRol())) {
                rolUsuario.setTextColor(context.getResources().getColor(R.color.light_green));
                editImageView.setVisibility(View.GONE);
            }
            if (usuario.getFotoURL() != null && !usuario.getFotoURL().isEmpty()) {
                Picasso.get()
                        .load(usuario.getFotoURL())
                        .placeholder(R.drawable.perfil_icono) // Reemplaza con tu imagen por defecto
                        .transform(new CropCircleTransformation())
                        .into(foto);
            } else {
                Picasso.get()
                        .load(R.drawable.perfil_icono) // Imagen por defecto
                        .transform(new CropCircleTransformation())
                        .into(foto);
            }

            editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Crear el fragmento de detalle y pasar los datos del usuario seleccionado
                    SuperAdminEditarAdministradorFragment fragment = SuperAdminEditarAdministradorFragment.newInstance(usuario);

                    // Navegar al nuevo fragmento
                    ((SuperAdmin) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container1, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SuperadminLogUsuario fragment = SuperadminLogUsuario.newInstance(usuario.getIdUsuario());

                    // Navegar al nuevo fragmento
                    ((SuperAdmin) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container1, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }

}
