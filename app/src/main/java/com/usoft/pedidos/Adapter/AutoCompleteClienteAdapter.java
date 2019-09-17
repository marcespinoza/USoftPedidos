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

import com.usoft.pedidos.Dom.Cliente;
import com.usoft.pedidos.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AutoCompleteClienteAdapter extends ArrayAdapter<Cliente> {

    private List<Cliente> countryListFull;

    public AutoCompleteClienteAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Cliente> countryList) {
        super(context, textViewResourceId, countryList);
        countryListFull = new ArrayList<>(countryList);
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
                    R.layout.autocompletecliente_row, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.nombrecliente);

        Cliente Cliente = getItem(position);

        if (Cliente != null) {
            textViewName.setText(Cliente.getNomcli());
        }

        return convertView;
    }

    private Filter countryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Cliente> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(countryListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Cliente item : countryListFull) {
                    if (item.getNomcli().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }
            Collections.sort(suggestions, new Comparator<Cliente>() {
                @Override
                public int compare(Cliente s1, Cliente s2) {
                    return s1.getNomcli().compareToIgnoreCase(s2.getNomcli());
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
            return ((Cliente) resultValue).getNomcli();
        }
    };
}

