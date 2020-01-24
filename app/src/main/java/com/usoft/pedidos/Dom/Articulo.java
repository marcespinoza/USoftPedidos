package com.usoft.pedidos.Dom;

import java.io.Serializable;

public class Articulo implements Serializable {

    public Articulo() {
    }

    String codart;
    String desart;
    String familia;
    String precio;
    String ultprc;
    String precio_original;
    String descripcion;
    String descripcioncorta;
    String marca;
    String descripcionMaestro;

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDescripcionMaestro() {
        return descripcionMaestro;
    }

    public void setDescripcionMaestro(String descripcionMaestro) {
        this.descripcionMaestro = descripcionMaestro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcioncorta() {
        return descripcioncorta;
    }

    public void setDescripcioncorta(String descripcioncorta) {
        this.descripcioncorta = descripcioncorta;
    }

    public String getPrecio_original() {
        return precio_original;
    }

    public void setPrecio_original(String precio_original) {
        this.precio_original = precio_original;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getUltprc() {
        return ultprc;
    }

    public void setUltprc(String ultprc) {
        this.ultprc = ultprc;
    }


    public String getCodart() {
        return codart;
    }

    public void setCodart(String codart) {
        this.codart = codart;
    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }


}
