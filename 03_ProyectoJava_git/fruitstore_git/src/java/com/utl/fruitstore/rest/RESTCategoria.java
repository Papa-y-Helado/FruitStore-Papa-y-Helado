package com.utl.fruitstore.rest;

import com.google.gson.Gson;
import com.utl.fruitstore.controller.ControllerCategoria;
import com.utl.fruitstore.model.Categoria;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;

/*
    Clase RESTCategoria
    Expone los endpoints REST para gestionar categorías.
    Se accede mediante la URL base: /api/categoria
 */
@Path("categoria")
public class RESTCategoria {

    // Endpoint GET /api/categoria/getAll
    // Devuelve todas las categorías en formato JSON
    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("filtro") @DefaultValue("") String filtro) {
        try {
            ControllerCategoria cc = new ControllerCategoria();
            List<Categoria> categorias = cc.getAll(filtro);
            return Response.ok(new Gson().toJson(categorias)).build();
        } catch (Exception e) {
            // Si ocurre un error, devolvemos un JSON con la excepción
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity("{\"exception\":\"" + e.getMessage() + "\"}").build();
        }
    }

    // Endpoint POST /api/categoria/insert
    // Inserta o actualiza una categoría
    @Path("insert")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(@FormParam("id") @DefaultValue("0") int id,
         @FormParam("nombre") String nombre) {
        try {
            ControllerCategoria cc = new ControllerCategoria();
            Categoria c = new Categoria();
            c.setId(id);
            c.setNombre(nombre);

            if (id == 0) {
                int idGenerado = cc.insert(c); // Inserta si el ID es 0
                c.setId(idGenerado);
            } else {
                cc.update(c); // Actualiza si ya existe
            }

            return Response.ok(new Gson().toJson(c)).build();
        } catch (Exception e) {
            // Si ocurre un error, devolvemos un JSON con la excepción
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity("{\"exception\":\"" + e.getMessage() + "\"}").build();
        }
    }

    // Endpoint POST /api/categoria/delete
    // Elimina una categoría por ID
    @Path("delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("idCategoria") int idCategoria) {
        try {
            ControllerCategoria cc = new ControllerCategoria();
            cc.delete(idCategoria);
            return Response.ok("{\"status\":\"deleted\"}").build();
        } catch (Exception e) {
            // Si ocurre un error, devolvemos un JSON con la excepción
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity("{\"exception\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
