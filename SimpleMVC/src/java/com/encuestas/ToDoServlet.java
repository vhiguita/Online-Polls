package com.encuestas;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class ToDoServlet extends HttpServlet
    implements ServletContextListener {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    ToDoList toDoList = (ToDoList)getServletContext().getAttribute("toDoList");
    List toDoItems = toDoList.getToDoItems();
    request.setAttribute("toDoItems", toDoItems);

    RequestDispatcher view = request.getRequestDispatcher("/index.jsp");
    view.forward(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  public void contextInitialized(ServletContextEvent sce) {
    ServletContext sc = sce.getServletContext();
    sc.setAttribute("toDoList",
        new ToDoList(sc.getInitParameter("jdbcDriver"),
                     sc.getInitParameter("jdbcConnectionString")));
  }

  public void contextDestroyed(ServletContextEvent sce) {
  }
}
