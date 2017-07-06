package com.fernando.delarocha.supermarket;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Frutas extends AppCompatActivity {

    private RecyclerView listaProductos;
    private DBAdapter dbAdapter;
    private ArrayList<Producto> listFrutas;
    private Toolbar toolbar;
    private Tools tools;
    private TextView barTextCount;
    private FrutAdapter productAdapter;
    private int checkBoxclicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frutas);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Pasillo B - FRUTAS");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //listaProductos = (ListView)findViewById(R.id.list_item);
        listaProductos = (RecyclerView)findViewById(R.id.recycler);
        listaProductos.setHasFixedSize(true);
        dbAdapter = new DBAdapter(this);
        listFrutas = new ArrayList<Producto>();
        barTextCount = new TextView(this);
        tools = new Tools(this);
        checkBoxclicked = Integer.parseInt(tools.getStringPreference(tools.PRODS_SHOP_CART));
        listFrutas = getFrutasFromDB("B");
        productAdapter = new FrutAdapter(this,listFrutas);
        listaProductos.setAdapter(productAdapter);
        listaProductos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

    }

    public ArrayList<Producto> getFrutasFromDB(String pasillo){
        dbAdapter.openToRead();
        listFrutas = new ArrayList<Producto>();
        try {
            listFrutas = dbAdapter.getProductosByPasillo(pasillo);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dbAdapter.close();
            return listFrutas;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        boolean res = false;
        switch(item.getItemId()){
            case R.id.cart_toolbar:
                dialogSubTotal();
                res = true;
                break;
        }
        return res;
    }

    private void dialogSubTotal(){

        try{
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialogo_subtotal);
            dialog.setCancelable(true);
            TextView textViewTitulo = (TextView) dialog.findViewById(R.id.textViewTitulo);
            RecyclerView recyclerDialog = (RecyclerView)dialog.findViewById(R.id.recyclerDialog);
            Button btnContinuarPago = (Button)dialog.findViewById(R.id.btnContinuarPago);
            dialog.show();
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    public class FrutAdapter extends RecyclerView.Adapter<Frutas.FrutAdapter.ProdViewHolder>{

        //LayoutInflater inflater;
        private ArrayList<Producto> pdtosArray;
        private Producto producto;

        public FrutAdapter(Context ctx, ArrayList<Producto> pdtos){
            //super(ctx,R.layout.item_info, pdtos);
            //inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.pdtosArray = pdtos;
        }

        @Override
        public ProdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_info,null);
            ProdViewHolder pvh = new Frutas.FrutAdapter.ProdViewHolder(view);

            return pvh;
        }

        @Override
        public void onBindViewHolder(Frutas.FrutAdapter.ProdViewHolder holder, int position) {

            producto = pdtosArray.get(position);
            holder.firstline.setText("Producto: "+producto.getDesc());
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
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return pdtosArray.size();
        }

        public  class ProdViewHolder extends RecyclerView.ViewHolder{

            private TextView firstline;
            private TextView secondline;
            private CheckBox checkSelect;
            private TextView thirdline;

            public ProdViewHolder(View itemView) {
                super(itemView);
                firstline = (TextView)itemView.findViewById(R.id.firstLine);
                secondline = (TextView)itemView.findViewById(R.id.secondLine);
                thirdline = (TextView)itemView.findViewById(R.id.ThirdLine);
                checkSelect = (CheckBox)itemView.findViewById(R.id.checkSelect);
            }
        }
    }
}
