package com.fernando.delarocha.supermarket;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Cereales extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerCereales;
    private TextView barTextCount;
    private ArrayList<Producto> listCerealesContent;
    private int checkBoxclicked;
    private DBAdapter dbAdapter;
    private Tools tools;
    private CerealAdapter cAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cereales);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Pasillo C - CEREALES");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tools = new Tools(this);
        dbAdapter = new DBAdapter(this);
        listCerealesContent = getCerealesFromDB("D");
        cAdapter = new CerealAdapter(listCerealesContent);
        barTextCount = new TextView(this);

        checkBoxclicked = Integer.parseInt(tools.getStringPreference(Tools.PRODS_SHOP_CART));
        recyclerCereales = (RecyclerView)findViewById(R.id.recycler);
        recyclerCereales.setAdapter(cAdapter);
        recyclerCereales.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));


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

    public ArrayList<Producto> getCerealesFromDB(String pasillo){
        ArrayList<Producto> arraylist = new ArrayList<Producto>();
        dbAdapter.openToRead();
        arraylist = new ArrayList<Producto>();
        try {
            arraylist = dbAdapter.getProductosByPasillo(pasillo);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dbAdapter.close();
            return arraylist;
        }

    }

    public class CerealAdapter extends RecyclerView.Adapter<Cereales.CerealAdapter.CerealVHolder>{

        private ArrayList<Producto> pdtos;
        private Producto producto;

        public CerealAdapter(ArrayList<Producto> p){
            this.pdtos = p;
        }

        @Override
        public CerealVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.item_info, null);
            CerealVHolder cerealholder = new CerealVHolder(v);
            return cerealholder;
        }

        @Override
        public void onBindViewHolder(Cereales.CerealAdapter.CerealVHolder holder, int position) {

            producto = pdtos.get(position);
            holder.firstLine.setText("Producto: "+producto.getDesc());
            holder.secondLine.setText("Pasillo: "+producto.getIdPasillo());
            holder.thirdLine.setText("Precio: $"+producto.getPrecio());
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
            return pdtos.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class CerealVHolder extends RecyclerView.ViewHolder{

            public TextView firstLine, secondLine, thirdLine;
            public CheckBox checkSelect;

            public CerealVHolder(View itemView) {
                super(itemView);
                firstLine = (TextView)itemView.findViewById(R.id.firstLine);
                secondLine = (TextView)itemView.findViewById(R.id.secondLine);
                thirdLine = (TextView)itemView.findViewById(R.id.ThirdLine);
                checkSelect = (CheckBox)itemView.findViewById(R.id.checkSelect);
            }
        }
    }
}
