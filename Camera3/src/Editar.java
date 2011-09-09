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
public class Editar extends Thread {

    String error = "";
    double latitud = 0;
    double longitud = 0;
    int tel = 0;
    private ComunicacionHttp http;
    MMSMIDlet padre = null;
    private java.io.InputStream is = null;
    private final Command exitCommand;
    String nombre, dir, intr, intra, acces, conf, imagen, map;
    public ImageItem imageItem,  imageItem1;
    byte[] data = null;
    byte[] data1 = null;
    String word;

    public Editar(MMSMIDlet eje) {
        http = new ComunicacionHttp(eje);
        padre = eje;
        exitCommand = new Command("Salir", Command.EXIT, 1);
    }

    public void run() {

        word = padre.buscanombre1.getString();
        String definition;
        try {
            //  word=word.trim();
            definition = lookUp(word);


        } catch (IOException ioe) {
        }


    }

    private String lookUp(String word) throws IOException {
        HttpConnection hc = null;
        InputStream in = null;
        String definition = null;
        try {
            String baseURL = "http://localhost:8080/SimpleMVC/lookup?nombre=";
            String url = baseURL + word;

            hc = (HttpConnection) Connector.open(url);
            hc.setRequestProperty("Connection", "close");
            in = hc.openInputStream();

//            int contentLength = (int) hc.getLength();
//            if (contentLength == -1) {
//                contentLength = 255;
//            }

            //Abrir Streams de entrada para la captura de la respuesta
            final int MAX_LENGTH = 1280;
            byte[] buf = new byte[MAX_LENGTH];
            int total = 0;
            while (total < MAX_LENGTH) {
                int count = in.read(buf, total, MAX_LENGTH - total);
                if (count < 0) {
                    break;
                }
                total += count;
            }
            in.close();
            ByteArrayInputStream bin = new ByteArrayInputStream(buf);
            DataInputStream dis = new DataInputStream(bin);
//            Alert al = new Alert("Leyendo....");
//            al.setTimeout(Alert.FOREVER);
//            Display.getDisplay(padre).setCurrent(al);
            nombre = dis.readUTF();
            tel = dis.readInt();
            dir = dis.readUTF();
            intr = dis.readUTF();
            intra = dis.readUTF();
            acces = dis.readUTF();
            imagen = dis.readUTF();
            map = dis.readUTF();
            System.out.println("name:" + nombre);
            System.out.println("tel:" + tel);
            System.out.println("direccion:" + dir);
            System.out.println("Intranet:" + intr);
            System.out.println("intra rapido:" + intra);
            System.out.println("intra acceso:" + acces);
            System.out.println("mapa:" + map);
            System.out.println("imagen:" + imagen);

        //indPr.stop();
//            byte[] raw = new byte[contentLength];
//            int length = in.read(raw);
//            // Clean up.
//            in.close();
//            hc.close();
//            definition = new String(raw, 0, length);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (hc != null) {
                    hc.close();
                }
            } catch (IOException ignored) {
            }
        }

        // imagen = "mapa2.png";
        if (imagen != null && imagen.length() != 0) {
            /* Imagen **/
            DataInputStream in2 = null;
            try {
                String url = "http://localhost:8080/SimpleMVC/images/" + imagen;
                hc = (HttpConnection) Connector.open(url);
                int length = (int) hc.getLength();
                //byte[] data = null;
                if (length != -1) {
                    data1 = new byte[length];
                    in2 = new DataInputStream(hc.openInputStream());
                    in2.readFully(data1);
                } else {
                    int chunkSize = 512;
                    int index = 0;
                    int readLength = 0;
                    in2 = new DataInputStream(hc.openInputStream());
                    data1 = new byte[chunkSize];
                    do {
                        if (data1.length < index + chunkSize) {
                            byte[] newData = new byte[index + chunkSize];
                            System.arraycopy(data1, 0, newData, 0, data1.length);
                            data1 = newData;
                        }
                        readLength = in2.read(data1, index, chunkSize);
                        index += readLength;
                    } while (readLength == chunkSize);
                    length = index;
                }
                Image image = Image.createImage(data1, 0, length);
                image = createThumbnail(image);
                imageItem = new ImageItem("", image, ImageItem.LAYOUT_TOP | ImageItem.LAYOUT_RIGHT, null);
                System.out.println(imageItem);


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
        System.out.println(map);
        if (map != null && map.length() != 0) {
            /* Imagen **/
            DataInputStream in2 = null;
            try {
                String url = "http://localhost:8080/SimpleMVC/images/" + map;
                hc = (HttpConnection) Connector.open(url);
                int length = (int) hc.getLength();
                //byte[] data = null;
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
                imageItem1 = new ImageItem("", image, ImageItem.LAYOUT_TOP | ImageItem.LAYOUT_RIGHT, null);
                System.out.println(imageItem1);

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
        String palabra = word;
        String busca = nombre;
        palabra = palabra.toUpperCase();
        busca = busca.toUpperCase();
        if (word.equals(nombre)) {
            padre.agregar(nombre, tel, dir, intr, intra, acces, imageItem, imageItem1, data1, data);

        } else {
            Alert al = new Alert("No se encontro la persona");
            al.setTimeout(Alert.FOREVER);
            Display.getDisplay(padre).setCurrent(al);


        }
        return definition;
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
