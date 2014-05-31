<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>Diplomatie</h1>
<c:if test="${!isActivated}">
    <div id="content" style="text-align: center;">
        <h1 style="margin-top: 180px;">Zurzeit existieren keine Herrschaftshäuset</h1>
        <p style="margin-top: 20px;">Gleichberechtigung, yay! In 7 Tagen werden die 3 besten Spieler auserkoren ein Haus anzuführen.</p>
    </div>
</c:if>
<c:if test="${isActivated}">
    <div id="content">
        <c:if test="${player.clan != null}">

        </c:if>
        <c:if test="${player.clan == null}">
            <h1>Entscheide dich für ein Haus</h1>
            <table style="width:800px; height: 300px; margin-top:60px; margin-left:auto; margin-right:auto; border-spacing: 80px 5px;">
                <tr>
                    <th style="width:50%;">Haus jorros</th>
                    <th style="width:50%;">Haus pussyslayer</th>
                </tr>
                <tr>
                    <td>3 Vasallen<br>Hauptstadt: Lorem</td>
                    <td>0 Vasallen<br>Hauptstadt: Dolorem</td>
                </tr>
                <tr>
                    <td><button style="width:100%;">Wählen</button></td>
                    <td><button style="width:100%;">Wählen</button></td>
                </tr>
            </table>
        </c:if>
    </div>
</c:if>

