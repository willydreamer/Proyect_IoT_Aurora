package com.example.aurora.Superadmin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.aurora.Bean.Usuario;
import com.example.aurora.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuperAdminEditarAdministradorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuperAdminEditarAdministradorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USUARIO = "usuario";
    private Usuario usuario;

    public SuperAdminEditarAdministradorFragment() {
        // Required empty public constructor
    }

    public static SuperAdminEditarAdministradorFragment newInstance(Usuario usuario) {
        SuperAdminEditarAdministradorFragment fragment = new SuperAdminEditarAdministradorFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USUARIO, usuario);
        fragment.setArguments(args);
        return fragment;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuario = (Usuario) getArguments().getSerializable(ARG_USUARIO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_super_admin_editar_administrador, container, false);

        EditText nombreEditText = view.findViewById(R.id.editTextText);
        EditText apellidoEditText = view.findViewById(R.id.editTextText3);
        EditText correoEditText = view.findViewById(R.id.editTextText2);
        EditText domicilioEditText = view.findViewById(R.id.editTextText4);
        EditText telefonoEditText = view.findViewById(R.id.editTextText5);

        Log.d("SuperAdmin", "Nombre: " + usuario.getNombre());
        Log.d("SuperAdmin", "Apellido: " + usuario.getApellido());

        if (usuario != null) {

            nombreEditText.setText(usuario.getNombre());
            apellidoEditText.setText(usuario.getApellido());
            correoEditText.setText(usuario.getCorreo());
            domicilioEditText.setText(usuario.getDomicilio());
            telefonoEditText.setText(usuario.getTelefono());
        }

        return view;
    }
}