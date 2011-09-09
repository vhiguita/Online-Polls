/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encuestas;

import java.io.*;

import java.util.List;
import javax.servlet.http.*;
import javax.servlet.*;

public class LookupServlet extends HttpServlet {

    private DataOutputStream dos;
    private ByteArrayOutputStream bout;

    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

//        PrintWriter out = null;
        String salida = null;
        bout = null;
        dos = null;

        bout = new ByteArrayOutputStream();
        dos = new DataOutputStream(bout);

        String word = request.getParameter("nombre");

        String message;

        if (word == null || word.length() == 0) {
            message = "[No word specified.]";
        } else {
            try {
                message = lookUp(word);
            } catch (IOException ioe) {
                message = "[Exception: " + ioe.toString() + "]";
            }
        }

        if (message == null || message.length() == 0) {
            message = "No se encontro Nada!";
        }

//        dos.writeUTF("ok");
        byte[] salidaB = bout.toByteArray();
        salida = new String(salidaB);
        PrintWriter out = response.getWriter();
        out.print(salida);
//        out.flush();


//    response.setContentType("text/plain");
//    response.setContentLength(message.length());
//    PrintWriter out = response.getWriter();
//    out.println(message);

    }

    public String lookUp(String word) throws IOException {

        String info = null;

//    HttpConnection conn = (HttpConnection) Connector.open(url, Connector.READ_WRITE);
//        conn.setRequestMethod(HttpConnection.POST);
//        //Abrir Streams de salida para envio de peticion
//
//        DataOutputStream dos = conn.openDataOutputStream();
//
//        dos.writeUTF(datos.getNombre());
//        dos.writeUTF(datos.getTel());
        try {
            ToDoList toDoList = (ToDoList) getServletContext().getAttribute("toDoList");

            List myList = toDoList.selectItem(word);
            if (myList.size() != 0) {
                for (int i = 0; i < myList.size(); i++) {
                    Datos datos = (Datos) myList.get(i);
                    dos.writeUTF(datos.getNombre());
                    dos.writeInt(datos.getTel());
                    dos.writeUTF(datos.getDir());
                    dos.writeUTF(datos.getIntra());
                    dos.writeUTF(datos.getIntranet());
                    dos.writeUTF(datos.getAcceso());
                    dos.writeUTF(datos.getMapa());
                    dos.writeUTF(datos.getImagen());
                }
            }

        } finally {
        }

        return info;
    }
}
