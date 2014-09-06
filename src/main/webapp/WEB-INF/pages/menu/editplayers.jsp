<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h1>Spieler</h1>
<div id="content">
    <table style="text-align:center; width:100%;">
        <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Aktionen</th>
        </tr>
        <c:forEach items="${players}" var="player">
            <c:if test="${!player.computer}">
            <tr>
                <td style="font-weight: bold;"><c:out value="${player.name}" /> :</td>
                <td>${player.email}</td>
                <td><button id="${player.id}_delete">Löschen</button><c:if test="${player.activated}"><button id="${player.id}_deleteUnits">Einheiten löschen</button><button id="${player.id}_reset">Reset</button></c:if><c:if test="${!player.activated}"><button id="${player.id}_link">Link</button></c:if></td>
            </tr>
            </c:if>
        </c:forEach>
    </table>
    <button id="players_add">Spieler hinzufügen</button>
    <script>
        <c:forEach items="${players}" var="player">
            $("#${player.id}_delete").click(function() {
                if(window.confirm("Spieler wirklich löschen?")) {
                    $.delete("/game/players/${player.id}");
                }
            });
            $("#${player.id}_deleteUnits").click(function() {
                if(window.confirm("Einheiten wirklich löschen?")) {
                    $.delete("/game/players/${player.id}/units");
                }
            });
            $("#${player.id}_reset").click(function() {
                if(window.confirm("Spieler wirklich resetten?")) {
                    $.put("/game/players/${player.id}/reset");
                }
            });
            $("#${player.id}_link").click(function() {
                window.prompt("Registrierungslink:", "http://www.clashofdynasties.de/register?key=${player.id}");
            });
        </c:forEach>

        $("#players_add").click(function() {
            $.post("/game/players/", function() {
                openMenu("editplayers");
            });
        });
    </script>
</div>