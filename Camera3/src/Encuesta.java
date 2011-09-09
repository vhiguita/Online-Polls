/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Victor
 */
import java.util.Vector;
import javax.microedition.rms.*;
import java.io.*;
import javax.microedition.lcdui.Image;

public class Encuesta {

    private static RecordStore imagesRS = null;
    static byte[] pngImage;
    static byte[] pngMap;

    static void almacenaEncuesta(Datos d) {
        Image image = d.getImagen();

//        String resourceName = "imagen.png";


        int height, width;
//        if (resourceName == null) {
//            return; // resource name is required
//        }
        // Calculate needed size and allocate buffer area
        height = image.getHeight();
        width = image.getWidth();

        int[] imgRgbData = new int[width * height];
        Image map = d.getMapa();
        int h, w;
        h = map.getHeight();
        w = map.getWidth();
        int[] imgMap = new int[w * h];


        try {
            map.getRGB(imgMap, 0, w, 0, 0, w, h);
            image.getRGB(imgRgbData, 0, width, 0, 0, width, height);
            imagesRS = RecordStore.openRecordStore("datos", true);

            //
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            DataOutputStream dout = new DataOutputStream(bout);

            // Mando Datos
            dout.writeUTF(d.getNombre());
            dout.writeUTF(d.getTel());
            dout.writeUTF(d.getDir());
            dout.writeUTF(d.getIntra());
            dout.writeUTF(d.getIntranet());
            dout.writeUTF(d.getAcceso());

            // Mando Imagen
            dout.writeInt(width);
            dout.writeInt(height);
            dout.writeLong(System.currentTimeMillis());
            dout.writeInt(imgRgbData.length);

            //  Serialize the image raw data
            for (int i = 0; i < imgRgbData.length; i++) {
                dout.writeInt(imgRgbData[i]);
            }

            dout.writeInt(d.getPngImage().length);
            pngImage = d.getPngImage();
            for (int j = 0; j < d.getPngImage().length; j++) {
                dout.writeByte(pngImage[j]);
            }

            dout.writeInt(h);
            dout.writeInt(w);
            dout.writeLong(System.currentTimeMillis());
            dout.writeInt(imgMap.length);
            System.out.println("imgMap.length: " + imgMap.length);
            for (int i = 0; i < imgMap.length; i++) {
                dout.writeInt(imgMap[i]);
            }
            dout.writeInt(d.getPngMap().length);
            pngMap = d.getPngMap();
            System.out.println("almacenaEncuesta: " + d.getPngMap().length);
            for (int j = 0; j < d.getPngMap().length; j++) {
//                System.out.println(pngMap[j]);
                dout.writeByte(pngMap[j]);
            }
            dout.flush();
            dout.close();
            byte[] data = bout.toByteArray();
            imagesRS.addRecord(data, 0, data.length);
            bout.reset();
            dout.close();

            bout.close();

            //imagesRS.closeRecordStore();
            log("stored to RMS");
        } catch (Exception e) {
            log("Err in Add to RMS" + e);
        } finally {
            try {
                // Close the Record Store
                if (imagesRS != null) {
                    imagesRS.closeRecordStore();
//                    log("Err in Add to RMS miami" );
                }
            } catch (Exception ignore) {
                // Ignore
                log("Err in Add to RMS sueden");
            }
        }
    }

    static void modificarEncuesta(Datos d, String persona) {
        try {
            imagesRS = RecordStore.openRecordStore("datos", true);
            RecordEnumeration re = imagesRS.enumerateRecords(null, null, true);
            boolean encontro = false;
            while (re.hasNextElement() && encontro == false) {
                int pos = re.nextRecordId();
                byte[] b = imagesRS.getRecord(pos);

                ByteArrayInputStream bais = new ByteArrayInputStream(b);
                DataInputStream dis = new DataInputStream(bais);
                String nombre = dis.readUTF();
                nombre = nombre.trim();
                System.out.println("mi nombre" + nombre);
                System.out.println("POSICION ACTUAL" + pos);
                if (persona.equals(nombre)) {
                    System.out.println("posicion actualizada" + pos);
                    modifEncuesta(d, pos);
                    encontro = true;

                }
                bais.reset();
                dis.reset();
                bais.close();
                dis.close();
            }
            imagesRS.closeRecordStore();
        } catch (Exception e) {
        }
    }

