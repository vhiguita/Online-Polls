package com.encuestas;

import java.util.*;
import java.sql.*;

public class ToDoList {

    private String jdbcConnectionString;
    private ArrayList list = new ArrayList();
    private boolean staleList = true;
    private Connection conn;

    public ToDoList(String jdbcDriver, String jdbcConnectionString) {
      //  this.jdbcConnectionString = jdbcConnectionString;

        // Load the driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            System.err.println("Error loading database driver " + jdbcDriver +
                    ":\n" + ex.getMessage());
        }
    }

    public List getToDoItems() {
        refreshList();
        return (List) list.clone();
    }

    public int getItemCount() {
        refreshList();
        return list.size();
    }

    public boolean addItem(String nombre, String telefono, String direccion, String intra, String intranet, String acceso, String imagen, String mapa) {

        List lista = selectItem(nombre);
            if (lista.size() != 0)
                return false;

        try {
            if (conn == null) {
                 conn = DriverManager.getConnection("jdbc:mysql://localhost/todo", "root", "admin");

               // conn = DriverManager.getConnection(jdbcConnectionString);
            }

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO encuestas (nombre, telefono, direccion, intra, intranet, acceso, imagen, mapa) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            stmt.setString(1, nombre);
            stmt.setInt(2, Integer.parseInt(telefono));
            stmt.setString(3, direccion);
            stmt.setString(4, intra);
            stmt.setString(5, intranet);
            stmt.setString(6, acceso);
            stmt.setString(7, imagen);
            stmt.setString(8, mapa);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                    "Error adding a to-do list item to the database:\n" +
                    ex.getMessage());
        }
        staleList = true;
        return true;
    }

    public void deleteItem(int id) {
        try {
            if (conn == null) {
                  conn = DriverManager.getConnection("jdbc:mysql://localhost/todo", "root", "admin");
                //conn = DriverManager.getConnection(jdbcConnectionString);
            }
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM encuestas WHERE id=?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(
                    "Error deleting a to-do list item from the database:\n" +
                    ex.getMessage());
        }
        staleList = true;
    }

    public boolean deleteName(String name) {

        List lista = selectItem(name);
            if (lista.size() == 0)
                return false;
        try {
            if (conn == null) {
                  conn = DriverManager.getConnection("jdbc:mysql://localhost/todo", "root", "admin");
               // conn = DriverManager.getConnection(jdbcConnectionString);
            }

            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM encuestas WHERE nombre=?");
            stmt.setString(1, name);
            stmt.executeUpdate();
            staleList = true;
            return true;
        } catch (SQLException ex) {
            System.err.println(
                    "Error al borrar una persona de la Base de Datos:\n" +
                    ex.getMessage());
            return false;
        }
        
    }
    
    public List selectItem(String word) {

        ArrayList list = new ArrayList();

        try {

            if (conn == null) {
                  conn = DriverManager.getConnection("jdbc:mysql://localhost/todo", "root", "admin");
               // conn = DriverManager.getConnection(jdbcConnectionString);
            }
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT nombre, telefono, direccion, intra, intranet, acceso, imagen, mapa, id FROM encuestas WHERE nombre='" + word + "'");

            list = new ArrayList();
            while (rs.next()) {
                list.add(new Datos(rs.getString(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getInt(9)));
            }
//                ResultSet rs = stmt.getResultSet();
//                list.add(new Datos("Becky", 451212, "Doh", 15));
//                while (rs.next()) {
//                        list.add(new Datos(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getInt(4)));
//                }

        } catch (SQLException ex) {
            System.err.println(
                    "Error selecting a personfrom the database:\n" +
                    ex.getMessage());
        }
        return list;
    }

    private void refreshList() {
        if (staleList) {
            try {
                if (conn == null) {
                      conn = DriverManager.getConnection("jdbc:mysql://localhost/todo", "root", "admin");
                   // conn = DriverManager.getConnection(jdbcConnectionString);
                }
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT nombre, telefono, direccion, intra, intranet, acceso, imagen, mapa, id FROM encuestas");

                list = new ArrayList();
                while (rs.next()) {
                    list.add(new Datos(rs.getString(1),
                            rs.getInt(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6),
                            rs.getString(7),
                            rs.getString(8),
                            rs.getInt(9)));
                }
            } catch (SQLException ex) {
                System.err.println(
                        "Error retrieving to-do list items from the database:\n" +
                        ex.getMessage());
            }
            staleList = false;
        }
    }
}
