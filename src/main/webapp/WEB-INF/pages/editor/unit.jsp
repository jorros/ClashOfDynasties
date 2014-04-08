<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h1>Gebäude</h1>
<div id="content">
    <table style="text-align:center; width:100%;">
        <tr>
            <th></th>
            <th>Beschreibung</th>
            <th>Geschwindigkeit</th>
            <th>Kosten</th>
            <th>Produktion</th>
        </tr>
        <c:forEach items="${unitBlueprints}" var="bp">
        <tr>
            <td style="font-weight: bold;"><c:out value="${bp.name}" /> :</td>
            <td><textarea id="${bp.id}_desc" style="width:300px; height:60px;">${bp.description}</textarea></td>
            <td><input id="${bp.id}_speed" style="width:50px; height:10px;" type="text" value="${bp.speed}" /></td>
            <td><input id="${bp.id}_price" style="width:50px; height:10px;" type="text" value="${bp.price}" /></td>
            <td><input id="${bp.id}_production" style="width:80px; height:10px;" type="text" value="${bp.requiredProduction}" /></td>
        </tr>
        </c:forEach>
    </table>
    <script>
        <c:forEach items="${unitBlueprints}" var="bp">
        $("#${bp.id}_desc").change(function() {
            $.get("/editor/unit/setDesc", { "id": ${bp.id}, "value": $(this).val() } );
        });
        $("#${bp.id}_speed").change(function() {
            $.get("/editor/unit/setSpeed", { "id": ${bp.id}, "value": $(this).val() } );
        });
        $("#${bp.id}_price").change(function() {
            $.get("/editor/unit/setPrice", { "id": ${bp.id}, "value": $(this).val() } );
        });
        $("#${bp.id}_production").change(function() {
            $.get("/editor/unit/setProduction", { "id": ${bp.id}, "value": $(this).val() } );
        });
        </c:forEach>
    </script>
</div>