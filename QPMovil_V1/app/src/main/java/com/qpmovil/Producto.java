package com.qpmovil;

import android.annotation.SuppressLint;

public class Producto {

    private String descripcion;
    private String codigo;
    private String stock;
    private String talle;
    private String color;
    private String precio;
    private String NombreCOL;
    private String codItm;



    private long id;

    public String getNombreCOL() {
        return NombreCOL;
    }

    public void setNombreCOL(String nombreCOL) {
        NombreCOL = nombreCOL;
    }

    public  long getId(){return id;}

    @SuppressLint("DefaultLocale")
    public String getPrecio() {
        int convertir =(int) (Double.parseDouble(precio));
        precio = String.valueOf(convertir);
        this.precio = precio;
        return precio;
    }

    public String getCodItm() {    return codItm; }

    public void setCodItm(String codItm) {
        this.codItm = codItm;
    }
    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getDescripcion() { return descripcion; }

    public String getTalle() { return talle; }

    public void setTalle(String talle) { this.talle = talle; }

    public String getColor() { return color; }

    public void setColor(String color) { this.color = color; }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    public String getCodigo()
    {
        return codigo;
    }

    public void setCodigo(String marca)
    {
        this.codigo = marca;
    }


    public String getStock()
    {
//        Integer convertir = Integer.parseInt(stock);
//        stock = convertir.toString();

        return stock;
    }

    public void setStock(String stock)
    {

        int convertir =(int) (Double.parseDouble(stock));
        stock = String.valueOf(convertir);
        this.stock = stock;
    }

    public Producto() {
        super();
    }

       @Override
    public String toString()
       {
        return super.toString();
       }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }
}
