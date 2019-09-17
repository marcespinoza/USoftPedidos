package com.usoft.pedidos.Interface;

public interface LoginInterface {
    interface Vista{
        void loginUsuario(String usuario, String clave);
        void mostrarError(String error);
        void mostrarExito(String usuario);
        void resultadoConexion(boolean b, String mensaje);
    }

    interface Modelo{
        void enviarUsuario(String usuario, String clave);
        void verificarEmpresa(String empresa);
    }

    interface Presentador{
        void verificarUsuario(String usuario, String clave);
        void devolverUsuario(String nombre);
        void usuarioIncorrecto();
        void mostrarError(String error);
        void verificarEmpresa(String empresa);
        void resultadoConexion(boolean b, String mensaje);
    }
}
