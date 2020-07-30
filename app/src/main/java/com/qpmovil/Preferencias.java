package com.qpmovil;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencias
{
    private SharedPreferences preference;


    public Preferencias(Context contexto)
    {
        preference = contexto.getSharedPreferences("datos", Context.MODE_PRIVATE);
    }

    public String getSrv()
    {
        return preference.getString("Servidor","");
    }

    public void setSrv(String srv)
    {

        SharedPreferences.Editor obj_editor = preference.edit();
        obj_editor.putString("Servidor",srv);

        obj_editor.commit();
    }

    public String getInst() {
        return preference.getString("Instancia","");
    }

    public void setInst(String inst) {
        SharedPreferences.Editor obj_editor = preference.edit();
        obj_editor.putString("Instancia", inst);
        obj_editor.commit();
    }

    public String getUsu() {
        return  preference.getString("Usuario","");
    }

    public void setUsu(String usu) {
        SharedPreferences.Editor obj_editor = preference.edit();
        obj_editor.putString("Usuario", usu);
        obj_editor.commit();
    }

    public String getPass() {
        return preference.getString("password","");
    }

    public void setPass(String pass) {
        SharedPreferences.Editor obj_editor = preference.edit();
        obj_editor.putString("password", pass);
        obj_editor.commit();
    }

    public String getBase() {
        return preference.getString("Base","");
    }

    public void setBase(String base) {
        SharedPreferences.Editor obj_editor = preference.edit();
        obj_editor.putString("Base", base);
        obj_editor.commit();
    }

}
