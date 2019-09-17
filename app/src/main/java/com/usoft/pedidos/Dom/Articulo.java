package com.usoft.pedidos.Dom;

public class Articulo {

    public Articulo() {
    }

    String codart;
    String desart;
    String familia;
    String precio;
    String ultprc;

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
