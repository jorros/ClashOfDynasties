<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<button onclick="openMenu('game/menu/formation?formation=${formation.id}')"><img src="assets/setFormation.png" /></button>
<br><br>
<table style="width:100%;">
    <tr>
        <td colspan="2">Spieler: ${formation.player.name} (${formation.player.nation.name})</td>
    </tr>
    <tr>
        <td colspan="2">Kampfstärke: 150</td>
    </tr>
</table>