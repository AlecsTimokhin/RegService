<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<h3>Авторизация на сервисе</h3>

<c:if test="${message != null}">
    <div class="${status}">
        ${message} Код ошибки: <b>${errorCode}</b>
    </div>
</c:if>

<c:if test="${currentUser != null}">
    <div>
        Вы уже авторизованны как ${currentUser.username}!
    </div>
</c:if>

<c:if test="${currentUser == null}">
    <div>

        <form action="/login" method="post">
            <table>
                <tr>
                    <td><label>Логин:</label></td>
                    <td><input type="text" name="username" value="" placeholder="Username" /></td>
                </tr>
                <tr>
                    <td><label>Пароль:</label></td>
                    <td><input type="password" name="password" placeholder="Password" /></td>
                </tr>

                <input type="hidden" name="_csrf" value="${_csrf.token}" />

                <tr>
                    <td><input type="reset" value="Сбросить"></td>
                    <td><input type="submit" value="Войти на сервис"></td>
                </tr>
            </table>
        </form>

    </div>
</c:if>
