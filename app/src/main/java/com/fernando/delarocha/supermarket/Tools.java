package com.fernando.delarocha.supermarket;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jmata on 29/06/2017.
 */

public class Tools {

    private Context context;
    private DBAdapter adapter;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ArrayList<Producto> lista;
    public static String PRODS_SHOP_CART = "productosEnCarrito";

    public Tools (Context ctx){
        this.context = ctx;
        adapter = new DBAdapter(ctx);
        this.lista = new ArrayList<Producto>();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setStringPreference(String keyname, String value){
        editor = preferences.edit();
        editor.putString(keyname, value);
        editor.commit();
    }

    public void saveArrayProdSelected(ArrayList<Producto> list){
        lista = list;
    }

    public ArrayList<Producto> getArrayProdSelected(){
        return lista;
    }

    public String getStringPreference(String keyname){
        return preferences.getString(keyname,null);
    }
}
