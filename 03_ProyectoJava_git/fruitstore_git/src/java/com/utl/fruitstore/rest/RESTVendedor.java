package com.utl.fruitstore.rest;

import com.google.gson.Gson;
import com.utl.fruitstore.controller.ControllerVendedor;
import com.utl.fruitstore.model.Vendedor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;

/*
    Clase RESTVendedor
    Expone los endpoints REST para gestionar vendedores.
    Se accede mediante la URL base: /api/vendedor
 */
@Path("vendedor")
public class RESTVendedor {

    // Endpoint GET /api/vendedor/getAll
    // Devuelve todos los vendedores en formato JSON
    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("filtro") @DefaultValue("") String filtro) {
        try {
            ControllerVendedor cv = new ControllerVendedor();
            List<Vendedor> vendedores = cv.getAll(filtro);
            return Response.ok(new Gson().toJson(vendedores)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity("{\"exception\":\"" + e.getMessage() + "\"}").build();
        }
    }

    // Endpoint POST /api/vendedor/insert
    // Inserta o actualiza un vendedor
    @Path("insert")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(@FormParam("id") @DefaultValue("0") int id,
         @FormParam("nombre") String nombre,
         @FormParam("email") String email,
         @FormParam("telefono") String telefono,
         @FormParam("fechaNacimiento") String fechaNacimiento,
         @FormParam("fechaAlta") String fechaAlta,
         @FormParam("genero") String genero,
         @FormParam("calle") String calle,
         @FormParam("numExt") String numExt,
         @FormParam("numInt") String numInt,
         @FormParam("colonia") String colonia,
         @FormParam("cp") String cp,
         @FormParam("ciudad") String ciudad,
         @FormParam("estado") String estado,
         @FormParam("pais") String pais,
         @FormParam("estatus") @DefaultValue("1") int estatus) {
        try {
            ControllerVendedor cv = new ControllerVendedor();
            Vendedor v = new Vendedor();

            // Asignamos valores al objeto vendedor
            v.setId(id);
            v.setNombre(nombre);
            v.setEmail(email);
            v.setTelefono(telefono);
            v.setFechaNacimiento(fechaNacimiento); // se convertirá a java.sql.Date en el controller
            v.setFechaAlta(fechaAlta);             // igual que arriba
            v.setGenero(genero);
            v.setCalle(calle);
            v.setNumExt(numExt);
            v.setNumInt(numInt);
            v.setColonia(colonia);
            v.setCp(cp);
            v.setCiudad(ciudad);
            v.setEstado(estado);
            v.setPais(pais);
            v.setEstatus(estatus);

            if (id == 0) {
                int idGenerado = cv.insert(v); // Inserta si el ID es 0
                v.setId(idGenerado);
            } else {
                cv.update(v); // Actualiza si ya existe
            }

            return Response.ok(new Gson().toJson(v)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity("{\"exception\":\"" + e.getMessage() + "\"}").build();
        }
    }

    // Endpoint POST /api/vendedor/save
    // Inserta o actualiza un vendedor según su ID, recibiendo un JSON completo
    @Path("save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("vendedor") String datosVendedor) {
        String out;
        ControllerVendedor cv = new ControllerVendedor();
        Gson gson = new Gson();
        Vendedor v;

        try {
            v = gson.fromJson(datosVendedor, Vendedor.class);
            if (v == null) {
                out = "{\"error\": \"No se proporcionaron datos del vendedor.\"}";
            } else {
                if (v.getId() == 0) {
                    cv.insert(v); // Inserta si el ID es 0
                } else {
                    cv.update(v); // Actualiza si ya existe
                }
                out = new Gson().toJson(v);
            }
        } catch (Exception e) {
            out = String.format("{\"exception\":\"%s\"}", e.toString().replaceAll("\"", ""));
        }
        return Response.ok(out).build();
    }

    // Endpoint POST /api/vendedor/delete
    // Elimina un vendedor por ID
    @Path("delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("idVendedor") int idVendedor) {
        try {
            ControllerVendedor cv = new ControllerVendedor();
            cv.delete(idVendedor);
            return Response.ok("{\"status\":\"deleted\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                 .entity("{\"exception\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
