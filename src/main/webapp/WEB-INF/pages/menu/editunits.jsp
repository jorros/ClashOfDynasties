<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cod" uri="/WEB-INF/clashofdynasties.tld" %>

<h1>Einheiten</h1>
<div id="content">
    <table style="text-align:center; width:100%;">
        <tr>
            <th></th>
            <th>Beschreibung</th>
            <th>Geschwindigkeit</th>
            <th>Kampfstärke</th>
            <th>Kosten</th>
            <th>Produktion</th>
        </tr>
        <c:forEach items="${unitBlueprints}" var="bp">
        <tr>
            <td style="font-weight: bold;"><c:out value="${bp.name}" /> (<c:if test="${bp.type == 1}">Infanterie</c:if><c:if test="${bp.type == 2}">Fernkampf</c:if><c:if test="${bp.type == 3}">Reiter</c:if><c:if test="${bp.type == 4}">Belagerung</c:if>):</td>
            <td><textarea id="${bp.id}_desc" style="width:300px; height:70px;">${bp.description}</textarea></td>
            <td><input id="${bp.id}_speed" style="width:60px; height:10px;" type="text" value="<cod:Number value="${bp.speed}"/>" /></td>
            <td><input id="${bp.id}_strength" style="width:50px; height:10px;" type="text" value="${bp.strength}" /></td>
            <td><input id="${bp.id}_price" style="width:50px; height:10px;" type="text" value="${bp.price}" /></td>
            <td><input id="${bp.id}_production" style="width:80px; height:10px;" type="text" value="${bp.requiredProduction}" /></td>
        </tr>
        </c:forEach>
    </table>
    <script>
        <c:forEach items="${unitBlueprints}" var="bp">
        $("#${bp.id}_desc").change(function() {
            $.put("/game/units/${bp.id}", { "description": $(this).val() } );
        });
        $("#${bp.id}_speed").change(function() {
            $.put("/game/units/${bp.id}", { "speed": $(this).val() } );
        });
        $("#${bp.id}_strength").change(function() {
            $.put("/game/units/${bp.id}", { "strength": $(this).val() } );
        });
        $("#${bp.id}_price").change(function() {
            $.put("/game/units/${bp.id}", { "price": $(this).val() } );
        });
        $("#${bp.id}_production").change(function() {
            $.put("/game/units/${bp.id}", { "production": $(this).val() } );
        });
        </c:forEach>
    </script>
</div>