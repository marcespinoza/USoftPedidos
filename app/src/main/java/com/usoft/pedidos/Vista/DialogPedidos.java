package com.usoft.pedidos.Vista;




import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.usoft.pedidos.Adapter.RecyclerListaPedido;
import com.usoft.pedidos.Dom.Pedido;
import com.usoft.pedidos.R;

import java.util.ArrayList;

public class DialogPedidos extends DialogFragment {

    private ArrayList<Pedido> lpedido = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerListaPedido adapter;

    public static DialogPedidos getInstanceFor(ArrayList<Pedido> list){
        DialogPedidos fragment=new DialogPedidos();
        fragment.lpedido = list;
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.lista_pedidos, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerlistap);
        adapter = new RecyclerListaPedido(getActivity(), lpedido);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics metrics = new DisplayMetrics(); //get metrics of screen
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int height = (int) (metrics.heightPixels*0.7); //set height to 90% of total
            int width = (int) (metrics.widthPixels*0.9); //set width to 90% of total
            dialog.getWindow().setLayout(width, height);
        }
    }
}