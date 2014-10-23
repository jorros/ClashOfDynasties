<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${visible}">
    <c:if test="${player == city.player}">
    <button title="Bauen" onclick="openMenu('build?city=${city.id}&demography=0')"><img src="assets/build.png" /></button>
    <button title="Karawane erstellen" <c:if test="${!canTrade}">disabled</c:if> onclick="isCaravanSelected=true; $('#caravanText').show();"><img src="assets/setCaravan.png" /></button>
    <button title="Formation erstellen" <c:if test="${empty city.units}">disabled</c:if> onclick="openMenu('formation?city=${city.id}', false)"><img src="assets/setFormation.png" /></button>
    </c:if>
    <button title="Lager öffnen" onclick="openMenu('store?city=${city.id}', false)"><img src="assets/showItems.png" /></button>
    <button title="Einheiten und Verteidigungsgebäude der Stadt anzeigen" onclick="openMenu('units?city=${city.id}', false)"><img src="assets/infoFormation.png" /></button>
    <c:if test="${city.report != null}">
        <button title="Kriegsbericht öffnen" onclick="openMenu('report?city=${city.id}', false)"><img src="assets/report.png" /></button>
    </c:if>
    <br><br>
    <b>${city.name}</b>
    <table style="width:100%;">
        <tr>
            <td colspan="2">Spieler: ${city.player.name} (${city.player.nation.name})</td>
        </tr>
        <c:if test="${!city.player.computer}">
        <tr>
            <td style="width:45%;">Population: ${ city.population }</td>
            <td>Zustimmung: <c:if test="${satisfaction != null}"><img src="assets/satisfaction/${satisfaction}.png" style="width:16px; height:16px;" />(${city.satisfaction}%)</c:if><c:if test="${satisfaction == null}">?</c:if></td>
        </tr>
        </c:if>
        <c:if test="${city.plague}">
            <tr>
                <td colspan="2" class="red">Es grassiert eine Seuche in dieser Stadt.</td>
            </tr>
        </c:if>
        <c:if test="${city.fire}">
            <tr>
                <td colspan="2" class="red">Ein Feuer wütet in dieser Stadt.</td>
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
        <c:if test="${city.type.id != 4}">
        <tr>
            <td colspan="2"><span style="vertical-align: bottom;">Wertvolle Resource: </span><img style="vertical-align:bottom;" src="assets/resources/${city.resource.id}.png" /> ${city.resource.name}</td>
        </tr>
        </c:if>
        <tr>
            <td colspan="2">Stadttyp: ${city.type.name} (${city.biome.name})</td>
        </tr>
        <c:if test="${maxSlots > 0}">
        <tr>
            <td colspan="2"><c:if test="${ city.player == player }">Freie </c:if>Bauplätze: <c:if test="${ city.player == player }">${freeSlots} von </c:if>${maxSlots}</td>
        </tr>
        </c:if>
    </table>
</c:if>