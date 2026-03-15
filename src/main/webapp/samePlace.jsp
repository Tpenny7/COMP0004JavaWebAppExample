<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Patient Data App</title>
  <style>
    .error { color: #b00020; }
    .patient { border: 1px solid #ddd; padding: 10px; margin: 10px 0; border-radius: 8px; }
    .patient h3 { margin: 0 0 6px 0; }
    .summary { color: #444; margin: 0 0 8px 0; }
    .hit { display: inline-block; padding: 2px 8px; margin: 2px; border-radius: 12px; background: #eef; }
    .hits { margin: 6px 0 0 0; padding-left: 18px; }
  </style>
</head>

<body>
<jsp:include page="/header.jsp"/>
<div class="main">

  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null) {
  %>
      <p class="error"><%= errorMessage %></p>
  <%
    }

    Map<String, ArrayList<String[]>> patients =
        (Map<String, ArrayList<String[]>>) request.getAttribute("addressPatients");

    boolean hasSharedResidence = false;

    if (patients != null && !patients.isEmpty()) {

      for (Map.Entry<String, ArrayList<String[]>> entry : patients.entrySet()) {
        String addressText = entry.getKey();
        ArrayList<String[]> residents = entry.getValue();

        if (residents == null || residents.size() < 2) continue;
        hasSharedResidence = true;

  %>

      <div class="patient">
        <h3><%= addressText %></h3>
        <p class="summary"><%= residents.size() %> residents</p>

        <ul class="hits">
          <%
            for (String[] person : residents) {
              if (person == null || person.length == 0) continue;

              String patientId = person[0];
              String name = person[1];

              String href = "patient?id=" + patientId;
          %>
            <li>
              <a href="<%= href %>"><span class="hit"><%= name %> (<%= patientId %>)</span></a>
            </li>
          <%
            }
          %>
        </ul>
      </div>

  <%
      }

      if (!hasSharedResidence && errorMessage == null) {
      %>
            <p>No two patients live in the same residence.</p>
      <%
          }

    } else if (errorMessage == null) {
  %>
      <p>Nothing found</p>
  <%
    }
  %>

</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
