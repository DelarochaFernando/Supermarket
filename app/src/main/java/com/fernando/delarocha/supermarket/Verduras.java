package com.fernando.delarocha.supermarket;

import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Verduras extends AppCompatActivity {

    private Toolbar toolbar;
    private DBAdapter dbAdapter;
    private RecyclerView listaProductos;
    private ArrayList<Producto> listVerduras;
    private TextView barTextCount;
    private Tools tools;
    private int checkBoxclicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verduras);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        listaProductos = (RecyclerView)findViewById(R.id.recyclerVerduras);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pasillo C - VERDURAS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbAdapter = new DBAdapter(this);
        listVerduras = new ArrayList<Producto>();
        barTextCount = new TextView(this);
        tools = new Tools(this);
        checkBoxclicked = Integer.parseInt(tools.getStringPreference(tools.PRODS_SHOP_CART));
        listVerduras = getFrutasFromDB("C");
        VerdurasAdapter adapter = new VerdurasAdapter(listVerduras);
        listaProductos.setAdapter(adapter);
        listaProductos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

    }

    public ArrayList<Producto> getFrutasFromDB(String pasillo){
        dbAdapter.openToRead();
        listVerduras = new ArrayList<Producto>();
        try {
            listVerduras = dbAdapter.getProductosByPasillo(pasillo);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dbAdapter.close();
            return listVerduras;
        }

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

    public class VerdurasAdapter extends RecyclerView.Adapter<VerdurasAdapter.VerdurasViewHolder>{

        private ArrayList<Producto> pdtosArray;
        private Producto pdto;

        public VerdurasAdapter(ArrayList<Producto> productos){
            this.pdtosArray = productos;
        }

        @Override
        public VerdurasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_info, null);
            VerdurasViewHolder holder = new VerdurasViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(VerdurasViewHolder holder, int position) {

            pdto = pdtosArray.get(position);
            holder.firstline.setText("Producto: "+pdto.getDesc());
            holder.secondline.setText("Pasillo: "+pdto.getIdPasillo());
            holder.thirdline.setText("Precio: $"+pdto.getPrecio());
            holder.checkSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        checkBoxclicked++;
                        barTextCount.setText(String.valueOf(checkBoxclicked));
                        tools.setStringPreference(tools.PRODS_SHOP_CART, String.valueOf(checkBoxclicked));
                    }else{
                        checkBoxclicked--;
                        barTextCount.setText(String.valueOf(checkBoxclicked));
                        tools.setStringPreference(tools.PRODS_SHOP_CART, String.valueOf(checkBoxclicked));
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return pdtosArray.size();
        }

        public class VerdurasViewHolder extends RecyclerView.ViewHolder{

            private TextView firstline;
            private TextView secondline;
            private CheckBox checkSelect;
            private TextView thirdline;

            public VerdurasViewHolder(View itemView){
                super(itemView);
                firstline = (TextView)itemView.findViewById(R.id.firstLine);
                secondline = (TextView)itemView.findViewById(R.id.secondLine);
                thirdline = (TextView)itemView.findViewById(R.id.ThirdLine);
                checkSelect = (CheckBox)itemView.findViewById(R.id.checkSelect);
            }
        }
    }
}
