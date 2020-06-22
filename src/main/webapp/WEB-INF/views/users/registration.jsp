<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<div id="AllForm">

    <div id="message" class=""></div>

    <form class='AjaxForm' method='post' name='RegNewUser' action="/users">

        <table id="main_user_register_table">
            <tr>
                <td valign="top">

                    <table class="user_register_table">

                        <tr>
                            <td><label>Логин <span class="required">*</span></label></td>
                            <td>
                                <input type="text" name="username" value="" placeholder="Alecs" />
                                <div id="usernameError" class="invalid-feedback">
                                    ${usernameError}
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td><label>Пароль <span class="required">*</span></label></td>
                            <td>
                                <input type="password" name="password" value="" placeholder="Password" />
                                <div id="passwordError" class="invalid-feedback">
                                    ${passwordError}
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td><label>Повтор пароля <span class="required">*</span></label></td>
                            <td>
                                <input type="password" name="password2" value="" placeholder="Retype password" />
                                <div id="password2Error" class="invalid-feedback">
                                    ${password2Error}
                                </div>
                            </td>
                        </tr>

                    </table>

                </td>
            </tr>
        </table>

        <c:if test="${canDoActions == true}">
            <div>
                Роли пользователя <span class="required">*</span>
                <c:forEach items="${roles}" var="role">
                    <div>
                        <label>
                            <c:if test="${user == null}">
                                <label><input type="checkbox" name="roles" value="${role}">${role}</label>
                            </c:if>
                            <c:if test="${user != null}">
                                <label>
                                    <c:if test="${user.roles.contains(role)}">
                                        <input type="checkbox" name="roles" value="${role}" checked>
                                    </c:if>
                                    <c:if test="${!user.roles.contains(role)}">
                                        <input type="checkbox" name="roles" value="${role}">
                                    </c:if>
                                    [[${role}]]
                                </label>
                            </c:if>
                        </label>
                    </div>
                </c:forEach>
                <div id="rolesError" class="invalid-feedback">
                    ${rolesError}
                </div>
            </div>
        </c:if>

        <hr>
        <input type="hidden" name="updatePass" value="1" />
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <input type="reset" value="Сбросить">
        <!--<input type="submit" value="Регистрация">-->
        <input type="button" class="send_button" value="Регистрация">

    </form>

    <script>
        $(document).ready(function(){
            $('div#AllForm').find('.send_button').click(function(){
                SendAjaxFormRest('div#AllForm', '/rest/users', 5000, 'POST', false);
            });
        });
    </script>

</div>
