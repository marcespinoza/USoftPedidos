package com.usoft.pedidos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.usoft.pedidos.Dom.Pedido;
import com.usoft.pedidos.R;
import com.usoft.pedidos.Vista.PedidoActivity;

import java.util.ArrayList;


public class RecyclerPedido extends RecyclerView.Adapter<RecyclerPedido.ViewHolder>{

    private Context context;
    private ArrayList<Pedido> listaPedidos;
    private TotalInterface totalInterface;

    public interface TotalInterface{
        void descontarTotal(String total);
    }

    public RecyclerPedido(Context context, ArrayList<Pedido> listaPedidos, PedidoActivity pedido) {
        this.context=context;
        this.listaPedidos = listaPedidos;
        totalInterface = ((TotalInterface) pedido);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);
        return new RecyclerPedido.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.codart.setText(listaPedidos.get(position).getCodigoarticulo());
        holder.cantidad.setText(listaPedidos.get(position).getCantidad());
        holder.total.setText(listaPedidos.get(position).getPrecio());
        holder.descripcion.setText(listaPedidos.get(position).getDescripcion());
        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalInterface.descontarTotal(listaPedidos.get(position).getTotal());
                listaPedidos.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,listaPedidos.size());

            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView codart;
        TextView cantidad;
        TextView total;
        TextView descripcion;
        ImageButton eliminar;

        public ViewHolder(View itemView){
            super(itemView);
            codart = itemView.findViewById(R.id.codart);
            cantidad = itemView.findViewById(R.id.cant);
            total = itemView.findViewById(R.id.preciolinea);
            descripcion = itemView.findViewById(R.id.desart);
            eliminar = itemView.findViewById(R.id.botoneliminar);
        }
    }

}
