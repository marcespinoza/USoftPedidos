package com.usoft.pedidos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.usoft.pedidos.Dom.Articulo;
import com.usoft.pedidos.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AutoCompleteArticuloAdapter extends ArrayAdapter<Articulo> {
    private List<Articulo> articuloLista;
    Context context;

    public AutoCompleteArticuloAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Articulo> countryList) {
        super(context, textViewResourceId, countryList);
        articuloLista = new ArrayList<>(countryList);
        this.context = context;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return countryFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.autocompletearticulo_row, parent, false
            );
        }
        TextView textViewName = convertView.findViewById(R.id.descripcionart);
        Articulo articulo = getItem(position);

        if (articulo != null) {
            if(articulo.getPrecio_original().equals("null")){
                textViewName.setTextColor(context.getResources().getColor(R.color.red_300));
            }else{
                textViewName.setTextColor(context.getResources().getColor(R.color.black));
            }
            textViewName.setText(articulo.getDesart());
        }

        return convertView;
    }

    private Filter countryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Articulo> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(articuloLista);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Articulo item : articuloLista) {
                    if (item.getDesart().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }
            Collections.sort(suggestions, new Comparator<Articulo>() {
                @Override
                public int compare(Articulo s1, Articulo s2) {
                    return s1.getDesart().compareToIgnoreCase(s2.getDesart());
                }
            });
            results.values = suggestions;
            results.count = suggestions.size();

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Articulo) resultValue).getDesart();
        }
    };
}
