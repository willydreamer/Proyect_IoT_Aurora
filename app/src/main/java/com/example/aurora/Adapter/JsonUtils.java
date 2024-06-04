package com.example.aurora.Adapter;
import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils{

    private static final Gson gson = new Gson();

    public static Map<String, Object> convertJsonObjectToMap(JsonObject jsonObject) {
        return gson.fromJson(jsonObject, HashMap.class);
    }

    public static JsonObject serializeUsuario(Usuario usuario) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idUsuario", usuario.getIdUsuario());
        jsonObject.addProperty("nombre", usuario.getNombre());
        jsonObject.addProperty("apellido", usuario.getApellido());
        jsonObject.addProperty("dni",usuario.getDni());
        jsonObject.addProperty("correo",usuario.getCorreo());
        jsonObject.addProperty("domicilio",usuario.getDomicilio());
        jsonObject.addProperty("telefono",usuario.getTelefono());
        jsonObject.addProperty("rol",usuario.getRol());
        jsonObject.addProperty("estado",usuario.getEstado());


        JsonArray sitiosArray = new JsonArray();
        ArrayList<Sitio> sitios = usuario.getSitios();
        if (sitios != null) {
            for (Sitio sitio : sitios) {
                JsonObject sitioJson = new JsonObject();
                sitioJson.addProperty("idSitio", sitio.getIdSitio());
                sitioJson.addProperty("departamento",sitio.getDepartamento());
                sitioJson.addProperty("provincia",sitio.getProvincia());
                sitioJson.addProperty("distrito",sitio.getDistrito());
                sitioJson.addProperty("tipoDeZona",sitio.getTipoDeZona());
                sitioJson.addProperty("latitud",sitio.getLatitud());
                sitioJson.addProperty("longitud",sitio.getLongitud());
                sitioJson.addProperty("operadora",sitio.getOperadora());
                sitiosArray.add(sitioJson);
            }
        }
        jsonObject.add("sitios", sitiosArray);

        return jsonObject;
    }

    public static JsonObject serializeSitio(Sitio sitio) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idSitio", sitio.getIdSitio());
        jsonObject.addProperty("departamento",sitio.getDepartamento());
        jsonObject.addProperty("provincia",sitio.getProvincia());
        jsonObject.addProperty("distrito",sitio.getDistrito());
        jsonObject.addProperty("tipoDeZona",sitio.getTipoDeZona());
        jsonObject.addProperty("latitud",sitio.getLatitud());
        jsonObject.addProperty("longitud",sitio.getLongitud());
        jsonObject.addProperty("operadora",sitio.getOperadora());

        JsonArray supervisoresArray = new JsonArray();
        List<Usuario> supervisores = sitio.getSupervisor();
        if (supervisores != null) {
            for (Usuario supervisor : supervisores) {
                JsonObject supervisorJson = new JsonObject();
                supervisorJson.addProperty("idUsuario", supervisor.getIdUsuario());
                supervisorJson.addProperty("nombre", supervisor.getNombre());
                supervisorJson.addProperty("apellido", supervisor.getApellido());
                supervisorJson.addProperty("dni", supervisor.getDni());
                supervisorJson.addProperty("correo",supervisor.getCorreo());
                supervisorJson.addProperty("domicilio",supervisor.getDomicilio());
                supervisorJson.addProperty("telefono",supervisor.getTelefono());
                supervisorJson.addProperty("rol",supervisor.getRol());
                supervisorJson.addProperty("estado",supervisor.getEstado());

                supervisoresArray.add(supervisorJson);
            }
        }
        jsonObject.add("supervisores", supervisoresArray);

        return jsonObject;
    }
}



