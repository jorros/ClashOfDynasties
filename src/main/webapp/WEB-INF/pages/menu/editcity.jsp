<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h1>St&auml;dte</h1>
<div id="content">
    <table style="text-align:center; width:100%;">
        <tr>
            <th></th>
            <th>Kapazität (rel)</th>
            <th>Grund V.Pkt.</th>
            <th>Grund Konsumrate</th>
            <th>Luxus1 Konsumrate</th>
            <th>Luxus2 Konsumrate</th>
            <th>Luxus3 Konsumrate</th>
            <th>Prod.rate</th>
            <th>Steuerrate</th>
        </tr>
        <c:forEach items="${cityTypes}" var="ct">
            <tr>
                <td style="font-weight: bold;"><c:out value="${ct.name}" /> :</td>
                <td><input id="${ct.id}_capacityRel" style="width:30px; height:10px;" type="text" value="${ct.capacity}" /></td>
                <td><input id="${ct.id}_defence" style="width:30px; height:10px;" type="text" value="${ct.defence}" /></td>
                <td><input id="${ct.id}_consumeBasic" style="width:30px; height:10px;" type="text" value="${ct.consumeBasic}" /></td>
                <td><input id="${ct.id}_consumeLuxury1" style="width:30px; height:10px;" type="text" value="${ct.consumeLuxury1}" /></td>
                <td><input id="${ct.id}_consumeLuxury2" style="width:30px; height:10px;" type="text" value="${ct.consumeLuxury2}" /></td>
                <td><input id="${ct.id}_consumeLuxury3" style="width:30px; height:10px;" type="text" value="${ct.consumeLuxury3}" /></td>
                <td><input id="${ct.id}_productionRate" style="width:30px; height:10px;" type="text" value="${ct.productionRate}" /></td>
                <td><input id="${ct.id}_taxes" style="width:60px; height:10px;" type="text" value="${ct.taxes}" /></td>
            </tr>
        </c:forEach>
    </table>
    <script>
        <c:forEach items="${cityTypes}" var="ct">
        $("#${ct.id}_capacityRel").change(function() {
            $.put("/game/citytypes/${ct.id}", { "capacity": $(this).val() });
        });
        $("#${ct.id}_defence").change(function() {
            $.put("/game/citytypes/${ct.id}", { "defence": $(this).val() });
        });
        $("#${ct.id}_consumeBasic").change(function() {
            $.put("/game/citytypes/${ct.id}", { "consumeBasic": $(this).val() });
        });
        $("#${ct.id}_consumeLuxury1").change(function() {
            $.put("/game/citytypes/${ct.id}", { "consumeLuxury1": $(this).val() });
        });
        $("#${ct.id}_consumeLuxury2").change(function() {
            $.put("/game/citytypes/${ct.id}", { "consumeLuxury2": $(this).val() });
        });
        $("#${ct.id}_consumeLuxury3").change(function() {
            $.put("/game/citytypes/${ct.id}", { "consumeLuxury3": $(this).val() });
        });
        $("#${ct.id}_taxes").change(function() {
            $.put("/game/citytypes/${ct.id}", { "taxes": $(this).val() });
        });
        $("#${ct.id}_productionRate").change(function() {
            $.put("/game/citytypes/${ct.id}", { "productionRate": $(this).val() });
        });
        </c:forEach>
    </script>
</div>