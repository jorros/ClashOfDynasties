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
        <h1>Registrieren</h1>
        <div id="content">
            <div style="width:317px; height:260px; margin: auto auto;">
                <form action="/step1" id="register" method="post">
                    <input type="hidden" name="key" value="${key}" />
                    <table>
                        <tr>
                            <td><label for="name">Name:</label></td>
                            <td><input type="text" id="name" name="name" /></td>
                        </tr>
                        <tr>
                            <td><label for="password">Passwort:</label></td>
                            <td><input type="password" id="password" name="password" /></td>
                        </tr>
                        <tr>
                            <td><label for="email">EMail:</label></td>
                            <td><input type="text" id="email" name="email" /></td>
                        </tr>
                        <tr>
                            <td colspan="2"><button type="submit" style="width:100%">Weiter</button></td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>