<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${player == city.player}">
<button><img src="assets/setCaravan.png" /></button>
<button onclick="openMenu('formation?city=${city.id}')"><img src="assets/setFormation.png" /></button>
</c:if>
<button onclick="openMenu('store?city=${city.id}')"><img src="assets/showItems.png" /></button>
<br><br>
<table style="width:100%;">
    <tr>
        <td colspan="2">Spieler: ${city.player.name} (${city.player.nation.name})</td>
    </tr>
    <c:if test="${city.player.id != 1}">
    <tr>
        <td style="width:45%;">Population: ${ city.population }</td>
        <td>Zustimmung: <c:if test="${satisfaction != null}"><img src="assets/satisfaction/${satisfaction}.png" style="width:16px; height:16px;" />(${city.satisfaction}%)</c:if><c:if test="${satisfaction == null}">?</c:if></td>
    </tr>
    </c:if>
    <c:if test="${ city.player == player }">
    <tr>
        <td colspan="2">Bilanz: ${city.getIncome() - city.getOutcome()} (<span class="green">${city.getIncome()}</span>/<span class="red">${city.getOutcome()}</span>)</td>
    </tr>
    </c:if>
    <tr>
        <td></td>
    </tr>
    <tr>
        <td colspan="2"><span style="vertical-align: bottom;">Wertvolle Resource: </span><img style="vertical-align:bottom;" src="assets/resources/${city.resource.id}.png" /> ${city.resource.name}</td>
    </tr>
    <tr>
        <td colspan="2">Stadttyp: ${city.type.name} (${city.biome.name})</td>
    </tr>
    <tr>
        <td colspan="2"><c:if test="${ city.player == player }">Freie </c:if>Bauplätze: <c:if test="${ city.player == player }">${freeSlots} von </c:if>${maxSlots}</td>
    </tr>
</table>