package com.usoft.pedidos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.usoft.pedidos.Dom.Pedido;
import com.usoft.pedidos.R;

import java.util.ArrayList;


public class RecyclerListaPedido extends RecyclerView.Adapter<RecyclerListaPedido.ViewHolder>{

    private Context context;
    private ArrayList<Pedido> listaPedidos;

    public RecyclerListaPedido(Context context, ArrayList<Pedido> listaPedidos) {
        this.context=context;
        this.listaPedidos = listaPedidos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_pedido, parent, false);
        return new RecyclerListaPedido.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nropedido.setText(listaPedidos.get(position).getNropedido());
        holder.desart.setText(listaPedidos.get(position).getDescripcion());
        holder.cant_ped.setText("Cant. ped: "+listaPedidos.get(position).getCantidad());
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nropedido;
        TextView desart;
        TextView cant_ped;

        public ViewHolder(View itemView){
            super(itemView);
            nropedido = itemView.findViewById(R.id.nro_pedido);
            desart = itemView.findViewById(R.id.desart);
            cant_ped = itemView.findViewById(R.id.cant_ped);
        }
    }

}
