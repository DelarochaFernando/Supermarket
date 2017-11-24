package com.fernando.delarocha.supermarket;

import android.database.Cursor;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Limpieza extends AppCompatActivity {

    private RecyclerView recyclerLimp;
    private Toolbar toolbar;
    private DBAdapter dbAdapter;
    private Tools tools;
    private ArrayList<Producto> listArtLimp;
    private TextView barTextCount;
    private int checkBoxclicked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limpieza);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Pasillo E - Articulos Limpieza");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tools = new Tools(this);
        dbAdapter = new DBAdapter(this);
        listArtLimp = new ArrayList<Producto>();
        listArtLimp = getProdLimpiezaFromDB("E");
        recyclerLimp = (RecyclerView)findViewById(R.id.recyclerLimp);
        LimpiezaAdapter limpiezaAdapter = new LimpiezaAdapter(listArtLimp);
        recyclerLimp.setAdapter(limpiezaAdapter);
        recyclerLimp.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        checkBoxclicked = Integer.parseInt(tools.getStringPreference(tools.PRODS_SHOP_CART));

    }

    public ArrayList<Producto> getProdLimpiezaFromDB(String pasillo){

        dbAdapter.openToRead();
        ArrayList<Producto> listaProds = new ArrayList<Producto>();
        try{
            listaProds = dbAdapter.getProductosByPasillo(pasillo);
            return listaProds;

        }catch (Exception e){
            e.printStackTrace();
            listaProds = null;
        }finally {
            dbAdapter.close();
            return listaProds;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.toolbar_design,menu);
        MenuItem item = menu.findItem(R.id.cart_toolbar);
        MenuItemCompat.setActionView(item,R.layout.shopp_cart_count);
        RelativeLayout rl = (RelativeLayout)MenuItemCompat.getActionView(item);
        barTextCount = (TextView)rl.findViewById(R.id.count_textview);
        barTextCount.setText(String.valueOf(checkBoxclicked));
        return true;
    }

    public class LimpiezaAdapter extends RecyclerView.Adapter<Limpieza.LimpiezaAdapter.LimPVHolder>{

        ArrayList<Producto> lista;
        Producto producto;

        public LimpiezaAdapter(ArrayList<Producto> list){

            this.lista = list;
        }

        @Override
        public LimPVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_info, null);
            LimPVHolder vh = new LimPVHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(LimPVHolder holder, int position) {

            producto = lista.get(position);
            holder.firstLine.setText("Producto: "+producto.getDesc());
            holder.secondline.setText("Pasillo: "+producto.getIdPasillo());
            holder.thirdline.setText("Precio: $"+producto.getPrecio());
            //holder.img.setImageBitmap(producto.getImg());
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
            return lista.size();
        }

        public class LimPVHolder extends RecyclerView.ViewHolder{

            private TextView firstLine;
            private TextView secondline;
            private CheckBox checkSelect;
            private TextView thirdline;

            public LimPVHolder(View itemView) {
                super(itemView);

                firstLine = (TextView)itemView.findViewById(R.id.firstLine);
                secondline = (TextView)itemView.findViewById(R.id.secondLine);
                thirdline = (TextView)itemView.findViewById(R.id.ThirdLine);
                checkSelect = (CheckBox)itemView.findViewById(R.id.checkSelect);
            }
        }
    }
}
