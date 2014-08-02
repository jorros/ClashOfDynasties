<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<h1>Bauen (${city.name})</h1>
<c:if test="${player == city.player}">
<div id="content">
    <div style="float:left;">
        <div class="section" style="width:285px; margin-bottom:20px;">
            <h4>Aktuelle Produktion</h4>
            <div>
                <c:if test="${city.buildingConstruction == null}">Kein Bauvorhaben</c:if>
                <c:if test="${city.buildingConstruction != null}">
                <img style="float:left; margin-right:5px;" src="assets/buildings/${city.buildingConstruction.blueprint.id}.png" />
                <span style="color:#FFF; font-weight:bold;">${city.buildingConstruction.blueprint.name}</span><br><span style="color:#FFF">noch ${productionTime}(${productionPercent}%)<br><a style="font-weight:bold; cursor:pointer;" onclick="stopBuild();">Abbrechen?</a></span>
                </c:if>
            </div>
        </div>
        <div class="section" style="width:285px; clear:left;">
            <h4>Öffentlich</h4>
            <div>
                <button id="building-1" onclick="build(0, 1);"><img style="width:32px;height:32px;" src="assets/buildings/1.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(1)}" default="0" /></span><button <c:if test="${city.countBuildings(1) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <button id="building-5" onclick="build(0, 5);" style="margin-left:10px;"><img style="width:32px;height:32px;" src="assets/buildings/5.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(5)}" default="0" /></span><button <c:if test="${city.countBuildings(5) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <button id="building-12" onclick="build(0, 12);" style="margin-left:10px;"><img style="width:32px;height:32px;" src="assets/buildings/12.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(12)}" default="0" /></span><button <c:if test="${city.countBuildings(12) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <br>

                <button id="building-4" onclick="build(0, 4);"><img style="width:32px;height:32px;" src="assets/buildings/4.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(4)}" default="0" /></span><button <c:if test="${city.countBuildings(4) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <button id="building-2" onclick="build(0, 2);" style="margin-left:10px;"><img style="width:32px;height:32px;" src="assets/buildings/2.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(2)}" default="0" /></span><button <c:if test="${city.countBuildings(2) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <button id="building-10" onclick="build(0, 10);" style="margin-left:10px;"><img style="width:32px;height:32px;" src="assets/buildings/10.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(10)}" default="0" /></span><button <c:if test="${city.countBuildings(10) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <br>

                <button id="building-6" onclick="build(0, 6);"><img style="width:32px;height:32px;" src="assets/buildings/6.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(6)}" default="0" /></span><button <c:if test="${city.countBuildings(6) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <button id="building-11" onclick="build(0, 11);" style="margin-left:10px;"><img style="width:32px;height:32px;" src="assets/buildings/11.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(11)}" default="0" /></span><button <c:if test="${city.countBuildings(11) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <button id="building-13" onclick="build(0, 13);" style="margin-left:10px;"><img style="width:32px;height:32px;" src="assets/buildings/13.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(13)}" default="0" /></span><button <c:if test="${city.countBuildings(13) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>
            </div>
        </div>
    </div>
    <div style="float:left;">
        <div class="section" style="width:285px; float:left">
            <h4>Wirtschaft</h4>
            <div>
                <button id="building-17" onclick="build(0, 17);" onclick=""><img style="width:32px;height:32px;" src="assets/buildings/17.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(17)}" default="0" /></span><button <c:if test="${city.countBuildings(17) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <button id="building-18" onclick="build(0, 18);" style="margin-left:10px;"><img style="width:32px;height:32px;" src="assets/buildings/18.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(18)}" default="0" /></span><button <c:if test="${city.countBuildings(18) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <br>

                <button id="building-19" onclick="build(0, 19);" onclick=""><img style="width:32px;height:32px;" src="assets/buildings/19.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(19)}" default="0" /></span><button <c:if test="${city.countBuildings(19) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <button id="building-20" onclick="build(0, 20);" style="margin-left:10px;"><img style="width:32px;height:32px;" src="assets/buildings/20.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(20)}" default="0" /></span><button <c:if test="${city.countBuildings(20) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <button id="building-23" onclick="build(0, 23);" style="margin-left:10px;"><img style="width:32px;height:32px;" src="assets/buildings/23.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(23)}" default="0" /></span><button <c:if test="${city.countBuildings(23) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <br>

                <button id="building-24" onclick="build(0, 24);" onclick=""><img style="width:32px;height:32px;" src="assets/buildings/24.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(24)}" default="0" /></span><button <c:if test="${city.countBuildings(24) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <button id="building-26" onclick="build(0, 26);" style="margin-left:10px;"><img style="width:32px;height:32px;" src="assets/buildings/26.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(26)}" default="0" /></span><button <c:if test="${city.countBuildings(26) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <button id="building-27" onclick="build(0, 27);" style="margin-left:10px;"><img style="width:32px;height:32px;" src="assets/buildings/27.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(27)}" default="0" /></span><button <c:if test="${city.countBuildings(27) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>
            </div>
        </div>
    </div>
    <div style="float:left;">
        <div class="section" style="width:285px; float:left">
            <h4>Militär</h4>
            <div>
                <button id="building-7" onclick="build(0, 7);" onclick=""><img style="width:32px;height:32px;" src="assets/buildings/7.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(7)}" default="0" /></span><button <c:if test="${city.countBuildings(7) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <button id="building-8" onclick="build(0, 8);" style="margin-left:10px;"><img style="width:32px;height:32px;" src="assets/buildings/8.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(8)}" default="0" /></span><button <c:if test="${city.countBuildings(8) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <button id="building-9" onclick="build(0, 9);" style="margin-left:10px;"><img style="width:32px;height:32px;" src="assets/buildings/9.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countBuildings(9)}" default="0" /></span><button <c:if test="${city.countBuildings(9) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <br>

                <button id="unit-1" onclick="build(1, 1);" onclick=""><img style="width:32px;height:32px;" src="assets/units/1.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countUnits(1)}" default="0" /></span><button <c:if test="${city.countUnits(1) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <button id="unit-5" onclick="build(1, 5);" style="margin-left:10px;"><img style="width:32px;height:32px;" src="assets/units/5.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countUnits(5)}" default="0" /></span><button <c:if test="${city.countUnits(5) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <button id="unit-6" onclick="build(1, 6);" style="margin-left:10px;"><img style="width:32px;height:32px;" src="assets/units/6.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countUnits(6)}" default="0" /></span><button <c:if test="${city.countUnits(6) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <br>

                <button id="unit-7" onclick="build(1, 7);"><img style="width:32px;height:32px;" src="assets/units/7.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countUnits(7)}" default="0" /></span><button <c:if test="${city.countUnits(7) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>

                <button id="unit-8" onclick="build(1, 8);" style="margin-left:10px;"><img style="width:32px;height:32px;" src="assets/units/8.png" /></button>
                <span style="color:#FFF; margin-left:-31px; vertical-align:-25px; text-align:center;"><c:out value="${city.countUnits(8)}" default="0" /></span><button <c:if test="${city.countUnits(8) == 0}">disabled</c:if> style="margin-left:0px; vertical-align:25px" class="remove"></button>
            </div>
        </div>
    </div>
    <div>
        <button onclick="closeMenu()" style="position:absolute; right:25px; bottom: 120px;">Schlie&szlig;en</button>
    </div>
</div>

<script>
    <c:forEach items="${buildingBlueprints}" var="building">
        $("#building-${building.id}").tooltip({
            content: "<span style=\"font-family:'Philosopher-Bold'; font-size:18px;\">${building.name}</span><br><br>Kosten: ${building.price}<br>${building.description}<br><br><c:if test="${fn:length(building.requiredBiomes) < 5}"><span class='<c:if test="${building.requiredBiomes.contains(city.biome)}">green</c:if><c:if test="${!building.requiredBiomes.contains(city.biome)}">red</c:if>'>Benötigt: <c:forEach items="${building.requiredBiomes}" var="biome" varStatus="status"><c:if test="${status.first == false && status.last == false}">, </c:if><c:if test="${status.last == true}"> oder </c:if><c:out value="${biome.name}" /></c:forEach></span></c:if><c:if test="${building.requiredResource != null}"><br><span class='<c:if test="${city.resource == building.requiredResource}">green</c:if><c:if test="${city.resource != building.requiredResource}">red</c:if>'>Benötigt: </span><img style='vertical-align:bottom;' src='assets/resources/${building.requiredResource.id}.png' /> <span>${building.requiredResource.name}</span></c:if>", show: { effect: "fade", duration: 400 }, items: "button" });
    </c:forEach>
    <c:forEach items="${unitBlueprints}" var="unit">
    $("#unit-${unit.id}").tooltip({
        content: "<span style=\"font-family:'Philosopher-Bold'; font-size:18px;\">${unit.name}</span><br><br>Kosten: ${unit.price}<br>${unit.description}<br><br><span>Benötigt: </span><span class='<c:if test="${city.countBuildings(7) == 0}">red</c:if><c:if test="${city.countBuildings(7) > 0}">green</c:if>'>Militäranlage</span>", show: { effect: "fade", duration: 400 }, items: "button" });
    </c:forEach>

    function build(type, blueprint) {
        $.put("/game/cities/${city.id}/build", { type: type, blueprint: blueprint }, function() {
            openMenu("build?city=${city.id}");
            loadTop();
        });
    }

    function stopBuild() {
        $.delete("/game/cities/${city.id}/build", function() {
            openMenu("build?city=${city.id}");
            loadTop();
        })
    }
</script>
</c:if>