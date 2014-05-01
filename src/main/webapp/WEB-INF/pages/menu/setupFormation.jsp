<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .sortable { list-style-type: none; margin: 0; padding: 0; width: 380px; min-height:260px; }
    .sortable li { margin: 3px 3px 3px 0; padding: 1px; float: left; width: 50px; height: 50px; font-size: 4em; text-align: center; }
    li.selected { outline: 1px dashed greenyellow; }
</style>
<script>
    function save()
    {
        var assigned = new Array();
        $.each($("#formation_list > li").get(), function(count, obj) {
            assigned.push($(obj).attr("id").slice(5));
        });

        $.put("/game/formations/${formation.id}", { "city": "${city.id}", "units": assigned, "name": $("#formation_name").val() }, function() {
            updateGame();
        });
        closeMenu();
    }
</script>
<h1>Zusammenstellen (${formation.name})</h1>
<div id="content" style="overflow:hidden;">
    <div style="height:60px;">
        <label for="formation_name">Name: </label>
        <input id="formation_name" style="width:300px;" type="text" value="${formation.name}" />
        <button onclick="save()" style="float:right;">Speichern</button>
    </div>
    <div style="float:left;">
        <div class="section" style="width:400px; margin-bottom:20px;">
            <h4>${city.name}</h4>
            <div style="height:270px; overflow-y:auto; overflow-x:hidden;">
                <ul id="city_list" class="sortable">
                    <c:forEach items="${city.units}" var="unit">
                        <li id="unit_${unit.id}"><img src="/game/units/${unit.blueprint.id}/icon?health=${unit.health}" /></li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
    <div style="float:right;">
        <div class="section" style="width:400px; margin-bottom:20px;">
            <h4>${formation.name}</h4>
            <div style="height:270px; overflow-y:auto; overflow-x:hidden;">
                <ul id="formation_list" class="sortable">
                    <c:forEach items="${formation.units}" var="unit">
                        <li id="unit_${unit.id}"><img src="/game/units/${unit.blueprint.id}/icon?health=${unit.health}" /></li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
    <div style="clear:both; text-align:center;">
        <span style="color:white;">Ordne die Einheiten mittels Drag'n'Drop zu</span>
    </div>
</div>

<script>
    $(".sortable").multisortable();
    $("#city_list, #formation_list").sortable({
        connectWith: ".sortable"
    });
</script>