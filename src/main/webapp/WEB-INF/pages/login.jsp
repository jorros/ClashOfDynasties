<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <title>Clash of Dynasties</title>
    <link rel="stylesheet" href="/css/theme.css" type="text/css">
    <script type="text/javascript" src="/lib/jquery-2.1.0.js"></script>
</head>
<body>
<div id="wrapper">
    <div id="body">
        <h1>Login</h1>
        <div id="content">
            <div style="width:317px; height:260px; margin: auto auto;">
                <form action="/login" id="login" method="post">
                    <table>
                        <tr>
                            <td><label for="username">Benutzer:</label></td>
                            <td><input type="text" id="username" name="username" /></td>
                        </tr>
                        <tr>
                            <td><label for="password">Passwort:</label></td>
                            <td><input type="password" id="password" name="password" /></td>
                        </tr>
                        <tr>
                            <td colspan="2"><label for="rememberme">Angemeldet bleiben?</label><input type="checkbox" id="rememberme" /></td>
                        </tr>
                        <tr>
                            <td colspan="2"><a href="/resetpassword">Passwort vergessen?</a></td>
                        </tr>
                        <tr>
                            <td colspan="2"><button type="submit" style="width:100%">Einloggen</button></td>
                        </tr>
                    </table>
                </form>
                <c:if test="${'fail' eq param.auth}">
                    <div style="color:red">
                        ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>
</body>
</html>