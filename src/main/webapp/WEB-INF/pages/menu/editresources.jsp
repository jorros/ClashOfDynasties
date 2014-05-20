<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h1>Ressourcen</h1>
<div id="content">
    <table style="text-align:center; width:100%;">
        <tr>
            <th></th>
            <th>Beschreibung</th>
            <th>Voraussetzungen</th>
            <th>Item</th>
            <th>Rate</th>
        </tr>
        <c:forEach items="${buildingBlueprints}" var="bp">
            <tr>
                <td style="font-weight: bold;"><c:out value="${bp.name}" /> :</td>
                <td><c:out value="${bp.description}"></c:out></td>
                <td><c:if test="${bp.requiredResource != null}"><c:out value="${bp.requiredResource.name}" />, </c:if><c:forEach items="${bp.requiredBiomes}" var="biome">${biome.name}, </c:forEach></td>
                <td><select id="${bp.id}_item"><option <c:if test="${bp.produceItem == null}">selected</c:if> value="0">Kein</option><c:forEach items="${items}" var="item"><option <c:if test="${item == bp.produceItem}">selected</c:if> value="${item.id}">${item.name}</option></c:forEach></select></td>
                <td><input id="${bp.id}_pps" style="width:30px; height:10px;" type="text" value="${bp.producePerStep}" /></td>
            </tr>
        </c:forEach>
    </table>
    <script>
        <c:forEach items="${buildingBlueprints}" var="bp">
        $("#${bp.id}_pps").change(function() {
            $.put("/game/buildings/${bp.id}", { "pps": $(this).val() });
        });
        $("#${bp.id}_item").change(function() {
            $.put("/game/buildings/${bp.id}", { "item": $(this).val() });
        });
        </c:forEach>
    </script>
</div>