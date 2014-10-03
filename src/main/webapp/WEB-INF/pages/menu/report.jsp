<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<style>
    .sortable { list-style-type: none; margin: 0; padding: 0; width: 380px; min-height:260px; }
    .sortable li { margin: 3px 3px 3px 0; padding: 1px; float: left; width: 50px; height: 50px; font-size: 4em; text-align: center; }
</style>
<h1>Kriegsbericht (${city.name})</h1>
<div id="content" style="overflow-y: hidden;">
    <div style="float:left;">
        <select id="formation_selector" style="width:400px; height:30px; margin-left:10px; margin-bottom:10px;">
            <option selected value="city">${city.name}</option>
            <c:forEach items="${formations}" var="formation">
            <option value="${formation.id}">${formation.name} <c:if test="${formation.player == player}">(Eigene)</c:if><c:if test="${alliedForces.contains(formation.player) && formation.player != player}">(Verbündet)</c:if><c:if test="${hostileForces.contains(formation.player)}">(Feindlich)</c:if></option>
            </c:forEach>
        </select>
        <div class="section" style="width:400px; margin-bottom:20px;">
            <h4>Zusammenstellung</h4>
            <div style="height:320px; overflow-y:auto; overflow-x:hidden;">
                <ul id="formation_city" class="sortable">
                    <li id="unit_city"><img src="/game/units/0/icon?health=${city.health}" /></li>
                    <c:forEach items="${city.buildings}" var="building">
                        <c:if test="${building.blueprint.defencePoints > 0}">
                            <li id="unit_${building.id}"><img src="/game/buildings/${building.blueprint.id}/icon?health=${building.health}" /></li>
                        </c:if>
                    </c:forEach>
                    <c:forEach items="${city.units}" var="unit">
                        <li id="unit_${unit.id}"><img src="/game/units/${unit.blueprint.id}/icon?health=${unit.health}" /></li>
                    </c:forEach>
                </ul>
                <c:forEach items="${formations}" var="formation">
                <ul id="formation_${formation.id}" class="sortable" style="display:none;">
                    <c:forEach items="${formation.units}" var="unit">
                    <li id="unit_${unit.id}"><img src="/game/units/${unit.blueprint.id}/icon?health=${unit.health}" /></li>
                    </c:forEach>
                </ul>
                </c:forEach>
            </div>
        </div>
    </div>
    <div style="float:right;">
        <div class="section" style="width:450px; margin-bottom:20px;">
            <h4>Verluste</h4>
            <div style="overflow-y:hidden; overflow-x:hidden;">
                <canvas id="totalLosses" width="400" height="150"></canvas>
            </div>
        </div>
    </div>
    <div style="float:right;">
        <div class="section" style="width:450px; margin-bottom:20px;">
            <h4>Kriegsparteien</h4>
            <div style="height:65px; overflow-y:auto; overflow-x:hidden;">
                <table style="width:100%;">
                    <tr>
                        <td style="width:50%;">
                            <c:forEach items="${alliedForces}" var="ally">
                                <c:choose>
                                    <c:when test="${ally == player}">
                                        <span class="blue">${ally.name} (${ally.nation.name})</span><br>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="green">${ally.name} (${ally.nation.name})</span><br>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </td>
                        <td>
                            <c:forEach items="${hostileForces}" var="enemy">
                                <span class="red">${enemy.name} (${enemy.nation.name})</span><br>
                            </c:forEach>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div>
        <button onclick="closeMenu()" style="position:absolute; right:25px; bottom: 120px;">Schlie&szlig;en</button>
    </div>
</div>

<script>
    $("#formation_selector").change(function(){
        $(".sortable").hide();
        $("#formation_" + $(this).val()).show();
    });

    var lostCtx = $("#totalLosses").get(0).getContext("2d");

    new Chart(lostCtx).Bar({
        labels: [<c:forEach items="${city.report.parties}" var="party" varStatus="status">"${party.player.name}"<c:if test="${!status.last}">,</c:if></c:forEach>],
        datasets: [{
            fillColor: "#FFFFFF",
            strokeColor: "#FFFFFF",
            data: [<c:forEach items="${city.report.parties}" var="party" varStatus="status">${party.losses}<c:if test="${!status.last}">,</c:if></c:forEach>]
        }]
    });
</script>