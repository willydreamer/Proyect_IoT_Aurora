package com.example.aurora.Superadmin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aurora.Bean.Usuario;
import com.example.aurora.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuperAdminCrearAdministradorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuperAdminCrearAdministradorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText editTextNombre;
    private EditText editTextApellido;
    private EditText editTextCorreo;
    private EditText editTextDomicilio;
    private EditText editTextTelefono;
    private Button buttonGuardar;

    private FirebaseFirestore db;

    public SuperAdminCrearAdministradorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SuperAdminCrearAdministradorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SuperAdminCrearAdministradorFragment newInstance(String param1, String param2) {
        SuperAdminCrearAdministradorFragment fragment = new SuperAdminCrearAdministradorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_super_admin_crear_administrador, container, false);

        editTextNombre = view.findViewById(R.id.editTextText);
        editTextApellido = view.findViewById(R.id.editTextText3);
        editTextCorreo = view.findViewById(R.id.editTextText2);
        editTextDomicilio = view.findViewById(R.id.editTextText4);
        editTextTelefono = view.findViewById(R.id.editTextText5);
        buttonGuardar = view.findViewById(R.id.button8);

        buttonGuardar.setOnClickListener(v -> guardarAdministrador());

        return view;
    }

    private void guardarAdministrador() {
        String nombre = editTextNombre.getText().toString().trim();
        String apellido = editTextApellido.getText().toString().trim();
        String correo = editTextCorreo.getText().toString().trim();
        String domicilio = editTextDomicilio.getText().toString().trim();
        String telefono = editTextTelefono.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) || TextUtils.isEmpty(correo) ||
                TextUtils.isEmpty(domicilio) || TextUtils.isEmpty(telefono)) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String idadmin = generarIdAdmin();

        Usuario nuevoAdministrador = new Usuario(idadmin, nombre, apellido, null, correo, domicilio, telefono, "administrador", "activo", null, null);

        db.collection("usuarios")
                .add(nuevoAdministrador)
                .addOnSuccessListener(documentReference -> {
                    Log.d("msg-test2", "Administrador guardado exitosamente");
                    Toast.makeText(getContext(), "Administrador creado exitosamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test3", "Error al guardar el administrador", e);
                    Toast.makeText(getContext(), "Error al crear el administrador", Toast.LENGTH_SHORT).show();
                });
    }

    private String generarIdAdmin() {
        String letrasAdmin = "ADMIN";
        Random random = new Random();
        int numeroAleatorio = random.nextInt(900) + 100; // Generar un número entre 100 y 999
        return letrasAdmin+numeroAleatorio;
    }
}