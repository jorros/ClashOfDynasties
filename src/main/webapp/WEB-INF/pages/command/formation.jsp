<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${visible}">
    <c:if test="${formation.deployed && player.id == formation.player.id && formation.lastCity.player.id == player.id }">
    <button title="Formation bearbeiten" onclick="openMenu('formation?formation=${formation.id}', false)"><img src="assets/setFormation.png" /></button>
    <button title="Formation auflösen" onclick="if(window.confirm('Bist du dir sicher?')) { $.delete('game/formations/${formation.id}', function() { forceUpdate(); }); deselect(); }"><img src="assets/removeCaravan.png" /></button>
    </c:if>
    <c:if test="${!formation.deployed || player.id != formation.player.id || formation.lastCity.player.id != player.id }">
        <button title="Formationsaufstellung anzeigen" onclick="openMenu('formation?formation=${formation.id}', false)"><img src="assets/infoFormation.png" /></button>
    </c:if>
    <br><br>
    <b>${formation.name}</b>
    <table style="width:100%;">
        <tr>
            <td colspan="2">Spieler: ${formation.player.name} (${formation.player.nation.name})</td>
        </tr>
        <tr>
            <td>Stärke: ${formation.getStrength()}</td>
            <td>Leben: ${formation.getHealth()}%</td>
        </tr>
        <tr>
            <td colspan="2">Unterhaltskosten: <span class="red">${formation.costs}</span></td>
        </tr>
        <tr>
            <c:choose>
                <c:when test="${formation.isDeployed()}">
                    <td colspan="2">Stationiert in ${formation.lastCity.name}</td>
                </c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${!formation.route.roads.isEmpty()}">
                            <td colspan="2">Marschiert über ${formation.route.next.name} nach ${formation.route.target.name}</td>
                        </c:when>
                        <c:otherwise>
                            <td colspan="2">Marschiert nach ${formation.route.next.name}</td>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
        </tr>
        <c:if test="${!formation.isDeployed()}">
            <tr>
                <td colspan="2">Dauer bis Ankunft: ${time}</td>
            </tr>
        </c:if>
        <tr>
            <td></td>
            <td></td>
        </tr>
    </table>
</c:if>