<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Patient deleted</title>
</head>
<body>
<jsp:include page="/header.jsp"/>

<div class="main">
  <h1>Delete patient</h1>
  <p><%= request.getAttribute("message") %></p>
  <p><a href="patientList">Back to patient list</a></p>
</div>

</body>
</html>
