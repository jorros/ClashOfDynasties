<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .sortable { list-style-type: none; margin: 0; padding: 0; width: 380px; min-height:260px; }
    .sortable li { margin: 3px 3px 3px 0; padding: 1px; float: left; width: 50px; height: 50px; font-size: 4em; text-align: center; }
</style>
<h1>Zusammenstellung (${formation.name})</h1>
<div id="content" style="overflow:hidden;">
    <div style="height:300px; overflow-y:auto; overflow-x:hidden;">
        <ul id="formation_list" class="sortable">
            <c:forEach items="${formation.units}" var="unit">
                <li id="unit_${unit.id}"><img src="/game/units/${unit.blueprint.id}/icon?health=${unit.health}" /></li>
            </c:forEach>
        </ul>
    </div>
</div>