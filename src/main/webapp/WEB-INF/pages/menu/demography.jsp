<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="cod" uri="/WEB-INF/clashofdynasties.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h1>Mein Reich</h1>
<div id="content" style="overflow-y:hidden;">
    <div class="tabMenu">
        <a href="#" onclick="openMenu('demography', false);"<c:if test="${page == 1}"> class="selected"</c:if>>Städte</a> | <a href="#" onclick="openMenu('demography?p=2', false);"<c:if test="${page == 2}"> class="selected"</c:if>>Karawanen</a> | <a href="#" onclick="openMenu('demography?p=3', false);"<c:if test="${page == 3}"> class="selected"</c:if>>Formationen</a>
    </div>
    <div style="width:100%; height:420px; overflow-y:auto; margin-top:20px;">
        <table class="ranking">
            <c:if test="${page == 1}">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Bauplätze</th>
                    <th>Produktion</th>
                    <th>Population</th>
                    <th>Zufriedenheit</th>
                    <th>Bilanz</th>
                    <th>Aktionen</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach items="${cities}" var="city">
                <tr style="height: 35px;">
                    <td><a style="cursor:pointer;" onclick="Crafty.viewport.centerOn(CityEntities['${city.id}'], 150); isFormationSelected = false; isCaravanSelected = false; CityEntities['${city.id}'].select();">${city.name}</a><br><span style="font-size:14px;">${city.alias}</span></td>
                    <td>${city.buildings.size()}/${city.capacity}</td>
                    <td><c:choose><c:when test="${city.buildingConstruction != null}"><c:if test="${city.buildingConstruction.count > 1}">${city.buildingConstruction.count}x </c:if>${city.buildingConstruction.blueprint.name}<br><span style="font-size:14px;">Dauer: <cod:Time time="${(city.buildingConstruction.requiredProduction - city.buildingConstruction.production) / city.getProductionRate()}"/></span></c:when><c:otherwise>Leer</c:otherwise></c:choose></td>
                    <td><span style="vertical-align:middle;">${city.population} <c:if test="${city.satisfaction <= 30 && city.population > 10}"></span><img style="vertical-align:middle;" src="assets/negIndicator.png" /></c:if><c:if test="${city.satisfaction >= 80 && city.population < city.countBuildings(1) * 10 + 10}"><img style="vertical-align:middle;" src="assets/posIndicator.png" /></c:if></td>
                    <td><span class="<c:if test="${city.satisfaction <= 30}">red</c:if><c:if test="${city.satisfaction >= 80}">green</c:if>">${city.satisfaction}</span></td>
                    <td><span class="<c:choose><c:when test="${city.income-city.outcome > 0}">green</c:when><c:otherwise>red</c:otherwise></c:choose>">${city.income - city.outcome}</span> (<span class="green">${city.income}</span>/<span class="red">${city.outcome}</span>)</td>
                    <td><button style="height:36px;" onclick="openMenu('build?city=${city.id}&demography=1')"><img style="vertical-align:middle;" src="assets/build.png" /></button><button style="height:36px;" onclick="setAlias('${city.id}', '${city.name}', '${city.alias}')"><img style="vertical-align:middle;" src="assets/setAlias.png" /></button></td>
                </tr>
            </c:forEach>
            </tbody>
            </c:if>
            <c:if test="${page == 2}">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Von</th>
                    <th>Nach</th>
                    <th>Ware (Hin)</th>
                    <th>Ware (Zurück)</th>
                    <th>Ladung</th>
                    <th>Richtung</th>
                    <th>Aktionen</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${caravans}" var="caravan">
                    <tr style="height: 35px;">
                        <td><a style="cursor:pointer;" onclick="Crafty.viewport.centerOn(CaravanEntities['${caravan.id}'], 150); isFormationSelected = false; isCaravanSelected = false; CaravanEntities['${caravan.id}'].select();">${caravan.name}</a></td>
                        <td><a style="cursor:pointer;" onclick="Crafty.viewport.centerOn(CityEntities['${caravan.point1.id}'], 150); isFormationSelected = false; isCaravanSelected = false; CityEntities['${caravan.point1.id}'].select();">${caravan.point1.name}</a><br><span style="font-size:14px;">${caravan.point1.alias}</span></td>
                        <td><a style="cursor:pointer;" onclick="Crafty.viewport.centerOn(CityEntities['${caravan.point2.id}'], 150); isFormationSelected = false; isCaravanSelected = false; CityEntities['${caravan.point2.id}'].select();">${caravan.point2.name}</a><br><span style="font-size:14px;"><c:if test="${caravan.point2.player == player}">${caravan.point2.alias}</c:if></span></td>
                        <td><c:choose><c:when test="${caravan.point1Item != null}">${caravan.point1Load}t ${caravan.point1Item.name}</c:when><c:otherwise>Nichts</c:otherwise></c:choose></td>
                        <td><c:choose><c:when test="${caravan.point2Item != null}">${caravan.point2Load}t ${caravan.point2Item.name}</c:when><c:otherwise>Nichts</c:otherwise></c:choose></td>
                        <td><c:if test="${caravan.point1StoreItem != null}"><fmt:formatNumber value="${caravan.point1Store}" maxFractionDigits="0" />t ${caravan.point1StoreItem.name}</c:if><c:if test="${caravan.point1StoreItem != null && caravan.point2StoreItem != null}"><br></c:if><c:if test="${caravan.point2StoreItem != null}"><fmt:formatNumber value="${caravan.point2Store}" maxFractionDigits="0" />t ${caravan.point2StoreItem.name}</c:if></td>
                        <td><c:choose><c:when test="${caravan.direction == 1}">Rückweg</c:when><c:otherwise>Hinweg</c:otherwise></c:choose></td>
                        <td><button style="height:36px;" onclick="openMenu('caravan?caravan=${caravan.id}&demography=1')"><img style="vertical-align:middle; width:30px; height:30px;" src="assets/setCaravan.png" /></button></td>
                    </tr>
                </c:forEach>
                </tbody>
            </c:if>
            <c:if test="${page == 3}">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Status</th>
                    <th>Zustand</th>
                    <th>Kampfstärke</th>
                    <th>Aktionen</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${formations}" var="formation">
                    <tr style="height: 35px;">
                        <td><a style="cursor:pointer;" onclick="Crafty.viewport.centerOn(FormationEntities['${formation.id}'], 150); isFormationSelected = false; isCaravanSelected = false; FormationEntities['${formation.id}'].select();">${formation.name}</a></td>
                        <td>
                            <c:choose>
                                <c:when test="${formation.deployed}">
                                    <c:choose>
                                        <c:when test="${formation.lastCity.report == null}">
                                            <span style="font-size:14px;">Stationiert in</span><br><a style="cursor:pointer;" onclick="Crafty.viewport.centerOn(CityEntities['${formation.lastCity.id}'], 150); isFormationSelected = false; isCaravanSelected = false; CityEntities['${formation.lastCity.id}'].select();">${formation.lastCity.name}</a>
                                        </c:when>
                                        <c:otherwise>
                                            <c:choose>
                                                <c:when test="${formation.lastCity.player == formation.player}">
                                                    <span style="font-size:14px;">Verteidigt</span><br><a style="cursor:pointer;" onclick="Crafty.viewport.centerOn(CityEntities['${formation.lastCity.id}'], 150); isFormationSelected = false; isCaravanSelected = false; CityEntities['${formation.lastCity.id}'].select();">${formation.lastCity.name}</a>
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="font-size:14px;">Kämpft in</span><br><a style="cursor:pointer;" onclick="Crafty.viewport.centerOn(CityEntities['${formation.lastCity.id}'], 150); isFormationSelected = false; isCaravanSelected = false; CityEntities['${formation.lastCity.id}'].select();">${formation.lastCity.name}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <span style="font-size:14px;">Unterwegs nach</span><br><a style="cursor:pointer;" onclick="Crafty.viewport.centerOn(CityEntities['${formation.route.target.id}'], 150); isFormationSelected = false; isCaravanSelected = false; CityEntities['${formation.route.target.id}'].select();">${formation.route.target.name}</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td><span class="<c:if test="${formation.health >= 90}">green</c:if><c:if test="${formation.health <= 30}">red</c:if>">${formation.health}</span></td>
                        <td>${formation.strength}</td>
                        <td><c:choose><c:when test="${formation.deployed && formation.lastCity.player.id == player.id}"><button style="height:36px;" onclick="openMenu('formation?formation=${formation.id}&demography=1')"><img style="vertical-align:middle; width:30px; height:30px;" src="assets/setFormation.png" /></button></c:when><c:otherwise><button style="height:36px;" onclick="openMenu('formation?formation=${formation.id}&demography=1')"><img style="vertical-align:middle; width:30px; height:30px;" src="assets/infoFormation.png" /></button></c:otherwise></c:choose></td>
                    </tr>
                </c:forEach>
                </tbody>
            </c:if>
        </table>
    </div>
</div>
<script>
    function setAlias(id, name, old) {
        var alias = prompt("Setze einen Alias für diese Stadt:", old);

        if(alias != null) {
            $.put("/game/cities/" + id + "/alias", { "alias": alias }, function () {
                openMenu("demography", false);
            });
        }
    }
</script>