package com.stebanramos.appnodejs.models;

public class Producto {
    private String nombre;
    private String precio;
    private String disponible;
    private String id;

    public Producto(String nombre, String precio, String disponible, String id) {
        this.nombre = nombre;
        this.precio = precio;
        this.disponible = disponible;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getDisponible() {
        return disponible;
    }

    public void setDisponible(String disponible) {
        this.disponible = disponible;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
