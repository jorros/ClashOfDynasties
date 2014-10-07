<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>Profil</h1>
<div id="content">
    <div style="float:left; width:400px;">
        <table>
            <tr>
                <td><label for="email">eMail:</label></td>
                <td><input type="text" id="email" value="${player.email}" /></td>
            </tr>
            <tr>
                <td colspan="2"><hr><h2>Passwort ändern</h2></td>
            </tr>
            <tr>
                <td><label for="oldpw">Altes Passwort:</label></td>
                <td><input type="text" id="oldpw" /><br></td>
            </tr>
            <tr>
                <td><label for="newpw">Neues Passwort:</label></td>
                <td><input type="text" id="newpw" /><br></td>
            </tr>
            <tr>
                <td><label for="newpw2">Neues Passwort wdh.:</label></td>
                <td><input type="text" id="newpw2" /></td>
            </tr>
        </table>
    </div>
    <div style="float:right; width:400px;">
        <h2>eMail Benachrichtigungen</h2>
        <div style="overflow-y: auto; height:350px;">
            <table>
                <tr>
                    <td><input type="checkbox" id="notificationCityConquered" <c:if test="${player.hasNotification(\"CityConquered\")}">checked</c:if> /></td>
                    <td><label for="notificationCityConquered">Du hast eine Stadt erobert</label></td>
                </tr>
                <tr>
                    <td><input type="checkbox" id="notificationCityLost" <c:if test="${player.hasNotification(\"CityLost\")}">checked</c:if> /></td>
                    <td><label for="notificationCityLost">Du hast eine Stadt verloren</label></td>
                </tr>
                <tr>
                    <td><input type="checkbox" id="notificationCityUpgrade" <c:if test="${player.hasNotification(\"CityUpgrade\")}">checked</c:if> /></td>
                    <td><label for="notificationCityUpgrade">Deine Stadt ist aufgestiegen</label></td>
                </tr>
                <tr>
                    <td><input type="checkbox" id="notificationDiplomaticAlliance" <c:if test="${player.hasNotification(\"DiplomaticAlliance\")}">checked</c:if> /></td>
                    <td><label for="notificationDiplomaticAlliance">Dir wird ein Allianzvorschlag unterbreitet</label></td>
                </tr>
                <tr>
                    <td><input type="checkbox" id="notificationDiplomaticPeace" <c:if test="${player.hasNotification(\"DiplomaticPeace\")}">checked</c:if> /></td>
                    <td><label for="notificationDiplomaticPeace">Dir wird ein Friedensvorschlag unterbreitet</label></td>
                </tr>
                <tr>
                    <td><input type="checkbox" id="notificationDiplomaticTrade" <c:if test="${player.hasNotification(\"DiplomaticTrade\")}">checked</c:if> /></td>
                    <td><label for="notificationDiplomaticTrade">Dir wird ein Handelspartnervorschlag unterbreitet</label></td>
                </tr>
                <tr>
                    <td><input type="checkbox" id="notificationDiplomaticWar" <c:if test="${player.hasNotification(\"DiplomaticWar\")}">checked</c:if> /></td>
                    <td><label for="notificationDiplomaticWar">Du erhälst eine Kriegserklärung</label></td>
                </tr>
                <tr>
                    <td><input type="checkbox" id="notificationDisease" <c:if test="${player.hasNotification(\"Disease\")}">checked</c:if> /></td>
                    <td><label for="notificationDisease">In deiner Stadt gibt es eine Seuche</label></td>
                </tr>
                <tr>
                    <td><input type="checkbox" id="notificationFire" <c:if test="${player.hasNotification(\"Fire\")}">checked</c:if> /></td>
                    <td><label for="notificationFire">In deiner Stadt brennt es</label></td>
                </tr>
                <tr>
                    <td><input type="checkbox" id="notificationLoss" <c:if test="${player.hasNotification(\"Loss\")}">checked</c:if> /></td>
                    <td><label for="notificationLoss">Du hast eine Formation verloren</label></td>
                </tr>
                <tr>
                    <td><input type="checkbox" id="notificationNewMessage" <c:if test="${player.hasNotification(\"NewMessage\")}">checked</c:if> /></td>
                    <td><label for="notificationNewMessage">Du hast eine Nachricht erhalten</label></td>
                </tr>
                <tr>
                    <td><input type="checkbox" id="notificationProductionReady" <c:if test="${player.hasNotification(\"ProductionReady\")}">checked</c:if> /></td>
                    <td><label for="notificationProductionReady">Deine Produktion (Gebäude/Einheit) ist fertig</label></td>
                </tr>
                <tr>
                    <td><input type="checkbox" id="notificationTrade" <c:if test="${player.hasNotification(\"Trade\")}">checked</c:if> /></td>
                    <td><label for="notificationTrade">Dir wird ein Handelsvorschlag unterbreitet</label></td>
                </tr>
                <tr>
                    <td><input type="checkbox" id="notificationTradeNotEnoughLoaded" <c:if test="${player.hasNotification(\"TradeNotEnoughLoaded\")}">checked</c:if> /></td>
                    <td><label for="notificationTradeNotEnoughLoaded">Dein Handelspartner hat nicht genug Ware eingeladen</label></td>
                </tr>
                <tr>
                    <td><input type="checkbox" id="notificationWar" <c:if test="${player.hasNotification(\"War\")}">checked</c:if> /></td>
                    <td><label for="notificationWar">Deine Stadt wird angegriffen</label></td>
                </tr>
            </table>
        </div>
    </div>
    <div>
        <button onclick="save();" style="position:absolute; right:25px; bottom: 120px;">Speichern</button>
    </div>
</div>
<script>
    function save() {
        if($("#newpw").val() != $("#newpw2").val()) {
            alert("Die neuen Passwörter stimmen nicht überein!");
        }

        var data = {
            email: $("#email").val(),
            oldpw: $("#oldpw").val(),
            newpw: $("#newpw").val(),
            newpw2: $("#newpw2").val(),
            CityConquered: $("#notificationCityConquered").is(":checked"),
            CityLost: $("#notificationCityLost").is(":checked"),
            CityUpgrade: $("#notificationCityUpgrade").is(":checked"),
            DiplomaticAlliance: $("#notificationDiplomaticAlliance").is(":checked"),
            DiplomaticPeace: $("#notificationDiplomaticPeace").is(":checked"),
            DiplomaticTrade: $("#notificationDiplomaticTrade").is(":checked"),
            DiplomaticWar: $("#notificationDiplomaticWar").is(":checked"),
            Disease: $("#notificationDisease").is(":checked"),
            Fire: $("#notificationFire").is(":checked"),
            Loss: $("#notificationLoss").is(":checked"),
            NewMessage: $("#notificationNewMessage").is(":checked"),
            ProductionReady: $("#notificationProductionReady").is(":checked"),
            Trade: $("#notificationTrade").is(":checked"),
            TradeNotEnoughLoaded: $("#notificationTradeNotEnoughLoaded").is(":checked"),
            War: $("#notificationWar").is(":checked")
        };

        $.put("/game/players/", data);
    }
</script>