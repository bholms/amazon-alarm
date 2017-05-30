<%@ page import="amazonalarm.ProductServlet" %>
<%@ page import="java.util.*" %>

<%--
  Created by IntelliJ IDEA.
  User: bwzhao
  Date: 5/13/17
  Time: 5:57 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Amazon Price Tracker</title>
</head>
<body>
    <!-- Main page -->
    <h2 class="message"><%=ProductServlet.getMessage()%>
    </h2>

    <form action="${pageContext.request.contextPath}/getProductInfo" method="POST">
        Amazon Product URL: <input type="text" name="product_url" value="https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882/ref=sr_1_1?ie=UTF8&qid=1496090941&sr=8-1&keywords=clean+code">
        <input type="submit" value="Submit"/>
    </form>

    <table style="width:100%; border:1px solid black">
        <tr>
            <th>Product</th>
            <th>Current Price</th>
        </tr>
        <!-- Need to loop this in the future -->
        <tr>
            <td><a href="${message["url"]}">${message["itemName"]}</a></td>
            <td>${message["price"]}</td>
        </tr>
    </table>

    <!-- Get existing product URLs from DB
    <sql:setDataSource var = "snapshot" driver = "com.mysql.jdbc.Driver"
                       url = "jdbc:mysql://localhost/TEST"
                       user = "root"  password = "pass123"/>
    <sql:query dataSource = "${snapshot}" var = "result">
        SELECT * from Employees;
    </sql:query>
    -->

    <!-- Insert new product URL to DB
    <sql:setDataSource var = "snapshot" driver = "com.mysql.jdbc.Driver"
                       url = "jdbc:mysql://localhost/TEST"
                       user = "root"  password = "pass123"/>
    <sql:update dataSource = "${snapshot}" var = "result">
        INSERT INTO Employees VALUES (104, 2, 'Nuha', 'Ali');
    </sql:update>

    <sql:query dataSource = "${snapshot}" var = "result">
        SELECT * from Employees;
    </sql:query>

    <table border = "1" width = "100%">
        <tr>
            <th>Emp ID</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Age</th>
        </tr>

        <c:forEach var = "row" items = "${result.rows}">
            <tr>
                <td><c:out value = "${row.id}"/></td>
                <td><c:out value = "${row.first}"/></td>
                <td><c:out value = "${row.last}"/></td>
                <td><c:out value = "${row.age}"/></td>
            </tr>
        </c:forEach>
    </table>
    -->

    <!-- Delete product URL from DB
    <sql:setDataSource var = "snapshot" driver = "com.mysql.jdbc.Driver"
                       url = "jdbc:mysql://localhost/TEST"
                       user = "root" password = "pass123"/>

    <c:set var = "empId" value = "103"/>

    <sql:update dataSource = "${snapshot}" var = "count">
        DELETE FROM Employees WHERE Id = ?
        <sql:param value = "${empId}" />
    </sql:update>

    <sql:query dataSource = "${snapshot}" var = "result">
        SELECT * from Employees;
    </sql:query>
    -->

</body>
</html>
