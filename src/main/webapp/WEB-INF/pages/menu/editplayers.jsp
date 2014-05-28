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
            <tr>
                <td style="font-weight: bold;"><c:out value="${player.name}" /> :</td>
                <td>${player.email}</td>
                <td><button id="${player.id}_delete">Löschen</button><button id="${player.id}_deleteUnits">Einheiten löschen</button><button id="${player.id}_reset">Reset</button></td>
            </tr>
        </c:forEach>
    </table>
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
                    $.put("/game/reset/${player.id}");
                }
            });
        </c:forEach>
    </script>
</div>