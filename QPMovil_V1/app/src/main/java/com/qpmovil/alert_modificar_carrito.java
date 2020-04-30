package com.qpmovil;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;

import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.Toast;



public class alert_modificar_carrito {

    private Dialog dialogo;





public alert_modificar_carrito(final Context contexto, final Producto_Carrito prod_carrito, final int position, final BaseAdapter adaptador, final Fragment_carrito frag_carrito)
{
    dialogo = new Dialog(contexto);

    dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialogo.setTitle("Modificar ítem del carrito");
    dialogo.setCancelable(true);
    dialogo.setContentView(R.layout.alert_agregaracarrito);




    TextView codITM = dialogo.findViewById(R.id.tv_alertCodITM);

    TextView talle = dialogo.findViewById(R.id.tv_alertTalle);
    TextView color = dialogo.findViewById(R.id.tv_alertColor);
    final TextView stock = dialogo.findViewById(R.id.tv_alertStock);

    final TextView precio = dialogo.findViewById(R.id.tv_alertPrecio);
    final TextView subtotal = dialogo.findViewById(R.id.et_subtotal);


    final EditText et_cantidad = dialogo.findViewById(R.id.ET_cantidad);
    ImageButton IB_sumarCantidad = dialogo.findViewById(R.id.IB_sumar);
    final ImageButton IB_restarCantidad=dialogo.findViewById(R.id.IB_restar);
    ImageButton IB_agregarcarr = dialogo.findViewById(R.id.IB_agregarcarr);

    et_cantidad.setText( String.valueOf(prod_carrito.getCantidad()));

    int SubT= Integer.parseInt(et_cantidad.getText().toString()) * Integer.parseInt(prod_carrito.producto.getPrecio());
    subtotal.setText("$ " + String.valueOf(SubT));

    codITM.setText(prod_carrito.producto.getDescripcion());
    precio.setText("$ "+prod_carrito.producto.getPrecio());
    talle.setText(prod_carrito.producto.getTalle());
    color.setText(prod_carrito.producto.getColor());
    stock.setText(prod_carrito.producto.getStock());

    //pongo a la escucha el boton restar, solo resta si es mayor que 0
    IB_restarCantidad.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            int valorActual;
            valorActual = Integer.parseInt(et_cantidad.getText().toString());

            if (valorActual > 0)
            {
                if(valorActual > 1){
                    valorActual -=1;
                    IB_restarCantidad.setColorFilter(Color.argb(172,172,172,172));
                }

                et_cantidad.setText(String.valueOf(valorActual));
                int SubT= valorActual * Integer.parseInt(prod_carrito.producto.getPrecio());
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

            if(valorActual > Integer.parseInt(stock.getText().toString()))
                Toast.makeText(contexto,"CUIDADO, El stock quedara en negativo",Toast.LENGTH_LONG).show();

            et_cantidad.setText(String.valueOf(valorActual));
            int SubT= valorActual * Integer.parseInt(prod_carrito.producto.getPrecio());
            subtotal.setText("$ "+String.valueOf(SubT));

            IB_restarCantidad.setColorFilter(Color.argb(0,0,0,0));





        }
    });

    //pongo a la escucha el IMAGE BUTTON TILDE
    IB_agregarcarr.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            try
            {
                int cantidad = Integer.parseInt(et_cantidad.getText().toString());
                Carrito carrito =   Carrito.obtenerInstancia();
                if(cantidad>0){
                    carrito.ModificarElementoDelCarrito(position,cantidad,adaptador,frag_carrito);
                    Toast.makeText(contexto,"Se modificó el ítem",Toast.LENGTH_LONG).show();
                    Cerrar_alertCarrito();
                }
                else{
                    Toast.makeText(contexto,"No puede modificar el carrito con productos con 0 unidades",Toast.LENGTH_LONG).show();
                }
            } catch (NumberFormatException nfe){
                Toast.makeText(contexto,"Elemento no numérico",Toast.LENGTH_LONG).show();
            }



            //carrito.AgregarAlCarrito(CargarProducto(cantidad,Prod));

        }
    });


        StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(politica);

    //codmadre = prod_carrito.producto.getCodigo();
    //configuracion.Configuracion(contexto);



    }

    public void Mostrar_alertCarrito()
    {
        dialogo.show();

    }

    private void Cerrar_alertCarrito()
    {

        dialogo.dismiss();

    }






}
