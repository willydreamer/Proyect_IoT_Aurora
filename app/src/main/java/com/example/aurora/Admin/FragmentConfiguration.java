package com.example.aurora.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.aurora.EditarPerfilActivity;
import com.example.aurora.General.LoginFragment;
import com.example.aurora.R;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentConfiguration extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_fragment_configuration, container, false);

        // Configurar el botón de cerrar sesión
        AppCompatButton buttonCerrarSesion = view.findViewById(R.id.buttonCerrarSesion);

        AppCompatButton buttonEditarPerfil = view.findViewById(R.id.btnEditarPerfil);

        buttonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar sesión
                FirebaseAuth.getInstance().signOut();

                // Redirigir a LoginFragment (que es una actividad)
                Intent intent = new Intent(getActivity(), LoginFragment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        buttonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Redirigir a Editar Perfil (que es una actividad)
                Intent intent = new Intent(getActivity(), EditarPerfilActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        return view;
    }
}
