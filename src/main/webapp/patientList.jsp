<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Patient Data App</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="main">
  <h1>Search</h1>
  <form method="POST" action="/runsearch">
    <input type="text" name="searchstring" required placeholder="Enter search keyword here"/>
    <input type="submit" value="Search"/>
  </form>
</div>
<div class="main">
  <h2>Patients:</h2>
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
      Map<String,String> patientSummary = (Map<String,String>) request.getAttribute("patientSummary");
      if (patientSummary != null)
      {
        for (Map.Entry element : patientSummary.entrySet())
        {
          String href = "patient?id=" + (String)element.getKey();
    %>
    <li><a href="<%=href%>"><%=element.getValue()%></a>
    </li>
    <%  }
      }
    %>
  </ul>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
