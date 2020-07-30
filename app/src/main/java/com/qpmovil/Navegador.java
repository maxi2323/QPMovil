package com.qpmovil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;



import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;

public class Navegador extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    FragmentManager FG = getSupportFragmentManager();
    Fragment FragmentoActual = null;
    private Context context;
    alert_salir dialogSalir;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        setContentView(R.layout.nav_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = findViewById(R.id.barra_principal);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);


        navigationView = findViewById(R.id.NV_pantalla_lateral);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        LlamarHome();






    }



    private void LlamarHome()
    {
        FragmentoActual  = new Fragment_Home();


        if(FragmentoActual !=null)
        {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.escenario,FragmentoActual).commit();
            SetCheckeable();
        }


    }

    private void SetCheckeable()
    {

        Menu m = navigationView.getMenu();
        MenuItem item = m.getItem(0);
        item.setChecked(true);
    }


    @Override
    public void onBackPressed() {
        FragmentManager Frag = getSupportFragmentManager();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.NV_pantalla_lateral);
        Menu menu = navigationView.getMenu();


        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);


        }else {
            if (FragmentoActual instanceof Fragment_Home)
            {
                super.onBackPressed();
            }else
            {
                LlamarHome();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.configuracion) {
            FragmentoActual = new Configuracion();
            FG.beginTransaction().replace(R.id.escenario, FragmentoActual,"Configuracion").commit(); //Con esto llama a la
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.configuracion:
//                FragmentoActual = new Fragment_datosConexion();
//                FG.beginTransaction().replace(R.id.escenario, FragmentoActual,"Fragment_datosConexion").commit(); //Con esto llama a la
//                return true;
//
//            default:
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item);
//
//        }
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();


        //   FragmentManager FG = getSupportFragmentManager();
        if (id == R.id.Home)
        {
            FragmentoActual = new Fragment_Home();
            FG.beginTransaction().replace(R.id.escenario,FragmentoActual,"Fragment_home").commit(); //Con esto llama a la

        } else if (id == R.id.codBarra)
        {
            FragmentoActual = new Fragment_ConsultaItem();
            FG.beginTransaction().replace(R.id.escenario, FragmentoActual,"Fragment_lecbarra").commit(); //Con esto llama a la

        } else if (id == R.id.Stock)
        {
            FragmentoActual = new Fragment_Busqueda();
            FG.beginTransaction().replace(R.id.escenario, FragmentoActual,"Fragment_stock").commit(); //Con esto llama a la
        }
        else if (id == R.id.DatosConexion)
        {
            FragmentoActual = new Fragment_datosConexion();
            FG.beginTransaction().replace(R.id.escenario, FragmentoActual,"Fragment_datosConexion").commit(); //Con esto llama a la
        }
        else  if (id == R.id.Carrito)
        {
            FragmentoActual = new Fragment_carrito();
            FG.beginTransaction().replace(R.id.escenario, FragmentoActual,"Fragment_Carrito").commit(); //Con esto llama a la
        }
        else  if (id == R.id.Promocion)
        {
            FragmentoActual = new Fragment_Promociones();
            FG.beginTransaction().replace(R.id.escenario, FragmentoActual,"Fragment_Promociones").commit(); //Con esto llama a la
        }
        else if (id == R.id.salir)
        {   /////Dialog parala confirmacion de cerrar la APP
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Importante");
            dialogo1.setMessage("¿Desea cerrar la aplicacion?");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    aceptar();
                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    //cancelar();
                }
            });
            dialogo1.show();
        }   /////Fin del Dialog para la confirmacion de cerrar la APP

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void aceptar() {
        finish();
    }


}
