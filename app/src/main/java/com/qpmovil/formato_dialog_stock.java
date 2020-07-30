package com.qpmovil;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class formato_dialog_stock extends BaseAdapter {

    protected Context c;
    protected List<Producto> productos;



    public formato_dialog_stock(Context contexto, List<Producto> productos) {
        this.c = contexto;
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
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            vi = inflater.inflate(R.layout.formato_dialog_stock, null);
        }


        final Producto item = productos.get(position);

        TextView precioItem = vi.findViewById(R.id.et_precio); //Asocio con lo que hay en listview_stock.xml
        precioItem.setText(item.getPrecio());

        TextView stock = vi.findViewById(R.id.et_stock);
        stock.setText(item.getStock());

        TextView color = vi.findViewById(R.id.et_Color);
        color.setText(item.getColor());

        TextView talle = vi.findViewById(R.id.et_talle);
        talle.setText(item.getTalle());

       ImageButton imagen = vi.findViewById(R.id.imagensumar);
        imagen.setImageResource(R.drawable.ic_agregarcarrito);

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {



                alert_agregar_carrito alerta = new alert_agregar_carrito(view.getContext(),item);

                //agrego la siguiente linea para llamar el dialogo

                alerta.Mostrar_alertCarrito();
            }
        });

        return vi;
    }
}
