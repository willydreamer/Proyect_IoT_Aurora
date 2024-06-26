package com.example.aurora.General;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aurora.Admin.MainActivity;
import com.example.aurora.R;
import com.example.aurora.Superadmin.SuperAdmin;
import com.example.aurora.Supervisor.Supervisor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class InicioFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio_fragment);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Button buttonIngresar = findViewById(R.id.buttonIngresar);
        buttonIngresar.setOnClickListener(v -> {
            if (currentUser != null) {
                String userId = currentUser.getUid();
                String emailLogueado = currentUser.getEmail();
                //Log.d("text-logueado", "Firebase correo: " + currentUser.getEmail());

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("usuarios")
                        .whereEqualTo("correo", emailLogueado)
                        .get()
                        .addOnCompleteListener(userTask -> {
                            for (QueryDocumentSnapshot document : userTask.getResult()) {
                                String role = document.getString("rol");
                                //Log.d("rol-autenticado-2", role);
                                redirectToRoleSpecificActivity(role);
                            }
                        });
            }
            else{
                Intent intent = new Intent(InicioFragment.this, LoginFragment.class);
                startActivity(intent);
            }
        });
    }
    private void redirectToRoleSpecificActivity(String role) {
        Intent intent;
        switch (role) {
            case "superadmin":
                intent = new Intent(this, SuperAdmin.class);
                startActivity(intent);
                break;
            case "administrador":
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case "supervisor":
                intent = new Intent(this, Supervisor.class);
                startActivity(intent);
                break;
            default:
                throw new IllegalStateException("Unexpected role: " + role);
        }
    }
}