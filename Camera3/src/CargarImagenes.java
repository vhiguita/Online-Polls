/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Victor
 */
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.location.*;
import javax.microedition.lcdui.*;
import java.io.*;
import javax.microedition.io.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 *
 * @author Andres Sierra
 */
public class CargarImagenes extends Thread {

    String error = "";
    double latitud = 0;
    double longitud = 0;
    int tel = 0;
    private ComunicacionHttp http;
    MMSMIDlet padre = null;
    private java.io.InputStream is = null;
    private final Command exitCommand;
    String imagen;
    public ImageItem imageItem,imageItem1;

    public CargarImagenes(MMSMIDlet eje) {
        http = new ComunicacionHttp(eje);
        padre = eje;
        exitCommand = new Command("Salir", Command.EXIT, 1);
    }

    public void run() {
        
        HttpConnection hc = null;


        imagen = "persona.png";
        if (imagen != null && imagen.length() != 0) {
            /* Imagen **/
            DataInputStream in2 = null;
            try {
                String url = "http://localhost:8080/SimpleMVC/images/" + imagen;
                hc = (HttpConnection) Connector.open(url);
                int length = (int) hc.getLength();
                byte[] data = null;
                if (length != -1) {
                    data = new byte[length];
                    in2 = new DataInputStream(hc.openInputStream());
                    in2.readFully(data);
                } else {
                    int chunkSize = 512;
                    int index = 0;
                    int readLength = 0;
                    in2 = new DataInputStream(hc.openInputStream());
                    data = new byte[chunkSize];
                    do {
                        if (data.length < index + chunkSize) {
                            byte[] newData = new byte[index + chunkSize];
                            System.arraycopy(data, 0, newData, 0, data.length);
                            data = newData;
                        }
                        readLength = in2.read(data, index, chunkSize);
                        index += readLength;
                    } while (readLength == chunkSize);
                    length = index;
                }
                Image image = Image.createImage(data, 0, length);
                image = createThumbnail(image);
                imageItem = new ImageItem("", image, ImageItem.LAYOUT_TOP | ImageItem.LAYOUT_RIGHT, null);
                padre.img = image;
                padre.pngImage = data;
              

            } catch (IOException ioe) {
//                StringItem stringItem = new StringItem(null, ioe.toString());
//                mProgressForm.append(stringItem);
//                mProgressForm.setTitle("Done.");
            } finally {
                try {
                    if (in2 != null) {
                        in2.close();
                    }
                    if (hc != null) {
                        hc.close();
                    }
                } catch (IOException ioe) {
                }
            }
        }
        imagen = "mapa2.png";
        if (imagen != null && imagen.length() != 0) {
            /* Imagen **/
            DataInputStream in2 = null;
            in2 = null;
            try {
                String url = "http://localhost:8080/SimpleMVC/images/" + imagen;
                hc = (HttpConnection) Connector.open(url);
                int length = (int) hc.getLength();
                byte[] data2 = null;
                if (length != -1) {
                    data2 = new byte[length];
                    in2 = new DataInputStream(hc.openInputStream());
                    in2.readFully(data2);
                } else {
                    int chunkSize = 512;
                    int index = 0;
                    int readLength = 0;
                    in2 = new DataInputStream(hc.openInputStream());
                    data2 = new byte[chunkSize];
                    do {
                        if (data2.length < index + chunkSize) {
                            byte[] newData = new byte[index + chunkSize];
                            System.arraycopy(data2, 0, newData, 0, data2.length);
                            data2 = newData;
                        }
                        readLength = in2.read(data2, index, chunkSize);
                        index += readLength;
                    } while (readLength == chunkSize);
                    length = index;
                }
                Image image = Image.createImage(data2, 0, length);
                image = createThumbnail(image);
                imageItem1 = new ImageItem("", image, ImageItem.LAYOUT_TOP | ImageItem.LAYOUT_RIGHT, null);
                padre.mapa = image;
                padre.pngMap = data2;
              
//                mProgressForm.setTitle("Done.");
            } catch (IOException ioe) {
//                StringItem stringItem = new StringItem(null, ioe.toString());
//                mProgressForm.append(stringItem);
//                mProgressForm.setTitle("Done.");
            } finally {
                try {
                    if (in2 != null) {
                        in2.close();
                    }
                    if (hc != null) {
                        hc.close();
                    }
                } catch (IOException ioe) {
                }
            }
        }
       
         padre.form.insert(0, imageItem);
         padre.form.insert(1, imageItem1);
    }

    private Image createThumbnail(Image image) {
        int sourceWidth = image.getWidth();
        int sourceHeight = image.getHeight();

        int thumbWidth = 30;//64
        int thumbHeight = -1;//

        if (thumbHeight == -1) {
            thumbHeight = thumbWidth * sourceHeight / sourceWidth;
        }

        Image thumb = Image.createImage(thumbWidth, thumbHeight);
        Graphics g = thumb.getGraphics();

        for (int y = 0; y < thumbHeight; y++) {
            for (int x = 0; x < thumbWidth; x++) {
                g.setClip(x, y, 1, 1);
                int dx = x * sourceWidth / thumbWidth;
                int dy = y * sourceHeight / thumbHeight;
                g.drawImage(image, x - dx, y - dy, Graphics.LEFT | Graphics.TOP);
            }
        }

        Image immutableThumb = Image.createImage(thumb);

        return immutableThumb;
    }

    void addError(Exception e) {
        e.printStackTrace();
        error += e.getMessage() + "\n";
    }

    public void commandAction(Command c, Displayable d) {
        if (c == exitCommand) {
            padre.exitApplication();
        }
    }
}
