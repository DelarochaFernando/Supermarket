package com.fernando.delarocha.supermarket;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.util.ArrayList;

public class LaunchActivity extends AppCompatActivity {

    private long result;
    private DBAdapter dbAdapter;
    private  ArrayList<Producto> ListProd;
    private Activity activity;
    private Toolbar toolbar;
    private Tools tools;
    private boolean catalogsInserted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        dbAdapter = new DBAdapter(this);
        catalogsInserted = false;
        tools = new Tools(this);



        try {
            catalogsInserted = areCatalogsInserted();
            if(catalogsInserted){
                Intent intent = new Intent(LaunchActivity.this, Pasillos.class);
                startActivity(intent);
            }else{
                tools.setStringPreference(tools.PRODS_SHOP_CART,"0");
                otherinsertProductosDB(LaunchActivity.this);

            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private boolean areCatalogsInserted(){
        boolean productosInsertados = false;
        try{
            int count = 0;
            Cursor c;
            dbAdapter.openToRead();
            c = dbAdapter.catalogosInsertados();
            if(c!=null){
                count = c.getCount();
                if(count>0){
                    productosInsertados = true;
                }else {
                    productosInsertados = false;
                }
            }else{
                productosInsertados = false;
            }
        }catch (Exception e){
            e.printStackTrace();
            productosInsertados = false;
        }finally {
            dbAdapter.close();
            return productosInsertados;
        }
    }

    private void otherinsertProductosDB(final Activity vity) throws XmlPullParserException {

        Thread thread = new Thread(){
            @Override
            public void run() {

                try{
                    //looper.prepare();
                    Resources res = vity.getResources();
                    ArrayList<Integer> xmlIdList = new ArrayList<Integer>();
                    xmlIdList.add(R.xml.bebidas);
                    xmlIdList.add(R.xml.frutas);
                    xmlIdList.add(R.xml.verduras);
                    xmlIdList.add(R.xml.cereales);
                    int listlentgth;
                    result = 0;
                    String eventName = "";
                    dbAdapter.openToRead();
                    for(int i=0; i<xmlIdList.size();i++) {

                        XmlResourceParser myparser = res.getXml(xmlIdList.get(i));
                        int eventType = myparser.getEventType();
                        Producto producto = null;
                        ListProd = new ArrayList<Producto>();
                        listlentgth = 0;

                        while (eventType != XmlPullParser.END_DOCUMENT) {

                            switch (eventType) {
                                case XmlPullParser.START_TAG:
                                    eventName = myparser.getName();
                                    if(eventName.equals("producto")){
                                        producto = new Producto();
                                    }
                                    break;
                                case XmlPullParser.END_TAG:
                                    eventName = myparser.getName();
                                    if(eventName.equals("producto")){
                                        ListProd.add(listlentgth, producto);
                                        producto = null;
                                        listlentgth++;
                                    }
                                    break;
                                case XmlPullParser.TEXT:
                                    if (eventName.equals("idpasillo")) {
                                        producto.setIdPasillo(myparser.getText());
                                    }
                                    if (eventName.equals("idproducto")) {
                                        producto.setIdProducto(myparser.getText());
                                    }
                                    if (eventName.equals("precio")) {
                                        producto.setPrecio(myparser.getText());
                                    }
                                    if (eventName.equals("desc")) {
                                        producto.setDesc(myparser.getText());
                                    }
                                    break;
                            }
                            eventType = myparser.next();
                        }

                        result = dbAdapter.insertarProductos(ListProd);

                    }
                    /*if(result>0){
                        Log.i("InsertCatalogoProductos", "Insertado Exitoso");
                    }*/

                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    dbAdapter.close();
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", 1);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                }

            }
        };
        thread.start();
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int id = msg.getData().getInt("id");
            switch (id){
                case 0:
                    /*if(catalogsInserted){
                        Log.i("InsertCatalogoProductos", "Catalogos Ya Insertados");
                        Intent pasillosIntent = new Intent(LaunchActivity.this, Pasillos.class);
                        startActivity(pasillosIntent);
                    }else {
                        try {
                            otherinsertProductosDB(activity);
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }*/break;
                case 1:
                    if(result>0){
                        Log.i("InsertCatalogoProductos", "Insertado Exitoso");
                        Intent pasillosIntent = new Intent(LaunchActivity.this, Pasillos.class);
                        startActivity(pasillosIntent);
                    }
                    break;
            }

        }
    };
}
