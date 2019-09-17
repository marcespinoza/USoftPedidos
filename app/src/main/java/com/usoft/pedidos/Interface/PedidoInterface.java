package com.usoft.pedidos.Interface;



import com.usoft.pedidos.Dom.Pedido;

import java.util.ArrayList;

public interface PedidoInterface {

    interface Vista{
        void articulosList(ArrayList articulos);
        void clientesList(ArrayList clientes);
        void mostraError(String error);
        void confirmaPedido(String contador);
        void mostrarNroPedido(String nropedido);
    }

    interface Presentador{
        void articulos(String nombre);
        void devolverArticulos(ArrayList articulos);
        void mostrarError(String error);
        void enviarLista(ArrayList<Pedido> list, String np, String cc);
        void confirmarPedido(String contador);
        void getNroPedido();
        void nroPedido(String nropedido);

        void obtenerClientes(String cuenta);
        void devolverClientes(ArrayList clientes);
    }

    interface Modelo{
        void getArticulos(String nombre);
        void enviarListaPedidos(ArrayList<Pedido> list, String np, String cc);
        void obtenerNroPedido();

        void obtenerClientes(String cuenta);
    }

}
