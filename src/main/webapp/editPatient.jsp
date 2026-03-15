<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<style>
  .required-mark { color: red; }
</style>
<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Add Patient</title>
</head>
<body>
<jsp:include page="/header.jsp"/>

<div class="main">
  <h1>Edit Patient</h1>
  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null) {
  %>
    <p style="color:red;"><%= errorMessage %></p>
  <%
    }
  %>
  <%
      Map<String,String> patient = (Map<String,String>) request.getAttribute("patient");
      String href = "editPatient?id=" + (String) request.getAttribute("id");
    %>
  <form method="post" action="<%=href%>">
    <!-- Names -->
    <label>Prefix:
          <input type="text" name="PREFIX"
          value = "<%= patient.getOrDefault("PREFIX","") %>">
        </label><br>

    <label>First name <span class="required-mark">*</span>:
      <input type="text" name="FIRST"
      value = "<%= patient.getOrDefault("FIRST","") %>"> required>
    </label><br>

    <label>Last name <span class="required-mark">*</span>:
      <input type="text" name="LAST"
      value = "<%= patient.getOrDefault("LAST","") %>"> required>
    </label><br>

    <label>Suffix:
          <input type="text" name="SUFFIX"
         value = "<%= patient.getOrDefault("SUFFIX","") %>">
        </label><br>

    <label>Maiden name:
              <input type="text" name="MAIDEN"
              value = "<%= patient.getOrDefault("MAIDEN","") %>">
            </label><br>

    <!-- Basic demographics -->
    <label>Birthdate: (YYYY-MM-DD) <span class="required-mark">*</span>:
          <input type="text" name="BIRTHDATE"
          value = "<%= patient.getOrDefault("BIRTHDATE","") %>">
        </label><br>

    <label>Deathdate: (YYYY-MM-DD):
          <input type="text" name="DEATHDATE"
          value = "<%= patient.getOrDefault("DEATHDATE","") %>">
        </label><br>

    <label>SSN:
          <input type="text" name="SSN"
          value = "<%= patient.getOrDefault("SSN","") %>">
        </label><br>

    <label>Drivers Licence Number:
          <input type="text" name="DRIVERS"
          value = "<%= patient.getOrDefault("DRIVERS","") %>">
        </label><br>

    <label>Passport Number:
          <input type="text" name="PASSPORT"
          value = "<%= patient.getOrDefault("PASSPORT","") %>">
        </label><br>

    <label>Gender:
      <input type="text" name="GENDER"
      value = "<%= patient.getOrDefault("GENDER","") %>">
    </label><br>

    <label>Marital Status:
          <input type="text" name="MARITAL"
          value = "<%= patient.getOrDefault("MARITAL","") %>">
        </label><br>

    <label>Race:
          <input type="text" name="RACE"
          value = "<%= patient.getOrDefault("RACE","") %>">
        </label><br>

    <label>Ethnicity:
          <input type="text" name="ETHNICITY"
          value = "<%= patient.getOrDefault("ETHNICITY","") %>">
        </label><br>

    <label>Birthplace:
          <input type="text" name="BIRTHPLACE"
          value = "<%= patient.getOrDefault("BIRTHPLACE","") %>">
        </label><br>


    <!-- Address -->
    <label>Address:
      <input type="text" name="ADDRESS"
      value = "<%= patient.getOrDefault("ADDRESS","") %>">
    </label><br>

    <label>City:
      <input type="text" name="CITY"
      value = "<%= patient.getOrDefault("CITY","") %>">
    </label><br>

    <label>State:
      <input type="text" name="STATE"
      value = "<%= patient.getOrDefault("STATE","") %>">
    </label><br>

    <label>ZIP:
      <input type="text" name="ZIP"
      value = "<%= patient.getOrDefault("ZIP","") %>">
    </label><br>


    <input type="submit" value="Submit Changes">
  </form>
</div>

<jsp:include page="/footer.jsp"/>
</body>
</html>
