<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Confirm delete</title>
</head>
<body>
<jsp:include page="/header.jsp"/>

<div class="main">
  <h1>Delete patient</h1>

  <p>Are you sure you want to delete patient with id
     <strong><%= request.getAttribute("id") %></strong>?</p>

  <form method="post" action="deletePatient">
    <input type="hidden" name="id" value="<%= request.getAttribute("id") %>">
    <input type="submit" value="Yes, delete">
  </form>

  <p><a href="patient?id=<%= request.getAttribute("id") %>">Cancel</a></p>
</div>

</body>
</html>
