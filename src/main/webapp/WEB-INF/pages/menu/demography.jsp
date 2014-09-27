<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="cod" uri="/WEB-INF/clashofdynasties.tld" %>

<h1>Mein Reich</h1>
<div id="content">
<table class="ranking">
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
            <td><a style="cursor:pointer;" onclick="Crafty.viewport.centerOn(CityEntities['${city.id}'], 150); isFormationSelected = false; isCaravanSelected = false; CityEntities['${city.id}'].select();">${city.name}</a></td>
            <td>${city.buildings.size()}/${city.capacity}</td>
            <td><c:choose><c:when test="${city.buildingConstruction != null}"><c:if test="${city.buildingConstruction.count > 1}">${city.buildingConstruction.count}x </c:if>${city.buildingConstruction.blueprint.name}<br><span style="font-size:14px;">Dauer: <cod:Time time="${(city.buildingConstruction.requiredProduction - city.buildingConstruction.production) / city.getProductionRate()}"/></span></c:when><c:otherwise>Leer</c:otherwise></c:choose></td>
            <td><span style="vertical-align:middle;">${city.population} <c:if test="${city.satisfaction <= 30 && city.population > 10}"></span><img style="vertical-align:middle;" src="assets/negIndicator.png" /></c:if><c:if test="${city.satisfaction >= 80 && city.population < city.countBuildings(1) * 10 + 10}"><img style="vertical-align:middle;" src="assets/posIndicator.png" /></c:if></td>
            <td><span class="<c:if test="${city.satisfaction <= 30}">red</c:if><c:if test="${city.satisfaction >= 80}">green</c:if>">${city.satisfaction}</span></td>
            <td><span class="<c:choose><c:when test="${city.income-city.outcome > 0}">green</c:when><c:otherwise>red</c:otherwise></c:choose>">${city.income - city.outcome}</span> (<span class="green">${city.income}</span>/<span class="red">${city.outcome}</span>)</td>
            <td><button style="height:36px;" onclick="openMenu('build?city=${city.id}')"><img style="vertical-align:middle;" src="assets/build.png" /></button></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</div>