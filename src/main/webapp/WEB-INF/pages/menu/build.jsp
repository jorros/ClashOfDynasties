<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cod" uri="/WEB-INF/clashofdynasties.tld" %>

<h1>Bauen (${city.name})</h1>
<c:if test="${player == city.player}">
<div id="content" style="overflow:hidden;">
    <div style="float:left;">
        <div class="section" style="width:285px; margin-bottom:20px;">
            <h4>Aktuelle Produktion</h4>
            <div>
                <c:if test="${city.buildingConstruction == null}">Kein Bauvorhaben</c:if>
                <c:if test="${city.buildingConstruction != null}">
                    <div>
                <img style="float:left; margin-right:5px;" src="assets/${city.buildingConstruction.blueprint.getClass().name == "de.clashofdynasties.models.BuildingBlueprint" ? "buildings" : "units"}/${city.buildingConstruction.blueprint.id}.png" />
                <span style="color:#FFF; font-weight:bold;"><c:if test="${city.buildingConstruction.count > 1}">${city.buildingConstruction.count}x </c:if>${city.buildingConstruction.blueprint.name}</span><br><span style="color:#FFF">noch ${productionTime}(${productionPercent}%)<br><a style="font-weight:bold; cursor:pointer;" onclick="stopBuild();">Abbrechen?</a></span>
                    </div>
                </c:if>
            </div>
        </div>
        <div class="section" style="width:285px; clear:left;">
            <h4>Öffentlich</h4>
            <div style="display:flex; flex-direction:row; flex-wrap:wrap; display:-webkit-flex; -webkit-flex-direction:row; -webkit-flex-wrap:wrap;">
                <c:choose>
                    <c:when test="${city.player.nation.id == 1}">
                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[0]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[4]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[11]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[3]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[1]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[9]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[5]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[10]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[12]}"/>
                    </c:when>
                    <c:otherwise>
                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[0]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[4]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[3]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[2]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[9]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[5]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[10]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[13]}"/>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    <div style="float:left;">
        <div class="section" style="width:285px; float:left">
            <h4>Wirtschaft</h4>
            <div style="display:flex; flex-direction:row; flex-wrap:wrap; display:-webkit-flex; -webkit-flex-direction:row; -webkit-flex-wrap:wrap;">
                <c:choose>
                    <c:when test="${city.player.nation.id == 1}">
                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[16]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[17]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[18]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[19]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[22]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[23]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[25]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[26]}"/>
                    </c:when>
                    <c:otherwise>
                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[16]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[17]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[18]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[20]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[15]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[23]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[24]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[21]}"/>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    <div style="float:left;">
        <div class="section" style="width:285px; float:left">
            <h4>Militär</h4>
            <div style="display:flex; flex-direction:row; flex-wrap:wrap; display:-webkit-flex; -webkit-flex-direction:row; -webkit-flex-wrap:wrap;">
                <c:choose>
                    <c:when test="${city.player.nation.id == 1}">
                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[6]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[7]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[8]}"/>

                        <cod:BuildItem city="${city}" blueprint="${unitBlueprints[0]}"/>

                        <cod:BuildItem city="${city}" blueprint="${unitBlueprints[1]}"/>

                        <cod:BuildItem city="${city}" blueprint="${unitBlueprints[2]}"/>

                        <cod:BuildItem city="${city}" blueprint="${unitBlueprints[3]}"/>

                        <cod:BuildItem city="${city}" blueprint="${unitBlueprints[4]}"/>
                    </c:when>
                    <c:otherwise>
                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[14]}"/>

                        <cod:BuildItem city="${city}" blueprint="${buildingBlueprints[8]}"/>

                        <cod:BuildItem city="${city}" blueprint="${unitBlueprints[5]}"/>

                        <cod:BuildItem city="${city}" blueprint="${unitBlueprints[7]}"/>

                        <cod:BuildItem city="${city}" blueprint="${unitBlueprints[8]}"/>

                        <cod:BuildItem city="${city}" blueprint="${unitBlueprints[6]}"/>

                        <cod:BuildItem city="${city}" blueprint="${unitBlueprints[4]}"/>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    <div>
        <button onclick="closeMenu()" style="position:absolute; right:25px; bottom: 120px;">Schlie&szlig;en</button>
    </div>
