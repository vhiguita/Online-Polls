/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Victor
 */
import javax.microedition.location.*;
import javax.microedition.lcdui.*;

/**
 *
 * @author Andres Sierra
 */
public class MapService extends Thread {

    String error = "";
    double latitud = 0;
    double longitud = 0;
    private ComunicacionHttp http;
    MMSMIDlet padre = null;
    private java.io.InputStream is = null;
    private final Command exitCommand;

    public MapService(MMSMIDlet eje) {
        http = new ComunicacionHttp(eje);
        padre = eje;
        exitCommand = new Command("Salir", Command.EXIT, 1);
    }

    public void run() {
        try {
            this.localizar();
            is = http.requestMap(latitud, longitud);
            Image im = Image.createImage(is);
            im = createThumbnail(im);
            ImageItem map = new ImageItem("", im, ImageItem.LAYOUT_TOP | ImageItem.LAYOUT_RIGHT, null);

            padre.mapa = im;
            padre.form3.delete(1);
            padre.form3.insert(1, map);
            Display.getDisplay(padre).setCurrent(padre.form3);
            java.io.InputStream isTemp = http.requestMap(latitud, longitud);
            final int MAX_LENGTH = 12000;
            byte[] bufMap = new byte[MAX_LENGTH];
            int total = 0;
            while (total < MAX_LENGTH) {
                int count = isTemp.read(bufMap, total, MAX_LENGTH - total);

                if (count < 0) {
                    break;
                }
                total += count;
            }

            isTemp.close();
//            for (int i = 0; i < MAX_LENGTH; i++) {
//                System.out.println(bufMap[i]);
//            }
            padre.data113 = bufMap;
            System.out.println("tamano padrepadre.pngMap.length: " + padre.pngMap.length);

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void localizar() {
        LocationProvider lp = null;
        javax.microedition.location.Location location = null;
        try {
            lp = LocationProvider.getInstance(null);
            location = lp.getLocation(-1); // Timeout
        } catch (LocationException e) {
            addError(e);
        } catch (InterruptedException e) {
            addError(e);
        }

        String res = "[RESULTADOS DE LA BUSQUEDA]\n";
        try {
            Coordinates coordinates = location.getQualifiedCoordinates();
            res += "Altitude:" + coordinates.getAltitude() + "\n";
            res += "Latitude:" + coordinates.getLatitude() + "\n";
            res += "Longitude:" + coordinates.getLongitude() + "\n";
            latitud = coordinates.getLatitude();
            longitud = coordinates.getLongitude();
            System.out.println("RESULTADOS PUNTOS: " + res);
        } catch (Exception e) {
            addError(e);
        }
        try {
            http.requestMap(latitud, longitud);
        } catch (Exception e) {
            e.printStackTrace();
        }
    /*  Form f = new Form("Results");
    f.append(res);
    f.append(error);
    //displayMapa.setCurrent(f);
    Display.getDisplay(padre).setCurrent(f); */
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
