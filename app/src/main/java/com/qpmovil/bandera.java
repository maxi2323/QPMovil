package com.qpmovil;



/* Utilizo esta clase para manejar los diferentes errores que se pueden producir durante una consulta
* Error 1=Error en la conexion
* Error 2=Error en la consulta
* Error 3=No se puede generar la conexion nuevamente*/


public class bandera {
    int valor;

    public bandera(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
}
