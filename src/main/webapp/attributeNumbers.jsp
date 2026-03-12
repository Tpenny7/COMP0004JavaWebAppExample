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

    Map<String, ArrayList<String[]>> results =
        (Map<String, ArrayList<String[]>>) request.getAttribute("attributeNumbers");

    if (results != null && !results.isEmpty()) {

      for (Map.Entry<String, ArrayList<String[]>> entry : results.entrySet()) {
        String attributeText = entry.getKey();
        ArrayList<String[]> patients = entry.getValue();

        if (attributeText == null || attributeText.isEmpty()) {continue;}

  %>

      <div class="patient">
        <h3><%= attributeText %></h3>
        <p class="summary"><%= patients.size() %> <%= (patients.size() == 1) ? "patient" : "patients" %></p>

        <ul class="hits">
          <%
            for (String[] person : patients) {
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
      } // end entry loop

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
