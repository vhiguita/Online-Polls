<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spfn" uri="http://www.sitepoint.com/jsp/taglibs/functions" %>
<%@page session="true" import="java.util.*, com.encuestas.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>To-Do List</title>
        <meta http-equiv="content-type"
              content="text/html; charset=iso-8859-1" />
        <link rel="stylesheet" type="text/css"
              href="<c:url value="/styles.css"/>" />
        <style type="text/css">
            body { background-color:gray; font-size=10pt; }
            H1 {font-size:20pt;}
            table {background-color:white;
                   border-collapse: collapse; }
        </style>
    </head>
    <body>
        <H1>Entrevistas Online</H1>
        <hr/><p/>
        <%
            ToDoList toDoList = (ToDoList) getServletContext().getAttribute("toDoList");
        %>
        <table border="1" cellpadding="2">
            <tr>
                <td>NOMBRE</td>
                <td>TELEFONO</td>
                <td>DIRECCION</td>
                <td>INTERNET</td>
                <td>SERVICIO</td>
                <td>ACCESO</td>
                <td>IMAGEN</td>
                <td>MAPA</td>
            </tr>
            <% // Scriptlet 4: display the books in the shopping cart

            List myList = toDoList.getToDoItems();
            for (int i = 0; i < myList.size(); i++) {
                Datos datos = (Datos) myList.get(i);
            %>
            <tr>
                <td><%= datos.getNombre()%> </td>
                <td align="center"><%= datos.getTel()%> </td>
                <td align="center"><%= datos.getDir()%> </td>
                <td align="center"><%= datos.getIntra()%> </td>
                <td align="center"><%= datos.getIntranet()%> </td>
                <td align="center"><%= datos.getAcceso()%> </td>
                <td align="center"><img src="images/<%= datos.getImagen()%>" alt="Angry face" /> </td>
                <td align="center"><img src="images/<%= datos.getMapa()%>" alt="Angry face" /> </td>
            </tr>
                    <%
            } // for (int i..
%>
        </table>
        <p/>
        <c:if test="${fn:length(toDoItems) > 0}">
            <form action="<c:url value="/DeleteItem.do"/>" method="post">
                <select name="deleteid" size="${spfn:max(2,fn:length(toDoItems))}">
                    <c:forEach var="toDoItem" items="${toDoItems}">
                        <option value="${toDoItem.id}">${fn:escapeXml(toDoItem)}</option>
                    </c:forEach>
                </select><br>
                <input type="submit" value="Delete Selected Item"/>
            </form>
        </c:if>
        <form action="<c:url value="/additem.jsp"/>" method="post">
            <input type="submit" value="Add New Item"/>
        </form>
    </body>
</html>
