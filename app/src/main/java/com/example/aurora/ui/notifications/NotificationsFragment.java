package com.example.aurora.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.aurora.Bean.Usuario;
import com.example.aurora.LoginFragment;
import com.example.aurora.R;
import com.example.aurora.databinding.FragmentNotificationsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private EditText nombreEditText, apellidoEditText, correoEditText, domicilioEditText, telefonoEditText;



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
        if (user != null) {
            String userId = user.getUid();
            db.collection("usuarios").document(userId).get()
                    .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Usuario usuario = documentSnapshot.toObject(Usuario.class);
                                if (usuario != null) {
                                    nombreEditText.setText(usuario.getNombre());
                                    apellidoEditText.setText(usuario.getApellido());
                                    correoEditText.setText(usuario.getCorreo());
                                    domicilioEditText.setText(usuario.getDomicilio());
                                    telefonoEditText.setText(usuario.getTelefono());
                                }
                            } else {
                                Toast.makeText(getContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new com.google.android.gms.tasks.OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}