package com.qpmovil;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;

public class formato_ListView_Stock extends BaseAdapter {

    protected Activity activity;
    protected List<Producto> productos;

    public formato_ListView_Stock(Activity activity, List<Producto> productos) {
        this.activity = activity;
        this.productos = productos;
    }

    @Override
    public int getCount() {
        return productos.size();
    }

    @Override
    public Object getItem(int i) {
        return productos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return productos.get(i).getId();
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        View vi=contentView;

        if(contentView == null)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            vi = inflater.inflate(R.layout.listview_stock, null);
        }

        Producto item = productos.get(position);

        TextView descripcion = vi.findViewById(R.id.descripcion); //Asocio con lo que hay en listview_stock.xml
        descripcion.setText(item.getDescripcion());

        TextView Cod_madre = vi.findViewById(R.id.codigoMadre);
        Cod_madre.setText(item.getCodigo());

        TextView stock = vi.findViewById(R.id.stock);
        stock.setText(item.getStock());



        return vi;
    }
}
