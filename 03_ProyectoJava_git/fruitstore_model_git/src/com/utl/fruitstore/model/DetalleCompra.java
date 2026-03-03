package com.utl.fruitstore.model;

/*
    Clase DetalleCompra
    Representa cada producto dentro de una compra.
    Contiene kilos, precio de compra y descuento.
 */
public class DetalleCompra {

    private int idCompra;
    private Producto producto;
    private double kilos;
    private double precioCompra;
    private double descuento;

    public DetalleCompra() {
    }

    public DetalleCompra(int idCompra, Producto producto, double kilos, double precioCompra, double descuento) {
        this.idCompra = idCompra;
        this.producto = producto;
        this.kilos = kilos;
        this.precioCompra = precioCompra;
        this.descuento = descuento;
    }

    // Getters y Setters
    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public double getKilos() {
        return kilos;
    }

    public void setKilos(double kilos) {
        this.kilos = kilos;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }
}
