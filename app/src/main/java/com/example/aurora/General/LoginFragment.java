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
import com.example.aurora.Bean.Usuario;
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
import androidx.activity.result.ActivityResultLauncher;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Arrays;


public class LoginFragment extends AppCompatActivity {
    private EditText correo;
    private EditText contrasena;
    private Button btnLogin;

    private Button btnRegistro;
    private final static String TAG = "msg-test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_fragment);

        correo = findViewById(R.id.correo);
        contrasena = findViewById(R.id.contrasena);
        btnLogin = findViewById(R.id.ingresarbotonlogin);
        btnRegistro = findViewById(R.id.registrarseboton);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registro();
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
                                                        Usuario usuarioLog = document.toObject(Usuario.class);
                                                        if(usuarioLog.getEstado().equals("activo") || usuarioLog.getEstado().equals("Activo")) {
                                                            String role = document.getString("rol");
                                                            Log.d("rol-autenticado-1", role);
                                                            redirectToRoleSpecificActivity(role);
                                                        }else{
                                                            Toast.makeText(LoginFragment.this, "Cuenta Inactiva", Toast.LENGTH_LONG).show();
                                                            FirebaseAuth.getInstance().signOut();
                                                        }
                                                    }
                                                } else {
                                                    Log.d(TAG, "No matching documents found.");
                                                    Toast.makeText(LoginFragment.this, "No se encontró el perfil del usuario.", Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", userTask.getException());
                                                Toast.makeText(LoginFragment.this, "Error obteniendo el perfil del usuario.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            } else if (user != null) {
                                user.sendEmailVerification().addOnCompleteListener(task2 -> {
                                    Toast.makeText(LoginFragment.this, "Se le ha enviado un correo para validar su cuenta", Toast.LENGTH_LONG).show();
                                });
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginFragment.this, "Autenticación fallida.", Toast.LENGTH_LONG).show();
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

    private void registro(){

        AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout.Builder(R.layout.registro)
                .setEmailButtonId(R.id.btnMail)
                .setGoogleButtonId(R.id.btnGoogle)
                .build();

        //no hay sesión
        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.Base_Theme_Aurora)
                .setIsSmartLockEnabled(false)
                .setAuthMethodPickerLayout(authMethodPickerLayout)
                .setLogo(R.drawable.netwise_1000)
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()
                ))
                .build();

        signInLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        Log.d(TAG, "Firebase uid: " + user.getUid());
                        Log.d(TAG, "Display name: " + user.getDisplayName());
                        Log.d(TAG, "Email: " + user.getEmail());


                        user.reload().addOnCompleteListener(task -> {
                            if (user.isEmailVerified()) {
                                Toast.makeText(this, "Usuario Verificado", Toast.LENGTH_LONG).show();
                            } else {
                                user.sendEmailVerification().addOnCompleteListener(task2 -> {
                                    Toast.makeText(this, "Se le ha enviado un correo para validar su cuenta", Toast.LENGTH_LONG).show();
                                    //
                                });
                            }
                        });
                    } else {
                        Log.d(TAG, "user == null");
                    }
                } else {
                    Log.d(TAG, "Canceló el Log-in");
                }
                btnLogin.setEnabled(true);
            }
    );


    public void goToMainActivity() {
        Intent intent = new Intent(LoginFragment.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}