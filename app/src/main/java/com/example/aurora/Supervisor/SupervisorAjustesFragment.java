package com.example.aurora.Supervisor;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aurora.Bean.Usuario;
import com.example.aurora.General.LoginFragment;
import com.example.aurora.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class SupervisorAjustesFragment extends Fragment {

    private FirebaseFirestore db;
    private Usuario usuario;

    private FirebaseUser usuarioLogueado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_supervisor_ajustes, container, false);

        // Configurar el botón de cerrar sesión
        AppCompatButton buttonCerrarSesion = view.findViewById(R.id.buttonCerrarSesion2);

        ImageView fotoUsuario = view.findViewById(R.id.imageView2);
        TextView nombre = view.findViewById(R.id.textView);
        TextView correo = view.findViewById(R.id.textView2);


        db = FirebaseFirestore.getInstance();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        usuarioLogueado = firebaseAuth.getCurrentUser();
        if (usuarioLogueado != null) { //user logged-in
            //Ojo uid del firebase user =! id del Document
            String correoStr = usuarioLogueado.getEmail();
            Log.d("correo", correoStr);
            //obtenerUsuarioPorCorreo(correo);
            db.collection("usuarios")
                    .whereEqualTo("correo", correoStr)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("document", document.toString());
                                    usuario = document.toObject(Usuario.class);
                                    Log.d("usuario", usuario.getNombre());

                                    if (usuario.getFotoURL() != null && !usuario.getFotoURL().isEmpty()) {
                                        Picasso.get()
                                                .load(usuario.getFotoURL())
                                                .placeholder(R.drawable.profile) // Reemplaza con tu imagen por defecto
                                                .transform(new CropCircleTransformation())
                                                .into(fotoUsuario);
                                    } else {
                                        Picasso.get()
                                                .load(R.drawable.profile) // Imagen por defecto
                                                .transform(new CropCircleTransformation())
                                                .into(fotoUsuario);
                                    }

                                    nombre.setText(usuario.getNombre()+" "+usuario.getApellido());
                                    correo.setText(usuario.getCorreo());
                                }
                            } else {
                                Log.d("Error", "no se encontró el usuario");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
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

        return view;

    }
}