<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Error</title></head>
<body>
  <h1>Something went wrong</h1>
  <p><%= request.getAttribute("errorMessage") %></p>
  <p><a href="<%= request.getContextPath() %>/">Back Home</a></p>
</body>
</html>
