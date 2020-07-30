package com.qpmovil;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;

public class formato_listView_promociones extends BaseAdapter {

    protected Activity activity;
    protected List<Promocion> promociones;

    public formato_listView_promociones (Activity activity, List<Promocion> promocion) {
        this.activity = activity;
        this.promociones = promocion;
    }

    @Override
    public int getCount() {
        return promociones.size();
    }

    @Override
    public Object getItem(int i) {
        return promociones.get(i);
    }

    @Override
    public long getItemId(int i) {
        return promociones.get(i).getId();
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        View vi=contentView;

        if(contentView == null)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            vi = inflater.inflate(R.layout.listview_promociones, null);
        }

        Promocion item = promociones.get(position);

        TextView descripcion = vi.findViewById(R.id.TV_promo_descrip); //Asocio con lo que hay en listview_stock.xml
        descripcion.setText(item.getDescripcion());

        TextView Codigo = vi.findViewById(R.id.TV_promo_codigo);
        Codigo.setText(item.getCodigo());

        TextView Tipo = vi.findViewById(R.id.TV_promo_tipo);
        Tipo.setText(item.getTipo());

        TextView Vigencia = vi.findViewById(R.id.TV_promo_vigencia);
        Vigencia.setText(item.getVigencia());

        TextView Dias = vi.findViewById(R.id.TV_promo_dias);
        Dias.setText(item.getDias());

        TextView Horarios = vi.findViewById(R.id.TV_promo_horarios);
        Horarios.setText(item.getHora());

        TextView Extras = vi.findViewById(R.id.TV_promo_extras);
        Extras.setText(item.getExtras());

        return vi;
    }
}
