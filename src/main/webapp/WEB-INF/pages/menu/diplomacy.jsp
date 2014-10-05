<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>Diplomatie</h1>
<div id="content">
    <div style="width:250px; overflow-y: auto; height: 100%; float:left;">
        <c:forEach items="${players}" var="player" varStatus="pstatus">
            <div style="cursor:pointer;" onclick="openMenu('diplomacy?pid=${player.id}');">
                <span style="font-size:20px; font-weight: bold;">${player.name}</span><br>
                <c:if test="${relations[player.id] == 0}"><span class="red">Verfeindet</span></c:if>
                <c:if test="${relations[player.id] == 1}"><span>Neutral</span></c:if>
                <c:if test="${relations[player.id] == 2}"><span class="blue">Handelspartner</span></c:if>
                <c:if test="${relations[player.id] == 3}"><span class="green">Verbündeter</span></c:if>
            </div>
            <c:if test="${!pstatus.last}">
                <hr>
            </c:if>
        </c:forEach>
    </div>
    <div style="float:right; width:650px; text-align:center;">
        <c:if test="${relation != null}">
            <h1>${otherPlayer.name}</h1><br>
            <c:if test="${relation.relation == 0}"><h2 class="red">Verfeindet</h2></c:if>
            <c:if test="${relation.relation == 1}"><h2>Neutral</h2></c:if>
            <c:if test="${relation.relation == 2}"><h2 class="blue">Handelspartner</h2></c:if>
            <c:if test="${relation.relation == 3}"><h2 class="green">Verbündeter</h2></c:if>
            <c:if test="${relation.ticksLeft != null}">
                <h3>Nur noch <c:if test="${hoursLeft > 1}">${hoursLeft} Stunden</c:if><c:if test="${hoursLeft == 1}">${hoursLeft} Stunde</c:if> <c:if test="${minutesLeft > 1}">${minutesLeft} Minuten</c:if><c:if test="${minutesLeft == 1}">${minutesLeft} Minute</c:if></h3>
            </c:if>
            <div style="width:450px; height:125px; margin:20px auto auto auto;">
                <table>
                    <tr>
                        <th style="width:152px;"><c:if test="${relation.relation == 0}">Frieden</c:if><c:if test="${relation.relation > 0}">Krieg</c:if></th>
                        <th style="width:152px;">Handelsbündnis</th>
                        <th style="width:152px;">Bündnis</th>
                    </tr>
                    <tr>
                        <td><c:if test="${relation.relation <= 1}">
                            <button style="width:100%;" id="proposeWar" <c:if test="${relation.ticksLeft != null}">disabled</c:if>>
                                <c:if test="${relation.relation == 0 && relation.pendingRelation != 1}">Vorschlagen</c:if>
                                <c:if test="${relation.relation != 1 && relation.pendingRelation == 1 && relation.pendingRelationPlayer != player}">Akzeptieren</c:if>
                                <c:if test="${relation.relation != 1 && relation.pendingRelation == 1 && relation.pendingRelationPlayer == player}">Zurücknehmen</c:if>
                                <c:if test="${relation.relation > 0}">Erklären!</c:if>
                            </button></c:if></td>
                        <td><c:if test="${relation.relation > 0 && relation.relation < 3}">
                            <button style="width:100%;" id="proposeTrading" <c:if test="${relation.ticksLeft != null}">disabled</c:if>>
                            <c:if test="${relation.relation != 2 && relation.pendingRelation != 2}">Vorschlagen</c:if>
                            <c:if test="${relation.relation != 2 && relation.pendingRelation == 2 && relation.pendingRelationPlayer != player}">Akzeptieren</c:if>
                            <c:if test="${relation.relation == 2}">Auflösen</c:if>
                            <c:if test="${relation.relation != 2 && relation.pendingRelation == 2 && relation.pendingRelationPlayer == player}">Zurücknehmen</c:if>
                            </button></c:if></td>
                        <td><c:if test="${relation.relation > 0}">
                            <button style="width:100%;" id="proposeAlliance" <c:if test="${relation.ticksLeft != null}">disabled</c:if>>
                            <c:if test="${relation.relation != 3 && relation.pendingRelation != 3}">Vorschlagen</c:if>
                            <c:if test="${relation.relation != 3 && relation.pendingRelation == 3 && relation.pendingRelationPlayer != player}">Akzeptieren</c:if>
                            <c:if test="${relation.relation == 3}">Auflösen</c:if>
                            <c:if test="${relation.relation != 3 && relation.pendingRelation == 3 && relation.pendingRelationPlayer == player}">Zurücknehmen</c:if>
                        </button></c:if></td>
                    </tr>
                    <tr>
                        <td>
                            <c:if test="${relation.relation != 1 && relation.pendingRelation == 1 && relation.pendingRelationPlayer != player}"><button style="width:100%;" id="declinePeace">Ablehnen</button></c:if>
                        </td>
                        <td><c:if test="${relation.relation > 0}">
                            <c:if test="${relation.relation != 2 && relation.pendingRelation == 2 && relation.pendingRelationPlayer != player}"><button style="width:100%;" id="declineTrading">Ablehnen</button></c:if>
                        </c:if></td>
                        <td><c:if test="${relation.relation > 0}">
                            <c:if test="${relation.relation != 3 && relation.pendingRelation == 3 && relation.pendingRelationPlayer != player}"><button style="width:100%;" id="declineAlliance">Ablehnen</button></c:if>
                        </c:if></td>
                    </tr>
                </table>
            </div>
            <h2 style="margin-top:20px;">Handelswege</h2>
            <table class="ranking" style="width:100%;">
                <tr>
                    <th>Name</th>
                    <th>Von</th>
                    <th>Nach</th>
                    <th>Ware (Hin)</th>
                    <th>Ware (Zurück)</th>
                    <th></th>
                </tr>
                <c:forEach items="${relation.pendingCaravans}" var="caravan">
                    <tr>
                        <td>${caravan.name}</td>
                        <td>${caravan.point1.name}</td>
                        <td>${caravan.point2.name}</td>
                        <td>${caravan.point1Load}t ${caravan.point1Item.name}</td>
                        <td>${caravan.point2Load}t ${caravan.point2Item.name}</td>
                        <td><c:if test="${caravan.point1.player == player}"><button id="dipl_removepending_${caravan.id}" style="height:30px;">Zurücknehmen</button></c:if><c:if test="${caravan.point1.player != player}"><button id="dipl_accept_${caravan.id}" style="height:30px;">Ja</button> <button id="dipl_decline_${caravan.id}" style="height:30px;">Nein</button></c:if></td>
                    </tr>
                </c:forEach>
                <c:forEach items="${relation.caravans}" var="caravan">
                    <tr>
                        <td>${caravan.name}</td>
                        <td>${caravan.point1.name}</td>
                        <td>${caravan.point2.name}</td>
                        <td>${caravan.point1Load}t ${caravan.point1Item.name}</td>
                        <td>${caravan.point2Load}t ${caravan.point2Item.name}</td>
                        <td><c:if test="${!caravan.terminate}"><button id="dipl_terminate_${caravan.id}" style="height:30px;">Auflösen</button></c:if></td>
                    </tr>
                </c:forEach>
            </table>

            <script>
                $("#proposeWar").click(function() {
                    $.put("/game/players/relation", { pid: "${otherPlayer.id}", pendingRelation: 0, accept: true }, function() {
                        openMenu("diplomacy?pid=${otherPlayer.id}");
                    });
                });

                $("#proposeTrading").click(function() {
                    $.put("/game/players/relation", { pid: "${otherPlayer.id}", pendingRelation: 2, accept: true }, function() {
                        openMenu("diplomacy?pid=${otherPlayer.id}");
                    });
                });

                $("#proposeAlliance").click(function() {
                    $.put("/game/players/relation", { pid: "${otherPlayer.id}", pendingRelation: 3, accept: true }, function() {
                        openMenu("diplomacy?pid=${otherPlayer.id}");
                    });
                });

                $("#declineTrading").click(function() {
                    $.put("/game/players/relation", { pid: "${otherPlayer.id}", pendingRelation: 2, accept: false }, function() {
                        openMenu("diplomacy?pid=${otherPlayer.id}");
                    });
                });

                $("#declineAlliance").click(function() {
                    $.put("/game/players/relation", { pid: "${otherPlayer.id}", pendingRelation: 3, accept: false }, function() {
                        openMenu("diplomacy?pid=${otherPlayer.id}");
                    });
                });

                $("#declinePeace").click(function() {
                    $.put("/game/players/relation", { pid: "${otherPlayer.id}", pendingRelation: 0, accept: false }, function() {
                        openMenu("diplomacy?pid=${otherPlayer.id}");
                    });
                });

                <c:forEach items="${relation.pendingCaravans}" var="caravan">
                    $("#dipl_removepending_${caravan.id}").click(function() {
                        $.delete('game/caravans/${caravan.id}', function() {
                            openMenu("diplomacy?pid=${otherPlayer.id}");
                        });
                    });
                    $("#dipl_accept_${caravan.id}").click(function() {
                        $.put('game/caravans/${caravan.id}/accept', function() {
                            openMenu("diplomacy?pid=${otherPlayer.id}");
                        });
                    });
                    $("#dipl_decline_${caravan.id}").click(function() {
                        $.delete('game/caravans/${caravan.id}', function() {
                            openMenu("diplomacy?pid=${otherPlayer.id}");
                        });
                    });
                </c:forEach>

                <c:forEach items="${relation.caravans}" var="caravan">
                    $("#dipl_terminate_${caravan.id}").click(function() {
                        $.delete("game/caravans/${caravan.id}");
                        openMenu("diplomacy?pid=${otherPlayer.id}");
                    });
                </c:forEach>
            </script>
        </c:if>
    </div>
</div>
