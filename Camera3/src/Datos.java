/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Victor
 */
import java.util.Vector;
import javax.microedition.lcdui.Image;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Victor
 */
public class Datos {

    private String nombre;
    private String tel;
    private String dir;
    private String intra;
    private String intranet;
    private String acceso;
    private Image imagen;
    byte[] pngImage;

    /*-->*/
    private Image mapa;
    /*-->*/
    byte[] pngMap;

    /*-->*/
    public Datos(String nombre, String tel, String dir, String intra, String intranet, String acceso, Image imagen, byte[] pngImage, Image mapa, byte[] pngMap) {
        this.nombre = nombre;
        this.tel = tel;
        this.dir = dir;
        this.intra = intra;
        this.intranet = intranet;
        this.acceso = acceso;
        this.imagen = imagen;
        this.pngImage = pngImage;

        /*-->*/ this.mapa = mapa;
        this.pngMap = pngMap;
    }
    public Datos(String nombre, String tel, String dir, String intra, String intranet, String acceso, byte[] pngImage,byte[] pngMap) {
        this.nombre = nombre;
        this.tel = tel;
        this.dir = dir;
        this.intra = intra;
        this.intranet = intranet;
        this.acceso = acceso;
        this.pngImage = pngImage;

        this.pngMap = pngMap;
    }
    /*-->*/
    public void setMapa(Image mapa) {
        this.mapa = mapa;
    }

    /*-->*/
    public void setPngMap(byte[] pngMap) {
        this.pngMap = pngMap;
    }

    /*-->*/
    public Image getMapa() {
        return mapa;
    }

    /*-->*/
    public byte[] getPngMap() {
        return pngMap;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTel() {
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

    public Image getImagen() {
        return imagen;
    }

    public byte[] getPngImage() {
        return pngImage;
    }

    public void setImagen(Image imagen) {
        this.imagen = imagen;
    }

    public void setPngImage(byte[] pngImage) {
        this.pngImage = pngImage;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTel(String tel) {
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
//    public void setPos(int pos) {
//        this.pos = pos;
//    }
//
//    public void setAficiones(Vector aficiones) {
//        this.aficiones = aficiones;
//    }
}
