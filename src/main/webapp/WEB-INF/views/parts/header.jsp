<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<head>

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">

    <meta name="description" content="Сервис авторизации"/>

    <title>${title}</title>

    <meta id="tokenValue" name="_csrf" content="${_csrf.token}"/>
    <meta id="tokenHeaderName" name="_csrf_header" content="${_csrf.headerName}"/>

    <link rel="stylesheet" href="/static/css/style.css" type="text/css"/>

    <script src="/static/js/jquery_3.1.1_min.js"></script>

    <script src="/static/js/main.js"></script>

</head>
<body>

<div id="wrapper" align="center">
    <div id="container" align="left">

        <c:if test="${currentUser != null}">
            <c:if test="${canDoActions == true}">
                <div id="admin_pls">

                    <table width="100%">
                        <tr>
                            <td align="left">

                                Админ-панель:

                                <a href="javascript://" onclick="loadDataToModalWindow('#Registration', '/users/registration', 2000);">
                                    Новый пользователь
                                </a>

                            </td>
                            <td align="right">
                                <c:if test="${currentUser.roles != null}">
                                    <span>
                                        Роли: ${currentUser.roles}
                                    </span>
                                </c:if>
                            </td>
                        </tr>
                    </table>

                </div>
            </c:if>
        </c:if>


        <div align="center"><h2><a href="/">Сервис авторизации</a></h2></div>


        <div id="nav">

            <b><a id="mainA" href="/">Главная</a></b>

            <c:if test="${currentUser != null}">
                <b><a class="navItem" href="/users/balance">Страница баланса</a></b>
            </c:if>

            <c:if test="${canDoActions == true}">
                <b><a target="_blank" class="navItem" href="/users/all">Список Users</a></b>
                <b><a target="_blank" class="navItem" href="/rest/users">REST Users</a></b>
            </c:if>

            <c:if test="${currentUser == null}">
                <div class="noneUser">
                    <a href="javascript://" onclick="loadDataToModalWindow('#Registration', '/users/registration', 2000);">Регистрация</a>
                    <a class="navItem" href="/login">Войти на сервис</a>
                </div>
            </c:if>


            <c:if test="${currentUser != null}">

                <div class="currentUser">

                    <div id="userAll">
                        <a id="myProfile" href="javascript://" onclick="loadDataToModalWindow('#Profile', '/users/profile', 2000);"
                           title="${currentUser.active == true ? 'Ваш профиль пользователя' : 'Ваш профиль неактивный!'}"
                           style="${currentUser.active == true ? '' : 'color:#333333'}">
                            Вы вошли как: <b>${currentUser.username}</b> |
                            Ваш баланс: <b>${currentUser.balance}</b>
                        </a>

                        <a href="javascript://" onclick="$('#logout_form').submit()">Выйти</a>

                        <form id="logout_form" style="display:none" action="/logout" method="post">
                            <input type="hidden" name="_csrf" value="${_csrf.token}" />
                            <button type="submit">Выйти</button>
                        </form>

                    </div>

                </div>


            </c:if>

        </div>

        <!-- Само модальное окно регистрации нового пользователя -->
        <div class="modal_window">
            <div id="Registration" class="window">
                <div class="top">
                    <div class="name">Регистрация нового пользователя</div>
                    <div class="close" title="Закрыть">X</div>
                    <div style="clear:both"></div>
                </div>
                <div class="content"></div>
            </div>
        </div>

        <!-- Само модальное окно профиля текущего пользователя -->
        <div class="modal_window">
            <div id="Profile" class="window">
                <div class="top">
                    <div class="name">Ваш профиль пользователя</div>
                    <div class="close" title="Закрыть">X</div>
                    <div style="clear:both"></div>
                </div>
                <div class="content"></div>
            </div>
        </div>