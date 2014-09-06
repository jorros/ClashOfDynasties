<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<button style="float:left;" onclick="SelectionMode = 0; $('#cr-stage').css('cursor', 'default');">Auswahl</button>
<button style="margin-left:5px; float:left;" onclick="SelectionMode = 1; $('#cr-stage').css('cursor', 'url(assets/cities/1.png), crosshair');">Stadt</button>
<button style="margin-left:5px; float:left;" onclick="SelectionMode = 2; $('#cr-stage').css('cursor', 'crosshair');">Weg</button>
<br><br>
<table style="width:100%;">
    <tr>
        <td colspan="2">Spieler: ${city.player.name} (${city.player.nation.name})</td>
    </tr>
    <tr>
        <td style="width:45%;">Stadtname:</td>
        <td><input style="height:10px;" id="name" type="text" value="${city.name}" /></td>
    </tr>
    <tr>
        <td>Bauplätze:</td>
        <td><input style="height:10px;" id="capacity" type="text" value="${city.capacity}" /></td>
    </tr>
    <tr>
        <td>Stadttyp:</td>
        <td><select id="type"><c:forEach items="${types}" var="type"><option <c:if test="${type.id == city.type.id}">selected</c:if> value="${type.id}">${type.name}</option></c:forEach></select></td>
    </tr>
    <tr>
        <td>Wertvolle Ressource:</td>
        <td><select id="resource"><c:forEach items="${resources}" var="resource"><option <c:if test="${resource.id == city.resource.id}">selected</c:if> value="${resource.id}">${resource.name}</option></c:forEach></select></td>
    </tr>
    <tr>
        <td>Biom:</td>
        <td><select id="biome"><c:forEach items="${biomes}" var="biome"><option <c:if test="${biome == city.biome}">selected</c:if> value="${biome.id}">${biome.name}</option></c:forEach></select></td>
    </tr>
    <tr>
        <td>Spieler:</td>
        <td><select id="player"><c:forEach items="${players}" var="player"><option <c:if test="${player == city.player}">selected</c:if> value="${player.id}">${player.name}</option></c:forEach></select></td>
    </tr>
    <tr>
        <td colspan="2"><button onclick="$.put('/game/cities/${city.id}', { name: $('#name').val(), capacity: $('#capacity').val(), 'type': $('#type').val(), resource: $('#resource').val(), biome: $('#biome').val(), player: $('#player').val() }, function() { forceUpdate(); });">Speichern</button> <button onclick="$.delete('/game/cities/${city.id}', function() { forceUpdate(); })">Entfernen</button> <button onclick="$.post('/game/cities/${city.id}/reset', function() { forceUpdate(); })">Reset</button></td>
    </tr>
</table>