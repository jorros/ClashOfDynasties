<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<h1>Ranking</h1>
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
            <tr <c:if test="${rankedPlayer.hasLost()}">style="text-decoration: line-through;"</c:if>>
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