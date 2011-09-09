package com.encuestas;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import javax.servlet.http.HttpServlet;

public class ServletEjemplo extends HttpServlet {

    private String imgDir;
    private File dir;
    
    public void init() throws ServletException {
        super.init();
        imgDir = this.getServletContext().getRealPath("/") + "\\images";
        dir = new File(imgDir);
    }

    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String salida = null;
        DataInputStream din = null;
        PrintWriter out = null;

        ByteArrayOutputStream bout = null;
        DataOutputStream dos = null;


        try {
            out = response.getWriter();
            ServletInputStream in = request.getInputStream();
            din = new DataInputStream(in);
        } catch (IOException io) {
            System.out.println("Se ha producido una excepcion");
        }

        try {

            // Info gral
            String nombre = din.readUTF();
            String telefono = din.readUTF();
            String direccion = din.readUTF();
            String intra = din.readUTF();
            String intranet = din.readUTF();
            String acceso = din.readUTF();

            // Get Image
            int length = din.readInt();
            System.out.print("length " + length);
            byte[] rawImg = new byte[length];
            for (int j = 0; j < length; j++) {
                rawImg[j] = din.readByte();
            }
            String imgName = saveImg(rawImg);

            // Get Map
            int mapLength = din.readInt();
            System.out.print("mapLength " + mapLength);
            byte[] rawMap = new byte[mapLength];
            for (int j = 0; j < mapLength; j++) {
                rawMap[j] = din.readByte();
            }           
            String mapName = saveMap(rawMap);

            // Add to Database
            boolean couldAdd = false;
            if (nombre != null && telefono != null) {
                ToDoList toDoList = (ToDoList) getServletContext().getAttribute("toDoList");
                couldAdd = toDoList.addItem(nombre, telefono, direccion, intra, intranet, acceso, imgName, mapName);
            }

            
            bout = new ByteArrayOutputStream();
            dos = new DataOutputStream(bout);

            if (couldAdd)
                dos.writeUTF("ok");
            else
                dos.writeUTF("false");
            
            byte[] salidaB = bout.toByteArray();
            salida = new String(salidaB);
            out.print(salida);
            out.flush();

            int contentLength = salida.length();

            //System.out.println("ContentLength= " + contentLength);            ae.printStackTrace();

            response.setContentLength(contentLength);
        } catch (IOException ae) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        out.print(salida);
    }

    public String saveMap(byte[] rawMap) {
        String filePathName = null;
        String fileName = null;
        try {
            fileName = "map" + dir.list().length + ".gif";
            filePathName = imgDir + "\\" + fileName;
            FileOutputStream file = new FileOutputStream(filePathName);
//                for (int i = 0; i < rawMap.length; i++) {
            file.write(rawMap);
//                }
            file.flush();
            file.close();
        } catch (IOException e) {
            System.out.println("Error--" + e.toString());
        }
        return fileName;
    }

    public String saveImg(byte[] rawImg) {
        String filePathName = null;
        String fileName = null;
        try {
            fileName = "pic" + dir.list().length + ".png";
            filePathName = imgDir + "\\" + fileName;
            FileOutputStream file = new FileOutputStream(filePathName);
//                for (int i = 0; i < rawMap.length; i++) {
            file.write(rawImg);
//                }
            file.flush();
            file.close();
        } catch (IOException e) {
            System.out.println("Error--" + e.toString());
        }
        return fileName;
    }
}