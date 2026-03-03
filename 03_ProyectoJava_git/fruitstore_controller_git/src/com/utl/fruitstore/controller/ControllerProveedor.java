package com.utl.fruitstore.controller;

import com.utl.fruitstore.db.ConexionMySQL;
import com.utl.fruitstore.model.Proveedor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
    Clase ControllerProveedor
    Maneja la lógica de acceso a datos para la tabla proveedor.
 */
public class ControllerProveedor {

    // Obtener todos los proveedores activos
    public List<Proveedor> getAll(String filtro) throws Exception {
        String sql = "SELECT * FROM proveedor WHERE estatus=1 ORDER BY nombre ASC";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        List<Proveedor> proveedores = new ArrayList<>();

        while (rs.next()) {
            proveedores.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        conn.close();

        return proveedores;
    }

    // Insertar un nuevo proveedor
    public int insert(Proveedor p) throws Exception {
        String sql = "INSERT INTO proveedor(nombre, email, telefono, ciudad, estatus) VALUES(?,?,?,?,?)";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        pstmt.setString(1, p.getNombre());
        pstmt.setString(2, p.getEmail());
        pstmt.setString(3, p.getTelefono());
        pstmt.setString(4, p.getCiudad());
        pstmt.setInt(5, p.getEstatus());

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

    // Actualizar un proveedor existente
    public void update(Proveedor p) throws Exception {
        String sql = "UPDATE proveedor SET nombre=?, email=?, telefono=?, ciudad=?, estatus=? WHERE idProveedor=?";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, p.getNombre());
        pstmt.setString(2, p.getEmail());
        pstmt.setString(3, p.getTelefono());
        pstmt.setString(4, p.getCiudad());
        pstmt.setInt(5, p.getEstatus());
        pstmt.setInt(6, p.getId());

        pstmt.executeUpdate();

        pstmt.close();
        conn.close();
    }

    // Eliminar un proveedor (borrado lógico)
    public void delete(int idProveedor) throws Exception {
        String sql = "UPDATE proveedor SET estatus=0 WHERE idProveedor=?";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, idProveedor);
        pstmt.executeUpdate();

        pstmt.close();
        conn.close();
    }

    // Método auxiliar para llenar objeto Proveedor desde ResultSet
    private Proveedor fill(ResultSet rs) throws Exception {
        Proveedor p = new Proveedor();
        p.setId(rs.getInt("idProveedor"));
        p.setNombre(rs.getString("nombre"));
        p.setEmail(rs.getString("email"));
        p.setTelefono(rs.getString("telefono"));
        p.setCiudad(rs.getString("ciudad"));
        p.setEstatus(rs.getInt("estatus"));
        return p;
    }
}
