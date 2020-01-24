package com.usoft.pedidos.Interface;


import com.usoft.pedidos.Dom.Articulo;

public interface LectorInterface {

    interface Vista{
        void enviarCodigo(String codigo);
        void mostrarArticulo(Articulo articulo, String imageString, String ms);
        void mostrarError(String error, String codart);
    }

    interface Presentador{
        void enviarCodigo(String codart);
        void devolverArticulo(Articulo articulo, String img, String imageString);
        void mostrarError(String mensaje, String codart);
    }

    interface Modelo{
        void obtenerArticulo(String codart);
    }


}
