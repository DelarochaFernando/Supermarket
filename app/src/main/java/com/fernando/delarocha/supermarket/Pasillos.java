package com.fernando.delarocha.supermarket;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;

public class Pasillos extends AppCompatActivity {

    RecyclerView recyclerPasillo;
    String[] ArrayPasillos, tituloPasillo;
    ArrayList<Producto> ListProd;
    private Toolbar toolbar;
    private Tools tools;
    private TextView barTextCount;
    long result;
    private int checkBoxclicked;
    DBAdapter dbAdapter;
    XmlResourceParser parser;
    private XmlPullParserFactory xmlFactoryObject;
    //Producto producto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasillos);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SuperMarket");

        dbAdapter = new DBAdapter(this);
        tools = new Tools(this);
        barTextCount = new TextView(this);
        checkBoxclicked = Integer.parseInt(tools.getStringPreference(tools.PRODS_SHOP_CART));
        ArrayPasillos = new String[]{"A","B","C","D","E"};
        tituloPasillo = new String[]{"Bebidas","Frutas","Verduras","Cereales","Limpieza"};

        recyclerPasillo = (RecyclerView)findViewById(R.id.recyclerPasillo);
        pasilloAdapter adapter = new pasilloAdapter();
        recyclerPasillo.setAdapter(adapter);
        recyclerPasillo.setLayoutManager(new GridLayoutManager(this, 3));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.toolbar_design, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_toolbar);
        MenuItemCompat.setActionView(menuItem,R.layout.shopp_cart_count);
        RelativeLayout rl = (RelativeLayout)MenuItemCompat.getActionView(menuItem);
        barTextCount = (TextView)rl.findViewById(R.id.count_textview);
        barTextCount.setText(String.valueOf(checkBoxclicked));
        return true;
    }

    public class pasilloAdapter extends RecyclerView.Adapter<pasilloAdapter.PasilloViewHolder>{

        LayoutInflater mInflater;
        @Override
        public PasilloViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = mInflater.inflate(R.layout.pasillo_layout,null);
            PasilloViewHolder pasilloholder = new PasilloViewHolder(v);

            return pasilloholder;
        }

        @Override
        public void onBindViewHolder(pasilloAdapter.PasilloViewHolder holder, final int position) {

            int index = position;
            String letra = ArrayPasillos[position];
            String descripcion = tituloPasillo[position];

            holder.letraPasillo.setText(letra);
            holder.descPasillo.setText(descripcion);

            switch (position){
                case 0:
                    //Bebidas
                    holder.letraPasillo.setTextColor(Color.BLUE);
                    holder.descPasillo.setTextColor(Color.BLUE);
                    break;
                case 1:
                    //Frutas
                    holder.letraPasillo.setTextColor(Color.RED);
                    holder.descPasillo.setTextColor(Color.RED);
                    break;
                case 2:
                    //Verduras
                    holder.letraPasillo.setTextColor(Color.GREEN);
                    holder.descPasillo.setTextColor(Color.GREEN);
                    break;
                case 3:
                    //Cereales
                    holder.letraPasillo.setTextColor(Color.YELLOW);
                    holder.descPasillo.setTextColor(Color.YELLOW);
                    break;
                case 4:
                    //Limpieza
                    holder.letraPasillo.setTextColor(Color.MAGENTA);
                    holder.descPasillo.setTextColor(Color.MAGENTA);
                    break;
            }
            holder.linearPasillo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(position){
                        case 0:
                            Intent goPasilloA = new Intent(Pasillos.this,Bebidas.class);
                            startActivity(goPasilloA);
                            break;
                        case 1:
                            Intent goPasilloB = new Intent(Pasillos.this,Frutas.class);
                            startActivity(goPasilloB);
                            break;
                        case 2:
                            Intent goPasilloC = new Intent(Pasillos.this, Verduras.class);
                            startActivity(goPasilloC);
                            break;
                        case 3:
                            Intent goPasilloD = new Intent(Pasillos.this, Cereales.class);
                            startActivity(goPasilloD);
                            break;
                    }
                }
            });


        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return ArrayPasillos.length;
        }

    public class PasilloViewHolder extends RecyclerView.ViewHolder{

        private TextView letraPasillo;
        private TextView descPasillo;
        private LinearLayout linearPasillo;

        public PasilloViewHolder(View itemView){
            super(itemView);
            linearPasillo = (LinearLayout)itemView.findViewById(R.id.linearPasillo);
            letraPasillo = (TextView) itemView.findViewById(R.id.letraPasillo);
            descPasillo = (TextView) itemView.findViewById(R.id.descPasillo);

        }
    }
    }

    /*public class Bebidas extends Producto{

        public Bebidas(String idPas, String idPdto, String desc, String prec){
            //prod = new Producto();
            this.setIdPasillo(idPas);
            this.setIdProducto(idPdto);
            this.setDesc(desc);
            this.setPrecio(prec);
        }

        public Bebidas(){

        }
    }

    public class Frutas extends Producto{

        public Frutas(String idPas, String idPdto, String desc, String prec){
            this.setIdPasillo(idPas);
            this.setIdProducto(idPdto);
            this.setDesc(desc);
            this.setPrecio(prec);
        }

        public Frutas(){

        }
    }

    public class Verduras extends Producto{

        public Verduras(String idPas, String idPdto, String desc, String prec){
            this.setIdPasillo(idPas);
            this.setIdProducto(idPdto);
            this.setDesc(desc);
            this.setPrecio(prec);
        }

        public Verduras(){

        }
    }

    public class Cereales extends Producto{

        public Cereales(String idPas, String idPdto, String desc, String prec){
            this.setIdPasillo(idPas);
            this.setIdProducto(idPdto);
            this.setDesc(desc);
            this.setPrecio(prec);
        }

        public Cereales(){

        }
    }*/

}
