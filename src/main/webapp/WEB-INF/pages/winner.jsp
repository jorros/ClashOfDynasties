<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        <h1>${winner.name} hat gewonnen!</h1>
        <div id="content">
            <table class="ranking">
                <thead>
                <tr>
                    <th>Platz</th>
                    <th>Spieler</th>
                    <th>Wirtschaft</th>
                    <th>Militär</th>
                    <th>Demografie</th>
                    <th>Gesamt</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${players}" var="rankedPlayer">
                    <tr>
                        <td <c:if test="${player == rankedPlayer}">class="green"</c:if>>${rankedPlayer.statistic.rank}</td>
                        <td <c:if test="${player == rankedPlayer}">class="green"</c:if>>${rankedPlayer.name}</td>
                        <td <c:if test="${maxEconomy == rankedPlayer}">class="green"</c:if>>${rankedPlayer.statistic.economy}</td>
                        <td <c:if test="${maxMilitary == rankedPlayer}">class="green"</c:if>>${rankedPlayer.statistic.military}</td>
                        <td <c:if test="${maxDemography == rankedPlayer}">class="green"</c:if>>${rankedPlayer.statistic.demography}</td>
                        <td <c:if test="${rankedPlayer.statistic.rank == 1}">class="green"</c:if>>${rankedPlayer.statistic.total}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
