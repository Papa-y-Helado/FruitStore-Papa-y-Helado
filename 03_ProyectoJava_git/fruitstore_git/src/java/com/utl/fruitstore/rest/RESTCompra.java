package com.utl.fruitstore.rest;

import com.google.gson.Gson;
import com.utl.fruitstore.controller.ControllerCompra;
import com.utl.fruitstore.model.Compra;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;

/*
    Clase RESTCompra
    Expone los endpoints REST para gestionar compras.
    Se accede mediante la URL base: /api/compra
 */
@Path("compra")
public class RESTCompra {

    // GET /api/compra/getAll
    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            ControllerCompra cc = new ControllerCompra();
            List<Compra> compras = cc.getAll();
            return Response.ok(new Gson().toJson(compras)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity("{\"exception\":\"" + e.getMessage() + "\"}").build();
        }
    }

    // POST /api/compra/insert
    @Path("insert")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(@FormParam("compra") String datosCompra) {
        String out;
        ControllerCompra cc = new ControllerCompra();
        Gson gson = new Gson();
        Compra c;

        try {
            c = gson.fromJson(datosCompra, Compra.class);
            if (c == null) {
                out = "{\"error\":\"No se proporcionaron datos de la compra.\"}";
            } else {
                int idGenerado = cc.insert(c);
                c.setIdCompra(idGenerado);
                out = new Gson().toJson(c);
            }
        } catch (Exception e) {
            out = String.format("{\"exception\":\"%s\"}", e.toString().replaceAll("\"", ""));
        }
        return Response.ok(out).build();
    }

    // POST /api/compra/delete
    @Path("delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("idCompra") int idCompra) {
        try {
            ControllerCompra cc = new ControllerCompra();
            cc.delete(idCompra);
            return Response.ok("{\"status\":\"deleted\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity("{\"exception\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
