package com.example.aurora.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.aurora.Bean.Usuario;
import com.example.aurora.General.LoginFragment;
import com.example.aurora.R;
import com.example.aurora.databinding.FragmentNotificationsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private EditText nombreEditText, apellidoEditText, correoEditText, domicilioEditText, telefonoEditText;
    private ImageView foto;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        nombreEditText = binding.editTextText;
        apellidoEditText = binding.editTextText2;
        correoEditText = binding.editTextText3;
        domicilioEditText = binding.editTextText4;
        telefonoEditText = binding.editTextText5;
        foto = binding.imageView6;

        /*final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        FirebaseAuth.getInstance();

        Button buttonCerrarSesion = binding.close;
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

        obtenerDatosUsuario();
        return root;
    }

    private void obtenerDatosUsuario() {
        FirebaseUser user = auth.getCurrentUser();
        String user1 = auth.getUid();
        Log.d("USARUAIOS",user1);
        if (user != null) {
            String userId = user.getEmail();
            Log.d("USUARIO",userId);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String idUsuario = "SUPER111"; // Valor que deseas buscar

            db.collection("usuarios")
                    .whereEqualTo("idUsuario", idUsuario)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            Usuario usuario = documentSnapshot.toObject(Usuario.class);
                            if (usuario != null) {
                                nombreEditText.setText(usuario.getNombre());
                                apellidoEditText.setText(usuario.getApellido());
                                correoEditText.setText(usuario.getCorreo());
                                domicilioEditText.setText(usuario.getDomicilio());
                                telefonoEditText.setText(usuario.getTelefono());
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
                            }
                        } else {
                            Toast.makeText(getContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error al buscar el usuario", Toast.LENGTH_SHORT).show();
                        Log.e("FirestoreError", "Error al buscar el usuario", e);
                    });

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}