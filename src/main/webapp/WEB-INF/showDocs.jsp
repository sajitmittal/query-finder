<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: sajit.m
  Date: 9/25/2016
  Time: 1:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
    <title>Display Top Documents</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
</head>
<body>
    <p>The result is</p>
    <table border="1">
    <tbody>
    <tr><th>Documents</th></tr>
    <%
        List<String> documents=(List<String>) request.getAttribute("documents");
        for (String doc: documents) {
    %>
    <tr>
        <td><br><%=doc%><br></td>
    </tr>
    <%}%>
    </tbody>
    </table>
</body>
</html>
