package com.utl.fruitstore.rest;

import com.google.gson.Gson;
import com.utl.fruitstore.controller.ControllerProveedor;
import com.utl.fruitstore.model.Proveedor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;

/*
    Clase RESTProveedor
    Expone los endpoints REST para gestionar proveedores.
    Se accede mediante la URL base: /api/proveedor
 */
@Path("proveedor")
public class RESTProveedor {

    // Endpoint GET /api/proveedor/getAll
    // Devuelve todos los proveedores en formato JSON
    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("filtro") @DefaultValue("") String filtro) {
        try {
            ControllerProveedor cp = new ControllerProveedor();
            List<Proveedor> proveedores = cp.getAll(filtro);
            return Response.ok(new Gson().toJson(proveedores)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity("{\"exception\":\"" + e.getMessage() + "\"}").build();
        }
    }

    // Endpoint POST /api/proveedor/insert
    // Inserta o actualiza un proveedor
    @Path("insert")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(@FormParam("id") @DefaultValue("0") int id,
         @FormParam("nombre") String nombre,
         @FormParam("email") String email,
         @FormParam("telefono") String telefono,
         @FormParam("ciudad") String ciudad) {
        try {
            ControllerProveedor cp = new ControllerProveedor();
            Proveedor p = new Proveedor();
            p.setId(id);
            p.setNombre(nombre);
            p.setEmail(email);
            p.setTelefono(telefono);
            p.setCiudad(ciudad);

            if (id == 0) {
                int idGenerado = cp.insert(p); // Inserta si el ID es 0
                p.setId(idGenerado);
            } else {
                cp.update(p); // Actualiza si ya existe
            }

            return Response.ok(new Gson().toJson(p)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity("{\"exception\":\"" + e.getMessage() + "\"}").build();
        }
    }

    // Endpoint POST /api/proveedor/save
    // Inserta o actualiza un proveedor según su ID, recibiendo un JSON completo
    @Path("save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("proveedor") String datosProveedor) {
        String out;
        ControllerProveedor cp = new ControllerProveedor();
        Gson gson = new Gson();
        Proveedor p;

        try {
            p = gson.fromJson(datosProveedor, Proveedor.class);
            if (p == null) {
                out = "{\"error\": \"No se proporcionaron datos del proveedor.\"}";
            } else {
                if (p.getId() == 0) {
                    cp.insert(p); // Inserta si el ID es 0
                } else {
                    cp.update(p); // Actualiza si ya existe
                }
                out = new Gson().toJson(p);
            }
        } catch (Exception e) {
            out = String.format("{\"exception\":\"%s\"}", e.toString().replaceAll("\"", ""));
        }
        return Response.ok(out).build();
    }

    // Endpoint POST /api/proveedor/delete
    // Elimina un proveedor por ID
    @Path("delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("idProveedor") int idProveedor) {
        try {
            ControllerProveedor cp = new ControllerProveedor();
            cp.delete(idProveedor);
            return Response.ok("{\"status\":\"deleted\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity("{\"exception\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
