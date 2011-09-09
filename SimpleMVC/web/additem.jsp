<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spfn" uri="http://www.sitepoint.com/jsp/taglibs/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>Agregar Entrevista</title>
  <meta http-equiv="content-type"
      content="text/html; charset=iso-8859-1" />
  <link rel="stylesheet" type="text/css"
      href="<c:url value="/styles.css"/>" />
</head>
<body>
  <form action="<c:url value="/AddItem.do"/>" method="post">
    <input type="text" name="nombre"/><br>
    <input type="text" name="telefono"/> <br>
    <input type="submit" value="Add New Item"/>
  </form>
</body>
</html>
