<%@ page import="java.util.Map" %>

<%
  Map<String,String> patient = (Map<String,String>) request.getAttribute("patient");
  String id = (String) request.getAttribute("id");
  String hrefEdit = "editPatient?id=" + id;
  String hrefDelete = "deletePatient?id=" + id;
%>

<html>
<head>
  <jsp:include page="/meta.jsp"/>
</head>
<body>
<jsp:include page="/header.jsp"/>

<div class="main">
  <p><a href ="<%= hrefEdit %>">Edit patient</a></p>
  <p><a href ="<%= hrefDelete %>">Delete patient</a></p>
  <h2>Patient details:</h2>


<table class="kv">
  <tbody>
  <%
    for (Map.Entry<String,String> e : patient.entrySet()) {
    if (e.getValue() != null && !(e.getValue().equals(""))){
      %>
        <tr>
          <th><%= e.getKey() %></th>
          <td><%= e.getValue() %></td>
        </tr>
      <%
      }
    }
  %>
  </tbody>
</table>
