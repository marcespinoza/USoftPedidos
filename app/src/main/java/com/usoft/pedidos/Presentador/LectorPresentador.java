package com.usoft.pedidos.Presentador;


import com.usoft.pedidos.Dom.Articulo;
import com.usoft.pedidos.Interface.LectorInterface;
import com.usoft.pedidos.Modelo.LectorModelo;
import com.usoft.pedidos.Vista.LectorActivity;

public class LectorPresentador implements LectorInterface.Presentador {

    private LectorInterface.Vista vista;
    private LectorInterface.Modelo modelo;

    public LectorPresentador(LectorActivity vista) {
        this.vista=vista;
        modelo = new LectorModelo(this);
    }


    @Override
    public void enviarCodigo(String codart) {
       modelo.obtenerArticulo(codart);
    }

    @Override
    public void devolverArticulo(Articulo articulo, String img, String ms) {
        vista.mostrarArticulo(articulo, img, ms);
    }

    @Override
    public void mostrarError(String mensaje, String codart) {
        vista.mostrarError(mensaje, codart);
    }
}
