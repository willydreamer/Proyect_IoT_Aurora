package com.example.aurora.Superadmin;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.aurora.Admin.CrearSupervisorActivity;
import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Random;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;


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

    private EditText editTextDNI;
    private Button buttonGuardar;

    private FirebaseFirestore db;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private ImageView imagen;

    private Uri imagenUri;
    private Button botonSubirFoto;
    private Button activobutton;
    private FirebaseAuth auth;

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
        editTextApellido = view.findViewById(R.id.editTextText2);
        editTextCorreo = view.findViewById(R.id.editTextText3);
        editTextDomicilio = view.findViewById(R.id.editTextText4);
        editTextTelefono = view.findViewById(R.id.editTextText5);
        buttonGuardar = view.findViewById(R.id.button8);
        editTextDNI = view.findViewById(R.id.editDNI);
        //obtenemos la imagen
        imagen = view.findViewById(R.id.fotoUsuario);
        //obtener boton subir
        botonSubirFoto =  view.findViewById(R.id.button12);

        activobutton = view.findViewById(R.id.button10);
        activobutton.setOnClickListener(v -> {
            actualizarActivoCrear(view);
        });

        buttonGuardar.setOnClickListener(v -> guardarAdministrador());

        //Para abrir galería:
        botonSubirFoto.setOnClickListener(v -> {
            Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //todas las imagenes
            pickPhotoIntent.setType("image/*");
            startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
        });

        return view;
    }
    private boolean correoValido(String correoStr) {
        return Patterns.EMAIL_ADDRESS.matcher(correoStr).matches();
    }


    private void guardarAdministrador() {
        String nombre = editTextNombre.getText().toString().trim();
        String apellido = editTextApellido.getText().toString().trim();
        String correo = editTextCorreo.getText().toString().trim();
        String domicilio = editTextDomicilio.getText().toString().trim();
        String telefono = editTextTelefono.getText().toString().trim();
        String act = activobutton.getText().toString().trim();
        String dni = editTextDNI.getText().toString().trim();
        String estado;
        if(act.equals("Estado: activo")){
            estado = "Activo";
        } else if (act.equals("Estado: inactivo")) {
            estado = "Inactivo";
        } else {
            estado = "Activo";
        }


        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) || TextUtils.isEmpty(correo) ||
                TextUtils.isEmpty(domicilio) || TextUtils.isEmpty(telefono)) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }


        //si no hay foto vacia
        if(imagenUri!=null) {

            if(!nombre.isEmpty() && !apellido.isEmpty() && !dni.isEmpty() && !correo.isEmpty() && !domicilio.isEmpty() && !telefono.isEmpty()) {
                if(dni.length()!=8){
                    Toast.makeText(getContext(), "DNI debe ser de 8 digitos", Toast.LENGTH_LONG).show();
                }else if(telefono.length()!=9) {
                    Toast.makeText(getContext(), "Telefono debe ser de 9 digitos", Toast.LENGTH_LONG).show();
                }else if(!correoValido(correo)) {
                    Toast.makeText(getContext(), "Correo No Valido", Toast.LENGTH_LONG).show();
                }else{

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    StorageReference imageReference = storageReference.child("fotosUsuario/" + System.currentTimeMillis() + ".jpg");

                    UploadTask uploadTask = imageReference.putFile(imagenUri);
                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            //Una vez que ya se subio la imagen en BD , obtenemos la URL de la imagen:
                            String imageUrl = uri.toString();

                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            FirebaseUser originalUser = auth.getCurrentUser(); // Guardar el usuario actual

                            auth.createUserWithEmailAndPassword(correo, "123456")
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            String userId = task.getResult().getUser().getUid();
                                            Usuario nuevoAdministrador = new Usuario(userId, nombre, apellido, dni, correo, domicilio, telefono, "administrador", estado, null, imageUrl);

                                            db.collection("usuarios")
                                                    .add(nuevoAdministrador)
                                                    .addOnSuccessListener(documentReference -> {
                                                        Log.d("msg-test2", "Administrador guardado exitosamente");
                                                        Toast.makeText(getContext(), "Administrador creado exitosamente", Toast.LENGTH_SHORT).show();

                                                        // Navegar al nuevo fragmento
                                                        SuperAdminUsersFragment fragment = SuperAdminUsersFragment.newInstance("hola", "comotas?");
                                                        ((SuperAdmin) getContext()).getSupportFragmentManager()
                                                                .beginTransaction()
                                                                .replace(R.id.container1, fragment)
                                                                .addToBackStack(null)
                                                                .commit();



                                                        // Volver a autenticar al usuario original
                                                        if (originalUser != null) {
                                                            auth.signInWithEmailAndPassword(originalUser.getEmail(), "123456")
                                                                    .addOnCompleteListener(signInTask -> {
                                                                        if (signInTask.isSuccessful()) {
                                                                            Log.d("msg-test4", "Usuario original reautenticado exitosamente");
                                                                        } else {
                                                                            Log.e("msg-test4", "Error al reautenticar al usuario original", signInTask.getException());
                                                                        }
                                                                    });
                                                        }

                                                        // Crear el log de la operación con el UID del usuario original
                                                        if (originalUser != null) {
                                                            db.collection("usuarios")
                                                                    .whereEqualTo("idUsuario", originalUser.getUid())  // Buscar documentos donde el campo 'idUsuario' sea igual a log.getIdUsuario()
                                                                    .get()
                                                                    .addOnCompleteListener(task1 -> {
                                                                        if (task1.isSuccessful() && task1.getResult() != null && !task1.getResult().isEmpty()) {
                                                                            // Obtener el primer documento que coincide con la consulta
                                                                            DocumentSnapshot document = task1.getResult().getDocuments().get(0);
                                                                            Usuario usuario = document.toObject(Usuario.class);
                                                                            if (usuario != null) {
                                                                                usuario.setIdUsuario(document.getId());
                                                                                crearLog("Se creó un Administrador", "Se creó un nuevo administrador", originalUser.getUid(), null,usuario);
                                                                            }
                                                                        } else {
                                                                            android.util.Log.d("msg-test", "Error getting document: ", task1.getException());
                                                                        }
                                                                    });

                                                        }
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.e("msg-test3", "Error al guardar el administrador", e);
                                                        Toast.makeText(getContext(), "Error al crear el administrador", Toast.LENGTH_SHORT).show();
                                                    });
                                        } else {
                                            Toast.makeText(getContext(), "Error al crear usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getContext(), "Falla en crear usuario: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });


                        }).addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Falla en subir foto: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                    });
                }
            } else{
                Toast.makeText(getContext(), "No Debe Haber Campos Vacios", Toast.LENGTH_LONG).show();
            }

            //Subimos la foto en BD

        }else{
            //si no hay foto, mostrar toast make
            Toast.makeText(getContext(), "Foto no seleccionada", Toast.LENGTH_LONG).show();
        }
    }

    private String generarIdAdmin() {
        String letrasAdmin = "ADMIN";
        Random random = new Random();
        int numeroAleatorio = random.nextInt(900) + 100; // Generar un número entre 100 y 999
        return letrasAdmin+numeroAleatorio;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imagen.setImageBitmap(imageBitmap);
                //imagenUri = getImageUri(imageBitmap);
                setImageUriWithCircularTransformation(imagenUri);
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                imagenUri = data.getData();
                imagen.setImageURI(imagenUri);
                setImageUriWithCircularTransformation(imagenUri);
            }
        }
    }

    private void setImageUriWithCircularTransformation(Uri uri) {
        Picasso.get()
                .load(uri)
                .placeholder(R.drawable.baseline_account_circle_24) // Reemplaza con tu imagen por defecto
                .transform(new CropCircleTransformation())
                .into(imagen);
    }


    public void crearLog(String actividad, String descripcion, String idUsuario, Sitio sitio, Usuario usuario){
        Date fechaActual = new Date();

        com.example.aurora.Bean.Log nuevoLog = new com.example.aurora.Bean.Log(fechaActual,  actividad,  descripcion, idUsuario, sitio, usuario);

        db.collection("logs")
                .add(nuevoLog)
                .addOnSuccessListener(documentReference -> {
                    Log.d("msg-test2", "Log guardado exitosamente");
                    Toast.makeText(getContext(), "Actividad Registrada", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test3", "Error al guardar el log", e);
                    Toast.makeText(getContext(), "Error al registrar la activada", Toast.LENGTH_SHORT).show();
                });
    }


    public void actualizarActivoCrear(View view){

        String textobutton = activobutton.getText().toString().trim();

        if(textobutton.equals("Estado: activo")){
            activobutton.setText("Estado: inactivo");
        } else if (textobutton.equals("Estado: inactivo")) {
            activobutton.setText("Estado: activo");
        }

    }


}