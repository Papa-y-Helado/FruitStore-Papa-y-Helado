package com.utl.fruitstore.controller;

import com.utl.fruitstore.db.ConexionMySQL;
import com.utl.fruitstore.model.Categoria;
import com.utl.fruitstore.model.ProductoDetalle;
import com.utl.fruitstore.model.Vendedor;
import com.utl.fruitstore.model.Venta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LiveGrios
 */
public class ControllerVenta
{
    
    public int insert(Venta v) throws Exception
    {
        String sqlVenta = "INSERT INTO venta(idVendedor, fechaVenta) VALUES(?, NOW())";
        String sqlDetalleVenta = "INSERT INTO detalle_venta(idVenta, idProducto, kilos, precioCompra, precioVenta, descuento) VALUES(?, ?, ?, ?, ?)";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmtVenta = null; 
        PreparedStatement pstmtDetalleVenta = null; 
        ResultSet rsVenta = null;
        
        conn.setAutoCommit(false);
        
        try
        {
            pstmtVenta = conn.prepareStatement(sqlVenta, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmtDetalleVenta = conn.prepareStatement(sqlDetalleVenta);

            pstmtVenta.setInt(1, v.getVendedor().getId());
            pstmtVenta.executeUpdate();
            rsVenta = pstmtVenta.getGeneratedKeys();
            rsVenta.next();
            v.setId(rsVenta.getInt(1));
            
            for (int i = 0; i < v.getProductos().size(); i++)
            {
                pstmtDetalleVenta.setInt(1, v.getId());
                pstmtDetalleVenta.setInt(2, v.getProductos().get(i).getId());
                pstmtDetalleVenta.setDouble(3, v.getProductos().get(i).getCantidad());
                pstmtDetalleVenta.setDouble(4, v.getProductos().get(i).getPrecioCompra());
                pstmtDetalleVenta.setDouble(5, v.getProductos().get(i).getPrecioVenta());
                pstmtDetalleVenta.setDouble(6, v.getProductos().get(i).getDescuento());
                pstmtDetalleVenta.addBatch();
            }
            pstmtDetalleVenta.executeBatch();
            conn.commit();
            rsVenta.close();
            pstmtVenta.close();
            conn.close();
            return v.getId();
        } 
        catch (Exception e)
        {
            try
            {
                conn.rollback();
            } 
            catch (Exception ex)
            {
                ex.printStackTrace();
                throw(ex);
            }
            conn.close();
            throw (e);
        }
    }
    
    public void update(Venta v) throws Exception
    {
        String sql = "UPDATE venta SET estatus=2 WHERE idVenta = ?";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, v.getId());
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }
 
    public Venta getByID(int id) throws Exception
    {
        Venta vt = null;
        
        String sql = "SELECT * FROM v_venta WHERE idVenta = " + id;
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        
        if(rs.next())
            vt = fillDetail(rs);
        
        rs.close();
        pstmt.close();
        conn.close();
        
        vt.update();
        return vt;
    }
    
    public List<Venta> getAllResumen(String filtro) throws Exception
    {
        String sql = "SELECT * FROM v_ventas_resumen";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        List<Venta> ventas = new ArrayList<>();
        
        while(rs.next())
            ventas.add(fill(rs));
        
        rs.close();
        pstmt.close();
        conn.close();
        
        return ventas;
    }
    
    
    private Venta fill(ResultSet rs) throws Exception
    {
        Venta vt = new Venta();
        Vendedor vd = new Vendedor();
        
        vd.setId(rs.getInt("idVendedor"));
        vd.setNombre(rs.getString("nombre"));
        
        vt.setDescuento(rs.getDouble("descuento"));
        vt.setFecha(rs.getString("fechaVenta"));
        vt.setId(rs.getInt("idVenta"));
        vt.setSubtotal(rs.getDouble("subtotal"));
        vt.setTotal(rs.getDouble("total"));
        vt.setVendedor(vd);
        
        return vt;
    }
    
    private Venta fillDetail(ResultSet rs) throws Exception
    {        
        Venta vt = new Venta();
        Vendedor vd = new Vendedor();
        
        vt.setProductos(new ArrayList<>());        
        vt.setFecha(rs.getString("fechaVenta"));
        vt.setId(rs.getInt("idVenta"));
        
        vd.setCp(rs.getString("codPostal"));
        vd.setCalle(rs.getString("calle"));
        vd.setFechaAlta(rs.getString("fechaAlta"));
        vd.setFechaNacimiento(rs.getString("fechaNac"));
        vd.setId(rs.getInt("idVendedor"));
        vd.setNombre(rs.getString("nombreVendedor"));
        vd.setCiudad(rs.getString("ciudad"));
        vd.setTelefono(rs.getString("telefono"));
        
        vt.setVendedor(vd);
        
        vt.getProductos().add(fillProducto(rs));        
        while(rs.next())
            vt.getProductos().add(fillProducto(rs));
        
        
        return vt;
    }
    
    private ProductoDetalle fillProducto(ResultSet rs) throws Exception
    {
        ProductoDetalle p = new ProductoDetalle();
        
        p.setId(rs.getInt("idProducto"));
        p.setNombre(rs.getString("nombreProducto"));
        p.setPrecioCompra(rs.getDouble("precioCompra"));
        p.setPrecioVenta(rs.getDouble("precioVenta"));
        p.setEstatus(rs.getInt("estatus"));
        
        p.setCategoria(new Categoria(rs.getInt("idGrupo"),
                             rs.getString("nombreGrupo")));
        
        p.setCantidad(rs.getDouble("kilos"));
        p.setDescuento(rs.getDouble("descuento"));
        p.setSubtotal(rs.getDouble("subtotal"));
        p.setTotal(rs.getDouble("total"));
        
        return p;
    }
}
