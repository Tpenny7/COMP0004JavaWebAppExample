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
    String query = request.getParameter("searchstring");
    if (query == null) query = "";
  %>
  <h1>Search Result: <%= query %></h1>
  <div class="main">
    <form method="GET" action="/runsearch">
      <input type="text" name="searchstring" required placeholder="Enter search keyword here"/>
      <input type="submit" value="Search"/>
    </form>
  </div>

  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null) {
  %>
      <p class="error"><%= errorMessage %></p>
  <%
    }

    Map<String, ArrayList<String>> patients =
        (Map<String, ArrayList<String>>) request.getAttribute("result");

    if (patients != null && !patients.isEmpty()) {
  %>

    <%
      for (Map.Entry<String, ArrayList<String>> entry : patients.entrySet()) {
        String patientId = entry.getKey();
        ArrayList<String> list = entry.getValue();
        String href = "patient?id=" + patientId;
    %>

      <div class="patient">
        <h3><a href="<%= href %>"><%= list.get(0) %></a></h3>

        <%
          if (list != null && list.size() > 1) {
        %>
            <ul class="hits">
              <%
                for (int i = 1; i < list.size(); i++) {
                  String hit = list.get(i);
              %>
                <li><span class="hit"><%= hit %></span></li>
              <%
                }
              %>
            </ul>
        <%
          } else {
        %>
            <p>(No field hits recorded)</p>
        <%
          }
        %>
      </div>

    <%
      } // end for
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
