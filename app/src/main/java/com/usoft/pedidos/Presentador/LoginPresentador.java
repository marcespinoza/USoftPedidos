package com.usoft.pedidos.Presentador;


import com.usoft.pedidos.Interface.LoginInterface;
import com.usoft.pedidos.Modelo.LoginModelo;
import com.usoft.pedidos.Vista.LoginActivity;

public class LoginPresentador implements LoginInterface.Presentador {

    LoginInterface.Vista vista;
    LoginInterface.Modelo modelo;

    public LoginPresentador(LoginActivity vista){
        this.vista = vista;
        modelo = new LoginModelo(this);
    }

    @Override
    public void verificarUsuario(String usuario, String clave) {
        if(usuario.equals("") || clave.equals("")){
            vista.mostrarError("Complete todos los campos");
        }else{
            modelo.enviarUsuario(usuario, clave);
        }
    }

    @Override
    public void usuarioIncorrecto() {
        vista.mostrarError("Usuario/contraseña incorrectos");
    }

    @Override
    public void mostrarError(String error) {
        vista.mostrarError(error);
    }

    @Override
    public void verificarEmpresa(String empresa) {
        modelo.verificarEmpresa(empresa);
    }

    @Override
    public void resultadoConexion(boolean b, String mensaje) {
        vista.resultadoConexion(b, mensaje);
    }

    @Override
    public void devolverUsuario(String nombre) {
        vista.mostrarExito(nombre);
    }
}
