package com.encuestas;

import java.util.Vector;

public class Datos {

    private int id;
    private String nombre;
    private int tel;
    private String dir;
    private String intra;
    private String intranet;
    private String acceso;
    private String imagen;
    private String mapa;

    private int pos = 0;

    public Datos(String nombre, int telefono, String direccion, String intra, String intranet, String acceso, String imagen, String mapa, int id) {
        this.nombre = nombre;
        this.tel = telefono;
        this.dir = direccion;
        this.intra = intra;
        this.intranet = intranet;
        this.acceso = acceso;
        this.imagen = imagen;
        this.mapa = mapa;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTel() {
        return tel;
    }

    public String getDir() {
        return dir;
    }

    public String getIntra() {
        return intra;
    }

    public String getIntranet() {
        return intranet;
    }

    public String getAcceso() {
        return acceso;
    }
    public String getImagen(){
        return imagen;
    }

    public String getMapa(){
        return mapa;
    }
    
    public void setImagen(String imagen){
        this.imagen=imagen;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setIntra(String intra) {
        this.intra = intra;
    }

    public void setIntranet(String intranet) {
        this.intranet = intranet;
    }

    public void setAcceso(String acceso) {
        this.acceso = acceso;
    }
    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getId() {
            return id;
    }

    public String toString() {
            return getNombre() + " -- " + getMapa();
    }
}
