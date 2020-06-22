<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<h3>Страница баланса</h3>

<c:if test="${currentUser != null}">
    <div>
        Ваш баланс: <b>${currentUser.balance}</b>
    </div>
</c:if>

