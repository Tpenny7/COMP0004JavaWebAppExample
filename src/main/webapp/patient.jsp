<%@ page import="java.util.Map" %>

<head>
  <jsp:include page="/meta.jsp"/>
  <title>Patient details</title>
</head>
<jsp:include page="/header.jsp"/>
<%
  Map<String,String> patient = (Map<String,String>) request.getAttribute("patient");
%>

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
