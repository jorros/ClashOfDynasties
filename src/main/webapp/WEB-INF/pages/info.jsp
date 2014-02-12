<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<button><img src="assets/setCaravan.png" /></button>
<button><img src="assets/setFormation.png" /></button>
<button><img src="assets/showItems.png" /></button>
<br><br>
<table style="width:100%;">
    <tr>
        <td colspan="2">Spieler: ${city.player.name} (${city.player.nation.name})</td>
    </tr>
    <tr>
        <td style="width:45%;">Population: ${ city.population }</td>
        <td>Zustimmung: <c:if test="${satisfaction != null}"><img src="assets/${satisfaction}32.png" style="width:16px; height:16px;" />(${city.satisfaction}%)</c:if><c:if test="${satisfaction == null}">?</c:if></td>
    </tr>
    <c:if test="${ city.player == player }">
    <tr>
        <td colspan="2">Bilanz: 3 (<span class="green">10</span>/<span class="red">7</span>)</td>
    </tr>
    </c:if>
    <tr>
        <td></td>
    </tr>
    <tr>
        <td colspan="2"><span style="vertical-align: bottom;">Wertvolle Resource: </span><img src="assets/resources/${city.resource.id}.png" /> ${city.resource.name}</td>
    </tr>
    <tr>
        <td colspan="2">Stadttyp: ${city.type.name} (${city.biome.name})</td>
    </tr>
    <c:if test="${ city.player == player }">
    <tr>
        <td colspan="2">Freie Bauplätze: ${freeSlots} von ${maxSlots}</td>
    </tr>
    </c:if>
</table>