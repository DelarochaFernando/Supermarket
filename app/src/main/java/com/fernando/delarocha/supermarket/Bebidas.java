package com.fernando.delarocha.supermarket;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.fernando.delarocha.supermarket.R.id.btnContinuarPago;

public class Bebidas extends AppCompatActivity {

    private RecyclerView recyclerBebidas;
    private String[] ArrayProductos, precios;
    private String[] pasillos;
    private ArrayList<Integer> images;
    private ArrayList<Producto> listBebidas, prodSeleccionados;
    private ProductAdapter productAdapter;
    private DBAdapter dbAdapter;
    private Toolbar toolbar;
    private TextView barTextCount;
    private int checkBoxclicked = 0;
    public ArrayList<Producto> productos;
    private Tools tools;
    private Context ctx;
    private Dialog dialogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bebidas);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Pasillo A - BEBIDAS");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tools = new Tools(this);
        prodSeleccionados = new ArrayList<Producto>();
        Object preferenceNull = Integer.parseInt(tools.getStringPreference(tools.PRODS_SHOP_CART));

        if(preferenceNull == null){
            checkBoxclicked = 0;
        }else {
            checkBoxclicked = Integer.parseInt(tools.getStringPreference(tools.PRODS_SHOP_CART));
        }

        recyclerBebidas = (RecyclerView)findViewById(R.id.recycler);
        recyclerBebidas.setHasFixedSize(true);
        dbAdapter = new DBAdapter(this);

        productos = new ArrayList<>();
        //precios = new String[]{"$25.00","$13.50","$25.50","$12.50","$35.00"};
        //pasillos = new String[]{"1","4","6","5","7"};
        barTextCount = new TextView(this);
        listBebidas = getBebidasFromDB("A");
        productAdapter = new ProductAdapter(listBebidas);
        recyclerBebidas.setAdapter(productAdapter);
        recyclerBebidas.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        /*listaProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dialogo = new Dialog(Bebidas.this);
                dialogo.setContentView(R.layout.item_info);

                ImageView prodImg = (ImageView)dialogo.findViewById(R.id.icon);
                TextView firstline = (TextView)dialogo.findViewById(R.id.firstLine);
                TextView secondline = (TextView)dialogo.findViewById(R.id.secondLine);
                TextView thirdline = (TextView)dialogo.findViewById(R.id.ThirdLine);
                Button btnBack = (Button)dialogo.findViewById(R.id.btnBack);
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogo.dismiss();
                    }
                });

                String precio = precios[position];
                String pasillo = pasillos[position];
                int data_img = images.get(position);
                String itemValue = String.valueOf(parent.getItemAtPosition(position));

                prodImg.setImageResource(data_img);
                firstline.setText("Articulo: "+itemValue);
                secondline.setText("Pasillo: "+pasillo);
                thirdline.setText("Precio: "+precio);
                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                params.copyFrom(dialogo.getWindow().getAttributes());
                params.width 	= WindowManager.LayoutParams.MATCH_PARENT; // WRAP_CONTENT MATCH_PARENT
                params.height   = WindowManager.LayoutParams.MATCH_PARENT;
                dialogo.show();
                dialogo.getWindow().setAttributes(params);

            }
        });*/
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
        //barTextCount.setText(String.valueOf(checkBoxclicked));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        boolean res = false;
        switch(item.getItemId()){
            case android.R.id.home:
                Intent goPasillos = new Intent(Bebidas.this, Pasillos.class);
                //update selected products extras and send back to Pasillos.
                goPasillos.putExtra("prodSeleccionados", prodSeleccionados);
                startActivity(goPasillos);
                res = true;
                break;
            case R.id.cart_toolbar:
                dialogSubTotal();
                res = true;
            break;

        }
        return res;
    }

    public ArrayList<Producto> getBebidasFromDB(String pasillo){
        dbAdapter.openToRead();
        listBebidas = new ArrayList<Producto>();
        try {
            listBebidas = dbAdapter.getProductosByPasillo(pasillo);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dbAdapter.close();
            return listBebidas;
        }

    }

    private void dialogSubTotal(){

        try{
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialogo_subtotal);
            dialog.setCancelable(true);

            dialog.show();
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProdViewHolder>{

        //LayoutInflater inflater;
        private ArrayList<Producto> pdtosArray;
        private Producto producto;
        private Producto ptoSelect;


        public ProductAdapter(ArrayList<Producto> pdtos){
            //super(ctx,R.layout.item_info, pdtos);
            //inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.pdtosArray = pdtos;

        }

        @Override
        public ProdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_info,null);
            ProdViewHolder pvh = new ProdViewHolder(view);

            return pvh;
        }

        @Override
        public void onBindViewHolder(ProdViewHolder holder, int position) {

            int ultimaPos = 0;
            producto = pdtosArray.get(position);
            //String tag = "Producto: "+producto.getDesc()+"\n"+"Precio: "+producto.getPrecio()+"\n";
            //holder.checkSelect.setText(tag);
            holder.firstline.setText("Producto: " + producto.getDesc());
            holder.secondline.setText("Pasillo: " + producto.getIdPasillo());
            holder.thirdline.setText("Precio: $" + producto.getPrecio());

            holder.checkSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        checkBoxclicked++;
                        barTextCount.setText(String.valueOf(checkBoxclicked));
                        tools.setStringPreference(tools.PRODS_SHOP_CART, String.valueOf(checkBoxclicked));
                        prodSeleccionados.add(producto);
                    }else{
                        checkBoxclicked--;
                        barTextCount.setText(String.valueOf(checkBoxclicked));
                        tools.setStringPreference(tools.PRODS_SHOP_CART, String.valueOf(checkBoxclicked));
                        prodSeleccionados.remove(producto);
                    }
                }
            });


                //Producto productoSelected =
                //holder.checkSelect.setChecked(false);

            //holder.img.setImageBitmap(producto.getImg());
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
