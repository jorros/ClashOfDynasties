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
        <td></td>
        <td></td>
    </tr>
</table>