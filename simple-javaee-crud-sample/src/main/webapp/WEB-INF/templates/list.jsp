<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
    <head>
        <title>list</title>
    </head>
    <body>
        <ul>
            <c:forEach items="${model.list}" var="item">
            <li><c:out value="${item.text}"/></li>
            </c:forEach>
        </ul>
    </body>
</html>
