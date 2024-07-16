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

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

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
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_super_admin_editar_administrador, container, false);

        editTextNombre = view.findViewById(R.id.editTextText);
        editTextApellido = view.findViewById(R.id.editTextText2);
        editTextCorreo = view.findViewById(R.id.editTextText3);
        editTextDomicilio = view.findViewById(R.id.editTextText4);
        editTextTelefono = view.findViewById(R.id.editTextText5);
        buttonGuardar = view.findViewById(R.id.button8);
        editTextDNI = view.findViewById(R.id.editDNI);

        imagen = view.findViewById(R.id.fotoUsuario);
        //obtener boton subir
        botonSubirFoto =  view.findViewById(R.id.button12);



        Log.d("SuperAdmin", "Nombre: " + usuario.getNombre());
        Log.d("SuperAdmin", "Apellido: " + usuario.getApellido());

        if (usuario != null) {

            editTextNombre.setText(usuario.getNombre());
            editTextApellido.setText(usuario.getApellido());
            editTextCorreo.setText(usuario.getCorreo());
            editTextDomicilio.setText(usuario.getDomicilio());
            editTextTelefono.setText(usuario.getTelefono());
            editTextDNI.setText(usuario.getDni());

            if (usuario.getFotoURL() != null && !usuario.getFotoURL().isEmpty()) {
                Picasso.get()
                        .load(usuario.getFotoURL())
                        .placeholder(R.drawable.perfil_icono) // Reemplaza con tu imagen por defecto
                        .transform(new CropCircleTransformation())
                        .into(imagen);
            } else {
                Picasso.get()
                        .load(R.drawable.perfil_icono) // Imagen por defecto
                        .transform(new CropCircleTransformation())
                        .into(imagen);
            }


            activobutton = view.findViewById(R.id.button10);
            activobutton.setOnClickListener(v -> {
                actualizarActivoEditar(view);
            });


            buttonGuardar.setOnClickListener(v -> actualizarAdministrador());

            //Para abrir galería:
            botonSubirFoto.setOnClickListener(v -> {
                Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //todas las imagenes
                pickPhotoIntent.setType("image/*");
                startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
            });
        }

        return view;
    }



    private void actualizarAdministrador() {
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


        /*if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) || TextUtils.isEmpty(correo) ||
                TextUtils.isEmpty(domicilio) || TextUtils.isEmpty(telefono)) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }*/

        if(!nombre.isEmpty() && !apellido.isEmpty() && !dni.isEmpty() && !correo.isEmpty() && !domicilio.isEmpty() && !telefono.isEmpty()) {
            if(dni.length()!=8){
                Toast.makeText(getContext(), "DNI debe ser de 8 digitos", Toast.LENGTH_LONG).show();
            }else if(telefono.length()!=9) {
                Toast.makeText(getContext(), "Telefono debe ser de 9 digitos", Toast.LENGTH_LONG).show();
            }else if(!correoValido(correo)) {
                Toast.makeText(getContext(), "Correo No Valido", Toast.LENGTH_LONG).show();
            }else {
                if((imagenUri!=null)) {

                    //Subimos la foto en BD
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    StorageReference imageReference = storageReference.child("fotosUsuario/" + System.currentTimeMillis() + ".jpg");

                    UploadTask uploadTask = imageReference.putFile(imagenUri);
                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            //Una vez que ya se subio la imagen en BD , obtenemos la URL de la imagen:
                            String imageUrl = uri.toString();

                            db.collection("usuarios")
                                    .whereEqualTo("idUsuario", usuario.getIdUsuario())  // Buscar documentos donde el campo 'id' sea igual a usuario.getId()
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            // Obtener el primer documento que coincide con la consulta
                                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                            String documentId = documentSnapshot.getId();

                                            // Actualizar el documento
                                            db.collection("usuarios")
                                                    .document(documentId)  // Accede al documento con el ID específico
                                                    .update("apellido", apellido, "correo",correo,"dni",dni,"domicilio",domicilio,"estado",estado,"fotoURL",imageUrl,"nombre",nombre,"telefono",telefono)  // Actualiza campos específicos
                                                    .addOnSuccessListener(aVoid -> {
                                                        Log.d("msg-test2", "Administrador actualizado exitosamente");
                                                        Toast.makeText(getContext(), "Administrador actualizado exitosamente", Toast.LENGTH_SHORT).show();

                                                        SuperAdminUsersFragment fragment = SuperAdminUsersFragment.newInstance("hola","comotas?");

                                                        // Navegar al nuevo fragmento
                                                        ((SuperAdmin) getContext()).getSupportFragmentManager()
                                                                .beginTransaction()
                                                                .replace(R.id.container1, fragment)
                                                                .addToBackStack(null)
                                                                .commit();
                                                        FirebaseAuth auth = FirebaseAuth.getInstance();
                                                        FirebaseUser user = auth.getCurrentUser();
                                                        if (user != null) {
                                                            String user1 = user.getUid();
                                                            Log.d("USUARIO", user1);
                                                            db.collection("usuarios")
                                                                    .whereEqualTo("idUsuario", user1)  // Buscar documentos donde el campo 'idUsuario' sea igual a log.getIdUsuario()
                                                                    .get()
                                                                    .addOnCompleteListener(task1 -> {
                                                                        if (task1.isSuccessful() && task1.getResult() != null && !task1.getResult().isEmpty()) {
                                                                            // Obtener el primer documento que coincide con la consulta
                                                                            DocumentSnapshot document = task1.getResult().getDocuments().get(0);
                                                                            Usuario usuario = document.toObject(Usuario.class);
                                                                            if (usuario != null) {
                                                                                usuario.setIdUsuario(document.getId());
                                                                                crearLog("Usuario Administrador Actualizado", "Se ha editado el usuario administrador " + nombre+ " "+apellido, user1, null,usuario);
                                                                            }
                                                                        } else {
                                                                            android.util.Log.d("msg-test", "Error getting document: ", task1.getException());
                                                                        }
                                                                    });
                                                        }
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.e("msg-test3", "Error al actualizar el administrador", e);
                                                        Toast.makeText(getContext(), "Error al actualizar el administrador", Toast.LENGTH_SHORT).show();
                                                    });

                                        } else {
                                            Log.e("msg-test4", "No se encontró un administrador con el ID especificado");
                                            Toast.makeText(getContext(), "No se encontró un administrador con el ID especificado", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("msg-test3", "Error al buscar el administrador", e);
                                        Toast.makeText(getContext(), "Error al buscar el administrador", Toast.LENGTH_SHORT).show();
                                    });

                        }).addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Falla en subir foto: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                    });
                }else{
                    //si no hay foto, mostrar toast make
                    if((usuario.getFotoURL() != null && !usuario.getFotoURL().isEmpty())){
                        db.collection("usuarios")
                                .whereEqualTo("idUsuario", usuario.getIdUsuario())  // Buscar documentos donde el campo 'id' sea igual a usuario.getId()
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        // Obtener el primer documento que coincide con la consulta
                                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                        String documentId = documentSnapshot.getId();

                                        // Actualizar el documento
                                        db.collection("usuarios")
                                                .document(documentId)  // Accede al documento con el ID específico
                                                .update("apellido", apellido, "correo",correo,"dni",dni,"domicilio",domicilio,"estado",estado                                                                                                   ,"nombre",nombre,"telefono",telefono)  // Actualiza campos específicos
                                                .addOnSuccessListener(aVoid -> {
                                                    Log.d("msg-test2", "Administrador actualizado exitosamente");
                                                    Toast.makeText(getContext(), "Administrador actualizado exitosamente", Toast.LENGTH_SHORT).show();
                                                    SuperAdminUsersFragment fragment = SuperAdminUsersFragment.newInstance("hola","comotas?");

                                                    // Navegar al nuevo fragmento
                                                    ((SuperAdmin) getContext()).getSupportFragmentManager()
                                                            .beginTransaction()
                                                            .replace(R.id.container1, fragment)
                                                            .addToBackStack(null)
                                                            .commit();
                                                    FirebaseAuth auth = FirebaseAuth.getInstance();
                                                    FirebaseUser user = auth.getCurrentUser();
                                                    if (user != null) {
                                                        String user1 = user.getUid();
                                                        Log.d("USUARIO", user1);
                                                        db.collection("usuarios")
                                                                .whereEqualTo("idUsuario", user1)  // Buscar documentos donde el campo 'idUsuario' sea igual a log.getIdUsuario()
                                                                .get()
                                                                .addOnCompleteListener(task1 -> {
                                                                    if (task1.isSuccessful() && task1.getResult() != null && !task1.getResult().isEmpty()) {
                                                                        // Obtener el primer documento que coincide con la consulta
                                                                        DocumentSnapshot document = task1.getResult().getDocuments().get(0);
                                                                        Usuario usuario = document.toObject(Usuario.class);
                                                                        if (usuario != null) {
                                                                            usuario.setIdUsuario(document.getId());
                                                                            crearLog("Usuario Administrador Actualizado", "Se ha editado el usuario administrador " + nombre+ " "+apellido, user1, null,usuario);
                                                                        }
                                                                    } else {
                                                                        android.util.Log.d("msg-test", "Error getting document: ", task1.getException());
                                                                    }
                                                                });
                                                    }
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.e("msg-test3", "Error al actualizar el administrador", e);
                                                    Toast.makeText(getContext(), "Error al actualizar el administrador", Toast.LENGTH_SHORT).show();
                                                });

                                    } else {
                                        Log.e("msg-test4", "No se encontró un administrador con el ID especificado");
                                        Toast.makeText(getContext(), "No se encontró un administrador con el ID especificado", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("msg-test3", "Error al buscar el administrador", e);
                                    Toast.makeText(getContext(), "Error al buscar el administrador", Toast.LENGTH_SHORT).show();
                                });

                    }else {
                        Toast.makeText(getContext(), "Foto no seleccionada", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } else{
            Toast.makeText(getContext(), "No Debe Haber Campos Vacios", Toast.LENGTH_LONG).show();
        }
        //si no hay foto vacia

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


    private boolean correoValido(String correoStr) {
        return Patterns.EMAIL_ADDRESS.matcher(correoStr).matches();
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


    public void actualizarActivoEditar(View view){

        String textobutton = activobutton.getText().toString().trim();

        if(textobutton.equals("Estado: activo")){
            activobutton.setText("Estado: inactivo");
        } else if (textobutton.equals("Estado: inactivo")) {
            activobutton.setText("Estado: activo");
        }

    }
}