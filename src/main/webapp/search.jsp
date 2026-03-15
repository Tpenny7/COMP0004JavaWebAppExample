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

<jsp:include page="/footer.jsp"/>
</body>
</html>
