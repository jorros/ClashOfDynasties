<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${player == caravan.player }">
<button onclick="openMenu('caravan?caravan=${caravan.id}')"><img src="assets/setCaravan.png" /></button>
<button onclick="if(window.confirm('Bist du dir sicher?')) { $.delete('game/caravans/${caravan.id}'); deselect(); }"><img src="assets/removeCaravan.png" /></button>
</c:if>
<br><br>
<table style="width:100%;">
    <tr>
        <td colspan="2">Spieler: ${caravan.player.name} (${caravan.player.nation.name})</td>
    </tr>
    <tr>
        <td colspan="2">Unterhaltskosten: <span class="red">1</span></td>
    </tr>
    <tr>
        <td colspan="2">Handelsweg: ${caravan.point1.name} bis ${caravan.point2.name}</td>
    </tr>
    <c:if test="${caravan.route.next == caravan.route.target}">
        <tr>
            <td colspan="2">Reist nach ${caravan.route.target.name}</td>
        </tr>
    </c:if>
    <c:if test="${caravan.route.next != caravan.route.target}">
        <tr>
            <td colspan="2">Reist über ${caravan.route.next.name} nach ${caravan.route.target.name}</td>
        </tr>
    </c:if>
    <tr>
        <td colspan="2">Dauer bis Ankunft: ${time}</td>
    </tr>
    <c:if test="${caravan.terminate}">
    <tr>
        <td colspan="2" class="red">Wird bei Ankunft aufgelöst.</td>
    </tr>
    </c:if>
    <tr>
        <td></td>
        <td></td>
    </tr>
</table>