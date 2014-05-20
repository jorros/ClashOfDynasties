<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        </tr>
        <c:forEach items="${buildingBlueprints}" var="bp">
        <tr>
            <td style="font-weight: bold;"><c:out value="${bp.name}" /> :</td>
            <td><textarea id="${bp.id}_desc" style="width:300px; height:60px;"><c:out value="${bp.description}"></c:out></textarea></td>
            <td><c:if test="${bp.requiredResource != null}"><c:out value="${bp.requiredResource.name}" />, </c:if><c:forEach items="${bp.requiredBiomes}" var="biome">${biome.name}, </c:forEach></td>
            <td><input id="${bp.id}_price" style="width:30px; height:10px;" type="text" value="${bp.price}" /></td>
            <td><input id="${bp.id}_defence" style="width:30px; height:10px;" type="text" value="${bp.defencePoints}" /></td>
            <td><input id="${bp.id}_production" style="width:60px; height:10px;" type="text" value="${bp.requiredProduction}" /></td>
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
        $("#${bp.id}_defence").change(function() {
            $.put("/game/buildings/${bp.id}", { "defence": $(this).val() });
        });
        </c:forEach>
    </script>
</div>