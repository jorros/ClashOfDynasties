<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<h1>Ranking</h1>
<div id="content">
    <table class="ranking">
        <thead>
            <th>Platz</th>
            <th>Spieler</th>
            <th>Haus</th>
            <th>Wirtschaft</th>
            <th>Militär</th>
            <th>Demografie</th>
            <th>Gesamt</th>
        </thead>
        <tbody>
        <c:forEach items="${ranking}" var="position" varStatus="stat">
            <tr>
                <td <c:if test="${player == players[position]}">class="green"</c:if>>${stat.count}</td>
                <td <c:if test="${player == players[position]}">class="green"</c:if>>${players[position].name}</td>
                <td>${players[position].clan}</td>
                <td <c:if test="${highestEconomy == position}">class="green"</c:if>>${economy[position]}</td>
                <td <c:if test="${highestMilitary == position}">class="green"</c:if>>${military[position]}</td>
                <td <c:if test="${highestDemography == position}">class="green"</c:if>>${demography[position]}</td>
                <td>${total[position]}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>