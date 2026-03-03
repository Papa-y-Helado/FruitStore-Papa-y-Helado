package com.utl.fruitstore.model;

import java.util.List;

/*
    Clase Compra
    Representa una compra realizada a un proveedor.
    Contiene la información principal de la compra y la lista de detalles.
 */
public class Compra {

    private int idCompra;
    private String fechaCompra;
    private Proveedor proveedor;
    private List<DetalleCompra> detalles;

    public Compra() {
    }

    public Compra(int idCompra, String fechaCompra, Proveedor proveedor, List<DetalleCompra> detalles) {
        this.idCompra = idCompra;
        this.fechaCompra = fechaCompra;
        this.proveedor = proveedor;
        this.detalles = detalles;
    }

    // Getters y Setters
    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public String getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public List<DetalleCompra> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCompra> detalles) {
        this.detalles = detalles;
    }
}
