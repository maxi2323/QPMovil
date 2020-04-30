package com.qpmovil;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class alert_agregar_carrito {

    private List<Producto> listaProducto = new ArrayList<Producto>();
    private Producto producto = null;
    private ListView mostrar;

    ResultSet resultado= null;


    private Fragment_datosConexion verificarconexion = new Fragment_datosConexion();
    alert_animacionespera animacionespera;
    private String codmadre;
    Dialog dialogo;
    Configuracion configuracion = new Configuracion();



    public alert_agregar_carrito(final Context contexto, final Producto item){//Constructor



        dialogo = new Dialog(contexto);

        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setTitle("Agregar al carrito");
        dialogo.setCancelable(true);
        dialogo.setContentView(R.layout.alert_agregaracarrito);

        TextView codITM = dialogo.findViewById(R.id.tv_alertCodITM);

        TextView talle = dialogo.findViewById(R.id.tv_alertTalle);
        TextView color = dialogo.findViewById(R.id.tv_alertColor);
        TextView stock = dialogo.findViewById(R.id.tv_alertStock);

        final TextView precio = dialogo.findViewById(R.id.tv_alertPrecio);
        final TextView subtotal = dialogo.findViewById(R.id.et_subtotal);

        final EditText et_cantidad = dialogo.findViewById(R.id.ET_cantidad);
        ImageButton IB_sumarCantidad = dialogo.findViewById(R.id.IB_sumar);
        final ImageButton IB_restarCantidad=dialogo.findViewById(R.id.IB_restar);
        ImageButton IB_agregarcarr = dialogo.findViewById(R.id.IB_agregarcarr);

        et_cantidad.setText("1");

        int SubT= Integer.parseInt(et_cantidad.getText().toString()) * Integer.parseInt(item.getPrecio());
        subtotal.setText("$ " + String.valueOf(SubT));

        //pongo a la escucha el boton restar, solo resta si es mayor que 0
        IB_restarCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int valorActual;
                valorActual = Integer.parseInt(et_cantidad.getText().toString());

                if (valorActual > 0)
                {
                   // valorActual -=1;

                    if(valorActual > 1){
                        valorActual -=1;
                        IB_restarCantidad.setColorFilter(Color.argb(172,172,172,172));
                    }

                    et_cantidad.setText(String.valueOf(valorActual));
                    int SubT= valorActual * Integer.parseInt(item.getPrecio());
                        subtotal.setText("$ " + String.valueOf(SubT));

                }

            }
        });
        // pongo a la escucha el boton sumar
        IB_sumarCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int valorActual;

                valorActual = Integer.parseInt(et_cantidad.getText().toString());

                valorActual += 1;

                if(valorActual > Integer.parseInt(item.getStock()))
                    Toast.makeText(contexto,"CUIDADO, El stock quedara en negativo",Toast.LENGTH_LONG).show();

                et_cantidad.setText(String.valueOf(valorActual));
                int SubT= valorActual * Integer.parseInt(item.getPrecio());
                subtotal.setText("$ "+String.valueOf(SubT));

                IB_restarCantidad.setColorFilter(Color.argb(0,0,0,0));


            }
        });

        //pongo a la escucha el IMAGE BUTTON TILDE
        IB_agregarcarr.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
            try{
                Producto Prod = item;
                int cantidad = Integer.parseInt(et_cantidad.getText().toString());
                Carrito carrito = Carrito.obtenerInstancia();
                if (cantidad > 0) {
                    carrito.AgregarAlCarrito(CargarProducto(cantidad, Prod));
                    Toast.makeText(contexto, "SE AGREGO AL CARRITO", Toast.LENGTH_LONG).show();
                    Cerrar_alertCarrito();
                } else {
                    Toast.makeText(contexto, "No puede agregar al carrito productos con 0 unidades", Toast.LENGTH_LONG).show();
                }
            }catch (NumberFormatException nfe){
                    Toast.makeText(contexto,"Elemento no numérico",Toast.LENGTH_LONG).show();
                }

            }
        });


        StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(politica);

        codmadre = item.getCodigo();
        configuracion.Configuracion(contexto);

        codITM.setText(item.getDescripcion());
        precio.setText("$ "+item.getPrecio());
        talle.setText(item.getTalle());
        color.setText(item.getColor());
        stock.setText(item.getStock());

      //  dialogo.show();

    }

    public  void Mostrar_alertCarrito()
    {
        dialogo.show();

    }

    private void Cerrar_alertCarrito()
    {
          dialogo.dismiss();

    }

    private Producto_Carrito CargarProducto(int cantidad, Producto producto)
    {
        Producto_Carrito producto_carrito = new Producto_Carrito();
        producto_carrito.setCantidad(cantidad) ;
        producto_carrito.producto = producto;

        return producto_carrito;

    }

}

