package com.qpmovil;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.Iterator;
import java.util.List;


public class Fragment_carrito extends Fragment {

    View menu;
    ListView mostrar;
    Button btn_finalizar, btn_vaciarCarrito;
    List<Producto_Carrito> lstProd_carr;
    ImageView Imag_cara;
    TextView Mensaje_noDatos;
    TextView Total;
    TextView textoTotal;
    View lineaDivisoria;
    Carrito carrito;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    menu = inflater.inflate(R.layout.fragment_carrito, container, false);


        mostrar = menu.findViewById(R.id.lst_mostrar_pedido);

        btn_vaciarCarrito = menu.findViewById(R.id.btn_vaciarCarrito);
        Imag_cara = menu.findViewById(R.id.Imagen_carita);
        Mensaje_noDatos = menu.findViewById(R.id.TV_nohayDatos);
        Total = menu.findViewById(R.id.TV_total);
        textoTotal = menu.findViewById(R.id.TV_textoTOTAL);
        lineaDivisoria  = menu.findViewById(R.id.dv_lineaDivisora);

         carrito = Carrito.obtenerInstancia();

        btn_vaciarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FinalizarCarrito(carrito);
                carritoVacio();
            }
        });




    if (carrito.ListarProduc_carrito().size() > 0) {
        lstProd_carr = null;
        lstProd_carr = carrito.ListarProduc_carrito();

        final formato_ListView_pedidoCarrito adaptador = new formato_ListView_pedidoCarrito(getActivity(), lstProd_carr,this);

        mostrar.setAdapter(adaptador);




        CalcularTotal(carrito.ListarProduc_carrito());

        //======================

    }
    else
        {
            carritoVacio();
        }


    return menu;
    }

    public double CalcularTotal(List<Producto_Carrito> lista)
    {
        Double total = 0.0 ;

        Iterator<Producto_Carrito> iterador = lista.iterator();

        while (iterador.hasNext())
        {

            Producto_Carrito prod_carr = iterador.next();


            Double precio = Double.valueOf(prod_carr.producto.getPrecio());


            total = total + (prod_carr.getCantidad() * precio)  ;
            Total.setText("$" + String.valueOf(total));



        }

        return total;
    }
    public  void MostrarTotal()
    {
        Total.setText(String.valueOf(CalcularTotal(carrito.ListarProduc_carrito())));

    }

        private void FinalizarCarrito(Carrito carrito)
        {
        carrito.ListarProduc_carrito().clear();
        }

        public   void carritoVacio(){
  //          btn_finalizar.setVisibility(View.GONE);
            Total.setVisibility(View.GONE);
            mostrar.setVisibility((View.GONE));
            lineaDivisoria.setVisibility(View.GONE);
            Imag_cara.setVisibility(View.VISIBLE);
            Mensaje_noDatos.setVisibility(View.VISIBLE);
            textoTotal.setVisibility(View.GONE);

        }



}

