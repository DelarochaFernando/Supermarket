package com.fernando.delarocha.supermarket;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by jmata on 29/05/2017.
 */

public class Producto {

    //private Bitmap img;
    private String desc;
    private String precio;
    private String idPasillo;
    private String idProducto;

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getIdPasillo() {
        return idPasillo;
    }

    public void setIdPasillo(String idpasillo) {
        this.idPasillo = idpasillo;
    }

    public Producto(){
        this.idProducto = "";
        this.desc = "";
        this.precio = "";
        this.idPasillo = "";
    }

    public Producto(String idProd, String desc, String precio, String idpasillo){
        this.idProducto = idProd;
        this.desc = desc;
        this.precio = precio;
        this.idPasillo = idpasillo;
    }


}
