<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .outer {
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        display:-webkit-flex;
        -webkit-flex-direction:row;
        -webkit-flex-wrap:wrap;
    }
</style>
<script>
    function save()
    {
        var units = {};
        var injuredUnits = {};

        <c:forEach items="${unitBlueprints}" var="bp">
        units[${bp.id}] = $("#${bp.id}_healthy").val();
        injuredUnits[${bp.id}] = $("#${bp.id}_injured").val();
        </c:forEach>

        var counter = 0;

        $.each(units, function(index, value) {
            counter += value;
        });
        $.each(injuredUnits, function(index, value) {
            counter += value;
        });

        if(counter == 0) {
            alert("Die Formation enthält keine Einheiten!");
            return;
        }

        <c:choose>
            <c:when test="${!create}">
            $.put("/game/formations/${formation.id}", { "city": "${city.id}", "unitsJson": JSON.stringify(units), "injuredUnitsJson": JSON.stringify(injuredUnits), "name": $("#formation_name").val() }, function() {
                forceUpdate();
                closeMenu();
            });
            </c:when>
            <c:otherwise>
            $.post("/game/formations/", { "city": "${city.id}", "unitsJson": JSON.stringify(units), "injuredUnitsJson": JSON.stringify(injuredUnits), "name": $("#formation_name").val() }, function() {
                forceUpdate();
                closeMenu();
            });
            </c:otherwise>
        </c:choose>
    }
</script>
<h1>${formation.name}</h1>
<div id="content" style="overflow:hidden;">
    <div class="outer" style="height:400px; overflow-y:hidden; overflow-x:hidden; margin-bottom:20px;">
        <c:forEach items="${unitBlueprints}" var="bp">
            <div style="clear:both; width:430px; height:70px; border-bottom: thin solid #fff; margin-right:30px;">
                <img style="float:left; margin-top:5px;" src="assets/units/${bp.id}.png" />
                <div style="float:left; margin-left:20px; margin-top:5px;">
                    <div>
                        <div style="width:170px; margin-bottom:10px; float:left;" id="${bp.id}_healthy_slider"></div>
                        <input id="${bp.id}_healthy" type="number" style="padding:5px; float:left; height:15px; margin-top:-5px; margin-left:12px; width:45px; font-size:15px;" value="${formationUnits[bp.id]}"<c:if test="${units[bp.id] == 0}"> disabled</c:if> /><span style="float:left; margin-left:5px;"> / ${units[bp.id]}</span>
                    </div>
                    <div>
                        <div style="width:170px; float:left;" id="${bp.id}_injured_slider"></div>
                        <input id="${bp.id}_injured" type="number" style="padding:5px; float:left; height:15px; margin-top:-5px; margin-left:12px; width:45px; font-size:15px;" value="${formationInjuredUnits[bp.id]}"<c:if test="${injuredUnits[bp.id] == 0}"> disabled</c:if> /><span class="red" style="float:left; margin-left:5px;"> / ${injuredUnits[bp.id]} Verletzte</span>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <div style="clear:both;">
    </div>
    <div style="height:60px;">
        <label for="formation_name">Name: </label>
        <input id="formation_name" maxlength="14" style="width:300px;" type="text" value="${formation.name}" />
        <button onclick="save()" style="float:right;"><c:choose><c:when test="${create}">Erstellen</c:when><c:otherwise>Ändern</c:otherwise></c:choose></button>
        <button onclick="closeMenu()" style="float:right;">Abbrechen</button>
    </div>
</div>

<script>
    <c:forEach items="${unitBlueprints}" var="bp">
    $("#${bp.id}_healthy_slider").slider({
        max: ${units[bp.id]},
        disabled: ${units[bp.id] == 0},
        slide: function(event, ui) {
            $("#${bp.id}_healthy").val(ui.value);
        },
        value: ${formationUnits[bp.id]}
    });
    $("#${bp.id}_injured_slider").slider({
        max: ${injuredUnits[bp.id]},
        disabled: ${injuredUnits[bp.id] == 0},
        slide: function(event, ui) {
            $("#${bp.id}_injured").val(ui.value);
        },
        value: ${formationInjuredUnits[bp.id]}
    });
    </c:forEach>
</script>