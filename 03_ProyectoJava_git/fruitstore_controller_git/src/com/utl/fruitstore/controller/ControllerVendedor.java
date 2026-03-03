package com.utl.fruitstore.controller;

import com.utl.fruitstore.db.ConexionMySQL;
import java.util.List;
import com.utl.fruitstore.model.Vendedor;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

/**
 *
 * @author LiveGrios
 */
public class ControllerVendedor {

    public List<Vendedor> getAll(String filtro) throws Exception {
        String sql = "SELECT * FROM v_vendedor ORDER BY nombre ASC";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        List<Vendedor> vendedores = new ArrayList<>();

        while (rs.next()) {
            vendedores.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        conn.close();

        return vendedores;
    }

    private Vendedor fill(ResultSet rs) throws Exception {
        Vendedor v = new Vendedor();

        v.setCalle(rs.getString("calle"));
        v.setCiudad(rs.getString("ciudad"));
        v.setColonia(rs.getString("colonia"));
        v.setCp(rs.getString("cp"));
        v.setEmail(rs.getString("email"));
        v.setEstado(rs.getString("estado"));
        v.setEstatus(rs.getInt("estatus"));
        v.setFechaAlta(rs.getString("fechaAlta"));
        v.setFechaNacimiento(rs.getString("fechaNac"));
        v.setGenero(rs.getString("genero"));
        v.setId(rs.getInt("idVendedor"));
        v.setNombre(rs.getString("nombre"));
        v.setNumExt(rs.getString("numExt"));
        v.setNumInt(rs.getString("numInt"));
        v.setPais(rs.getString("pais"));
        v.setTelefono(rs.getString("telefono"));
        return v;
    }

    // Inserta un nuevo vendedor en la BD
    public int insert(Vendedor v) throws Exception {
        String sql = "INSERT INTO vendedor(nombre, fechaNac, genero, calle, numExt, numInt, colonia, cp, ciudad, estado, pais, telefono, fechaAlta, email, estatus) "
             + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        pstmt.setString(1, v.getNombre());
        pstmt.setDate(2, java.sql.Date.valueOf(v.getFechaNacimiento())); // convierte String a Date
        pstmt.setString(3, v.getGenero());
        pstmt.setString(4, v.getCalle());
        pstmt.setString(5, v.getNumExt());
        pstmt.setString(6, v.getNumInt());
        pstmt.setString(7, v.getColonia());
        pstmt.setString(8, v.getCp());
        pstmt.setString(9, v.getCiudad());
        pstmt.setString(10, v.getEstado());
        pstmt.setString(11, v.getPais());
        pstmt.setString(12, v.getTelefono());
        pstmt.setDate(13, java.sql.Date.valueOf(v.getFechaAlta())); // también como DATE
        pstmt.setString(14, v.getEmail());
        pstmt.setInt(15, v.getEstatus());

        pstmt.executeUpdate();

        ResultSet rs = pstmt.getGeneratedKeys();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt(1);
        }

        rs.close();
        pstmt.close();
        conn.close();

        return id;
    }

// Actualiza un vendedor existente
    public void update(Vendedor v) throws Exception {
        String sql = "UPDATE vendedor SET nombre=?, fechaNac=?, genero=?, calle=?, numExt=?, numInt=?, colonia=?, cp=?, ciudad=?, estado=?, pais=?, telefono=?, fechaAlta=?, email=?, estatus=? "
             + "WHERE idVendedor=?";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, v.getNombre());
        pstmt.setDate(2, java.sql.Date.valueOf(v.getFechaNacimiento())); // fechaNac como DATE
        pstmt.setString(3, v.getGenero());
        pstmt.setString(4, v.getCalle());
        pstmt.setString(5, v.getNumExt());
        pstmt.setString(6, v.getNumInt());
        pstmt.setString(7, v.getColonia());
        pstmt.setString(8, v.getCp());
        pstmt.setString(9, v.getCiudad());
        pstmt.setString(10, v.getEstado());
        pstmt.setString(11, v.getPais());
        pstmt.setString(12, v.getTelefono());
        pstmt.setDate(13, java.sql.Date.valueOf(v.getFechaAlta())); // fechaAlta como DATE
        pstmt.setString(14, v.getEmail());
        pstmt.setInt(15, v.getEstatus());
        pstmt.setInt(16, v.getId());

        pstmt.executeUpdate();

        pstmt.close();
        conn.close();
    }

    public void delete(int idVendedor) throws Exception {
        String sql = "UPDATE vendedor SET estatus=0 WHERE idVendedor=?";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, idVendedor);
        pstmt.executeUpdate();

        pstmt.close();
        conn.close();
    }

    /*
    public void delete(int idVendedor) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
// */
}
