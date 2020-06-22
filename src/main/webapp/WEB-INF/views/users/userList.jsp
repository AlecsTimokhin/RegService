<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<h3>Список всех пользователей</h3>

<div align="center" id="message" class="${status}">${message}</div>

Всего пользователей: <b id="countUsers">${count}</b>

<table id="mainTable" class="users_table">
    <thead>
    <tr>
        <th>№</th>
        <th>id</th>
        <th>Username</th>
        <th>Баланс</th>
        <th>Роли</th>
        <c:if test="${canDoActions == true}">
            <th>Удалить пользователя</th>
        </c:if>
    </tr>
    </thead>
    <%
        int i = 1;
    %>
    <c:forEach items="${users}" var="user">
        <tr class="userTr">
            <td class="numberTd"><%=i++%></td>
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.balance}</td>
            <td>${user.roles}</td>
            <c:if test="${canDoActions == true}">
                <td>
                    <a href="javascript://" onclick="deleteUser(${user.id}, 3000, $(this).parents('tr'));">Удалить</a>
                </td>
            </c:if>
        </tr>
    </c:forEach>

</table>
