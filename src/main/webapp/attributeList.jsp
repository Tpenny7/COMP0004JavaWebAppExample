<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Patient Data App</title>
</head>
<body>
<jsp:include page="/header.jsp"/>

<div class="main">
  <h2>Select an Attribute:</h2>
  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null)
    {
  %>
      <p style="color: red;"><%= errorMessage %></p>
  <%
    }
  %>
  <ul>
    <%
      ArrayList<String> attributeNames = (ArrayList<String>) request.getAttribute("attributeNames");
      if (attributeNames != null)
      {
        for (String attribute : attributeNames)
        {
          String href = "attribute?name=" + attribute;
    %>
    <li><a href="<%=href%>"><%=attribute%></a>
    </li>
    <%  }
      }
    %>
  </ul>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
