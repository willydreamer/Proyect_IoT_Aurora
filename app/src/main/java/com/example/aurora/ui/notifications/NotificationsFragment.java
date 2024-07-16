package com.example.aurora.ui.notifications;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.General.InicioFragment;
import com.example.aurora.General.LoginFragment;
import com.example.aurora.R;
import com.example.aurora.Superadmin.SuperAdmin;
import com.example.aurora.Superadmin.SuperAdminUsersFragment;
import com.example.aurora.databinding.FragmentNotificationsBinding;
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

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private Uri imagenUri;

    private EditText nombreEditText, apellidoEditText, correoEditText, domicilioEditText, telefonoEditText, dniEditText;
    private ImageView foto;

    private Usuario usuario;



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
        foto = binding.fotoUsuario;
        dniEditText = binding.editDNI;

        /*final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        FirebaseAuth.getInstance();

        Button buttonCerrarSesion = binding.close;
        Button buttonGuardar = binding.button8;
        Button botonSubirFoto = binding.button12;
        buttonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar sesión
                FirebaseAuth.getInstance().signOut();

                // Redirigir a LoginFragment (que es una actividad)
                Intent intent = new Intent(getActivity(), InicioFragment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        buttonGuardar.setOnClickListener(v -> actualizarSuperAdmin());

        //Para abrir galería:
        botonSubirFoto.setOnClickListener(v -> {
            Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //todas las imagenes
            pickPhotoIntent.setType("image/*");
            startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
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
            String idUsuario = "SUPER111"; // Valor que deseas bu|scar

            db.collection("usuarios")
                    .whereEqualTo("idUsuario", user1)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            usuario = documentSnapshot.toObject(Usuario.class);
                            if (usuario != null) {
                                nombreEditText.setText(usuario.getNombre());
                                apellidoEditText.setText(usuario.getApellido());
                                correoEditText.setText(usuario.getCorreo());
                                domicilioEditText.setText(usuario.getDomicilio());
                                telefonoEditText.setText(usuario.getTelefono());
                                dniEditText.setText(usuario.getDni());
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

    private void actualizarSuperAdmin() {
        String nombre = nombreEditText.getText().toString().trim();
        String apellido = apellidoEditText.getText().toString().trim();
        String correo = correoEditText.getText().toString().trim();
        String domicilio = domicilioEditText.getText().toString().trim();
        String telefono = telefonoEditText.getText().toString().trim();
        String dni = dniEditText.getText().toString().trim();
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) || TextUtils.isEmpty(correo) ||
                TextUtils.isEmpty(domicilio) || TextUtils.isEmpty(telefono)) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        //si no hay foto vacia
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
                                            .update("apellido", apellido, "correo",correo,"dni",dni,"domicilio",domicilio,"fotoURL",imageUrl,"nombre",nombre,"telefono",telefono)  // Actualiza campos específicos
                                            .addOnSuccessListener(aVoid -> {
                                                Log.d("msg-test2", "Super Administrador actualizado exitosamente");
                                                Toast.makeText(getContext(), "Super Administrador actualizado exitosamente", Toast.LENGTH_SHORT).show();

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
                                                    crearLog("Usuario Super Administrador Actualizado", "Se ha editado el usuario Super Administrador " + usuario.getNombre()+ " "+usuario.getApellido(), user1, null);
                                                }
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.e("msg-test3", "Error al actualizar el Super Administrador", e);
                                                Toast.makeText(getContext(), "Error al actualizar el Super Administrador", Toast.LENGTH_SHORT).show();
                                            });

                                } else {
                                    Log.e("msg-test4", "No se encontró un Super Administrador con el ID especificado");
                                    Toast.makeText(getContext(), "No se encontró un Super Administrador con el ID especificado", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e("msg-test3", "Error al buscar el Super Administrador", e);
                                Toast.makeText(getContext(), "Error al buscar el Super Administrador", Toast.LENGTH_SHORT).show();
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
                                        .update("apellido", apellido, "correo",correo,"dni",dni,"domicilio",domicilio                                                                                                ,"nombre",nombre,"telefono",telefono)  // Actualiza campos específicos
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("msg-test2", "Super Administrador actualizado exitosamente");
                                            Toast.makeText(getContext(), "Super Administrador actualizado exitosamente", Toast.LENGTH_SHORT).show();
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
                                                crearLog("Usuario Super Administrador Actualizado", "Se ha editado el usuario superadmin " + usuario.getNombre()+ " "+usuario.getApellido(), user1, null);
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("msg-test3", "Error al actualizar el Super Administrador", e);
                                            Toast.makeText(getContext(), "Error al actualizar el Super Administrador", Toast.LENGTH_SHORT).show();
                                        });

                            } else {
                                Log.e("msg-test4", "No se encontró un Super Administrador con el ID especificado");
                                Toast.makeText(getContext(), "No se encontró un Super Administrador con el ID especificado", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e("msg-test3", "Error al buscar el Super Administrador", e);
                            Toast.makeText(getContext(), "Error al buscar el Super Administrador", Toast.LENGTH_SHORT).show();
                        });

            }else {
                Toast.makeText(getContext(), "Foto no seleccionada", Toast.LENGTH_LONG).show();
            }

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                foto.setImageBitmap(imageBitmap);
                //imagenUri = getImageUri(imageBitmap);
                setImageUriWithCircularTransformation(imagenUri);
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                imagenUri = data.getData();
                foto.setImageURI(imagenUri);
                setImageUriWithCircularTransformation(imagenUri);
            }
        }
    }

    private void setImageUriWithCircularTransformation(Uri uri) {
        Picasso.get()
                .load(uri)
                .placeholder(R.drawable.baseline_account_circle_24) // Reemplaza con tu imagen por defecto
                .transform(new CropCircleTransformation())
                .into(foto);
    }

    public void crearLog(String actividad, String descripcion, String idUsuario, Sitio sitio){
        Date fechaActual = new Date();

        com.example.aurora.Bean.Log nuevoLog = new com.example.aurora.Bean.Log(fechaActual,  actividad,  descripcion, idUsuario, sitio);

        db.collection("logs")
                .add(nuevoLog)
                .addOnSuccessListener(documentReference -> {
                    Log.d("msg-test2", "Log guardado exitosamente");
                    Toast.makeText(getContext(), "Actividad Registrada", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test3", "Error al guardar el log", e);
                    Toast.makeText(getContext(), "Error al registrar la activadad", Toast.LENGTH_SHORT).show();
                });
    }
}