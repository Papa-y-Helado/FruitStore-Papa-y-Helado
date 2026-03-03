package com.utl.fruitstore.controller;

import com.utl.fruitstore.db.ConexionMySQL;
import com.utl.fruitstore.model.Compra;
import com.utl.fruitstore.model.DetalleCompra;
import com.utl.fruitstore.model.Proveedor;
import com.utl.fruitstore.model.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
    Clase ControllerCompra
    Maneja la lógica de acceso a datos para la tabla compra y detalle_compra.
 */
public class ControllerCompra {

    // Obtener todas las compras
    public List<Compra> getAll() throws Exception {
        String sql = "SELECT c.idCompra, c.fechaCompra, p.idProveedor, p.nombre AS nombreProveedor "
             + "FROM compra c INNER JOIN proveedor p ON c.idProveedor = p.idProveedor "
             + "ORDER BY c.fechaCompra DESC";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        List<Compra> compras = new ArrayList<>();

        while (rs.next()) {
            compras.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        conn.close();

        return compras;
    }

    // Insertar una nueva compra con sus detalles
    public int insert(Compra c) throws Exception {
        String sqlCompra = "INSERT INTO compra(idProveedor, fechaCompra) VALUES(?,?)";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        conn.setAutoCommit(false);

        PreparedStatement pstmtCompra = conn.prepareStatement(sqlCompra, PreparedStatement.RETURN_GENERATED_KEYS);
        pstmtCompra.setInt(1, c.getProveedor().getId());
        pstmtCompra.setDate(2, java.sql.Date.valueOf(c.getFechaCompra()));

        pstmtCompra.executeUpdate();

        ResultSet rs = pstmtCompra.getGeneratedKeys();
        int idCompra = 0;
        if (rs.next()) {
            idCompra = rs.getInt(1);
        }
        rs.close();
        pstmtCompra.close();

        // Insertar detalles
        String sqlDetalle = "INSERT INTO detalle_compra(idCompra, idProducto, kilos, precioCompra, descuento) VALUES(?,?,?,?,?)";
        PreparedStatement pstmtDetalle = conn.prepareStatement(sqlDetalle);

        for (DetalleCompra d : c.getDetalles()) {
            pstmtDetalle.setInt(1, idCompra);
            pstmtDetalle.setInt(2, d.getProducto().getId());
            pstmtDetalle.setDouble(3, d.getKilos());
            pstmtDetalle.setDouble(4, d.getPrecioCompra());
            pstmtDetalle.setDouble(5, d.getDescuento());
            pstmtDetalle.executeUpdate();
        }

        pstmtDetalle.close();
        conn.commit();
        conn.close();

        return idCompra;
    }

    // Eliminar una compra (borrado físico: primero detalles, luego compra)
    public void delete(int idCompra) throws Exception {
        String sqlDetalle = "DELETE FROM detalle_compra WHERE idCompra=?";
        String sqlCompra = "DELETE FROM compra WHERE idCompra=?";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        conn.setAutoCommit(false);

        PreparedStatement pstmtDetalle = conn.prepareStatement(sqlDetalle);
        pstmtDetalle.setInt(1, idCompra);
        pstmtDetalle.executeUpdate();
        pstmtDetalle.close();

        PreparedStatement pstmtCompra = conn.prepareStatement(sqlCompra);
        pstmtCompra.setInt(1, idCompra);
        pstmtCompra.executeUpdate();
        pstmtCompra.close();

        conn.commit();
        conn.close();
    }

    // Método auxiliar para llenar objeto Compra desde ResultSet
    private Compra fill(ResultSet rs) throws Exception {
        Compra c = new Compra();
        Proveedor p = new Proveedor();

        c.setIdCompra(rs.getInt("idCompra"));
        c.setFechaCompra(rs.getString("fechaCompra"));

        p.setId(rs.getInt("idProveedor"));
        p.setNombre(rs.getString("nombreProveedor"));
        c.setProveedor(p);

        return c;
    }
}
