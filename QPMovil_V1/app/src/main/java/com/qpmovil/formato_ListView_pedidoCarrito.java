package com.qpmovil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;


public class formato_ListView_pedidoCarrito extends BaseAdapter {

    private  Activity activity;
    private Fragment_carrito fr_carrito;
    private List<Producto_Carrito> lst_prod_carr;
    private formato_ListView_pedidoCarrito adp;

    public formato_ListView_pedidoCarrito(Activity activity, List<Producto_Carrito> lstProduc_carrito,Fragment_carrito frag_carrito)
    {
        this .activity = activity;
        this.lst_prod_carr = lstProduc_carrito;
        this.fr_carrito = frag_carrito;
        this.adp = this;


    }


    @Override
    public int getCount() {
        return lst_prod_carr.size();
    }

    @Override
    public Object getItem(int i) {
        return lst_prod_carr.get(i);
    }

    @Override
    public long getItemId(int i) {
      return lst_prod_carr.get(i).producto.getId();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View vista = view;

        if(vista == null)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vista = inflater.inflate(R.layout.listview_pedido_carrito, null);

        }


     final   Producto_Carrito item = lst_prod_carr.get(i);

        int cant = item.getCantidad();
        int precio = Integer.valueOf(item.producto.getPrecio());

        TextView TalleYcolor = vista.findViewById(R.id.TV_carrito_codigo);
        TextView Descripcion = vista.findViewById(R.id.TV_carrito_descripcion);
        TextView Precio = vista.findViewById(R.id.TV_carrito_precio);
        TextView Cantidad =  vista.findViewById(R.id.TV_carrito_cantidad);
        TextView SubTotal =  vista.findViewById(R.id.TV_SubTotal);
        ImageButton modificarItem =vista.findViewById(R.id.IB_Modificar_del_carrito);
        ImageButton borrarItem= vista.findViewById(R.id.IB_eliminar_del_carrito);



        TalleYcolor.setText("Talle: " + item.producto.getTalle()+ "  " +" Color: " + item.producto.getColor());
        Descripcion.setText(item.producto.getDescripcion());

        Precio.setText("Precio: " + "$" + precio);
        Cantidad.setText("Cantidad de Items: " + String.valueOf(item.getCantidad()));

        SubTotal.setText("Subtotal: " + "$" + (precio*cant));


        modificarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(politica);



                alert_modificar_carrito alerta = new alert_modificar_carrito(activity,item,i,adp,fr_carrito);
                 alerta.Mostrar_alertCarrito();
                fr_carrito.CalcularTotal(lst_prod_carr);


            }
        });

        borrarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Confirmacion(i);


            }
        });



        return vista;
    }


    private void Confirmacion(final int i)
    {
        AlertDialog.Builder alerta  = new AlertDialog.Builder(activity);


        alerta.setTitle("Importante");
        alerta.setMessage("¿Desea eliminar el ítem  del carrito? ");
        alerta.setCancelable(true);

        alerta.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

                lst_prod_carr.remove(i);
                notifyDataSetChanged();
                fr_carrito.CalcularTotal(lst_prod_carr);
                if (lst_prod_carr.size() == 0)
                {
                    fr_carrito.carritoVacio();
                }



            }
        });
        alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
            }
        });
        alerta.show();
    }

}
