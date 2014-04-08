<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<button onclick="openMenu('game/menu/infoformation?formation=${formation.id}')"><img src="assets/infoFormation.png" /></button>
<c:if test="${formation.deployed && player.id == formation.player.id && formation.lastCity.player.id == player.id }">
<button onclick="openMenu('game/menu/formation?formation=${formation.id}')"><img src="assets/setFormation.png" /></button>
<button onclick="if(window.confirm('Bist du dir sicher?')) { $.get('game/formations/remove?formation=${formation.id}'); deselect(); }"><img src="assets/removeCaravan.png" /></button>
</c:if>
<br><br>
<table style="width:100%;">
    <tr>
        <td colspan="2">Spieler: ${formation.player.name} (${formation.player.nation.name})</td>
    </tr>
    <tr>
        <td colspan="2">Kampfstärke: 150</td>
    </tr>
    <tr>
        <td colspan="2">Leben: ${formation.getHealth()}</td>
    </tr>
    <tr>
        <c:if test="${formation.isDeployed()}">
            <td colspan="2">Stationiert in ${formation.lastCity.name}</td>
        </c:if>
        <c:if test="${!formation.isDeployed()}">
            <c:if test="${!formation.route.roads.isEmpty()}">
                <td colspan="2">Marschiert über ${formation.route.next.name} nach ${formation.route.roads[formation.route.roads.size() - 1]}</td>
            </c:if>
            <c:if test="${formation.route.roads.isEmpty()}">
                <td colspan="2">Marschiert nach ${formation.route.next.name}</td>
            </c:if>
        </c:if>
    </tr>
    <c:if test="${!formation.isDeployed()}">
        <tr>
            <td colspan="2">Dauer bis Ankunft: ${formation.route.time}</td>
        </tr>
    </c:if>
    <tr>
        <td></td>
        <td></td>
    </tr>
</table>