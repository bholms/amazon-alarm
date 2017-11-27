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
        Amazon Standard Identification Numbers (ASINs): <input type="text" name="product_asin" value="B01GEW27DA">
        <input type="submit" value="Submit"/>
    </form>

    <table style="width:100%; border:1px solid black">
        <tr>
            <th>Product</th>
            <th>Current Price</th>
        </tr>
        <!-- Need to loop this in the future -->
        <tr>
            <td><img src="${message["imageURL"]}">
                <a href="${message["url"]}" target="_blank">${message["title"]}</a>
            </td>
            <td>${message["price"]}</td>
        </tr>
    </table>

</body>
</html>
