<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <title>JSP - Custom Tag and Scriptlet</title>
    </head>
    <body>
        <% for (int i = 0; i < 3; i++) { %>
            <c:forEach items="${messages}" var="message">
                <p>
                    <c:out value="${message}"/>
                    <%= i %>
                </p>
            </c:forEach>
        <% } %>
    </body>
</html>
