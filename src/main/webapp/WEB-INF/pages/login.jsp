<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <title>Clash of Dynasties</title>
    <link rel="stylesheet" href="/css/theme.css" type="text/css">
    <script type="text/javascript" src="/js/jquery-2.0.3.js"></script>
</head>
<body>
<div id="wrapper">
    <div id="header">
        <ul>
            <li class="menus">
                <a id="menu1" href="/fame">hall of fame</a>
            </li>
            <li class="menus">
                <a id="menu2" href="/register">registrieren</a>
            </li>
            <li class="selected menus">
                <a id="menu3" href="/">login</a>
            </li>
            <li class="menus">
                <a id="menu4" href="/renewpw">passwort</a>
            </li>
            <li class="menus">
                <a id="menu5" href="/stat">statistik</a>
            </li>
        </ul>
    </div>
    <div id="body">
            <div id="content">
                <div class="section">
                    <div style="width:440px; height:191px; margin:0 auto;">
                        <h3>Login</h3>
                        <div>
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
                        </div>
                        <c:if test="${'fail' eq param.auth}">
                            <div style="color:red">
                                Fehler beim Login!<br />
                                Grund : ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
    </div>
</div>
</body>
</html>