<%--
  Created by IntelliJ IDEA.
  User: Nodo
  Date: 14/03/2018
  Time: 21:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
    <head>
        <title>Title</title>
    </head>
    <body>
        <sec:authentication property="lastname"/>
    </body>
</html>