</div>

<script>
    <c:forEach items="${buildingBlueprints}" var="building">
    $(".build[data-blueprint='${building.id}'][data-type='0']").tooltipster({
        theme: 'tooltipster-light',
        functionReady: function() {
            stopMenuUpdate = true;
        },
        functionAfter: function() {
            stopMenuUpdate = false;
        },
        maxWidth: 350,
        content: $('<span style="font-family:"Philosopher-Bold"; font-size:18px;">${building.name}</span><br><br>Kosten: ${building.price}<br>${building.description}<br><br><c:if test="${fn:length(building.requiredBiomes) < 5}"><span class="<c:if test="${building.requiredBiomes.contains(city.biome)}">green</c:if><c:if test="${!building.requiredBiomes.contains(city.biome)}">red</c:if>">Benötigt: <c:forEach items="${building.requiredBiomes}" var="biome" varStatus="status"><c:if test="${status.first == false && status.last == false}">, </c:if><c:if test="${status.last == true}"> oder </c:if><c:out value="${biome.name}" /></c:forEach></span></c:if><c:if test="${building.requiredResource != null}"><br><span class="<c:if test="${city.resource == building.requiredResource}">green</c:if><c:if test="${city.resource != building.requiredResource}">red</c:if>">Benötigt: </span><img style="vertical-align:bottom;" src="assets/resources/${building.requiredResource.id}.png" /> <span>${building.requiredResource.name}</span></c:if>')
    }).click(function() {
        build(0, ${building.id});
    });

    $(".remove[data-blueprint='${building.id}'][data-type='0']").click(function() {
        remove(0, ${building.id});
    });
    </c:forEach>
    <c:forEach items="${unitBlueprints}" var="unit">
    $(".build[data-blueprint='${unit.id}'][data-type='1']").tooltipster({
        theme: 'tooltipster-light',
        functionReady: function () {
            stopMenuUpdate = true;
        },
        functionAfter: function () {
            stopMenuUpdate = false;
        },
        maxWidth: 350,
        content: $("<span style=\"font-family:'Philosopher-Bold'; font-size:18px;\">${unit.name}</span><br><br>Kosten: ${unit.price}<br>${unit.description}<br><br><span>Benötigt: </span><span class='<c:if test="${city.countBuildings(7) == 0}">red</c:if><c:if test="${city.countBuildings(7) > 0}">green</c:if>'>Militäranlage</span>")
    }).click(function() {
        build(1, ${unit.id});
    });

    $(".remove[data-blueprint='${unit.id}'][data-type='1']").click(function() {
        remove(1, ${unit.id});
    });
    </c:forEach>

    function build(type, blueprint) {
        var count = 1;

        if(type == 1) {
            count = prompt("Wieviele Einheiten willst du ausbilden?", 1);

            if(count == null)
                return;
        }

        $.put("/game/cities/${city.id}/build", { type: type, blueprint: blueprint, count: count }, function() {
            openMenu("build?city=${city.id}");
            forceUpdate();
        });
    }

    function remove(type, blueprint) {
        var count = 1;

        if(type == 1) {
            count = prompt("Wieviele Einheiten willst du entlassen?", 1);

            if(count == null)
                return;
        }

        $.put("/game/cities/${city.id}/destroy", { type: type, blueprint: blueprint, count: count }, function() {
            openMenu("build?city=${city.id}");
            forceUpdate();
        });
    }

    function stopBuild() {
        $.delete("/game/cities/${city.id}/build", function() {
            openMenu("build?city=${city.id}");
            forceUpdate();
        })
    }
</script>
</c:if>