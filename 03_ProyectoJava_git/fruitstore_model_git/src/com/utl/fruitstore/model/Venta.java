/*
Artifact:   Venta.java

Version:    1.0
Date:       2024-05-21 19:00:00
Author:     Miguel Angel Gil Rios
Email:      angel.grios@gmail.com - mgil@utleon.edu.mx
Comments:   Version inicial del codigo.

Version:    1.1
Date:       2025-04-29 08:15:00
Author:     Miguel Angel Gil Rios
Email:      angel.grios@gmail.com - mgil@utleon.edu.mx
Comments:   Se cambio la declaracion del paquete para adaptarlo a un proyecto
            didactico de la UTL.
*/
package com.utl.fruitstore.model;

import java.util.List;

/**
 *
 * @author LiveGrios
 */
public class Venta
{
    int id;
    String fecha;
    Vendedor vendedor;
    List<ProductoDetalle> productos;
    double subtotal;
    double descuento;
    double total;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getFecha()
    {
        return fecha;
    }

    public void setFecha(String fecha)
    {
        this.fecha = fecha;
    }

    public Vendedor getVendedor()
    {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor)
    {
        this.vendedor = vendedor;
    }

    public List<ProductoDetalle> getProductos()
    {
        return productos;
    }

    public void setProductos(List<ProductoDetalle> productos)
    {
        this.productos = productos;
    }
    
    public double getTotal()
    {
        return total;
    }

    public void setTotal(double total)
    {
        this.total = total;
    }

    public double getSubtotal()
    {
        return subtotal;
    }

    public void setSubtotal(double subtotal)
    {
        this.subtotal = subtotal;
    }

    public double getDescuento()
    {
        return descuento;
    }

    public void setDescuento(double descuento)
    {
        this.descuento = descuento;
    }

    public void update()
    {
        subtotal = 0;
        descuento = 0;
        total = 0;
        if (productos != null)
        {
            for (ProductoDetalle pv : productos)
            {
                subtotal += pv.getPrecioVenta() * pv.getCantidad();
                descuento += pv.getDescuento();
            }
            total = subtotal - descuento;
        }
    }
    
}
