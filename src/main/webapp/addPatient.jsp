<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
  <h1>Add Patient</h1>

  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null) {
  %>
    <p style="color:red;"><%= errorMessage %></p>
  <%
    }
  %>

  <form method="post" action="addPatient">
    <!-- Names -->
    <label>Prefix:
          <input type="text" name="PREFIX">
        </label><br>

    <label>First name <span class="required-mark">*</span>:
      <input type="text" name="FIRST" required>
    </label><br>

    <label>Last name <span class="required-mark">*</span>:
      <input type="text" name="LAST" required>
    </label><br>

    <label>Suffix:
          <input type="text" name="SUFFIX">
        </label><br>

    <label>Maiden name:
              <input type="text" name="MAIDEN">
            </label><br>

    <!-- Basic demographics -->
    <label>Birthdate: (YYYY-MM-DD) <span class="required-mark">*</span>:
          <input type="text" name="BIRTHDATE">
        </label><br>

    <label>Deathdate: (YYYY-MM-DD):
          <input type="text" name="DEATHDATE">
        </label><br>

    <label>SSN:
          <input type="text" name="SSN">
        </label><br>

    <label>Drivers Licence Number:
          <input type="text" name="DRIVERS">
        </label><br>

    <label>Passport Number:
          <input type="text" name="PASSPORT">
        </label><br>

    <label>Gender:
      <input type="text" name="GENDER">
    </label><br>

    <label>Marital Status:
          <input type="text" name="MARITAL">
        </label><br>

    <label>Race:
          <input type="text" name="RACE">
        </label><br>

    <label>Ethnicity:
          <input type="text" name="ETHNICITY">
        </label><br>

    <label>Birthplace:
          <input type="text" name="BIRTHPLACE">
        </label><br>


    <!-- Address -->
    <label>Address:
      <input type="text" name="ADDRESS">
    </label><br>

    <label>City:
      <input type="text" name="CITY">
    </label><br>

    <label>State:
      <input type="text" name="STATE">
    </label><br>

    <label>ZIP:
      <input type="text" name="ZIP">
    </label><br>


    <input type="submit" value="Add Patient">
  </form>
</div>

<jsp:include page="/footer.jsp"/>
</body>
</html>
