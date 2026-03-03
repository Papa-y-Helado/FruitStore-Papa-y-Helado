package com.utl.fruitstore.rest;

import com.google.gson.Gson;
import com.utl.fruitstore.controller.ControllerVenta;
import com.utl.fruitstore.model.Venta;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;

/*
    Clase RESTVenta
    Expone los endpoints REST para gestionar ventas.
    Se accede mediante la URL base: /api/venta
 */
@Path("venta")
public class RESTVenta {

    @Path("getAllResumen")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllResumen() {
        try {
            ControllerVenta cv = new ControllerVenta();
            List<Venta> ventas = cv.getAllResumen("");
            return Response.ok(new Gson().toJson(ventas)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity("{\"exception\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @Path("getByID")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByID(@QueryParam("idVenta") int idVenta) {
        try {
            ControllerVenta cv = new ControllerVenta();
            Venta v = cv.getByID(idVenta);
            return Response.ok(new Gson().toJson(v)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity("{\"exception\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @Path("insert")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(@FormParam("venta") String datosVenta) {
        String out;
        ControllerVenta cv = new ControllerVenta();
        Gson gson = new Gson();
        Venta v;

        try {
            v = gson.fromJson(datosVenta, Venta.class);
            if (v == null) {
                out = "{\"error\":\"No se proporcionaron datos de la venta.\"}";
            } else {
                int idGenerado = cv.insert(v);
                v.setId(idGenerado);
                out = new Gson().toJson(v);
            }
        } catch (Exception e) {
            out = String.format("{\"exception\":\"%s\"}", e.toString().replaceAll("\"", ""));
        }
        return Response.ok(out).build();
    }

    @Path("update")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@FormParam("idVenta") int idVenta) {
        try {
            ControllerVenta cv = new ControllerVenta();
            Venta v = new Venta();
            v.setId(idVenta);
            cv.update(v);
            return Response.ok("{\"status\":\"updated\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity("{\"exception\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
