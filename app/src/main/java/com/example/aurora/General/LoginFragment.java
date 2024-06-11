package com.example.aurora.General;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aurora.Admin.MainActivity;
import com.example.aurora.R;
import com.example.aurora.Superadmin.SuperAdmin;
import com.example.aurora.Supervisor.Supervisor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


public class LoginFragment extends AppCompatActivity {
    EditText correo;
    EditText contrasena;
    Button btnLogin;
    private final static String TAG = "msg-test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_fragment);

        correo = findViewById(R.id.correo);
        contrasena = findViewById(R.id.contrasena);
        btnLogin = findViewById(R.id.ingresarbotonlogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // Olvidar contraseña
        TextView olvidemicontrasena = findViewById(R.id.olvidemicontrasena);
        olvidemicontrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginFragment.this, CambiarContrasenaActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        TextView netwieserTextView = findViewById(R.id.Netwieser);
//        Typeface typeface = ResourcesCompat.getFont(this, R.font.rem);
//        netwieserTextView.setTypeface(typeface);

    }

    private void login() {
        String correoLogin = correo.getText().toString().trim();
        String pswdLogin = contrasena.getText().toString().trim();
        String TAG = "msg-auth";

        if (correoLogin.isEmpty() || pswdLogin.isEmpty()) {
            Toast.makeText(LoginFragment.this, "Ingrese correo y contraseña.", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(correoLogin, pswdLogin)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                // Se obtiene el rol del usuario
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("usuarios")
                                        .whereEqualTo("correo", correoLogin)
                                        .get()
                                        .addOnCompleteListener(userTask -> {
                                            if (userTask.isSuccessful()) {
                                                if (!userTask.getResult().isEmpty()) {
                                                    for (QueryDocumentSnapshot document : userTask.getResult()) {
                                                        String role = document.getString("rol");
                                                        Log.d("rol-autenticado-1", role);
                                                        redirectToRoleSpecificActivity(role);
                                                    }
                                                } else {
                                                    Log.d(TAG, "No matching documents found.");
                                                    Toast.makeText(LoginFragment.this, "No se encontró el perfil del usuario.", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", userTask.getException());
                                                Toast.makeText(LoginFragment.this, "Error obteniendo el perfil del usuario.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else if (user != null) {
                                user.sendEmailVerification().addOnCompleteListener(task2 -> {
                                    Toast.makeText(LoginFragment.this, "Se le ha enviado un correo para validar su cuenta", Toast.LENGTH_SHORT).show();
                                });
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginFragment.this, "Autenticación fallida.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void redirectToRoleSpecificActivity(String role) {
        if (role == null) {
            // Manejar el caso cuando el rol es nulo
            Log.e("TAG-role", "Role is null");
            return;
        }

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
    public void goToMainActivity() {
        Intent intent = new Intent(LoginFragment.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}