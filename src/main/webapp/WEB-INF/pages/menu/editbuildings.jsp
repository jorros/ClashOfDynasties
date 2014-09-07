<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h1>Gebäude</h1>
<div id="content">
    <table style="text-align:center; width:100%;">
        <tr>
            <th></th>
            <th>Beschreibung</th>
            <th>Voraussetzungen</th>
            <th>Kosten</th>
            <th>V.Pkt.</th>
            <th>Produktion</th>
            <th>Max Anzahl</th>
            <th>Item</th>
            <th>Rate</th>
        </tr>
        <c:forEach items="${buildingBlueprints}" var="bp">
        <tr>
            <td style="font-weight: bold;"><c:out value="${bp.name}" /> :</td>
            <td><textarea id="${bp.id}_desc" style="width:210px; height:60px;"><c:out value="${bp.description}"/></textarea></td>
            <td><c:if test="${bp.requiredResource != null}"><c:out value="${bp.requiredResource.name}" />, </c:if><c:forEach items="${bp.requiredBiomes}" var="biome">${biome.name}, </c:forEach></td>
            <td><input id="${bp.id}_price" style="width:30px; height:10px;" type="text" value="${bp.price}" /></td>
            <td><input id="${bp.id}_defence" style="width:30px; height:10px;" type="text" value="${bp.defencePoints}" /></td>
            <td><input id="${bp.id}_production" style="width:60px; height:10px;" type="text" value="${bp.requiredProduction}" /></td>
            <td><input id="${bp.id}_maxcount" style="width:30px; height:10px;" type="text" value="${bp.maxCount}" /></td>
            <td><select id="${bp.id}_item"><option <c:if test="${bp.produceItem == null}">selected</c:if> value="0">Kein</option><c:forEach items="${items}" var="item"><option <c:if test="${item == bp.produceItem}">selected</c:if> value="${item.id}">${item.name}</option></c:forEach></select></td>
            <td><input id="${bp.id}_pps" style="width:60px; height:10px;" type="text" value="<fmt:formatNumber type="number" maxFractionDigits="8" value="${bp.producePerStep}"/>" /></td>
        </tr>
        </c:forEach>
    </table>
    <script>
        <c:forEach items="${buildingBlueprints}" var="bp">
        $("#${bp.id}_desc").change(function() {
            $.put("/game/buildings/${bp.id}", { "description": $(this).val() });
        });
        $("#${bp.id}_price").change(function() {
            $.put("/game/buildings/${bp.id}", { "price": $(this).val() });
        });
        $("#${bp.id}_production").change(function() {
            $.put("/game/buildings/${bp.id}", { "production": $(this).val() });
        });
        $("#${bp.id}_maxcount").change(function() {
            $.put("/game/buildings/${bp.id}", { "maxcount": $(this).val() });
        });
        $("#${bp.id}_defence").change(function() {
            $.put("/game/buildings/${bp.id}", { "defence": $(this).val() });
        });
        $("#${bp.id}_pps").change(function() {
            $.put("/game/buildings/${bp.id}", { "pps": $(this).val() });
        });
        $("#${bp.id}_item").change(function() {
            $.put("/game/buildings/${bp.id}", { "item": $(this).val() });
        });
        </c:forEach>
    </script>
</div>