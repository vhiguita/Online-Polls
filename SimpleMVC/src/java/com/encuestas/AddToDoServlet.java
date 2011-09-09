package com.encuestas;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class AddToDoServlet extends HttpServlet {
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String nombre = request.getParameter("nombre");
    String telefono = request.getParameter("telefono");
    String imagen = request.getParameter("imagen");
    
    if (nombre != null && telefono != null) {
      ToDoList toDoList = (ToDoList)getServletContext().getAttribute("toDoList");
      // toDoList.addItem(nombre, telefono, imagen);
    }
    response.sendRedirect("index.html");
  }
}
