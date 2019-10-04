package com.usoft.pedidos.Presentador;

import com.usoft.pedidos.Dom.Pedido;
import com.usoft.pedidos.Interface.PedidoInterface;
import com.usoft.pedidos.Modelo.PedidoModelo;
import com.usoft.pedidos.Vista.PedidoActivity;

import java.util.ArrayList;

public class PedidoPresentador implements PedidoInterface.Presentador {

    PedidoInterface.Vista vista;
    PedidoInterface.Modelo modelo;

    public PedidoPresentador(PedidoActivity vista) {
        this.vista = vista;
        modelo = new PedidoModelo(this);
    }

    @Override
    public void articulos(String nombre, String codigoCliente) {
        modelo.getArticulos(nombre, codigoCliente);
    }



    @Override
    public void devolverArticulos(ArrayList articulos) {
        vista.articulosList(articulos);
    }


    @Override
    public void mostrarError(String error) {
        vista.mostraError(error);
    }

    @Override
    public void enviarLista(ArrayList<Pedido> list, String np, String cc) {
        modelo.enviarListaPedidos(list, np ,cc);
    }

    @Override
    public void confirmarPedido(String contador) {
        vista.confirmaPedido(contador);
    }

    @Override
    public void getNroPedido() {
        modelo.obtenerNroPedido();
    }

    @Override
    public void nroPedido(String nropedido) {
        vista.mostrarNroPedido(nropedido);
    }

    @Override
    public void obtenerClientes(String cuenta) {
        modelo.obtenerClientes(cuenta);
    }

    @Override
    public void devolverClientes(ArrayList clientes) {
        vista.clientesList(clientes);
    }


}