    static void modifEncuesta(Datos d, int pos) {
        Image image = d.getImagen();
        int height, width;

        // Calculate needed size and allocate buffer area
        height = image.getHeight();
        width = image.getWidth();
        int[] imgRgbData = new int[width * height];

        Image map = d.getMapa();
        int h, w;
        h = map.getHeight();
        w = map.getWidth();
        int[] imgMap = new int[w * h];

        try {
            map.getRGB(imgMap, 0, w, 0, 0, w, h);
            image.getRGB(imgRgbData, 0, width, 0, 0, width, height);
            imagesRS = RecordStore.openRecordStore("datos", true);

            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bas);
            dos.writeUTF(d.getNombre());
            dos.writeUTF(d.getTel());
            dos.writeUTF(d.getDir());
            dos.writeUTF(d.getIntra());
            dos.writeUTF(d.getIntranet());
            dos.writeUTF(d.getAcceso());
            dos.writeInt(width);
            dos.writeInt(height);
            dos.writeLong(System.currentTimeMillis());
            dos.writeInt(imgRgbData.length);
            //  Serialize the image raw data
            for (int i = 0; i < imgRgbData.length; i++) {
                dos.writeInt(imgRgbData[i]);
            }
            dos.writeInt(d.getPngImage().length);
            pngImage = d.getPngImage();
            System.out.println("almacenar:prim:ult:" + pngImage[0] + " " + pngImage[pngImage.length - 1]);
            for (int j = 0; j < d.getPngImage().length; j++) {
                dos.writeByte(pngImage[j]);
            }
            dos.writeInt(h);
            dos.writeInt(w);
            dos.writeLong(System.currentTimeMillis());
            dos.writeInt(imgMap.length);
            for (int i = 0; i < imgMap.length; i++) {
                dos.writeInt(imgMap[i]);
            }
            dos.writeInt(d.getPngMap().length);
            pngMap = d.getPngMap();
            System.out.println("almacenar:prim:ult:" + pngImage[0] + " " + pngImage[pngImage.length - 1]);
            for (int j = 0; j < d.getPngMap().length; j++) {
                dos.writeByte(pngMap[j]);
            }
            dos.flush();
            dos.close();
            byte[] data = bas.toByteArray();
            imagesRS.setRecord(pos, data, 0, data.length);
            bas.reset();
            dos.close();
            bas.close();
            imagesRS.closeRecordStore();
        } catch (Exception e) {
        }

    }

    static Vector leerEncuesta() {

//        RecordStore imagesRS = null;
        Image img = null;
        Image mapa = null;
        Vector persons = new Vector();
        try {
            Datos datos;
            imagesRS = RecordStore.openRecordStore("datos", true);
            RecordEnumeration re = imagesRS.enumerateRecords(null, null, true);

            // For each record
            while (re.hasNextElement()) {
                byte[] b = re.nextRecord();
                ByteArrayInputStream bais = new ByteArrayInputStream(b);
                DataInputStream dis = new DataInputStream(bais);
                String nombre = dis.readUTF();
                String tel = dis.readUTF();
                String dir = dis.readUTF();
                String in = dis.readUTF();
                System.out.println(nombre);
                System.out.println(tel);
                String intra = dis.readUTF();
                String acceso = dis.readUTF();
                int width = dis.readInt();
                int height = dis.readInt();
                long timestamp = dis.readLong();
                int length = dis.readInt();

                int[] rawImg = new int[width * height];

                //Serialize the image raw data
                for (int i = 0; i < length; i++) {
                    rawImg[i] = dis.readInt();
                }
                img = Image.createRGBImage(rawImg, width, height, false);
                int longitud = dis.readInt();
                pngImage = new byte[longitud];
                for (int i = 0; i < longitud; i++) {
                    pngImage[i] = dis.readByte();
                }


                int w = dis.readInt();
                int h = dis.readInt();
                long timestamp1 = dis.readLong();
                int len = dis.readInt();

                int[] rawImg1 = new int[w * h];

                //Serialize the image raw data
                for (int i = 0; i < len; i++) {
                    rawImg1[i] = dis.readInt();
                }
                mapa = Image.createRGBImage(rawImg1, w, h, false);
                longitud = dis.readInt();
                pngMap = new byte[longitud];
                for (int i = 0; i < longitud; i++) {
                    pngMap[i] = dis.readByte();
                }
//                int w = dis.readInt();
//                int h = dis.readInt();
//                long stamp = dis.readLong();
//                int len = dis.readInt();
//                int[] rawImg1 = new int[w * h];
//                for (int i = 0; i < len; i++) {
//                    rawImg1[i] = dis.readInt();
//                    System.out.println(rawImg1[i]);
//                }
//                mapa = Image.createRGBImage(rawImg1, width, height, false);
///*-->*/         byte[] pngMap = new byte[longMap];
///*-->*/         for (int i = 0; i < longMap; i++) {
///*-->*/             pngMap[i] = dis.readByte();
///*-->*/         }
//                bais.reset();
//                dis.reset();

                datos = new Datos(nombre, tel, dir, in, intra, acceso, img, pngImage, mapa, pngMap);
                persons.addElement(datos);
                bais.close();
                dis.close();
//                imagesRS.closeRecordStore();
            }
        } catch (Exception e) {
            System.out.println("salida error" + e);
        }

        return persons;
    }

    public static void delete(String persona) throws Exception {
        try {

            imagesRS = RecordStore.openRecordStore("datos", true);

            RecordEnumeration re = imagesRS.enumerateRecords(null, null, true);

            while (re.hasNextElement()) {
                int pos = re.nextRecordId();
                byte[] b = imagesRS.getRecord(pos);

                ByteArrayInputStream bais = new ByteArrayInputStream(b);
                DataInputStream dis = new DataInputStream(bais);
                String nombre = dis.readUTF();
                nombre = nombre.trim();
                System.out.println("mi nombre" + nombre);
//                System.out.println("su nombre" + persona);
//                String genero = dis.readUTF();
//                int a = dis.readInt();
//                Vector aficiones = new Vector(a);
//                for (int i = 0; i < a; i++) {
//                    aficiones.addElement(dis.readUTF());
//                }
                //int pos=dis.readInt();
                System.out.println("POSICION ACTUAL" + pos);
                if (persona.equals(nombre)) {
                    System.out.println("posicion eliminada" + pos);
                    imagesRS.deleteRecord(pos);

                }
                bais.reset();
                dis.reset();
                bais.close();
                dis.close();
            }
            imagesRS.closeRecordStore();

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public static void log(String msg) {
        System.out.println("Msg: " + msg);
    }
}
