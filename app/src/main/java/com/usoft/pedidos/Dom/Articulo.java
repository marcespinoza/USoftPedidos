package com.usoft.pedidos.Dom;

public class Articulo {

    public Articulo() {
    }

    String codart;
    String desart;
    String familia;
    String precio;
    String ultprc;
    String precio_original;

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
