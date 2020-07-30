package com.qpmovil;

import android.util.Log;
import android.widget.BaseAdapter;


import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public  class Carrito {

   private float total;
    private String cajero;
    private int idPedido;
    private  List<Producto_Carrito>  listProductos =new ArrayList<Producto_Carrito>();
    public  static   Carrito InstanciaCarrito ;

    public static Carrito obtenerInstancia()
    {
        if(InstanciaCarrito == null)
        {
            InstanciaCarrito = new Carrito();
        }


        return InstanciaCarrito;

    }


    private Carrito() {

//        this.total = total;
//        this.cajero = cajero;
//        this.idPedido = idPedido;




    }
    public List<Producto_Carrito> ListarProduc_carrito()
    {

    return listProductos;
    }

    public void AgregarAlCarrito(Producto_Carrito producto_carr)
    {
        Producto_Carrito aux;
        int i=0;
        int pos = 0;
        int cant, band=999;


        ListIterator it = listProductos.listIterator();
        while(it.hasNext() && band==999 ) {
            aux = listProductos.get(i);

            if(aux.producto.getCodigo().equals(producto_carr.producto.getCodigo())) {
                band=i;
                pos=i;
            }
            else {
                it.next();
                i++;
            }
        }

        if( band!=999 ){
            cant = listProductos.get(pos).getCantidad();
            producto_carr.setCantidad(producto_carr.getCantidad()+cant);
            listProductos.set(pos, producto_carr);
            Log.i("info","Modifique la cantidad de un item ya cargado, no lo ducplico");
        }
        else {
            Log.i("info", "Llegue al metodo agregar carrito");
            listProductos.add(producto_carr);
        }
    }

    public void ModificarElementoDelCarrito(int i, int cantidad, BaseAdapter adaptador, Fragment_carrito fragment_carrito)
    {

        listProductos.get(i).setCantidad(cantidad);
        adaptador.notifyDataSetChanged();
        fragment_carrito.CalcularTotal(listProductos);



    }



}