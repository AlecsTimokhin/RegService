<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<div id="UpdateFormProfile" xmlns:th="http://www.w3.org/1999/html">

    <c:if test="${currentUser != null}">
        <c:if test="${currentUser.active == false}">
            <div class="error">
                Ваш профиль неактивен!<br>
                Вы не можете его редактировать.<br>
                Обратитесь к администратору сервиса.
                <hr>
            </div>
        </c:if>
    </c:if>

    Ваш username: <b>${currentUser.username}</b>
    <br>
    Баланс вашего счета: <b>${currentUser.balance}</b>

</div>