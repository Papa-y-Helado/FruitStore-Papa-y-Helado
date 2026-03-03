package com.utl.fruitstore.rest;

import com.google.gson.Gson;
import com.utl.fruitstore.controller.ControllerProducto;
import com.utl.fruitstore.model.Producto;
import com.utl.fruitstore.model.Categoria;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;

/**
 * Clase RESTProducto Expone los endpoints REST para gestionar productos. Se accede mediante la URL base: /api/producto
 */
@Path("producto")
public class RESTProducto {

    /**
     * Endpoint GET /api/producto/getAll Devuelve todos los productos en formato JSON.
     *
     * @param filtro parámetro opcional para filtrar resultados.
     */
    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("filtro") @DefaultValue("") String filtro) {
        try {
            ControllerProducto cp = new ControllerProducto();
            List<Producto> productos = cp.getAll(filtro);
            return Response.ok(new Gson().toJson(productos)).build();
        } catch (Exception e) {
            // Si ocurre un error, devolvemos un JSON con la excepción
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity("{\"exception\":\"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Endpoint POST /api/producto/insert Inserta un nuevo producto en la BD. Los datos se reciben como parámetros de formulario.
     *
     * @param nombre
     * @param idCategoria
     * @param precioCompra
     * @param precioVenta
     * @param existencia
     */
    @Path("insert")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(@FormParam("nombre") String nombre,
         @FormParam("idCategoria") int idCategoria,
         @FormParam("precioCompra") double precioCompra,
         @FormParam("precioVenta") double precioVenta,
         @FormParam("existencia") double existencia) {
        try {
            ControllerProducto cp = new ControllerProducto();
            Producto p = new Producto();
            p.setNombre(nombre);
            p.setPrecioCompra(precioCompra);
            p.setPrecioVenta(precioVenta);
            p.setExistencia(existencia);

            Categoria cat = new Categoria();
            cat.setId(idCategoria);
            p.setCategoria(cat);

            int idGenerado = cp.insert(p);
            p.setId(idGenerado);

            return Response.ok(new Gson().toJson(p)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity("{\"exception\":\"" + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Endpoint POST /api/producto/save Inserta o actualiza un producto según su ID. Recibe un JSON completo del producto como parámetro.
     */
    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("producto") String datosProducto) {
        String out;
        ControllerProducto cp = new ControllerProducto();
        Gson gson = new Gson();
        Producto p;

        try {
            p = gson.fromJson(datosProducto, Producto.class);
            if (p == null) {
                out = "{\"error\": \"No se proporcionaron datos del producto.\"}";
            } else {
                if (p.getId() == 0) {
                    cp.insert(p); // Si el ID es 0, se inserta
                } else {
                    cp.update(p); // Si ya tiene ID, se actualiza
                }
                out = new Gson().toJson(p);
            }
        } catch (Exception e) {
            out = String.format("{\"exception\":\"%s\"}", e.toString().replaceAll("\"", ""));
        }
        return Response.ok(out).build();
    }

    /**
     * Endpoint POST /api/producto/delete Elimina un producto por ID.
     */
    @Path("delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("idProducto") int idProducto) {
        try {
            ControllerProducto cp = new ControllerProducto();
            cp.delete(idProducto);
            return Response.ok("{\"status\":\"deleted\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity("{\"exception\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
