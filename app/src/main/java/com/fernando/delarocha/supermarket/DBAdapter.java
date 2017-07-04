package com.fernando.delarocha.supermarket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by jmata on 12/06/2017.
 */

public class DBAdapter{

    Context ctx;
    private DbHelper helper;
    private static String DB_NAME = "supermarket";
    private static int DB_VERSION = 1;
    private static SQLiteDatabase db;
    private Pasillos mPasillo;
    private static String CREATE_Productos =

            "CREATE TABLE productos (_id integer primary key autoincrement,"+
                    "idPasillo TEXT null,"+
                    "idProducto TEXT null,"+
                    "desc TEXT null,"+
                    "precio TEXT null);";

    public DBAdapter(Context context){
        helper = new DbHelper(context);
        this.ctx = context;
        mPasillo = new Pasillos();
    }

    public DBAdapter open()throws SQLiteException{
        try{
            db = helper.getWritableDatabase();

        }catch (SQLiteException ex){
            db = helper.getWritableDatabase();
        }
        return this;
    }

    public DBAdapter openToRead()throws SQLiteException{

        try{

            db = helper.getReadableDatabase();
        }catch(SQLiteException e){
            db = helper.getReadableDatabase();

        }
        return this;
    }

    public void close(){
        db.close();
    }

    public long insertarProductos(ArrayList<Producto> productos) {
        int cantProd = productos.size();
        long res = 0;
        try {
            //db.execSQL("DELETE FROM productos;");
                if (cantProd > 0) {

                    ContentValues values = new ContentValues();
                    for (int i = 0; i < cantProd; i++) {
                        values.put("idPasillo", productos.get(i).getIdPasillo());
                        values.put("idProducto", productos.get(i).getIdProducto());
                        values.put("desc", productos.get(i).getDesc());
                        values.put("precio", productos.get(i).getPrecio());
                        res = db.insert("productos", null, values);
                    }

                } else {
                    return 0;
                }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        finally {
            return res;
        }
    }

    public Cursor catalogosInsertados(){
        Cursor c = null;
        try{
            c = db.rawQuery("SELECT idPasillo, idProducto, desc, precio FROM productos",null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return c;
    }

    public ArrayList<Producto> getProductosByPasillo(String pasillo){

        int count = 0;
        int i = 0;
        Cursor c = null;
        Producto pdto;
        ArrayList<Producto> listaProductos = new ArrayList<Producto>();
        try{
            c = db.rawQuery("SELECT idPasillo, idProducto, desc, precio FROM productos WHERE idPasillo='"+pasillo+"'",null);
            if(c!=null){
                count = c.getCount();
                if(count>0){
                    if(c.moveToFirst()){
                        do {
                            pdto = new Producto();
                            pdto.setIdPasillo(c.getString(0));
                            pdto.setIdProducto(c.getString(1));
                            pdto.setDesc(c.getString(2));
                            pdto.setPrecio(c.getString(3));
                            listaProductos.add(i,pdto);
                            i++;
                        }while (c.moveToNext());
                    }


                }else{ listaProductos = null;}

            }else{
                listaProductos = null;
            }
            return listaProductos;
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaProductos;
    }

    private static class DbHelper extends SQLiteOpenHelper {


    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_Productos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(newVersion == oldVersion){

        }else{
            db.execSQL("DROP TABLE productos IF EXISTS");
            db.execSQL(CREATE_Productos);
        }
    }

    }
}
