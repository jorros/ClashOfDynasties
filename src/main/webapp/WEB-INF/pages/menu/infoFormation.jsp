<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .sortable { list-style-type: none; margin: 0; padding: 0; width: 380px; min-height:260px; }
    .sortable li { margin: 3px 3px 3px 0; padding: 1px; float: left; width: 50px; height: 50px; font-size: 4em; text-align: center; }
</style>
<h1>Zusammenstellung (${name})</h1>
<div id="content" style="overflow:hidden;">
    <div style="height:340px; overflow-y:auto; overflow-x:hidden;">
        <ul id="formation_list" class="sortable" style="width:100%;">
            <c:forEach items="${units}" var="unit">
                <li id="unit_${unit.id}"><img src="/game/units/${unit.blueprint.id}/icon?health=${unit.health}" /></li>
            </c:forEach>
        </ul>
    </div>
    <div class="formationHelp" style="margin-top:4px;">
        <table>
            <c:forEach items="${unitsTotal}" var="unit">
                <tr>
                    <td>${unit.value}x</td>
                    <td>${unit.key} <c:if test="${unitsInjured[unit.key] > 0}"><span class="red">(${unitsInjured[unit.key]} verletzt)</span></c:if></td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <div>
        <button onclick="closeMenu()" style="position:absolute; right:25px; bottom: 120px;">Schlie&szlig;en</button>
    </div>
</div>