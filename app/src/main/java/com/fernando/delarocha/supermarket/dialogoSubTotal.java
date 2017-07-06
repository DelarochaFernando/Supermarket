package com.fernando.delarocha.supermarket;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jmata on 06/07/2017.
 */

public class dialogoSubTotal extends DialogFragment {

    private RecyclerView recyclerDialog;
    private myRecyclerAdapter rAdapter;
    private ArrayList<Producto> prodSelected;
    private Tools tools;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.dialogo_subtotal,container,false);
        TextView textViewTitulo = (TextView) view.findViewById(R.id.textViewTitulo);
        RecyclerView recyclerDialog = (RecyclerView)view.findViewById(R.id.recyclerDialog);
        Button btnContinuarPago = (Button)view.findViewById(R.id.btnContinuarPago);
        context = getActivity().getApplicationContext();
        tools = new Tools(context);
        prodSelected = new ArrayList<Producto>();
        prodSelected = tools.getArrayProdSelected();
        rAdapter = new myRecyclerAdapter(prodSelected);
        recyclerDialog.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    public class myRecyclerAdapter extends RecyclerView.Adapter<myRecyclerAdapter.ViewHolder>{

        ArrayList<Producto> listArticulos;

        public myRecyclerAdapter(ArrayList<Producto> prodsSelected){
          listArticulos = prodsSelected;
        }


        @Override
        public myRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_dialog_items,parent,false);
            myRecyclerAdapter.ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(myRecyclerAdapter.ViewHolder holder, int position) {
            Producto producto = listArticulos.get(position);
                holder.titPrecio.setText(producto.getPrecio());
                holder.titProd.setText(producto.getDesc());
                String pasillo = producto.getIdPasillo();
            if(pasillo.equals("A")){holder.titPasillo.setText("Bebidas");}
            if(pasillo.equals("B")){holder.titPasillo.setText("Frutas");}
            if(pasillo.equals("C")){holder.titPasillo.setText("Verduras");}
            if(pasillo.equals("D")){holder.titPasillo.setText("Cereales");}
        }

        @Override
        public int getItemCount() {
            return listArticulos.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            private CheckBox checkQuitItem;
            private TextView titProd, titPrecio, titPasillo;

            public ViewHolder(View itemView) {
                super(itemView);
                checkQuitItem = (CheckBox) itemView.findViewById(R.id.checkQuitItem);
                titProd = (TextView)itemView.findViewById(R.id.titProd);
                titPasillo = (TextView)itemView.findViewById(R.id.titPasillo);
                titPrecio = (TextView)itemView.findViewById(R.id.titPrecio);
            }
        }
    }
}
